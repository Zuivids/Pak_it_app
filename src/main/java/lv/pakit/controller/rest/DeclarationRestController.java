package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.dto.request.declaration.DeclarationRequest;
import lv.pakit.service.declaration.DeclarationService;
import org.springframework.web.bind.annotation.*;

import static lv.pakit.model.user.UserRole.ADMIN;

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
    @RequiresRole(ADMIN)
    public void deleteDeclaration(@PathVariable("id") long id) {
        declarationService.deleteById(id);
    }
}
