package com.sliderbackend.services.storage;

import com.sliderbackend.data.model.Media;
import com.sliderbackend.data.model.dto.GoogleFileDTO;
import com.sliderbackend.data.model.dto.PresentationUploadDTO;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.data.repository.MediaRepo;
import com.sliderbackend.util.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final GoogleDriveService googleDriveService;
    private final MediaRepo mediaRepo;

    public Response<Media> createMediaInPresentation(PresentationUploadDTO presentationUploadDTO){
        Response<Media> response = Utils.getSuccessFulResponse();
        Media media = new Media();

        switch (presentationUploadDTO.getStorageLocation()){
            case GOOGLE_DRIVE -> {
                Response<GoogleFileDTO> googleDriveResponse = handleCreateMediaInFolderInGoogleDrive(presentationUploadDTO);
                GoogleFileDTO googleFileDTO = googleDriveResponse.getModelList().get(0);
                media = new Media(googleFileDTO);
            }
            case CLOUDINARY, AWS_S3_BUCKET -> throw new NotImplementedException(format("%s isn't supported yet", presentationUploadDTO.getStorageLocation().getValue()));
        }
        media.setSize(presentationUploadDTO.getFile().getSize());
        media.setContentType(presentationUploadDTO.getFile().getContentType());
        media = mediaRepo.create(media);
        response.setModelList(Collections.singletonList(media));
        return response;
    }

    private Response<GoogleFileDTO> handleCreateMediaInFolderInGoogleDrive(PresentationUploadDTO presentationUploadDTO) {
        Response<GoogleFileDTO> response;
        String path = "";

        String folderId = processFolderPath(path);
        response = googleDriveService.uploadFileToFolder(folderId, presentationUploadDTO.getFile());

        return response;
    }

    private String processFolderPath(String path) {
        return null;
    }
}
