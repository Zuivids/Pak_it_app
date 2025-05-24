package lv.pakit.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "clientId")
public class ClientResponse {

    private long clientId;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String fullName;
}