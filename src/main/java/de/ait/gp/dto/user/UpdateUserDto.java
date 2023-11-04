package de.ait.gp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "UpdateUser")
public class UpdateUserDto {

    @NotEmpty
    @NotBlank
    @Schema(name = "firstName", description = "user's firstname", example = "Sergey")
    private String firstName;

    @NotEmpty
    @NotBlank
    @Schema(name = "lastName", description = "user's lastname", example = "Sedakov")
    private String lastName;

    @NotNull
    @Pattern(regexp = "^(\\d{2}\\.\\d{2}\\.\\d{4}( \\d{2}:\\d{2}:\\d{2}:\\d{1})?)$")
    @Schema(name = "dateOfBirth", description = "user's date of birth", example = "05.03.1990")
    private String dateOfBirth;

    @Email
    @NotNull
    @Schema(name = "email", description = "user's email", example = "user@gmail.com")
    private String email;

    @NotEmpty
    @NotBlank
    @Schema(name = "postCode", description = "user's postcode", example = "46446")
    private String postCode;

    @NotEmpty
    @NotBlank
    @Schema(name = "address", description = "user's addres", example = "Berlinstr. 8")
    private String address;

    @NotEmpty
    @NotBlank
    @Schema(name = "city", description = "user's city", example = "Berlin")
    private String city;
    @NotEmpty
    @NotBlank
    @Schema(name = "phone", description = "user's phone", example = "+495451619")
    private String phone;

    @NotNull
    @NotBlank
    @Schema(name = "gender", description = "user's gender", example = "MALE")
    private String gender;

}
