package de.ait.gp.dto.kindergarten;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "KindergartenToFavorite")
public class KindergartenToFavoriteDto {
    private Long kindergartenId;
}
