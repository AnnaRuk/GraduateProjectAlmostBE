package de.ait.gp.dto.kindergarten;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(name = "KindergartensWithCities")
public class KindergartensWithCitiesDto {
    @NotNull
    @Schema(name = "cities", description = "List of cities of kindergartens")
    private List<String> cities;
    @NotNull
    @Schema(name = "KindergartenList", description = "List of kindergartens")
    private List<KindergartenDto> KindergartenDTOList;

}
