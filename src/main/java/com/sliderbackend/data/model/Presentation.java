package com.sliderbackend.data.model;

import com.sliderbackend.data.repository.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "presentations")
@Data
public class Presentation implements BaseModel {
    @Id
    private String id;
    private String title;
    private String content;
    private String privacySettings;
}



