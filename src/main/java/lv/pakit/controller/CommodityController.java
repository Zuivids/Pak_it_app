package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.CommodityDto;
import lv.pakit.dto.request.CommodityRequest;
import lv.pakit.model.Commodity;
import lv.pakit.service.CommodityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommodityController extends BaseController {

    private final CommodityService commodityService;

    @GetMapping("/commodity/{id}")
    public String getCommodityById(@PathVariable long id, Model model) {
        return handleRequest(() -> {
            CommodityDto commodityDto = commodityService.retrieveById(id);
            model.addAttribute("commodity", commodityDto);

        }, "commodity-show-one-page", "commodity-show-one-page", model);

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
    public String saveCommodity(@Valid @ModelAttribute("commodity") CommodityRequest commodityRequest, BindingResult bindingResult, Model model) {
        return handleRequest(() -> {
            commodityService.create(commodityRequest);
        }, "redirect:/commodity","commodity-add-new-page", model, bindingResult);

    }

    @GetMapping("/commodity/{id}/edit")
    public String editCommodity(@PathVariable("id") long id, Model model) {
        CommodityDto commodityDto = commodityService.retrieveById(id);
        model.addAttribute("commodity", commodityDto);

        return "commodity-edit-page";
    }

    @PostMapping("/commodity/{id}/edit")
    public String updateCommodity(@PathVariable("id") long id, @Valid @ModelAttribute("commodity") CommodityRequest commodityRequest) {
        commodityService.updateById(id, commodityRequest);

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

    @GetMapping("/commodity/search")
    public String searchCommodities(@RequestParam(required = false) String query, Model model) {
        List<CommodityDto> results = commodityService.search(query);
        model.addAttribute("commodities", results);
        return "commodity-show-many-page";
    }
}
