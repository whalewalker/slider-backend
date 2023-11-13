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
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

import static com.sliderbackend.data.model.general.enums.StorageLocation.GOOGLE_DRIVE;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class StorageService {
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
        String path = UUID.randomUUID() + "_" + presentationUploadDTO.getTitle();

        String presentationId = getPresentationId(path, presentationUploadDTO.getTitle());
        response = googleDriveService.uploadFileToFolder(presentationId, presentationUploadDTO.getFile(), presentationUploadDTO.getFile().getOriginalFilename(), presentationUploadDTO.getFile().getContentType());

        return response;
    }

    private String getPresentationId(String path, String presentationName) {
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
