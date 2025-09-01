package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.client.ClientCreateRequest;
import lv.pakit.dto.request.client.ClientUpdateRequest;
import lv.pakit.service.ClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class ClientRestController {

    private final ClientService clientService;

    @PostMapping
    public void saveClient(@Valid @RequestBody ClientCreateRequest request) {
        clientService.create(request);
    }

    @PutMapping("/{id}")
    public void editClient(@PathVariable("id") long id, @Valid @RequestBody ClientUpdateRequest request) {
        clientService.updateById(id, request);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteClient(@PathVariable("id") long id) {
        clientService.deleteById(id);
    }
}
