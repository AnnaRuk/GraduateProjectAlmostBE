package de.ait.gp.dto.child;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "NewChild")
public class NewChildDto {
    @NotBlank
    @NotEmpty
    @Schema(name = "firstName", description = "child's firstname", example = "Sergey")
    private String firstName;
    @NotBlank
    @NotEmpty
    @Schema(name = "lastName", description = "child's lastname", example = "Sedakov")
    private String lastName;
    @NotNull
    @NotBlank
    @Schema(name = "gender", description = "child's gender", example = "MALE")
    private String gender;
    @NotNull
    @Pattern(regexp = "^(\\d{2}\\.\\d{2}\\.\\d{4}( \\d{2}:\\d{2}:\\d{2}:\\d{1})?)$")
    @Schema(name = "dateOfBirth", description = "child's date of birth", example = "05.03.1990")
    private String dateOfBirth;
}
