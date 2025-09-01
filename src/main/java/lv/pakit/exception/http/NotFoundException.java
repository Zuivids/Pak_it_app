package lv.pakit.exception.http;

import lv.pakit.dto.response.RestErrorResponse;
import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpStatusException {

    public NotFoundException(String userMessage) {
        super(HttpStatus.BAD_REQUEST, RestErrorResponse.builder()
                .errorMessage(userMessage)
                .build());
    }
}
