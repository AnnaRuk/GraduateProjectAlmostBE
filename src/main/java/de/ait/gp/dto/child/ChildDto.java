package de.ait.gp.dto.child;

import de.ait.gp.models.Child;
import de.ait.gp.utils.TimeDateFormatter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Child", description = "child's data")

public class ChildDto {
    @NotNull
    @Schema(description = "child's identifier", example = "1")
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

    @Pattern(regexp = "^(\\d{2}\\.\\d{2}\\.\\d{4}( \\d{2}:\\d{2}:\\d{2}:\\d{1})?)$")
    @Schema(name = "dateOfBirth", description = "child's date of birth", example = "05.03.1990")
    private String dateOfBirth;

    public static ChildDto from(Child child) {
        return ChildDto.builder()
                .id(child.getId())
                .firstName(child.getFirstName())
                .lastName(child.getLastName())
                .dateOfBirth(child.getDateOfBirth()!=null ? child.getDateOfBirth().format(TimeDateFormatter.DATE_FORMAT): null)
                .gender(child.getGender().toString())
                .build();
    }
    public static List<ChildDto> from(List<Child> childList) {
        return childList.stream()
                .map(ChildDto::from)
                .toList();
    }
}
