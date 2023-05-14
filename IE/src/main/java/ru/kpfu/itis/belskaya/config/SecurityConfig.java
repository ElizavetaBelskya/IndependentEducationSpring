package ru.kpfu.itis.belskaya.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.kpfu.itis.belskaya.authentication.EmailRolePasswordAuthenticationProvider;
import ru.kpfu.itis.belskaya.authentication.EmailRolePasswordFilter;
import ru.kpfu.itis.belskaya.services.AccountService;

/**
 * @author Elizaveta Belskaya
 */

@Configuration
@EnableWebSecurity
@ComponentScan("ru.kpfu.itis.belskaya.config")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public EmailRolePasswordAuthenticationProvider provider() {
        return new EmailRolePasswordAuthenticationProvider(accountService(), passwordEncoder());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider());
                //.userDetailsService(accountService()).passwordEncoder(passwordEncoder());
    }


    public EmailRolePasswordFilter filter() throws Exception {
        return new EmailRolePasswordFilter(authenticationManagerBean(), "/main?status=failed", "/main?status=success");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAt(filter(), UsernamePasswordAuthenticationFilter.class).authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/main").permitAll()
                .usernameParameter("email").passwordParameter("password")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/main?status=logout").and()
                .exceptionHandling().accessDeniedPage("/forbidden")
                .and()
                .csrf().ignoringAntMatchers("/main")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
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
