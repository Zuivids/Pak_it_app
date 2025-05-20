package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.ClientDto;
import lv.pakit.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ClientController extends BaseController {

    private final ClientService clientService;

    @GetMapping("/client/{id}")
    public String getClientById(@PathVariable long id, Model model) {
        return handleRequest(() -> {
            ClientDto clientDto = clientService.retrieveById(id);
            model.addAttribute("client", clientDto);
        }, "client-show-one-page", "client-show-one-page", model);

    }

    @GetMapping("client")
    public String getAllClients(Model model) {
        return handleRequest(() -> {
            model.addAttribute("clients", clientService.retrieveAll());
        }, "client-show-many-page", "client-show-many-page", model);
    }

    @GetMapping("client/new")
    public String showClientForm(Model model) {
        return handleRequest(() -> {
            model.addAttribute("client", new ClientDto());
        }, "client-add-new-page", "client-add-new-page", model);
    }

    @PostMapping("/client")
    public String saveClient(@Valid @ModelAttribute("client") ClientDto clientDto, BindingResult bindingResult, Model model) {
        return handleRequest(() -> {
            clientService.create(clientDto);
        }, "redirect:/client", "client-add-new-page", model, bindingResult);
    }

    @GetMapping("client/{id}/edit")
    public String editClient(@PathVariable("id") long id, Model model) {
        return handleRequest(() -> {
            ClientDto clientDto = clientService.retrieveById(id);
            model.addAttribute("client", clientDto);
        }, "client-edit-page", "client-edit-page", model);
    }

    @PostMapping("/client/{id}/edit")
    public String editedClient(@PathVariable("id") long id, @Valid @ModelAttribute("client") ClientDto clientDto, BindingResult bindingResult, Model model) {
        return handleRequest(() -> {
            clientService.updateById(id, clientDto);
        }, "redirect:/client", "client-edit-page", model, bindingResult);
    }

    @GetMapping("/client/{id}/delete")
    public String deleteClient(@PathVariable("id") long id, Model model) {
        return handleRequest(() -> {
            ClientDto clientDto = clientService.retrieveById(id);
            model.addAttribute("client", clientDto);
        }, "client-delete-page", "client-delete-page", model);
    }

    @PostMapping("/client/{id}/delete")
    public String deletedClient(@PathVariable("id") long id, Model model) {
        return handleRequest(() -> {
            clientService.deleteById(id);
        }, "redirect:/client", "commodity-delete-page", model);
    }
}
