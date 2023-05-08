package ru.kpfu.itis.belskaya.authentication;

import lombok.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kpfu.itis.belskaya.models.Account;
import ru.kpfu.itis.belskaya.services.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EmailRolePasswordAuthenticationProvider implements AuthenticationProvider {

    private AccountService accountService;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailRolePasswordToken token = (EmailRolePasswordToken) authentication;
        String email = token.getName();
        String role = token.getRole();
        String password = authentication.getCredentials() == null? "" : authentication.getCredentials().toString();
        Optional<Account> user = accountService.findAccount(email, Account.Role.valueOf(role));
        if (!user.isPresent()) {
            throw new BadCredentialsException("Invalid email, role or password");
        }
        Account account = user.get();
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new BadCredentialsException("Invalid email, role or password");
        }

        List<GrantedAuthority> authorities = new ArrayList<>(account.getAuthorities());
        return new UsernamePasswordAuthenticationToken(account, password, authorities);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(EmailRolePasswordToken.class);
    }

}
