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
public class CommodityPageController {

    private final CommodityService commodityService;

    @GetMapping("/commodity")
    public String getAllCommodities(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("commodities", commodityService.fetchByQuery(query));
        return "commodity-show-many-page";
    }

    @GetMapping("/commodity/new")
    public String showCommodityCreateForm() {
        return "commodity-add-new-page";
    }

    @GetMapping("/commodity/{id}/edit")
    public String showCommodityEditForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("commodity", commodityService.fetchById(id));

        return "commodity-edit-page";
    }

    @GetMapping("/commodity/{id}/delete")
    public String showCommodityDeleteForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("commodity", commodityService.fetchById(id));

        return "commodity-delete-page";
    }
}