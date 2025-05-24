package lv.pakit.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FieldErrorException extends PakItException {

    private static final String ERROR_MESSAGE = "Invalid Parameters";

    private Map<String, String> fieldErrors;

    public FieldErrorException(String field, String error) {
        super(ERROR_MESSAGE);
        fieldErrors = Map.of(field, error);
    }

    public FieldErrorException(Map<String, String> fieldErrors) {
        super(ERROR_MESSAGE);
        this.fieldErrors = fieldErrors;
    }

}
