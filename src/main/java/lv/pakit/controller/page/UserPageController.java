package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final UserService userService;

    @GetMapping("user")
    public String getAllUser(Model model) {
        model.addAttribute("users", userService.fetchAll());
        return "user-show-many-page";
    }

    @GetMapping("user/new")
    public String showUserForm() {
        return "user-add-new-page";
    }

    @GetMapping("user/{id}/edit")
    public String editUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));

        return "user-edit-page";
    }

    @GetMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));

        return "user-delete-page";
    }
}
