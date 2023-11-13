package com.sliderbackend.services.contract;

import com.sliderbackend.data.model.general.Response;
import org.springframework.web.multipart.MultipartFile;

public interface IStorage {
    Response<?> uploadFileToFolder(String folderId, MultipartFile multipartFile, String mediaName, String contentType);
    Response<?> createFolder(String folder);
}
