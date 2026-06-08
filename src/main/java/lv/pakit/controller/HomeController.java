package lv.pakit.controller;

import lombok.RequiredArgsConstructor;
import lv.pakit.service.LandingPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LandingPageService landingPageService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("lpLv", landingPageService.getLvValues());
        model.addAttribute("lpEn", landingPageService.getEnValues());
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
