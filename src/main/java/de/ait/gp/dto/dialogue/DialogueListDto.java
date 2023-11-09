package de.ait.gp.dto.dialogue;

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
@Schema(name = "DialogueList", description = "List of dialogues with messages")
public class DialogueListDto {
    @NotNull
    @Schema(name = "dialogues", description = "List of dialogues with messages")
    private List<DialogueDto> dialogues;
}
