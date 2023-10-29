package de.ait.gp.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@Schema(name = "NewAddress", description = "address details")
public class NewAddressDto {


    @NotNull
    @NotEmpty
    @NotBlank
    @Schema(description = "German city", example = "Berlin")
    private String city;

    @NotNull
    @NotEmpty
    @NotBlank
    @Schema(description = "zip code/postcode", example = "10245")
    private String postcode;

    @NotNull
    @NotEmpty
    @NotBlank
    @Schema(description = "street + house number", example = "Alexanderstrasse, 7")
    private String address;

}
