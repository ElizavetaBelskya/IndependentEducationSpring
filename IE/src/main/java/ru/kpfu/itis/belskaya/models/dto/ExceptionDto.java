package ru.kpfu.itis.belskaya.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Error information")
public class ExceptionDto {

    @Schema(description = "Error message", example = "Order not found")
    private String message;
    @Schema(description = "HTTP-status of error", example = "404")
    private int status;

}
