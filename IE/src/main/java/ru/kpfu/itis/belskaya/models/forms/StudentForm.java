package ru.kpfu.itis.belskaya.models.forms;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import ru.kpfu.itis.belskaya.models.City;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Elizaveta Belskaya
 */
@Data
@Getter
@Setter
public class StudentForm {

    @NotBlank
    @Length(min = 3, max = 255)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "8[0-9]{10}")
    private String phone;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]{8,}", message = "The password is unreliable")
    private String password;

    @NotNull
    City city;


}
