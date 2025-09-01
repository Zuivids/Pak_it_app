package lv.pakit.exception.http;

import lv.pakit.dto.response.RestErrorResponse;
import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpStatusException {

    public BadRequestException(String userMessage) {
        super(HttpStatus.BAD_REQUEST, RestErrorResponse.builder()
                .errorMessage(userMessage)
                .build());
    }
}
