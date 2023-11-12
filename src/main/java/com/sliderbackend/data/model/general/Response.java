package com.sliderbackend.data.model.general;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {
    private String statusCode;
    private String responseCode;
    private String responseMessage;
    private List<Error> errors;
    private List<T> modelList;
    private long count;
}
