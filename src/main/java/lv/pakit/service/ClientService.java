package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.client.ClientCreateRequest;
import lv.pakit.dto.request.client.ClientUpdateRequest;
import lv.pakit.dto.response.ClientResponse;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Client;
import lv.pakit.repo.IClientRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final IClientRepo clientRepo;

    public ClientResponse fetchById(long id) {
        return mapToDto(requireById(id));
    }

    public List<ClientResponse> fetchAll() {
        return clientRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public void create(ClientCreateRequest request) {
        Client client = Client.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .fullName(request.getFullName())
                .build();

        clientRepo.save(client);
    }

    public void updateById(long id, ClientUpdateRequest request) {
        Client client = requireById(id);

        client.setEmail(request.getEmail());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setFullName(request.getFullName());

        clientRepo.save(client);
    }

    public void deleteById(long id) {
        clientRepo.deleteById(id);
    }

    public ClientResponse mapToDto(Client client) {
        return ClientResponse.builder()
                .clientId(client.getClientId())
                .username(client.getUsername())
                .password(client.getPassword())
                .email(client.getEmail())
                .phoneNumber(client.getPhoneNumber())
                .fullName(client.getFullName())
                .build();
    }

    public Client requireById(long id) {
        return clientRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Client with id (" + id + ") not found!"));
    }
}