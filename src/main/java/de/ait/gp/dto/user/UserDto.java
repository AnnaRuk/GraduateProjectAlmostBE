package de.ait.gp.dto.user;

import de.ait.gp.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "User", description = "Registration details")
public class UserDto {

    @Schema(description = "user's identifier", example = "1")
    private Long id;

    @Schema(description = "user's firstname", example = "Anna")
    private String firstName;

    @Schema(description = "user's lastname", example = "Bieliaieva")
    private String lastName;

    @Schema(description = "user's email", example = "user@gmail.com")
    private String email;

    @Schema(description = "user's role", example = "USER")
    private String role;


    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();
    }

    public static List<UserDto> from(Collection<User> users) {
        return users.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }
}
