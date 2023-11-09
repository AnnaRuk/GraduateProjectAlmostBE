package de.ait.gp.dto.dialogue.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "NewMessage", description = "New Message data")
public class NewMessageDto {
    @NotNull
    @Schema(name = "recipientId",description = "recipient's identifier", example = "1")
    private Long recipientId;
    @NotEmpty
    @NotBlank
    @Schema(name = "messageText", description = "text of message", example = "Kurlyk! Kurlyk!")
    private String messageText;

}
