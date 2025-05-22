package lv.pakit.dto.request.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCreateRequest {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String fullName;
}