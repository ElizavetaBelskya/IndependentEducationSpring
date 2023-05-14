package ru.kpfu.itis.belskaya;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.kpfu.itis.belskaya.config.OpenApiConfig;
import ru.kpfu.itis.belskaya.config.RootConfig;
import ru.kpfu.itis.belskaya.config.SecurityConfig;
import ru.kpfu.itis.belskaya.config.WebConfig;

/**
 * @author Elizaveta Belskaya
 */
@Configuration
@EnableAutoConfiguration
@Import({RootConfig.class, WebConfig.class, SecurityConfig.class, OpenApiConfig.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }


}
