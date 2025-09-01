package lv.pakit.model.converter;

import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.pakit.config.properties.PakItDatabaseProperties;
import lv.pakit.exception.http.InternalErrorException;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class CryptoConverter implements AttributeConverter<String, String> {

    private static final Pattern ENCRYPTED_VALUE_PATTER = Pattern.compile("^\\{(.+)}(.+)$");

    private final PakItDatabaseProperties pakItDatabaseProperties;

    @Override
    public String convertToDatabaseColumn(String value) {
        if (value == null) {
            return null;
        }

        final String salt = KeyGenerators.string().generateKey();
        final String encryptedValue = Encryptors
                .delux(pakItDatabaseProperties.getEncryptionKey(), salt)
                .encrypt(value);

        return "{%s}%s".formatted(salt, encryptedValue);
    }

    @Override
    public String convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        final Matcher matcher = ENCRYPTED_VALUE_PATTER.matcher(value);
        if (!matcher.matches()) {
            throw new InternalErrorException("Encrypted value does not match the expected pattern");
        }

        final String salt = matcher.group(1);
        final String encryptedValue = matcher.group(2);

        return Encryptors
                .delux(pakItDatabaseProperties.getEncryptionKey(), salt)
                .decrypt(encryptedValue);
    }
}
