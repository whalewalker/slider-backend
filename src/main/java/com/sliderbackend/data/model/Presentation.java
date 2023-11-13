package com.sliderbackend.data.model;

import com.sliderbackend.data.model.general.enums.StorageLocation;
import com.sliderbackend.data.repository.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "presentations")
@Data
@NoArgsConstructor
public class Presentation implements BaseModel {
    @Id
    private String id;

    private String title;
    private String folderId;
    private String path;
    private StorageLocation location;
}




