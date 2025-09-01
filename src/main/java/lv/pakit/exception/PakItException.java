package lv.pakit.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PakItException extends RuntimeException {

    public PakItException(String message) {
        super(message);
    }

    public PakItException(Throwable cause) {
        super(cause);
    }
}
