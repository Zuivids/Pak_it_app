package lv.pakit.service.auth;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.exception.http.InternalErrorException;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@RequiredArgsConstructor
@Slf4j
public class TotpService {

    private static final int CODE_DIGITS = 6;
    private static final int VALIDITY_PERIOD_SECONDS = 30;
    private static final String ISSUER_NAME = "PakIt";
    private static final HashingAlgorithm HASHING_ALGORITHM = HashingAlgorithm.SHA256;

    public boolean codeIsValid(String secret, String code) {
        final var timeProvider = new SystemTimeProvider();
        final var codeGenerator = new DefaultCodeGenerator(HASHING_ALGORITHM, CODE_DIGITS);
        final var codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        return codeVerifier.isValidCode(secret, code);
    }

    public String generateSecret() {
        final var secretGenerator = new DefaultSecretGenerator();

        return secretGenerator.generate();
    }

    public String generateQrCode(String secret, String email)  {
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
