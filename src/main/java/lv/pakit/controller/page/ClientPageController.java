package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static lv.pakit.model.user.UserRole.ADMIN;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientPageController {

    private final ClientService clientService;

    @GetMapping
    public String getAllClients(Model model) {
        model.addAttribute("clients", clientService.fetchAll());
        return "client/client-show-many-page";
    }

    @GetMapping("/new")
    public String showClientForm() {
        return "client/client-add-new-page";
    }

    @GetMapping("/{id}/edit")
    public String editClient(@PathVariable("id") long id, Model model) {
        model.addAttribute("client", clientService.fetchById(id));

        return "client/client-edit-page";
    }

    @GetMapping("/{id}/delete")
    @RequiresRole(ADMIN)
    public String deleteClient(@PathVariable("id") long id, Model model) {
        model.addAttribute("client", clientService.fetchById(id));

        return "client/client-delete-page";
    }
}
