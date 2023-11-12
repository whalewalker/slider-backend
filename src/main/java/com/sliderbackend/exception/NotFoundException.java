package com.sliderbackend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends RuntimeException {
    private final String massage;

    public NotFoundException(String message) {
        this.massage = message;
    }
}
