package com.sliderbackend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DatabaseException extends RuntimeException {
    private final String massage;

    public DatabaseException(String message) {
        this.massage = message;
    }
}
