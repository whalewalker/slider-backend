package com.sliderbackend.data.model.general.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum StorageLocation {
    GOOGLE_DRIVE("GOOGLE_DRIVE"),
    CLOUDINARY("CLOUDINARY"),
    AWS_S3_BUCKET("AWS_S3_BUCKET");

    private String value;

    StorageLocation(String value) {this.value = value;}

    @JsonCreator
    public static StorageLocation decode(final String code){
        return Stream.of(StorageLocation.values())
                .filter(targetEnum -> targetEnum.value.equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
