package lv.pakit.controller.page;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.response.CommodityResponse;
import lv.pakit.service.CommodityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public String getAllCommodities(@RequestParam(required = false) String query,
                                    @RequestParam(defaultValue = "0") int page,
                                    Model model) {
        Page<CommodityResponse> commodityPage = commodityService.fetchByQueryPaged(
                query, PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "commodityCode")));

        int totalPages = commodityPage.getTotalPages();
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);

        model.addAttribute("commodities", commodityPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
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