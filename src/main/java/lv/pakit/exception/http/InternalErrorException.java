package lv.pakit.exception.http;

import lv.pakit.dto.response.RestErrorResponse;
import org.springframework.http.HttpStatus;

public class InternalErrorException extends HttpStatusException {

    private static final String USER_ERROR_MESSAGE = "An internal error occurred";

    public InternalErrorException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                RestErrorResponse.builder()
                        .errorMessage(USER_ERROR_MESSAGE)
                        .build(),
                message
        );
    }

    public InternalErrorException(Throwable cause) {
        super(
                HttpStatus.BAD_REQUEST,
                RestErrorResponse.builder()
                        .errorMessage(USER_ERROR_MESSAGE)
                        .build(),
                cause
        );
    }
}
