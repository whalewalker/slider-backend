package com.sliderbackend.services.storage;


import com.sliderbackend.data.model.Media;
import com.sliderbackend.data.model.Presentation;
import com.sliderbackend.data.model.dto.GoogleFileDTO;
import com.sliderbackend.data.model.dto.PresentationUploadDTO;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.data.repository.MediaRepo;
import com.sliderbackend.data.repository.PresentationRepo;
import com.sliderbackend.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static com.sliderbackend.data.model.general.enums.StorageLocation.GOOGLE_DRIVE;
import static com.sliderbackend.util.Utils.createTempFileFromMultipartFile;
import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {
    private static final int DPI = 300;
    private final GoogleDriveService googleDriveService;
    private final MediaRepo mediaRepo;
    private final PresentationRepo presentationRepo;


    public Response<Media> createMediaInPresentation(PresentationUploadDTO presentationUploadDTO) {
        Response<Media> response = Utils.getSuccessFulResponse();
        Media media = new Media();

        switch (presentationUploadDTO.getStorageLocation()) {
            case GOOGLE_DRIVE -> {
                Response<GoogleFileDTO> googleDriveResponse = handleCreateMediaInFolderInGoogleDrive(presentationUploadDTO);
                GoogleFileDTO googleFileDTO = googleDriveResponse.getModelList().get(0);
                media = new Media(googleFileDTO);
            }
            case CLOUDINARY, AWS_S3_BUCKET ->
                    throw new NotImplementedException(format("%s isn't supported yet", presentationUploadDTO.getStorageLocation().getValue()));
        }
        media.setSize(presentationUploadDTO.getFile().getSize());
        media.setContentType(presentationUploadDTO.getFile().getContentType());
        media = mediaRepo.create(media);
        response.setModelList(Collections.singletonList(media));
        return response;
    }

    private Response<GoogleFileDTO> handleCreateMediaInFolderInGoogleDrive(PresentationUploadDTO presentationUploadDTO) {
        Response<GoogleFileDTO> response;

        String folderId = getFolderId(presentationUploadDTO.getPath(), presentationUploadDTO.getTitle());

        response = googleDriveService.uploadFileToFolder(folderId, presentationUploadDTO.getFile(), presentationUploadDTO.getFile().getOriginalFilename(), presentationUploadDTO.getFile().getContentType());
        convertPdfToImages(presentationUploadDTO.getFile(), folderId);
        return response;
    }


    public void convertPdfToImages(MultipartFile pdfFile, String folderId) {
        try (final PDDocument document = PDDocument.load(createTempFileFromMultipartFile(pdfFile))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            List<CompletableFuture<Void>> uploadFutures = IntStream.range(0, document.getNumberOfPages())
                    .parallel()
                    .mapToObj(page -> convertPageToImage(pdfRenderer, pdfFile, page))
                    .map(imageFileFuture -> imageFileFuture.thenAcceptAsync(imageFile ->
                            googleDriveService.uploadFileToFolder(folderId, imageFile, imageFile.getName(), MediaType.IMAGE_PNG_VALUE)))
                    .toList();

            // Wait for all CompletableFuture instances to complete
            CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0])).join();
        } catch (IOException ex) {
            log.error("Exception while trying to convert PDF to images: ", ex);
        }
    }

    private CompletableFuture<File> convertPageToImage(PDFRenderer pdfRenderer, MultipartFile pdfFile, int page) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, DPI, ImageType.RGB);
                File imageFile = Utils.createTempFileFromMultipartFile(pdfFile);
                ImageIO.write(bim, "png", imageFile);
                return imageFile;
            } catch (IOException ex) {
                log.error("Error converting PDF page to image: {}", ex.getMessage());
                return null;
            }
        });
    }


    private String getFolderId(String path, String presentationName) {
        Presentation existingPresentation = presentationRepo.getByPath(path);

        if (existingPresentation == null) {
            Response<GoogleFileDTO> googleFileDtoResponse = googleDriveService.createFolder(path);
            GoogleFileDTO googleFileDto = googleFileDtoResponse.getModelList().get(0);
            Presentation presentation = new Presentation();
            presentation.setFolderId(googleFileDto.getId());
            presentation.setLocation(GOOGLE_DRIVE);
            presentation.setPath(path);
            presentation.setTitle(presentationName);
            presentationRepo.create(presentation);
            return googleFileDto.getId();
        } else {
            return existingPresentation.getFolderId();
        }
    }

}
