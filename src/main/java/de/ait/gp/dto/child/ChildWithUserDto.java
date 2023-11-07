package de.ait.gp.dto.child;


import de.ait.gp.dto.user.UserInRequestAndDialogDto;
import de.ait.gp.models.Child;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ChildWithUser", description = "child's and user's data in request")
public class ChildWithUserDto {
    @NotNull
    @Schema(name = "child", description = "child's data in request")
    private ChildDto child;
    @NotNull
    @Schema(name = "userInRequestAndDialog", description = "user's data in request")
    private UserInRequestAndDialogDto userInRequestAndDialog;

    public static ChildWithUserDto from(Child child) {
        return ChildWithUserDto.builder()
                .child(ChildDto.from(child))
                .userInRequestAndDialog(UserInRequestAndDialogDto.from(child.getParent()))
                .build();
    }
    public static List<ChildWithUserDto> from(List<Child> children) {
        return children.stream()
                .map(ChildWithUserDto::from)
                .toList();
    }
}
