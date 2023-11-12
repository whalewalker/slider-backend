package com.sliderbackend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DuplicateException extends RuntimeException {
    private final String massage;

    public DuplicateException(String message) {
        this.massage = message;
    }
}
