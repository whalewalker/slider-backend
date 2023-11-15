package com.sliderbackend.data.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sliderbackend.data.model.general.enums.StorageLocation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import static com.sliderbackend.data.model.general.enums.StorageLocation.GOOGLE_DRIVE;

@Data
public class PresentationUploadDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotNull
    private MultipartFile file;
    private StorageLocation storageLocation = GOOGLE_DRIVE;

    @JsonIgnore
    private String path;
}
