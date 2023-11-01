package com.sliderbackend.data.model.general.enums;

public enum ResponseCode {
    Successful("U00", "Successful"),
    BadRequest("U02", "Bad request"),
    DuplicateRequest("U03", "Entity already exist"),
    NotFound("U04", "Entity doesn't exist"),
    SystemError("U96", "Internal System Error, Please try again later."),
    Unauthorized("U01", "Unauthorized");

    public final String code;
    public final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}