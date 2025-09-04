package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.dto.request.commodity.CommodityRequest;
import lv.pakit.service.CommodityService;
import org.springframework.web.bind.annotation.*;

import static lv.pakit.model.UserRole.ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/commodity")
public class CommodityRestController {

    private final CommodityService commodityService;

    @PostMapping
    public void saveCommodity(@Valid @RequestBody CommodityRequest commodityRequest) {
        commodityService.create(commodityRequest);
    }

    @PutMapping("/{id}")
    public void updateCommodity(@PathVariable("id") long id, @Valid @RequestBody CommodityRequest commodityRequest) {
        commodityService.updateById(id, commodityRequest);
    }

    @DeleteMapping("/{id}/delete")
    @RequiresRole(ADMIN)
    public void deleteCommodity(@PathVariable("id") long id) {
        commodityService.deleteById(id);
    }
}
