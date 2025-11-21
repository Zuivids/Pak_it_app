package lv.pakit.config.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lv.pakit.annotations.RequiresRole;
import lv.pakit.exception.http.BadRequestException;
import lv.pakit.service.auth.AuthService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class RequiresRoleInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Method method = handlerMethod.getMethod();
        RequiresRole annotation = method.getAnnotation(RequiresRole.class);
        if (annotation == null || authService.hasRole(annotation.value())) {
            return true;
        }

        if (annotation.page()) {
            response.sendRedirect("/home");
            return false;
        }

        throw new BadRequestException("Action not authorized");
    }
}
