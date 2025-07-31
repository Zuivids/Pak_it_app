package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BackOfficeClientPageController {

    private final ClientService clientService;

    @GetMapping("backoffice/client")
    public String getAllClients(Model model) {
        model.addAttribute("clients", clientService.fetchAll());
        return "bo-client-show-many-page";
    }

    @GetMapping("backoffice/client/new")
    public String showClientForm() {
        return "bo-client-add-new-page";
    }


}
