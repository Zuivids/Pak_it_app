package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.service.LandingPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static lv.pakit.model.user.UserRole.ADMIN;

@Controller
@RequestMapping("/landing-page")
@RequiredArgsConstructor
public class LandingPageAdminController {

    private final LandingPageService landingPageService;

    @GetMapping
    @RequiresRole(value = ADMIN, page = true)
    public String show(Model model) {
        model.addAttribute("configs", landingPageService.getAllOrdered());
        return "landing-page/landing-page-edit";
    }
}
