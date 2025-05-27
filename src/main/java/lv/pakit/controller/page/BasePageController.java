package lv.pakit.controller.page;

import lv.pakit.exception.PakItException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public abstract class BasePageController {

    protected String handleErrors(Runnable runnable, String page, Model model, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            return page;
        }

        try {
            runnable.run();
        } catch (PakItException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return page;
    }
}