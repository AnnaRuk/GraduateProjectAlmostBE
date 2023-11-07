package de.ait.gp.dto.user;

import de.ait.gp.models.User;
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
@Schema(name = "UserInRequest", description = "User in request data")
public class UserInRequestAndDialogDto {
    @NotNull
    @Schema(description = "user's identifier", example = "1")
    private Long id;
    @NotBlank
    @NotEmpty
    @Schema(name = "firstName", description = "user's firstname", example = "Sergey")
    private String firstName;
    @NotBlank
    @NotEmpty
    @Schema(name = "lastName", description = "user's lastname", example = "Sedakov")
    private String lastName;

    public static UserInRequestAndDialogDto from(User user) {
        return UserInRequestAndDialogDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
