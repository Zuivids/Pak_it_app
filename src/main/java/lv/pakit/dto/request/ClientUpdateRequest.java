package lv.pakit.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpdateRequest {

    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String fullName;
}
