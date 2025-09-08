package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static lv.pakit.model.UserRole.ADMIN;

@Controller
@RequiredArgsConstructor
public class ClientPageController {

    private final ClientService clientService;

    @GetMapping("client")
    public String getAllClients(Model model) {
        model.addAttribute("clients", clientService.fetchAll());
        return "client-show-many-page";
    }

    @GetMapping("client/new")
    public String showClientForm() {
        return "client-add-new-page";
    }

    @GetMapping("client/{id}/edit")
    public String editClient(@PathVariable("id") long id, Model model) {
        model.addAttribute("client", clientService.fetchById(id));

        return "client-edit-page";
    }

    @GetMapping("/client/{id}/delete")
    @RequiresRole(ADMIN)
    public String deleteClient(@PathVariable("id") long id, Model model) {
        model.addAttribute("client", clientService.fetchById(id));

        return "client-delete-page";
    }
}
