package lv.pakit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ClientRequest {

    @NotNull
    private String username;
    @NotNull
    private String password;
}
