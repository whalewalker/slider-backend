package com.sliderbackend.services.storage;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.sliderbackend.data.model.dto.GoogleDriveResponse;
import com.sliderbackend.data.model.dto.GoogleFileDTO;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.exception.NotFoundException;
import com.sliderbackend.exception.SystemException;
import com.sliderbackend.services.contract.IStorage;
import com.sliderbackend.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleDriveService implements IStorage {

    private final Drive googleDrive;
    private final Utils utils;

    @Value("${google.drive.parent.id}")
    private String parentIds;

    @Override
    public Response<GoogleFileDTO> uploadFileToFolder(String folderId, MultipartFile multipartFile, String mediaName, String contentType) {
        File file = new File();
        file.setName(mediaName);
        file.setParents(Collections.singletonList(folderId));
        return createFileInGoogleDrive(multipartFile, contentType, file);
    }

    @Override
    public Response<?> uploadFileToFolder(String folderId, java.io.File newFile, String mediaName, String contentType) {
        File file = new File();
        file.setName(mediaName);
        file.setParents(Collections.singletonList(folderId));
        return createFileInGoogleDrive(newFile, contentType, file);
    }


    private Response<GoogleFileDTO> createFileInGoogleDrive(MultipartFile multipartFile, String contentType, File file) {
        try {
            java.io.File newFile = Utils.createTempFileFromMultipartFile(multipartFile);
            return createFileInGoogleDrive(newFile, contentType, file);
        } catch (Exception e) {
            log.error(e.toString());
            throw new SystemException("Error occurred while creating file on google drive");
        }
    }

    private Response<GoogleFileDTO> createFileInGoogleDrive(java.io.File newFile, String contentType, File file) {
        Response<GoogleFileDTO> response = Utils.getSuccessFulResponse();
        GoogleFileDTO googleFileDto;

        try {
            FileContent content = new FileContent(contentType, newFile);
            File uploadedFile = googleDrive.files()
                    .create(file, content)
                    .setFields("id,name,thumbnailLink,webContentLink,webViewLink,md5Checksum")
                    .execute();

            googleFileDto = new GoogleFileDTO(uploadedFile);
        } catch (Exception e) {
            log.error(e.toString());
            throw new SystemException("Error occurred while creating file on google drive");
        }

        response.setModelList(Collections.singletonList(googleFileDto));
        return response;
    }

    @Override
    public Response<GoogleFileDTO> createFolder(String folder) {
        File file = new File();
        file.setName(folder);
        file.setParents(Collections.singletonList(parentIds));
        return createFolderInGoogleDrive(file);
    }


    private Response<GoogleFileDTO> createFolderInGoogleDrive(File file) {
        try {
            Response<GoogleFileDTO> response = Utils.getSuccessFulResponse();

            file.setMimeType("application/vnd.google-apps.folder");

            File uploadedFile = googleDrive.files()
                    .create(file)
                    .setFields("id,name,permissions")
                    .execute();

            GoogleFileDTO googleFileDto = new GoogleFileDTO(uploadedFile);

            response.setModelList(Collections.singletonList(googleFileDto));
            return response;
        } catch (Exception e) {
            log.error(e.toString());
            if (e.getClass().equals(GoogleJsonResponseException.class))
                handleGoogleJsonResponseException((GoogleJsonResponseException) e);
            throw new SystemException("Error occurred while creating folder on google drive");
        }
    }

    private void handleGoogleJsonResponseException(GoogleJsonResponseException e) {
        GoogleDriveResponse googleDriveResponse = utils.jsonUnMarshall(e.getContent(), GoogleDriveResponse.class);
        log.error(e.toString());
        if (googleDriveResponse.getCode() == 404)
            throw new NotFoundException(googleDriveResponse.getMessage());
        throw new SystemException(googleDriveResponse.getMessage());
    }


    private byte[] compress(MultipartFile file, String mediaName) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            ZipEntry entry = new ZipEntry(mediaName);
            zos.putNextEntry(entry);

            byte[] fileContent = file.getBytes();

            zos.write(fileContent, 0, fileContent.length);
            zos.closeEntry();

            zos.finish();

            return baos.toByteArray();
        }
    }
}
