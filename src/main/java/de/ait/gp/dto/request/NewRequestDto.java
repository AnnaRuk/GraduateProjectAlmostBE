package de.ait.gp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "NewRequest", description = "New request data")
public class NewRequestDto {
    @NotNull
    @Schema(name = "childId",description = "child's identifier", example = "1")
    private Long childId;
    @NotNull
    @Schema(name = "kindergartenId",description = "kindergarten's identifier", example = "1")
    private Long kindergartenId;

}
