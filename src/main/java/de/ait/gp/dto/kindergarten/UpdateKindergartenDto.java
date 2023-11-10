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
@Schema(name = "UpdateKindergarten", description = "Kindergarten's data to update")
public class UpdateKindergartenDto {
    @NotNull
    @Schema(description = "Kindergarten's identifier", example = "1")
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
    @Schema(name = "address", description = "address of Kindergarten", example = "Address,11")
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
    @Schema(name = "linkImg", description = "Link to kindergarten's image", example = "ImageLink")
    private String linkImg;
    @NotBlank
    @NotEmpty
    @Schema(name = "description", description = "description of Kindergarten", example = "Kindergarten's description")
    private String description;

}
