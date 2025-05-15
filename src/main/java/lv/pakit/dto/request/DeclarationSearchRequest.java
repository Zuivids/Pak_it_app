package lv.pakit.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeclarationSearchRequest {

    private String identifierCode;
    //TODO add all other fields
}
