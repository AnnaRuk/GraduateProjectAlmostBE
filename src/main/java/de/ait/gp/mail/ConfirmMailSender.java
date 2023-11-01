package de.ait.gp.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class ConfirmMailSender {
    private final JavaMailSender javaMailSender;

    @Async
    public void send(String mail, String subject, String text){

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        try {
            helper.setTo(mail);
            helper.setSubject(subject);
            helper.setFrom("beliaeva.job@gmail.com");
            helper.setText(text, true);
        } catch (MessagingException e){
            throw new IllegalStateException(e);
        }
        javaMailSender.send(message);
    }
}
