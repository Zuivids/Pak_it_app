package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.declaration.DeclarationRequest;
import lv.pakit.service.DeclarationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DeclarationRestController {

    private final DeclarationService declarationService;

    @PostMapping("/declaration")
    public void saveDeclaration(@Valid @RequestBody DeclarationRequest declarationRequest) {
        declarationService.create(declarationRequest);
    }

    @PutMapping("/declaration/{id}")
    public void updateDeclaration(@PathVariable("id") long id, @Valid @RequestBody DeclarationRequest declarationRequest) {
        declarationService.updateById(id, declarationRequest);
    }

    @DeleteMapping("/declaration/{id}")
    public void deleteDeclaration(@PathVariable("id") long id) {
        declarationService.deleteById(id);
    }
}
