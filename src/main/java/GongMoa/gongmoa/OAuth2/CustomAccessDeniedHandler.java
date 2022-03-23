package GongMoa.gongmoa.OAuth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated()) {
            response.sendRedirect("/login-form");
        } else if(authentication.getAuthorities()
                .stream().anyMatch(r -> r.getAuthority().equals(Role.NOT_VALID.getKey()))) {
            log.info("not validated");
            request.getRequestDispatcher("/errors/not-validated").forward(request, response);
        } else {
            throw new RuntimeException("처리되지 않은 denied access");
        }
    }
}
