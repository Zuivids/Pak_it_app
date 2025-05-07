package lv.pakit.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private long clientId;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String fullName;
}
