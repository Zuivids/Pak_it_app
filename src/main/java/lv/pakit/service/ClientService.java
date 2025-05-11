package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.ClientDto;
import lv.pakit.dto.request.ClientCreateRequest;
import lv.pakit.dto.request.ClientUpdateRequest;
import lv.pakit.exception.NotFoundException;
import lv.pakit.model.Client;
import lv.pakit.repo.IClientRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final IClientRepo clientRepo;

    public void create(ClientCreateRequest clientCreateRequest) {
        Client client = mapToClient(clientCreateRequest);
        clientRepo.save(client);
    }

    public ClientDto retrieveById(long id) {
        Client client = requireClientById(id);

        return mapToDto(client);
    }

    public List<ClientDto> retrieveAll() {
        return clientRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public void updateById(long id, ClientUpdateRequest clientUpdateRequest) {
        Client client = requireClientById(id);

        client.setEmail(clientUpdateRequest.getEmail());
        client.setPhoneNumber(clientUpdateRequest.getPhoneNumber());
        client.setFullName(clientUpdateRequest.getFullName());

        clientRepo.save(client);
    }

    public void deleteById(long id) {
        requireClientById(id);
        clientRepo.deleteById(id);
    }

    public ClientDto mapToDto(Client client) {
        return ClientDto.builder()
                .clientId(client.getClientId())
                .username(client.getUsername())
                .password(client.getPassword())
                .email(client.getEmail())
                .phoneNumber(client.getPhoneNumber())
                .fullName(client.getFullName())
                .build();
    }

    private Client mapToClient(ClientCreateRequest clientCreateRequest) {
        return Client.builder()
                .username(clientCreateRequest.getUsername())
                .password(clientCreateRequest.getPassword())
                .email(clientCreateRequest.getEmail())
                .phoneNumber(clientCreateRequest.getPhoneNumber())
                .fullName(clientCreateRequest.getFullName())
                .build();
    }

    private Client requireClientById(long id) {
        return clientRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Client with id (" + id + ") not found!"));
    }
}
