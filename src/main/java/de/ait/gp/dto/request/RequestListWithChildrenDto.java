package de.ait.gp.dto.request;

import de.ait.gp.dto.child.ChildWithUserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(name = "RequestListWithChildren", description = "List of requests with children's data")
public class RequestListWithChildrenDto {

    @Schema(name = "childWithUserList", description = "List of children with user")
    private List<ChildWithUserDto> childrenWithUser;
    @Schema(name = "requests", description = "List of requests")
    private List<RequestDto> requests;
}
