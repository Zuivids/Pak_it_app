package lv.pakit.exception.http;

import lombok.Getter;
import lv.pakit.dto.response.RestErrorResponse;
import org.springframework.http.HttpStatus;

@Getter
public class HttpStatusException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final RestErrorResponse restErrorResponse;

    public HttpStatusException(HttpStatus httpStatus, RestErrorResponse restErrorResponse) {
        super("An error occurred when handling a request");
        this.httpStatus = httpStatus;
        this.restErrorResponse = restErrorResponse;
    }

    public HttpStatusException(HttpStatus httpStatus, RestErrorResponse restErrorResponse, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.restErrorResponse = restErrorResponse;
    }

    public HttpStatusException(HttpStatus httpStatus, RestErrorResponse restErrorResponse, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
        this.restErrorResponse = restErrorResponse;
    }
}
