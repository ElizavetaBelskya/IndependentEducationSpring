package ru.kpfu.itis.belskaya.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Getter
@Setter
public class EmailRolePasswordToken extends UsernamePasswordAuthenticationToken {

    private String email;

    private String password;

    private String role;

    public EmailRolePasswordToken(String email, String password, String role) {
        super(email, password);
        this.email = email;
        this.role = role;
    }



}
