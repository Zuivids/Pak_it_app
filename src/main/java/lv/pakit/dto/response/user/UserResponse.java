package lv.pakit.dto.response.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private final String username;
    private final String email;
    private final Boolean isTotp;
    private final long userId;
    private final String firstName;
    private final String lastName;
}
