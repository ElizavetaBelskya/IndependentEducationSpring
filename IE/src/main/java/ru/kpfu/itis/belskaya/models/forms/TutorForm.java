package ru.kpfu.itis.belskaya.models.forms;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import ru.kpfu.itis.belskaya.models.City;
import ru.kpfu.itis.belskaya.models.Subject;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author Elizaveta Belskaya
 */
@Data
@Getter
@Setter
public class TutorForm {

    @NotBlank(message = "Can't be empty")
    @Length(min = 3, max = 255, message = "Impossible length")
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

    @NotNull(message = "Can't be null")
    private boolean gender;

    private Boolean isWorkingOnline;

    @NotEmpty(message = "Choose your subjects")
    private List<Subject> subjects;

}
