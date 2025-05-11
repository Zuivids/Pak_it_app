package lv.pakit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ClientUpdateRequest {

    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String fullName;
}
