package de.ait.gp.dto.kindergarten;

import de.ait.gp.models.Kindergarten;
import io.swagger.v3.oas.annotations.media.Schema;
import  lombok.*;

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
@Schema(name = "Kindergarten")
public class KindergartenDto {
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


    public static KindergartenDto from(Kindergarten kindergarten, String phone) {
        return KindergartenDto.builder()
                .title(kindergarten.getTitle())
                .city(kindergarten.getCity())
                .postcode(kindergarten.getPostcode())
                .address(kindergarten.getAddress())
                .capacity(kindergarten.getCapacity())
                .linkImg(kindergarten.getLinkImg())
                .description(kindergarten.getDescription())
                .phone(phone)
                .build();
    }

    public static KindergartenDto from(Kindergarten kindergarten) {
        return KindergartenDto.builder()
                .title(kindergarten.getTitle())
                .city(kindergarten.getCity())
                .postcode(kindergarten.getPostcode())
                .address(kindergarten.getAddress())
                .capacity(kindergarten.getCapacity())
                .linkImg(kindergarten.getLinkImg())
                .description(kindergarten.getDescription())
                .build();
    }


}
