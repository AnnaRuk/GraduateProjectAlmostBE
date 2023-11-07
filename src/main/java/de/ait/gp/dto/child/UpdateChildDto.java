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
@Schema(name = "UpdateChild")
public class UpdateChildDto {
    @NotNull
    @Schema(description = "Child's identifier", example = "1")
    private Long id;
    @NotBlank
    @NotEmpty
    @Schema(name = "firstName", description = "child's firstname", example = "Sergey")
    private String firstName;
    @NotBlank
    @NotEmpty
    @Schema(name = "lastName", description = "child's lastname", example = "Sedakov")
    private String lastName;
    @NotEmpty
    @NotBlank
    @Schema(name = "gender", description = "child's gender", example = "MALE")
    private String gender;

    @Pattern(regexp = "^(?:19|20)\\d\\d-(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2\\d|3[0-1])$")
    @Schema(name = "dateOfBirth", description = "child's date of birth", example = "1990-03-05")
    private String dateOfBirth;
}
