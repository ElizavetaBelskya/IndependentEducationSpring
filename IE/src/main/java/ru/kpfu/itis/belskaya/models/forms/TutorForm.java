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
    @NotNull
    private boolean gender;
    private boolean isWorkingOnline;
    private List<Subject> subjectList;



}
