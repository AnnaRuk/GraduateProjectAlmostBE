package de.ait.gp.dto.dialogue;

import de.ait.gp.dto.dialogue.message.MessageDto;
import de.ait.gp.dto.user.UserInRequestAndDialogDto;
import de.ait.gp.models.Dialogue;
import de.ait.gp.models.Message;
import de.ait.gp.models.User;
import de.ait.gp.utils.UserUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Dialogue", description = "Dialogue data")
public class DialogueDto {
    @NotNull
    @Schema(name = "id", description = "Dialogue's identifier", example = "1")
    private Long id;
    @NotNull
    @Schema(name = "recipient", description = "recipient's data")
    private UserInRequestAndDialogDto recipient;
    @NotNull
    @Schema(name = "messages", description = "List of messages")
    private List<MessageDto> messages;

    public static DialogueDto from(Dialogue dialogue, Long userId) {
        User recipient = UserUtils.getRecipient(dialogue.getMembers(), userId);
        return DialogueDto.builder()
                .id(dialogue.getId())
                .recipient(UserInRequestAndDialogDto.from(recipient))
                .messages(MessageDto.from(dialogue.getMessages()
                        .stream()
                        .sorted(Comparator.comparing(Message::getDateTime).reversed())
                        .toList())
                )
                .build();
    }

    public static List<DialogueDto> from(List<Dialogue> dialogues, Long userId) {
        return dialogues.stream()
                .map(dialogue -> DialogueDto.from(dialogue, userId))
                .sorted((d1, d2) ->
                        d2.messages.get(d2.messages.size() - 1).getMessageDateTime()
                                .compareTo(d1.messages.get(d1.messages.size() - 1).getMessageDateTime())
                ).toList();
    }

}
