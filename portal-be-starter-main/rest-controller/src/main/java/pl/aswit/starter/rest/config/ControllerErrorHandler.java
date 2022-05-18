package pl.aswit.starter.rest.config;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pl.aswit.starter.model.error.ErrorCode;
import pl.aswit.starter.rest.config.model.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import static pl.aswit.starter.model.error.ErrorCode.GENERIC_ERROR_CODE;
import static pl.aswit.starter.model.error.ErrorCode.VALIDATION_ERROR_CODE;

@Slf4j
@RestControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralExceptionError(Exception exception) {
        return makeResponseError(GENERIC_ERROR_CODE, String.format("Unpredicted, not caught exception occurred: %s", exception.getMessage()), exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationError(MethodArgumentNotValidException exception) {
        val message = new StringBuilder();
        exception
            .getBindingResult()
            .getFieldErrors()
            .forEach(fieldError -> message.append(String.format("%s %s, ", fieldError.getField(), fieldError.getDefaultMessage())));

        exception.getBindingResult().getGlobalErrors().forEach(fieldError -> message.append(String.format("%s, ", fieldError.getDefaultMessage())));

        return makeResponseError(VALIDATION_ERROR_CODE, message.substring(0, message.length() - 2), exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleValidationError(ConstraintViolationException exception) {
        val message = new StringBuilder();
        exception
            .getConstraintViolations()
            .forEach(fieldError -> message.append(String.format("%s %s, ", fieldError.getPropertyPath(), fieldError.getMessage())));

        return makeResponseError(VALIDATION_ERROR_CODE, message.substring(0, message.length() - 2), exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse handleValidationError(BindException exception) {
        val message = new StringBuilder();
        exception
            .getFieldErrors()
            .forEach(fieldError ->
                message.append(String.format("%s.%s %s, ", fieldError.getField(), fieldError.getObjectName(), fieldError.getDefaultMessage()))
            );

        return makeResponseError(VALIDATION_ERROR_CODE, message.substring(0, message.length() - 2), exception);
    }

    public static ErrorResponse makeResponseError(ErrorCode code, String message, Exception e) {
        log.warn(String.format("ERROR: code=%s message=%s", code, message), e);
        return ErrorResponse.builder().code(code.getCode()).message(message).build();
    }
}
