package de.ait.gp.dto.child;

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
@Schema(name = "ChildLList")
public class ChildDtoList {
    @NotNull
    @Schema(name = "children", description = "list of children")
    List<ChildDto> children;
}
