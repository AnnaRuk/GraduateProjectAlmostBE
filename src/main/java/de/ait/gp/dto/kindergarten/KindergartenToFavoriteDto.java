package de.ait.gp.dto.kindergarten;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "KindergartenToFavorite", description = "Kindergarten's Id to add in favorites")
public class KindergartenToFavoriteDto {
    private Long kindergartenId;
}
