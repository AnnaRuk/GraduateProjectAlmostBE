package de.ait.gp.validation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "ValidationError", description = "Description of the validation error")
public class ValidationErrorDto {

    @Schema(description = "name of the field in which the error occurred", example = "price")
    private String field;
    @Schema(description = "the value entered by the user and which was rejected by the server", example = "1000.0")
    private String rejectedValue;
    @Schema(description = "the message we need to show to the user", example = "must be less than or equal to 200")
    private String message;
}
