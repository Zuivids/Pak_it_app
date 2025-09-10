package lv.pakit.controller.page;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lv.pakit.exception.http.InternalErrorException;
import org.springframework.http.HttpHeaders;
import com.lowagie.text.pdf.PdfPTable;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationSearchRequest;
import lv.pakit.service.ClientService;
import lv.pakit.service.CommodityService;
import lv.pakit.service.DeclarationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class DeclarationPageController {

    private final DeclarationService declarationService;
    private final CommodityService commodityService;
    private final ClientService clientService;

    @GetMapping("/declaration")
    public String getAllDeclarations(@ModelAttribute(value = "query") DeclarationSearchRequest request,
                                     BindingResult bindingResult, Model model) {
        model.addAttribute("declarations", declarationService.search(request));

        return "declaration-show-many-page";
    }

    @GetMapping("/declaration/{id}")
    public String getDeclarationById(@PathVariable long id, Model model) {
        addDeclarationToModel(id, model);
        return "declaration-show-one-page";
    }

    @GetMapping("/declaration/new")
    public String showDeclarationCreateForm(Model model) {
        model.addAttribute("commodities", commodityService.fetchAll());
        model.addAttribute("clients", clientService.fetchAll());

        return "declaration-add-new-page";
    }

    @GetMapping("/declaration/{id}/edit")
    public String showDeclarationEditForm(@PathVariable("id") long id, Model model) {
        addDeclarationToModel(id, model);
        model.addAttribute("commodities", commodityService.fetchAll());
        model.addAttribute("clients", clientService.fetchAll());

        return "declaration-edit-page";
    }

    @GetMapping("/declaration/{id}/delete")
    public String showDeclarationDeleteForm(@PathVariable("id") long id, Model model) {
        addDeclarationToModel(id, model);

        return "declaration-delete-page";
    }

    private void addDeclarationToModel(long id, Model model) {
        model.addAttribute("declaration", declarationService.fetchById(id));
    }

    @GetMapping("/declaration/{id}/pdf")
    public void downloadPdf(@PathVariable long id, HttpServletResponse response) {
        declarationService.getDeclarationPdf(id, response);
    }

}
