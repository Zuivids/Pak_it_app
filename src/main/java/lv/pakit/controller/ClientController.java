package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.ClientDto;
import lv.pakit.dto.request.ClientRequest;
import lv.pakit.model.Client;
import lv.pakit.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/client/{id}")
    public String getClientById(@PathVariable long id, Model model) {
        ClientDto clientDto = clientService.retrieveById(id);
        model.addAttribute("client", clientDto);

        return "client-show-one-page";
    }

    @GetMapping("client")
    public String getAllClients(Model model) {
        model.addAttribute("clients",clientService.retrieveAll());

        return "client-show-many-page";
    }

    @GetMapping("client/new")
    public String showClientForm(Model model){
        model.addAttribute("client", new Client());

        return "client-add-new-page";
    }

    @PostMapping("/client")
    public String saveClient(@Valid @ModelAttribute ClientRequest clientRequest){
        clientService.create(clientRequest);

        return "redirect:/client";
    }

    @GetMapping("client/{id}/edit")
    public String editClient(@PathVariable("id") long id, Model model){
        ClientDto clientDto = clientService.retrieveById(id);
        model.addAttribute("client", clientDto);

        return "client-edit-page";
    }

    @PostMapping("/client/{id}/edit")
    public String editedClient(@PathVariable("id") long id, @Valid @ModelAttribute("client") ClientDto clientDto){
        clientService.updateById(id, clientDto);

        return "redirect:/client";
    }

    @GetMapping("/client/{id}/delete")
    public String deleteClient(@PathVariable("id") long id, Model model){
        ClientDto clientDto = clientService.retrieveById(id);
        model.addAttribute("client", clientDto);

        return "client-delete-page";
    }

    @PostMapping("/client/{id}/delete")
    public String deletedClient(@PathVariable("id") long id){
        clientService.deleteById(id);

        return "redirect:/client";
    }
}
