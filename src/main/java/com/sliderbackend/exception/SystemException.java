package com.sliderbackend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SystemException extends RuntimeException {
    private final String massage;

    public SystemException(String message) {
        this.massage = message;
    }
}
