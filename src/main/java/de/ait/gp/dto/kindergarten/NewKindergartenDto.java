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
    @Schema(name = "linkImg", description = "Link to kindergarten's image")
    private String linkImg;
    @NotBlank
    @NotEmpty
    @Schema(name = "description", description = "description of Kindergarten")
    private String description;
}
