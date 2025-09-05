package lv.pakit.dto.response.user;

import lombok.*;

@Getter
@Builder
public class UserResponse {

    private final long userId;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Boolean totpConfirmed;
    private final Boolean isTotp;
}
