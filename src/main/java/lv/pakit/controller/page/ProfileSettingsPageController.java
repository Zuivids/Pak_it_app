package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.model.User;
import lv.pakit.service.AuthService;
import lv.pakit.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile/settings")
@RequiredArgsConstructor
public class ProfileSettingsPageController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping
    public String getUserSettings(Model model) {
        final User authenticatedUser = authService.getAuthenticatedUser();

        model.addAttribute("user", userService.getUser(authenticatedUser.getUserId()));

        return "profile/profile-settings-page";
    }
}
