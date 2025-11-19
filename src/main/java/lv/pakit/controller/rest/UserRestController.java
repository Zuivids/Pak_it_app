package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.dto.request.user.UserCreateRequest;
import lv.pakit.dto.request.user.UserUpdateRequest;
import lv.pakit.service.UserService;
import org.springframework.web.bind.annotation.*;

import static lv.pakit.model.user.UserRole.ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    @PostMapping
    @RequiresRole(ADMIN)
    public void saveUser(@Valid @RequestBody UserCreateRequest request) {
        userService.create(request);
    }

    @PutMapping("/{id}")
    @RequiresRole(ADMIN)
    public void editUser(@PathVariable("id") long id, @Valid @RequestBody UserUpdateRequest request) {
        userService.updateById(id, request);
    }

    @DeleteMapping("/{id}/delete")
    @RequiresRole(ADMIN)
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteById(id);
    }
}
