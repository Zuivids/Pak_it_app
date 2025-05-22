package lv.pakit.dto.request.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientUpdateRequest {

    private String email;
    private String phoneNumber;
    private String fullName;
}