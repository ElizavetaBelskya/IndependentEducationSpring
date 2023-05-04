package ru.kpfu.itis.belskaya.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kpfu.itis.belskaya.models.User;
import ru.kpfu.itis.belskaya.services.AccountService;
import ru.kpfu.itis.belskaya.services.UserService;

/**
 * @author Elizaveta Belskaya
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/tutor/register", "/student/register").permitAll()
                .antMatchers("/tutor/**").hasAuthority("TUTOR")
                .antMatchers("/student/**").hasAuthority("STUDENT")
                .and()
                .formLogin()
                .loginPage("/main").permitAll()
                .defaultSuccessUrl("/main?status=success")
                .failureUrl("/main?status=failed")
                .usernameParameter("emailAndRole")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/main?status=logout")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    public AccountService accountService(){
        return new AccountService();
    }

}
