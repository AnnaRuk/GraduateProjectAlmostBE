package de.ait.gp.dto.request;

import de.ait.gp.models.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Request", description = "Request data")
public class RequestDto {
    @NotNull
    @Schema(name = "id",description = "request's identifier", example = "1")
    private Long id;
    @NotNull
    @Schema(name = "childId",description = "child's identifier", example = "1")
    private Long childId;
    @NotNull
    @Schema(name = "kindergartenId",description = "kindergarten's identifier", example = "1")
    private Long kindergartenId;
    @NotEmpty
    @NotBlank
    @Schema(name = "status",description = "request's status", example = "CONFIRMED")
    private String status;
    @Schema(name = "requestDateTime", description = "request's date and time", example = "1990-03-05 10:44:14.000000")
    private String requestDateTime;

    public static RequestDto from(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .childId(request.getChild().getId())
                .kindergartenId(request.getKindergarten().getId())
                .requestDateTime(request.getRequestDateTime().toString())
                .status(request.getStatus()!=null ? request.getStatus().toString() : null)
                .build();
    }
    public static List<RequestDto> from(List<Request> requests) {
        return requests.stream()
                .map(RequestDto::from)
                .toList();
    }
}
