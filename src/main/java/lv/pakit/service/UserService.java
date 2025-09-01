package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.user.TotpConfirmRequest;
import lv.pakit.dto.response.user.TotpSetupResponse;
import lv.pakit.dto.response.user.UserResponse;
import lv.pakit.exception.http.BadRequestException;
import lv.pakit.exception.http.InternalErrorException;
import lv.pakit.exception.http.NotFoundException;
import lv.pakit.model.User;
import lv.pakit.repo.IUserRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TotpService totpService;
    private final IUserRepo userRepo;

    public UserResponse getUser(long userId) {
        return mapToDto(requireById(userId));
    }

    public TotpSetupResponse setupTotp(long userId) {
        final User user = requireById(userId);
        final String totpSecret = totpService.generateSecret();

        user.setTotpSecret(totpSecret);
        user.setTotpConfirmed(false);
        userRepo.save(user);

        final String qrCode = totpService.generateQrCode(totpSecret, user.getEmail());

        return TotpSetupResponse.builder()
                .qrCode(qrCode)
                .build();
    }

    public void confirmTotp(long userId, TotpConfirmRequest request) {
        final User user = requireById(userId);

        if (user.getTotpSecret() == null) {
            throw new InternalErrorException("Totp secret not set up for user");
        }
        if (!totpService.codeIsValid(user.getTotpSecret(), request.getTotp())) {
            throw new BadRequestException("Invalid 2FA code");
        }

        user.setTotpConfirmed(true);
        userRepo.save(user);
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
