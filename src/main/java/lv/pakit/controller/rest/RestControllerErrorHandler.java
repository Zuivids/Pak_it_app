package lv.pakit.controller.rest;

import lv.pakit.dto.response.RestErrorResponse;
import lv.pakit.exception.PakItException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "lv.pakit.controller.rest")
public class RestControllerErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestErrorResponse processValidationErrors(MethodArgumentNotValidException e) {
        return RestErrorResponse.builder()
                .errorMessage("Invalid request parameters")
                .fieldErrors(mapFieldErrors(e.getBindingResult()))
                .build();
    }

    @ExceptionHandler(PakItException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestErrorResponse processPakItException(PakItException e) {
        return RestErrorResponse.builder()
                .errorMessage(e.getMessage())
                .build();
    }

    private Map<String, String> mapFieldErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}