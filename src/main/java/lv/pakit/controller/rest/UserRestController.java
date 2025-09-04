package lv.pakit.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.user.UserCreateRequest;
import lv.pakit.dto.request.user.UserUpdateRequest;
import lv.pakit.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    @PostMapping
    public void saveUser(@Valid @RequestBody UserCreateRequest request) {
        userService.create(request);
    }

    @PutMapping("/{id}")
    public void editUser(@PathVariable("id") long id, @Valid @RequestBody UserUpdateRequest request) {
        userService.updateById(id, request);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteById(id);
    }
}
