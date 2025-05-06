package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.CommodityDto;
import lv.pakit.dto.request.CommodityRequest;
import lv.pakit.model.Commodity;
import lv.pakit.service.CommodityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommodityController {

    private final CommodityService commodityService;

    @GetMapping("/commodity/{id}")
    public String getCommodityById(@PathVariable long id, Model model) {
        CommodityDto commodityDto = commodityService.retrieveById(id);
        model.addAttribute("commodity", commodityDto);

        return "commodity-show-one-page";
    }

    @GetMapping("/commodity")
    public String getAllCommodities(Model model) {
        model.addAttribute("commodities", commodityService.retrieveAll());

        return "commodity-show-many-page";
    }

    @GetMapping("/commodity/new")
    public String showCommodityForm(Model model) {
        model.addAttribute("commodity", new Commodity());

        return "commodity-add-new-page";
    }

    @PostMapping("/commodity")
    public String saveCommodity(@Valid @ModelAttribute CommodityRequest commodityRequest) {
        commodityService.create(commodityRequest);

        return "redirect:/commodity";
    }

    @GetMapping("/commodity/{id}/edit")
    public String editCommodity(@PathVariable("id") long id, Model model) {
        CommodityDto commodityDto = commodityService.retrieveById(id);
        model.addAttribute("commodity", commodityDto);

        return "commodity-edit-page";
    }

    @PostMapping("/commodity/{id}/edit")
    public String updateCommodity(@PathVariable("id") long id, @Valid @ModelAttribute("commodity") CommodityDto commodityDto) {
        commodityService.updateById(id, commodityDto);

        return "redirect:/commodity";
    }

    @GetMapping("/commodity/{id}/delete")
    public String deleteCommodity(@PathVariable("id") long id, Model model) {
        //TODO soft delete
        CommodityDto commodityDto = commodityService.retrieveById(id);
        model.addAttribute("commodity", commodityDto);

        return "commodity-delete-page";
    }

    @PostMapping("/commodity/{id}/delete")
    public String deletedCommodity(@PathVariable("id") long id) {
        commodityService.deleteById(id);

        return "redirect:/commodity";
    }
}
