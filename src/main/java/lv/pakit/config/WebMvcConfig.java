package lv.pakit.config;

import lombok.RequiredArgsConstructor;
import lv.pakit.config.interceptors.RequiresRoleInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequiresRoleInterceptor requiresRoleInterceptor;

    @Value("${pakit.upload-dir:./uploads/landing}")
    private String uploadDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requiresRoleInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absPath = Paths.get(uploadDir).toAbsolutePath().toUri().toString();
        if (!absPath.endsWith("/"))
            absPath += "/";
        registry.addResourceHandler("/uploads/landing/**")
                .addResourceLocations(absPath);
    }
}
