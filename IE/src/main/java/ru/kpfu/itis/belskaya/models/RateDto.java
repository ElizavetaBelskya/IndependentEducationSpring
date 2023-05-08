package ru.kpfu.itis.belskaya.models;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateDto {
    private Integer rating;

    private String comment;

    private Student student;
    private Long tutorId;

}
