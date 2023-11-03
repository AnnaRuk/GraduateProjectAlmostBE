package de.ait.gp.dto.kindergarten;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "NewKindergarten")
public class NewKindergartenDto {
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
    @Schema(name = "postcode", description = "postcode of Kindergarten", example = "4954516")
    private String postcode;
    @NotNull
    @Schema(name = "capacity", description = "capacity of Kindergarten", example = "50")
    private Integer capacity;
    @NotBlank
    @NotEmpty
    @Schema(name = "linkImg", description = "Link to kindergarten's image", example = "ImageLink")
    private String linkImg;
    @NotBlank
    @NotEmpty
    @Schema(name = "description", description = "description of Kindergarten", example = "Kindergarten description")
    private String description;
}
