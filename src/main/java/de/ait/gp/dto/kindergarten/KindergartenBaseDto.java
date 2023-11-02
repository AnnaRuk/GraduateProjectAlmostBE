package de.ait.gp.dto.kindergarten;

import de.ait.gp.models.Kindergarten;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(name = "KindergartenBase")
public class KindergartenBaseDto {


    @NotBlank
    @NotEmpty
    @Schema(name = "title", description = "title of Kindergarten")
    private String title;
    @NotBlank
    @NotEmpty
    @Schema(name = "city", description = "city of Kindergarten")
    private String city;
    @NotBlank
    @NotEmpty
    @Schema(name = "address", description = "address of Kindergarten")
    private String address;
    @NotNull
    @Schema(name = "capacity", description = "capacity of Kindergarten")
    private Integer capacity;


    public static KindergartenBaseDto from(Kindergarten kindergarten) {
        return KindergartenBaseDto.builder()
                .title(kindergarten.getTitle())
                .city(kindergarten.getCity())
                .address(kindergarten.getAddress())
                .capacity(kindergarten.getCapacity())
                .build();
    }
    public static List<KindergartenBaseDto> from(List<Kindergarten> kindergartenList) {
        return kindergartenList.stream()
                .map(KindergartenBaseDto::from)
                .toList();
    }
}