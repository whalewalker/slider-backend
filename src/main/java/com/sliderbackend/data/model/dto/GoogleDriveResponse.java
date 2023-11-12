package com.sliderbackend.data.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class GoogleDriveResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    private List<GoogleDriveError> errors;
}