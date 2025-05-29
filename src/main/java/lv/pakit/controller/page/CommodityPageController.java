package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.service.CommodityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommodityPageController extends BasePageController {

    private final CommodityService commodityService;

    @GetMapping("/commodity")
    public String getAllCommodities(@RequestParam(required = false) String query, Model model) {
        return handleErrors(() ->
                        model.addAttribute("commodities", commodityService.fetchByQuery(query)),
                "commodity-show-many-page", "commodity-show-many-page", model);
    }

    @GetMapping("/commodity/new")
    public String showCommodityCreateForm() {
        return "commodity-add-new-page";
    }

    @GetMapping("/commodity/{id}/edit")
    public String showCommodityEditForm(@PathVariable("id") long id, Model model) {
        return handleErrors(() ->
                        model.addAttribute("commodity", commodityService.fetchById(id)),
                "commodity-edit-page", "commodity-edit-page", model);
    }

    @GetMapping("/commodity/{id}/delete")
    public String showCommodityDeleteForm(@PathVariable("id") long id, Model model) {
        return handleErrors(() ->
                        model.addAttribute("commodity", commodityService.fetchById(id)),
                "commodity-delete-page", "commodity-delete-page", model);
    }
}