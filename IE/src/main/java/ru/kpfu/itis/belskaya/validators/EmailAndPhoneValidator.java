package ru.kpfu.itis.belskaya.validators;

import lombok.AllArgsConstructor;
import okhttp3.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@AllArgsConstructor
@PropertySource("classpath:app.properties")
public class EmailAndPhoneValidator {

    @Resource
    private Environment env;

    public boolean validateEmail(String email) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(env.getRequiredProperty("validator.email.url") + email)
                .get()
                .addHeader("X-RapidAPI-Key", env.getRequiredProperty("validator.key"))
                .addHeader("X-RapidAPI-Host", env.getRequiredProperty("validator.email.host"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                if (responseBody.contains("\"valid\":true")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public boolean validatePhone(String phone, String countryCode) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(env.getRequiredProperty("validator.phone.url") + phone + "&country=" + countryCode)
                .get()
                .addHeader("X-RapidAPI-Key", env.getRequiredProperty("validator.key"))
                .addHeader("X-RapidAPI-Host", env.getRequiredProperty("validator.phone.host") )
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println(responseBody);
            if (response.isSuccessful()) {

                if (responseBody.contains("\"valid\":true")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

    }





}
