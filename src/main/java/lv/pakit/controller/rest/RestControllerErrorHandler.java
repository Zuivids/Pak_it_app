package lv.pakit.controller.rest;

import lv.pakit.dto.response.RestErrorResponse;
import lv.pakit.exception.FieldErrorException;
import lv.pakit.exception.PakItException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "lv.pakit.controller.rest")
public class RestControllerErrorHandler {

    @ExceptionHandler(PakItException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestErrorResponse processPakItException(PakItException e) {
        return RestErrorResponse.builder()
                .errorMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestErrorResponse processValidationErrors(MethodArgumentNotValidException e) {
        return RestErrorResponse.builder()
                .errorMessage("Invalid request parameters")
                .fieldErrors(mapFieldErrors(e.getBindingResult()))
                .build();
    }

    @ExceptionHandler(FieldErrorException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestErrorResponse processFieldErrorException(FieldErrorException e) {
        return RestErrorResponse.builder()
                .errorMessage(e.getMessage())
                .fieldErrors(e.getFieldErrors())
                .build();
    }

    private Map<String, String> mapFieldErrors(BindingResult bindingResult) {
        Map<String, String> fieldErrors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            fieldErrors.putIfAbsent(fieldError.getField(),
                    fieldError.getDefaultMessage());
        });

        return fieldErrors;
    }
}