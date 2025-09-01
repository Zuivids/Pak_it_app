package lv.pakit.exception.http;

import lombok.Getter;
import lv.pakit.dto.response.RestErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class FieldErrorException extends HttpStatusException {

    private static final String USER_ERROR_MESSAGE = "Invalid Parameters";

    public FieldErrorException(String field, String error) {
        super(HttpStatus.BAD_REQUEST, RestErrorResponse.builder()
                .errorMessage(USER_ERROR_MESSAGE)
                .fieldErrors(Map.of(field, error))
                .build());
    }

    public FieldErrorException(Map<String, String> fieldErrors) {
        super(HttpStatus.BAD_REQUEST, RestErrorResponse.builder()
                .errorMessage(USER_ERROR_MESSAGE)
                .fieldErrors(fieldErrors)
                .build());
    }

}
