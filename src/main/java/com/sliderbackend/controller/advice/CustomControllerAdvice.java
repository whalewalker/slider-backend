package com.sliderbackend.controller.advice;


import com.sliderbackend.data.model.general.Error;
import com.sliderbackend.data.model.general.Response;
import com.sliderbackend.data.model.general.enums.ResponseCode;
import com.sliderbackend.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Demilade Oladugba on 5/13/2021
 **/

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@ResponseBody
@Slf4j
public class CustomControllerAdvice {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        Response<?> response = new Response<>();
        log.error(e.getMessage());
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        response.setResponseMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response<?>> handleSystemException(SystemException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        response.setResponseCode(ResponseCode.SystemError.code);
        response.setResponseMessage(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response<?>> handleException(Exception e) {
        log.error(e.getMessage());
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        response.setResponseCode(ResponseCode.SystemError.code);
        response.setResponseMessage(ResponseCode.SystemError.message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<?>> handleNotFoundException(NotFoundException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        response.setResponseCode(ResponseCode.NotFound.code);
        response.setResponseMessage(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DatabaseException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleDatabaseException(DatabaseException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        response.setResponseMessage(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        log.error(e.getMessage());
        response.setResponseMessage("Json parse error");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<Response<?>> handleDuplicateException(DuplicateException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.CONFLICT.value()));
        response.setResponseCode(ResponseCode.DuplicateRequest.code);
        response.setResponseMessage(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleValidationException(MethodArgumentNotValidException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        response.setResponseMessage(ResponseCode.BadRequest.message);
        BindingResult result = e.getBindingResult();
        List<FieldError> errorList = result.getFieldErrors();
        List<Error> errors = errorList.stream().map(fieldError -> new Error(fieldError.getField(), fieldError.getDefaultMessage())).toList();
        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleDuplicateKeyException(DuplicateKeyException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        response.setResponseMessage(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "Duplicate key error");
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        response.setResponseMessage("Data integrity Error");
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response<?>> handleDataAccessResourceFailureException(DataAccessResourceFailureException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        response.setResponseCode(ResponseCode.SystemError.code);
        response.setResponseMessage("An Error Occurred");
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        if (Objects.requireNonNull(e.getRequiredType()).getEnumConstants() != null){
            response.setResponseMessage(String.format("%s isn't a valid %s. Valid %s include %s",
                    e.getValue(), e.getName(), e.getName(),
                    Arrays.toString(Objects.requireNonNull(e.getRequiredType()).getEnumConstants())
            ));
        }else{
            response.setResponseMessage(String.format("%s isn't a valid %s.",
                    e.getValue(), e.getName()));
        }
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        response.setResponseMessage(String.format("%s is required", e.getParameterName()));
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        response.setResponseMessage(String.format("%s is required", e.getRequestPartName()));
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        response.setResponseMessage("Invalid content type");
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        response.setResponseMessage(Objects.requireNonNull(e.getMessage()).substring(159));
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response<?>> handleIOException(IOException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        response.setResponseCode(ResponseCode.SystemError.code);
        response.setResponseMessage(ResponseCode.SystemError.message);
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleInvalidRequestException(InvalidRequestException e) {
        Response<?> response = new Response<>();
        response.setStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setResponseCode(ResponseCode.BadRequest.code);
        response.setResponseMessage(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}