package ru.kpfu.itis.belskaya;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.kpfu.itis.belskaya.config.RootConfig;
import ru.kpfu.itis.belskaya.config.SecurityConfig;
import ru.kpfu.itis.belskaya.config.WebConfig;

/**
 * @author Elizaveta Belskaya
 */
@Configuration
@EnableAutoConfiguration
@Import({RootConfig.class, WebConfig.class, SecurityConfig.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }


}
