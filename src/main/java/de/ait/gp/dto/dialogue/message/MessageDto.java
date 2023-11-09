package de.ait.gp.dto.dialogue.message;

import de.ait.gp.models.Message;
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
@Schema(name = "Message", description = "Message data")
public class MessageDto {
    @NotNull
    @Schema(name = "id", description = "Message's identifier", example = "1")
    private Long id;
    @NotNull
    @Schema(name = "senderId", description = "Sender's identifier", example = "1")
    private Long senderId;
    @Schema(name = "messageDateTime", description = "Message's date and time", example = "1990-03-05 10:44:14.000000")
    private String messageDateTime;
    @NotEmpty
    @NotBlank
    @Schema(name = "messageText", description = "text of message", example = "Kurlyk! Kurlyk!")
    private String messageText;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .messageText(message.getMessageText())
                .messageDateTime(message.getDateTime().toString())
                .build();
    }
    public static List<MessageDto> from(List<Message> messages) {
        return messages.stream()
                .map(MessageDto::from)
                .toList();
    }
}
