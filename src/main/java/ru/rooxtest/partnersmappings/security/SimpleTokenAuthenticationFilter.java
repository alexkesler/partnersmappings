package ru.rooxtest.partnersmappings.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import ru.rooxtest.partnersmappings.security.crypt.CryptService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр, реализующий авторизацию по токену
 */
public class SimpleTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private CryptService cryptService;

    private final String AUTH_HEADER = "Authorization";

    public SimpleTokenAuthenticationFilter(String defaultFilterProcessesUrl,
                                           AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl);
        super.setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(new TokenSimpleUrlAuthenticationSuccessHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String encryptedToken = httpServletRequest.getHeader(AUTH_HEADER);
        if (encryptedToken==null || encryptedToken.isEmpty()) throw new AuthenticationServiceException("Token not found");

        try {
            Token token = cryptService.decryptToken(encryptedToken);
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(token.getUserName(), token.getPassword()));
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }

    }

}
