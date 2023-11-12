package com.sliderbackend.data.model.dto;

import com.google.api.services.drive.model.File;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class GoogleFileDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String id;

    private String thumbnailLink;

    private String webViewLink;

    private String webContentLink;

    private String md5Checksum;

    public GoogleFileDTO(File file) {
        name = file.getName();
        id = file.getId();
        webContentLink = file.getWebContentLink();
        thumbnailLink = file.getThumbnailLink();
        webViewLink = file.getWebContentLink();
        md5Checksum = file.getMd5Checksum();
    }
}