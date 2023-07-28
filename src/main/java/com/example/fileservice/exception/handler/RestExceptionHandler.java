package com.example.fileservice.exception.handler;

import com.example.fileservice.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomFileNotFoundException.class)
    public ErrorMessage fileNotFoundExceptionHandler(CustomFileNotFoundException exception) {
        return new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomFileAlreadyExistsException.class)
    public ErrorMessage fileAlreadyExistsExceptionHandler(CustomFileAlreadyExistsException exception) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(FileWriteToArchiveException.class)
    public ErrorMessage fileWriteToArchiveExceptionHandler(FileWriteToArchiveException exception) {
        return new ErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
    }

}
