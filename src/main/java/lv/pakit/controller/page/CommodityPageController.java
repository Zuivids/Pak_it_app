package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.service.CommodityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/commodity")
@RequiredArgsConstructor
public class CommodityPageController {

    private final CommodityService commodityService;

    @GetMapping
    public String getAllCommodities(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("commodities", commodityService.fetchByQuery(query));
        return "commodity/commodity-show-many-page";
    }

    @GetMapping("/new")
    public String showCommodityCreateForm() {
        return "commodity/commodity-add-new-page";
    }

    @GetMapping("/{id}/edit")
    public String showCommodityEditForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("commodity", commodityService.fetchById(id));

        return "commodity/commodity-edit-page";
    }

    @GetMapping("/{id}/delete")
    public String showCommodityDeleteForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("commodity", commodityService.fetchById(id));

        return "commodity/commodity-delete-page";
    }
}