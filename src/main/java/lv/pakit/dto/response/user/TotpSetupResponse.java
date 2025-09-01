package lv.pakit.dto.response.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TotpSetupResponse {

    private final String qrCode;
}
