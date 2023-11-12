package com.sliderbackend.services.contract;

import com.sliderbackend.data.model.general.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

public interface IStorage {
    Response<?> uploadFileToFolder(String folderId, MultipartFile multipartFile);
}
