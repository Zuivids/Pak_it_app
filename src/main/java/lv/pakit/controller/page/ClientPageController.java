package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ClientPageController extends BasePageController {

    private final ClientService clientService;

    @GetMapping("client")
    public String getAllClients(Model model) {
        return handleErrors(() ->
                        model.addAttribute("clients", clientService.fetchAll()),
                "client-show-many-page", "client-show-many-page", model);
    }

    @GetMapping("client/new")
    public String showClientForm() {
        return "client-add-new-page";
    }

    @GetMapping("client/{id}/edit")
    public String editClient(@PathVariable("id") long id, Model model) {
        return handleErrors(() ->
                        model.addAttribute("client", clientService.fetchById(id)),
                "client-edit-page", "client-edit-page", model);
    }

    @GetMapping("/client/{id}/delete")
    public String deleteClient(@PathVariable("id") long id, Model model) {
        return handleErrors(() ->
                        model.addAttribute("client", clientService.fetchById(id)),
                "client-delete-page", "client-delete-page", model);
    }
}
