package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.commodity.CommodityRequest;
import lv.pakit.service.CommodityService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommodityRestController {

    private final CommodityService commodityService;

    @PostMapping("/commodity")
    public void saveCommodity(@Valid @RequestBody CommodityRequest commodityRequest) {
        commodityService.create(commodityRequest);
    }

    @PutMapping("/commodity/{id}")
    public void updateCommodity(@PathVariable("id") long id, @Valid @RequestBody CommodityRequest commodityRequest) {
        commodityService.updateById(id, commodityRequest);
    }

    @DeleteMapping("/commodity/{id}/delete")
    public void deleteCommodity(@PathVariable("id") long id) {
        commodityService.deleteById(id);
    }
}