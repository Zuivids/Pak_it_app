package lv.pakit.controller.rest.error;

import lombok.extern.slf4j.Slf4j;
import lv.pakit.dto.response.RestErrorResponse;
import lv.pakit.exception.PakItException;
import lv.pakit.exception.http.HttpStatusException;
import lv.pakit.exception.http.InternalErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "lv.pakit.controller.rest")
public class RestControllerErrorHandler {

    @ExceptionHandler({PakItException.class, InternalErrorException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse processInternalErrorException(PakItException e) {
        log.error("An internal error occurred", e);

        return RestErrorResponse.builder()
                .errorMessage("An internal error occurred")
                .build();
    }

    @ExceptionHandler(HttpStatusException.class)
    public ResponseEntity<RestErrorResponse> processHttpStatusException(HttpStatusException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getRestErrorResponse());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RestErrorResponse processValidationErrors(MethodArgumentNotValidException e) {
        return RestErrorResponse.builder()
                .errorMessage("Invalid request parameters")
                .fieldErrors(mapFieldErrors(e.getBindingResult()))
                .build();
    }

    private static Map<String, String> mapFieldErrors(BindingResult bindingResult) {
        Map<String, String> fieldErrors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError ->
                fieldErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage())
        );

        return fieldErrors;
    }
}
