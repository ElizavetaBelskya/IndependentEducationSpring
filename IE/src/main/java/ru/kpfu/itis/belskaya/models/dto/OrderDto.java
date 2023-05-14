package ru.kpfu.itis.belskaya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.Tutor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order")
public class OrderDto {

    @Schema(description = "Order id", example = "1")
    private Long id;

    @Schema(description = "Subject of order", example = "Math")
    @NotBlank(message = "It can't be empty")
    private String subject;

    @Schema(description = "Student id", example = "1")
    private Long studentId;

    @Schema(description = "Comment to order", example = "I need the professional school teacher")
    private String description;

    @Schema(description = "Gender of potential tutor", example = "MALE")
    @NotBlank(message = "It can't be empty")
    private String tutorGender;

    @Schema(description = "Minimal rating for potential tutor", example = "4.0")
    @NotNull(message = "It can't be empty")
    private float minRating;

    @Schema(description = "Date of order creation", example = "")
    private LocalDateTime creationDate;

    @Schema(description = "Tutor id", example = "1")
    private Long tutorId;

    @Schema(description = "List of candidates id", example = "[1, 2, 3]")
    private List<Long> candidates;

    @Schema(description = "Format of learning", example = "ONLINE")
    @NotBlank(message = "It can't be empty")
    private String online;

    @Schema(description = "Potential price", example = "800")
    @NotNull(message = "It can't be empty")
    @Min(value = 100, message = "The price can't be less than 100")
    @Max(value = 10000, message = "The price can't be greater than 10000")
    private Integer price;



}
