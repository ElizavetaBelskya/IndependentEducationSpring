package ru.kpfu.itis.belskaya.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmailRolePasswordFilter extends UsernamePasswordAuthenticationFilter {

    public EmailRolePasswordFilter(AuthenticationManager authenticationManager, String authPage, String failureUrl, String successUrl) {
        super();
        setAuthenticationManager(authenticationManager);
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(authPage, "POST"));
        super.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureUrl));
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl(successUrl);
        super.setAuthenticationSuccessHandler(successHandler);
        setUsernameParameter("email");
        setPasswordParameter("password");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        EmailRolePasswordToken authRequest = new EmailRolePasswordToken(email, password, role);

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }


}
