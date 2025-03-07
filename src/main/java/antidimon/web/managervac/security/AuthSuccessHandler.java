package antidimon.web.managervac.security;

import antidimon.web.managervac.security.jwt.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@AllArgsConstructor
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
