package de.ait.gp.dto.user;

import de.ait.gp.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static de.ait.gp.dto.Gender.NOT_SELECTED;
import static de.ait.gp.utils.TimeDateFormatter.DATE_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "User", description = "Registration details")
public class UserDto {

    @Schema(description = "user's identifier", example = "1")
    private Long id;
    @NotBlank
    @NotEmpty
    @Schema(description = "user's firstname", example = "Anna")
    private String firstName;
    @NotBlank
    @NotEmpty
    @Schema(description = "user's lastname", example = "Bieliaieva")
    private String lastName;
    @NotBlank
    @NotEmpty
    @Schema(description = "user's email", example = "user@gmail.com")
    private String email;
    @NotBlank
    @NotEmpty
    @Schema(description = "user's role", example = "USER")
    private String role;

    @Schema(name = "postcode", description = "user's postcode", example = "46446")
    private String postcode;
    @Schema(name = "address", description = "user's address", example = "Berlin str. 8")
    private String address;
    @Schema(name = "city", description = "user's city", example = "Berlin")
    private String city;
    @Schema(name = "phone", description = "user's phone", example = "+495451619")
    private String phone;
    @Schema(name = "dateOfBirth", description = "user's date of birth", example = "1990-05-03")
    private String dateOfBirth;

    @Enumerated(value = EnumType.STRING)
    @Schema(name = "gender", description = "user's gender", example = "MALE")
    private String gender;


    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .postcode(user.getPostcode())
                .address(user.getAddress())
                .city(user.getCity())
                .phone(user.getPhone())
                .dateOfBirth(user.getDateOfBirth() != null ? user.getDateOfBirth().format(DATE_FORMAT) : null)
                .gender(user.getGender()!=null ? user.getGender().toString() : NOT_SELECTED.toString())
                .build();
    }

    public static List<UserDto> from(Collection<User> users) {
        return users.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }
}
