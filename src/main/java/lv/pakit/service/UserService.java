package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.user.UserCreateRequest;
import lv.pakit.dto.request.user.UserUpdateRequest;
import lv.pakit.dto.response.user.UserResponse;
import lv.pakit.exception.http.NotFoundException;
import lv.pakit.model.User;
import lv.pakit.repo.IUserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepo userRepo;

    public UserResponse getUser(long userId) {
        return mapToDto(requireById(userId));
    }

    public List<UserResponse> fetchAll() {
        return userRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public void create(UserCreateRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();

        userRepo.save(user);
    }

    public void updateById(long id, UserUpdateRequest request) {
        User user = requireById(id);

        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        userRepo.save(user);
    }

    public void deleteById(long id) {
        userRepo.deleteById(id);
    }

    public User requireById(long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id (" + userId + ") not found!"));
    }

    private UserResponse mapToDto(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .isTotp(user.isTotp())
                .build();
    }
}
