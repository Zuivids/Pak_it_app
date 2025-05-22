package lv.pakit.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class RestErrorResponse {

    private String errorMessage;
    private Map<String, String> fieldErrors;
}