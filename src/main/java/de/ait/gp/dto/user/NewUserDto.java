package de.ait.gp.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "NewUser", description = "Registration details")
public class NewUserDto {

    @Email
    @NotNull
    @Schema(description = "user's email", example = "user@gmail.com")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    @Schema(description = "user's password", example = "Qwerty007!")
    private String hashPassword;

    @NotNull
    @NotEmpty
    @NotBlank
    @Schema(description = "user's name", example = "Anna")
    private String firstName;

    @NotNull
    @NotEmpty
    @NotBlank
    @Schema(description = "user's lastname", example = "Bieliaieva")
    private String lastName;

    @NotNull
    @Schema(description = "user's role", example = "USER/MANAGER")
    private String role;

    @Schema(description = "user's gender", example = "MALE/FEMALE/DIVERSE")
    private String gender;


}
