package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static lv.pakit.model.user.UserRole.ADMIN;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserPageController {

    private final UserService userService;

    @GetMapping
    @RequiresRole(ADMIN)
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.fetchAll());

        return "user/user-show-many-page";
    }

    @GetMapping("/new")
    @RequiresRole(ADMIN)
    public String showUserForm() {
        return "user/user-add-new-page";
    }

    @GetMapping("/{id}/edit")
    @RequiresRole(ADMIN)
    public String editUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));

        return "user/user-edit-page";
    }

    @GetMapping("/{id}/delete")
    @RequiresRole(ADMIN)
    public String deleteUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));

        return "user/user-delete-page";
    }
}
