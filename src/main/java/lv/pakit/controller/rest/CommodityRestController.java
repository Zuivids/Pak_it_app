package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.commodity.CommodityRequest;
import lv.pakit.service.CommodityService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public void deleteCommodity(@PathVariable("id") long id) {
        commodityService.deleteById(id);
    }

    @PostMapping("/upload")
    public void uploadCommoditiesFromExcel(@RequestParam("file") MultipartFile file) {
        commodityService.importFromExcel(file);
    }
}
