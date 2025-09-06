package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static lv.pakit.model.UserRole.ADMIN;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final UserService userService;

    @GetMapping("user")
    @RequiresRole(ADMIN)
    public String getAllUser(Model model) {
        model.addAttribute("users", userService.fetchAll());
        return "user-show-many-page";
    }

    @GetMapping("user/new")
    @RequiresRole(ADMIN)
    public String showUserForm() {
        return "user-add-new-page";
    }

    @GetMapping("user/{id}/edit")
    @RequiresRole(ADMIN)
    public String editUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));

        return "user-edit-page";
    }

    @GetMapping("/user/{id}/delete")
    @RequiresRole(ADMIN)
    public String deleteUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));

        return "user-delete-page";
    }
}
