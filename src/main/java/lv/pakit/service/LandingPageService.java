package lv.pakit.service;

import lombok.RequiredArgsConstructor;
import lv.pakit.exception.http.BadRequestException;
import lv.pakit.exception.http.NotFoundException;
import lv.pakit.model.LandingPageConfig;
import lv.pakit.repo.ILandingPageConfigRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LandingPageService {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp");

    private final ILandingPageConfigRepo repo;

    @Value("${pakit.upload-dir:./uploads/landing}")
    private String uploadDir;

    public List<LandingPageConfig> getAllOrdered() {
        return repo.findAllByOrderBySortOrderAsc();
    }

    public Map<String, String> getLvValues() {
        return repo.findAll().stream()
                .collect(Collectors.toMap(
                        LandingPageConfig::getConfigKey,
                        c -> c.getValueLv() != null ? c.getValueLv() : ""));
    }

    public Map<String, String> getEnValues() {
        return repo.findAll().stream()
                .collect(Collectors.toMap(
                        LandingPageConfig::getConfigKey,
                        c -> c.getValueEn() != null ? c.getValueEn() : ""));
    }

    public void updateText(String key, String valueLv, String valueEn) {
        LandingPageConfig config = requireByKey(key);
        if (!"text".equals(config.getConfigType())) {
            throw new BadRequestException("Config key is not a text field: " + key);
        }
        config.setValueLv(valueLv);
        config.setValueEn(valueEn);
        repo.save(config);
    }

    public String uploadImage(String key, MultipartFile file) throws IOException {
        LandingPageConfig config = requireByKey(key);
        if (!"image".equals(config.getConfigType())) {
            throw new BadRequestException("Config key is not an image field: " + key);
        }
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("No file provided");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new BadRequestException("Invalid file type. Allowed: jpg, png, gif, webp");
        }

        Path dir = Paths.get(uploadDir).toAbsolutePath();
        Files.createDirectories(dir);

        String ext = getExtension(file.getOriginalFilename());
        String filename = key + "_" + System.currentTimeMillis() + ext;
        Path dest = dir.resolve(filename);
        file.transferTo(dest);

        String publicPath = "/uploads/landing/" + filename;
        config.setValueLv(publicPath);
        repo.save(config);

        return publicPath;
    }

    private LandingPageConfig requireByKey(String key) {
        return repo.findById(key)
                .orElseThrow(() -> new NotFoundException("Landing page config not found: " + key));
    }

    private String getExtension(String filename) {
        if (filename == null)
            return ".jpg";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot).toLowerCase() : ".jpg";
    }
}
