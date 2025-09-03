package lv.pakit.service;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import lombok.RequiredArgsConstructor;
import lv.pakit.dto.request.user.TotpConfirmRequest;
import lv.pakit.dto.response.user.TotpSetupResponse;
import lv.pakit.exception.http.BadRequestException;
import lv.pakit.exception.http.InternalErrorException;
import lv.pakit.model.User;
import lv.pakit.repo.IUserRepo;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@RequiredArgsConstructor
public class TotpService {

    private static final int CODE_DIGITS = 6;
    private static final int VALIDITY_PERIOD_SECONDS = 30;
    private static final String ISSUER_NAME = "PakIt";
    private static final HashingAlgorithm HASHING_ALGORITHM = HashingAlgorithm.SHA256;

    private final UserService userService;
    private final IUserRepo userRepo;

    public boolean codeIsValid(String secret, String code) {
        final var timeProvider = new SystemTimeProvider();
        final var codeGenerator = new DefaultCodeGenerator(HASHING_ALGORITHM, CODE_DIGITS);
        final var codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        return codeVerifier.isValidCode(secret, code);
    }

    public TotpSetupResponse setupTotp(long userId) {
        final User user = userService.requireById(userId);
        final String totpSecret = generateSecret();

        user.setTotpSecret(totpSecret);
        user.setTotpConfirmed(false);
        userRepo.save(user);

        final String qrCode = generateQrCode(totpSecret, user.getEmail());

        return TotpSetupResponse.builder()
                .qrCode(qrCode)
                .build();
    }

    public void confirmTotp(long userId, TotpConfirmRequest request) {
        final User user = userService.requireById(userId);

        if (user.getTotpSecret() == null) {
            throw new InternalErrorException("Totp secret not set up for user");
        }
        if (!codeIsValid(user.getTotpSecret(), request.getTotp())) {
            throw new BadRequestException("Invalid 2FA code");
        }

        user.setTotpConfirmed(true);
        userRepo.save(user);
    }

    private String generateSecret() {
        final var secretGenerator = new DefaultSecretGenerator();

        return secretGenerator.generate();
    }

    private String generateQrCode(String secret, String email)  {
        final QrData data = new QrData.Builder()
                .secret(secret)
                .label(email)
                .algorithm(HASHING_ALGORITHM)
                .digits(CODE_DIGITS)
                .period(VALIDITY_PERIOD_SECONDS)
                .issuer(ISSUER_NAME)
                .build();

        try {
            final var generator = new ZxingPngQrGenerator();
            final byte[] imageData = generator.generate(data);
            final String mimeType = generator.getImageMimeType();

            return getDataUriForImage(imageData, mimeType);
        } catch (QrGenerationException e) {
            throw new InternalErrorException(e);
        }
    }
}
