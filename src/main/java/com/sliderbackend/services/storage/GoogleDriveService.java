package com.sliderbackend.services.storage;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.sliderbackend.data.model.dto.GoogleFileDTO;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.exception.SystemException;
import com.sliderbackend.services.contract.IStorage;
import com.sliderbackend.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleDriveService implements IStorage {

    private final Drive googleDrive;

    @Override
    public Response<GoogleFileDTO> uploadFileToFolder(String folderId, MultipartFile multipartFile) {
        File file = new File();
        file.setName(multipartFile.getOriginalFilename());
        file.setParents(Collections.singletonList(folderId));
        return createFileInGoogleDrive(multipartFile, file);
    }


    private Response<GoogleFileDTO> createFileInGoogleDrive(MultipartFile multipartFile, File file) {
        Response<GoogleFileDTO> response = Utils.getSuccessFulResponse();
        GoogleFileDTO googleFileDto;
        String fileName = multipartFile.getOriginalFilename();
        assert fileName != null;
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        java.io.File newFile = null;
        try {
            newFile = java.io.File.createTempFile(fileName, prefix);
            multipartFile.transferTo(newFile);

            FileContent content = new FileContent(multipartFile.getContentType(), newFile);
            File uploadedFile = googleDrive.files()
                    .create(file, content)
                    .setFields("id,name,thumbnailLink,webContentLink,webViewLink,md5Checksum,permissions")
                    .execute();

            googleFileDto = new GoogleFileDTO(uploadedFile);
        } catch (Exception e) {
            log.error(e.toString());
            throw new SystemException("Error occurred while creating file on google drive");
        } finally {
            assert newFile != null;
            java.io.File f = new java.io.File(newFile.toURI());
            if (f.delete()){log.info("TEMP FILE DELETED");};
        }

        response.setModelList(Collections.singletonList(googleFileDto));
        return response;
    }
}
