package com.sliderbackend.data.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class GoogleDriveError implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String domain;

    private String location;

    private String locationType;

    private String message;

    private String reason;

}