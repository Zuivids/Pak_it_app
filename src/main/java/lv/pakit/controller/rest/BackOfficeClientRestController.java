package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.client.ClientCreateRequest;
import lv.pakit.dto.request.client.ClientUpdateRequest;
import lv.pakit.service.ClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BackOfficeClientRestController {

    private final ClientService clientService;

    @PostMapping("backoffice/client")
    public void saveClient(@Valid @RequestBody ClientCreateRequest request) {
        clientService.create(request);
    }

}