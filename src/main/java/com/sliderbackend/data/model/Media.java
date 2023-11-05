package com.sliderbackend.data.model;

import com.sliderbackend.data.repository.BaseModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.UUID;

@Document(collection = "media")
@Data
@Builder
public class Media implements BaseModel {
    @Id
    private String id;
    private String mediaType;
    private String path;
    private String uuid = UUID.randomUUID().toString();
    private String name;

    @Field(targetType = FieldType.BINARY)
    private byte[] fileByte;
}