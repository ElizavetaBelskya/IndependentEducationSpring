package ru.kpfu.itis.belskaya.models.forms;

import lombok.*;
import ru.kpfu.itis.belskaya.models.Order;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Elizaveta Belskaya
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderForm {

    @NotBlank(message = "It can't be empty")
    private String subject;

    private String description;

    private String gender;

    private Boolean rating;

    private String online;

    @NotNull
    @Min(value = 0, message = "Price must be a positive value")
    @Max(value = 10000, message = "The price should be adequate")
    private Integer price;



}
