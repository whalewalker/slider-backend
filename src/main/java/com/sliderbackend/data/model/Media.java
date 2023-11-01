package com.sliderbackend.data.model;

import com.sliderbackend.data.repository.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "media")
@Data
public class Media implements BaseModel {
    @Id
    private String id;
    private String mediaType;
    private String path;
}