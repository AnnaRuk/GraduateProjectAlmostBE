package de.ait.gp.dto.kindergarten;

import de.ait.gp.models.Kindergarten;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Kindergarten List")
public class KindergartenDtoList {

    @NotNull
    @Schema(name = "kindergartens", description = "all favorite kindergartens")
    List<KindergartenDto> kindergartens;




}
