package lv.pakit.controller;

import lv.pakit.exception.PakItException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    protected String handleRequest(Runnable runnable, String successPage, Model model) {
        return handleRequest(runnable, successPage, model, null);
    }

    protected String handleRequest(Runnable runnable, String successPage, Model model, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            model.addAttribute("fieldErrors", mapFieldErrors(bindingResult));
            System.out.println(model.getAttribute("fieldErrors"));
        }

        try {
            runnable.run();
        } catch (PakItException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return successPage;
    }

    private Map<String, String> mapFieldErrors(BindingResult bindingResult) {
        final var fieldErrors = new HashMap<String, String>();

        for (var fieldError : bindingResult.getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return fieldErrors;
    }

}
