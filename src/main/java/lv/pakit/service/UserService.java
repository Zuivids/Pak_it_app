package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.response.user.UserResponse;
import lv.pakit.exception.http.NotFoundException;
import lv.pakit.model.User;
import lv.pakit.repo.IUserRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepo userRepo;

    public UserResponse getUser(long userId) {
        return mapToDto(requireById(userId));
    }

    public User requireById(long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id (" + userId + ") not found!"));
    }

    private UserResponse mapToDto(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .isTotp(user.isTotp())
                .build();
    }
}
