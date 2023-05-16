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

    @NotBlank(message = "Can't be empty")
    @Length(min = 1, max = 30, message = "Impossible length")
    @Pattern(regexp = "[A-Z][a-z]{1,30}")
    private String name;

    @NotBlank(message = "Can't be empty")
    @Email(message = "Doesn't meet the Email requirement")
    private String email;

    @NotBlank(message = "Can't be empty")
    @Pattern(regexp = "8[0-9]{10}", message = "Doesn't meet phone requirement")
    private String phone;

    @NotBlank(message = "Can't be empty")
    @Pattern(regexp = "[A-Za-z0-9]{8,}", message = "The password is unreliable")
    private String password;

    @NotNull(message = "Can't be null")
    City city;


}
