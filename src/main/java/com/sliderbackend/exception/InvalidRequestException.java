package com.sliderbackend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidRequestException extends RuntimeException {
    private final String massage;

    public InvalidRequestException(String message) {
        this.massage = message;
    }
}
