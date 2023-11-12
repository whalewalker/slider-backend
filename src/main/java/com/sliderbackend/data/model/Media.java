package com.sliderbackend.data.model;

import com.sliderbackend.data.model.dto.GoogleFileDTO;
import com.sliderbackend.data.model.general.enums.StorageLocation;
import com.sliderbackend.data.repository.BaseModel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "media")
@Data
@NoArgsConstructor
public class Media implements BaseModel {
    @Id
    private String id;
    private String path;
    private String name;
    private String storageId;
    private String checkSum;
    private String contentType;
    private long size;
    private StorageLocation location;
    private long position;

    public Media(GoogleFileDTO googleFileDto) {
        path = googleFileDto.getWebContentLink();
        name = googleFileDto.getName();
        location = StorageLocation.GOOGLE_DRIVE;
        storageId = googleFileDto.getId();
        checkSum = googleFileDto.getMd5Checksum();
    }
}