package com.sliderbackend.data.model;

import com.sliderbackend.data.repository.BaseModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "media")
@Data
@Builder
public class Media implements BaseModel {
    @Id
    private String id;
    private String resourceType;
    private String format;
    private String publicId;
    private String url;
    private String uuid;
    private String name;
    private long position;

}