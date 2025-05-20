package lv.pakit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.CommodityDto;
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
        return handleRequest(() -> {
            model.addAttribute("commodities", commodityService.retrieveAll());
        }, "commodity-show-many-page", "commodity-show-many-page", model);
    }

    @GetMapping("/commodity/new")
    public String showCommodityForm(Model model) {
        return handleRequest(() -> {
            model.addAttribute("commodity", new CommodityDto());
        }, "commodity-add-new-page", "commodity-add-new-page", model);
    }

    @PostMapping("/commodity")
    public String saveCommodity(@Valid @ModelAttribute("commodity") CommodityDto commodityDto, BindingResult bindingResult, Model model) {
        return handleRequest(() -> {
            commodityService.create(commodityDto);
        }, "redirect:/commodity", "commodity-add-new-page", model, bindingResult);
    }

    @GetMapping("/commodity/{id}/edit")
    public String editCommodity(@PathVariable("id") long id, Model model) {
        return handleRequest(() -> {
            CommodityDto commodityDto = commodityService.retrieveById(id);
            model.addAttribute("commodity", commodityDto);
        }, "commodity-edit-page", "commodity-edit-page", model);
    }

    @PostMapping("/commodity/{id}/edit")
    public String updateCommodity(@PathVariable("id") long id, @Valid @ModelAttribute("commodity") CommodityDto commodityDto,
                                  BindingResult bindingResult, Model model) {
        return handleRequest(() -> {
            commodityService.updateById(id, commodityDto);
        }, "redirect:/commodity", "commodity-edit-page", model, bindingResult);
    }

    @GetMapping("/commodity/{id}/delete")
    public String deleteCommodity(@PathVariable("id") long id, Model model) {
        //TODO soft delete
        return handleRequest(() -> {
            CommodityDto commodityDto = commodityService.retrieveById(id);
            model.addAttribute("commodity", commodityDto);
        }, "commodity-delete-page", "commodity-delete-page", model);
    }

    @PostMapping("/commodity/{id}/delete")
    public String deletedCommodity(@PathVariable("id") long id, Model model) {
        return handleRequest(() -> {
            commodityService.deleteById(id);
        }, "redirect:/commodity", "commodity-delete-page", model);
    }

    @GetMapping("/commodity/search")
    public String searchCommodities(@RequestParam(required = false) String query, Model model) {
        return handleRequest(() -> {
            List<CommodityDto> results = commodityService.search(query);
            model.addAttribute("commodities", results);
        }, "commodity-show-many-page", "commodity-show-many-page", model);
    }
}
