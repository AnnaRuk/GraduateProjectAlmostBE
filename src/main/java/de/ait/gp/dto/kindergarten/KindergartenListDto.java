package de.ait.gp.dto.kindergarten;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "KindergartenList")
public class KindergartenListDto {

    @NotNull
    @Schema(name = "kindergartens", description = "List of favorite kindergartens")
    List<KindergartenDto> kindergartens;




}
