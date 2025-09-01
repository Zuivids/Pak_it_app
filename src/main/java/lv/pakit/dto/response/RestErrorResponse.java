package lv.pakit.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class RestErrorResponse {

    private final String errorMessage;
    private final Map<String, String> fieldErrors;
}
