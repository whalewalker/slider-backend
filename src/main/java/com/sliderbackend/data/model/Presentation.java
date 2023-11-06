package com.sliderbackend.data.model;

import com.sliderbackend.data.repository.BaseModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "presentations")
@Data
@Builder
public class Presentation implements BaseModel {
    @Id
    private String id;
    private String title;
    private String privacySettings;
    private String uuid;

    @DBRef
    private List<Media> content;
}




