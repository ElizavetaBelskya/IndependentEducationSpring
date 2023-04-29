package ru.kpfu.itis.belskaya.models.forms;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Getter
@Setter
public class LoginForm {

    @NotBlank(message = "It can't be empty")
    private String email;

    @NotBlank(message = "It can't be empty")
    private String password;

    private String role;

    private String emailAndRole;

}
