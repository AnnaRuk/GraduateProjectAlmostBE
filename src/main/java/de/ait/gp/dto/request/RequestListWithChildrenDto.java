package de.ait.gp.dto.request;

import de.ait.gp.dto.child.ChildWithUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(name = "RequestListWithChildren", description = "List of requests with list of children's data")
public class RequestListWithChildrenDto {
    @NotNull
    @Schema(name = "childWithUserList", description = "List of children with user")
    private List<ChildWithUserDto> childWithUserList;
    @NotNull
    @Schema(name = "requests", description = "List of requests")
    private List<RequestDto> requests;
}
