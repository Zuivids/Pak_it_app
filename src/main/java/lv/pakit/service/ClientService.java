package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.ClientDto;
import lv.pakit.exception.NotFoundException;
import lv.pakit.exception.PakItException;
import lv.pakit.model.Client;
import lv.pakit.repo.IClientRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final IClientRepo clientRepo;

    public void create(ClientDto clientDto) {
        Client client = mapToClient(clientDto);
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

    public void updateById(long id, ClientDto clientDto) {
        Client client = requireClientById(id);

        client.setEmail(clientDto.getEmail());
        client.setPhoneNumber(clientDto.getPhoneNumber());
        client.setFullName(clientDto.getFullName());

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

    private Client mapToClient(ClientDto clientDto) {
        return Client.builder()
                .username(clientDto.getUsername())
                .password(clientDto.getPassword())
                .email(clientDto.getEmail())
                .phoneNumber(clientDto.getPhoneNumber())
                .fullName(clientDto.getFullName())
                .build();
    }

    private Client requireClientById(long id) {
        return clientRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Client with id (" + id + ") not found!"));
    }
}
