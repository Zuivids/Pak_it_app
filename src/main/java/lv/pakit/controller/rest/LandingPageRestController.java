package lv.pakit.controller.rest;

import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.service.LandingPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static lv.pakit.model.user.UserRole.ADMIN;

@RestController
@RequestMapping("/api/landing-page")
@RequiredArgsConstructor
public class LandingPageRestController {

    private final LandingPageService landingPageService;

    @PostMapping("/{key}/text")
    @RequiresRole(ADMIN)
    public ResponseEntity<Void> updateText(
            @PathVariable String key,
            @RequestParam String valueLv,
            @RequestParam String valueEn) {
        landingPageService.updateText(key, valueLv, valueEn);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{key}/image")
    @RequiresRole(ADMIN)
    public ResponseEntity<Map<String, String>> uploadImage(
            @PathVariable String key,
            @RequestParam MultipartFile file) throws IOException {
        String path = landingPageService.uploadImage(key, file);
        return ResponseEntity.ok(Map.of("path", path));
    }
}
