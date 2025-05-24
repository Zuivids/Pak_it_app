package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.client.ClientCreateRequest;
import lv.pakit.dto.request.client.ClientUpdateRequest;
import lv.pakit.service.ClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClientRestController {

    private final ClientService clientService;

    @PostMapping("/client")
    public void saveClient(@Valid @RequestBody ClientCreateRequest request) {
        clientService.create(request);
    }

    @PutMapping("/client/{id}")
    public void editClient(@PathVariable("id") long id, @Valid @RequestBody ClientUpdateRequest request) {
        clientService.updateById(id, request);
    }

    @DeleteMapping("/client/{id}/delete")
    public void deleteClient(@PathVariable("id") long id) {
        clientService.deleteById(id);
    }
}