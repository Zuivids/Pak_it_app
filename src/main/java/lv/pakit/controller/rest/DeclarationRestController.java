package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationRequest;
import lv.pakit.service.DeclarationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/declaration")
public class DeclarationRestController {

    private final DeclarationService declarationService;

    @PostMapping
    public void saveDeclaration(@Valid @RequestBody DeclarationRequest declarationRequest) {
        declarationService.create(declarationRequest);
    }

    @PutMapping("/{id}")
    public void updateDeclaration(@PathVariable("id") long id, @Valid @RequestBody DeclarationRequest declarationRequest) {
        declarationService.updateById(id, declarationRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteDeclaration(@PathVariable("id") long id) {
        declarationService.deleteById(id);
    }
}
