package de.ait.gp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Message", description = "Any message from the server")
public class StandardResponseDto {
    @Schema(description = "Possible: error message, status change, etc.", example = "Error or any other message from the server")
    private String message;
}
