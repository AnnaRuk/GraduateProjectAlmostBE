package de.ait.gp.dto.kindergarten;

import de.ait.gp.models.Kindergarten;
import io.swagger.v3.oas.annotations.media.Schema;
import  lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @NotBlank
    @NotEmpty
    @Schema(name = "postcode", description = "postcode of Kindergarten")
    private String postcode;
    @NotNull
    @Schema(name = "capacity", description = "capacity of Kindergarten")
    private Integer capacity;
    @NotBlank
    @NotEmpty
    @Schema(name = "Link", description = "Link to kindergarten's image")
    private String linkImg;
    @NotBlank
    @NotEmpty
    @Schema(name = "phone", description = "phone of Kindergarten's manager")
    private String phone;
    @NotBlank
    @NotEmpty
    @Schema(name = "description", description = "description of Kindergarten")
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


}
