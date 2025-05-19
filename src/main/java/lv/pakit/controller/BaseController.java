package lv.pakit.controller;

import lv.pakit.exception.PakItException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    protected String handleRequest(Runnable runnable, String successPage, String errorPage, Model model) {
        return handleRequest(runnable, successPage, errorPage, model, null);
    }

    protected String handleRequest(Runnable runnable, String successPage, String errorPage, Model model, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            return errorPage;
        }

        try {
            runnable.run();
        } catch (PakItException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return errorPage;
        }

        return successPage;
    }
}
