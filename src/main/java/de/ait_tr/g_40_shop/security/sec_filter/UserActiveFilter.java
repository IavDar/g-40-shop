package de.ait_tr.g_40_shop.security.sec_filter;
import de.ait_tr.g_40_shop.security.sec_service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class UserActiveFilter extends GenericFilterBean {

    private AuthService service;

    public UserActiveFilter(AuthService service) {
        this.service = service;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (!service.isUserActive(httpRequest.getRemoteUser())) {
            throw new IOException("User is not active");
        }

        filterChain.doFilter(request, response);
    }

}
