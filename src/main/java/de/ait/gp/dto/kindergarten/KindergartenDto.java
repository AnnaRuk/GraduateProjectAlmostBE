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
@Schema(name = "Kindergarten")
public class KindergartenDto {
    @Schema(description = "kindergarten's identifier", example = "1")
    private Long id;
    @NotBlank
    @NotEmpty
    @Schema(name = "title", description = "title of Kindergarten", example = "Title")
    private String title;
    @NotBlank
    @NotEmpty
    @Schema(name = "city", description = "city of Kindergarten", example = "City")
    private String city;
    @NotBlank
    @NotEmpty
    @Schema(name = "address", description = "address of Kindergarten", example = "Address, 11")
    private String address;
    @NotBlank
    @NotEmpty
    @Schema(name = "postcode", description = "postcode of Kindergarten", example = "1234567")
    private String postcode;
    @NotNull
    @Schema(name = "capacity", description = "capacity of Kindergarten", example = "50")
    private Integer capacity;
    @NotBlank
    @NotEmpty
    @Schema(name = "Link", description = "Link to kindergarten's image", example = "ImageLink")
    private String linkImg;
    @NotBlank
    @NotEmpty
    @Schema(name = "phone", description = "phone of Kindergarten's manager", example = "+123456789")
    private String phone;
    @NotBlank
    @NotEmpty
    @Schema(name = "description", description = "description of Kindergarten", example = "Kindergarten's description")
    private String description;


    public static KindergartenDto from(Kindergarten kindergarten) {
        return KindergartenDto.builder()
                .id(kindergarten.getId())
                .title(kindergarten.getTitle())
                .city(kindergarten.getCity())
                .postcode(kindergarten.getPostcode())
                .address(kindergarten.getAddress())
                .capacity(kindergarten.getCapacity())
                .linkImg(kindergarten.getLinkImg())
                .description(kindergarten.getDescription())
                .phone(kindergarten.getManager()!=null ? kindergarten.getManager().getPhone() : null)
                .build();
    }
    public static List<KindergartenDto>     from(List<Kindergarten> kindergartens) {
        return kindergartens.stream()
                .map(KindergartenDto::from)
                .toList();
    }




}
