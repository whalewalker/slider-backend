package com.sliderbackend.data.model.general.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PresentationRequest {
    private List<MultipartFile> files;
    private String title;
}
