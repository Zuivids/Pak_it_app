package lv.pakit.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "userId")
public class UserResponse {

    private long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean totpConfirmed;
}