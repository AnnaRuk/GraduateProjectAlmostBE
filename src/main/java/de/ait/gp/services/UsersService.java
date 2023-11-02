package de.ait.gp.services;

import de.ait.gp.dto.user.NewUserDto;
import de.ait.gp.dto.user.UserDto;
import de.ait.gp.exceptions.RestException;
import de.ait.gp.mail.ConfirmMailSender;
import de.ait.gp.mail.MailTemplatesUtil;
import de.ait.gp.models.ConfirmationCode;
import de.ait.gp.models.User;
import de.ait.gp.repositories.ConfirmationCodeRepository;
import de.ait.gp.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@EnableAsync
@Service
@RequiredArgsConstructor
public class UsersService {

   private final UsersRepository usersRepository;
   private final PasswordEncoder passwordEncoder;
   private final ConfirmationCodeRepository confirmationCodeRepository;
   private final ConfirmMailSender confirmMailSender;
    private final MailTemplatesUtil mailTemplatesUtil;

   @Value("${base.url}")
   private String baseUrl;


   @Transactional
    public UserDto register(NewUserDto newUser) {

        if (usersRepository.existsByEmail(newUser.getEmail())){
            throw new RestException(HttpStatus.CONFLICT, "User with email <" + newUser.getEmail() + "> already exists");
        }

        User user = User.builder()
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .hashPassword(passwordEncoder.encode(newUser.getPassword()))
                .role(User.Role.USER)
                .state(User.State.NOT_CONFIRMED)
                .build();

        usersRepository.save(user);

        String valueCode = UUID.randomUUID().toString();

        ConfirmationCode code = ConfirmationCode.builder()
                .code(valueCode)
                .expiredDateTime(LocalDateTime.now().plusMonths(1))
                .userByCode(user)
                .build();

        confirmationCodeRepository.save(code);

        String link = baseUrl + "/confirm.html?id=" + valueCode;

        String html = mailTemplatesUtil.createConfirmationMail(
                user.getFirstName(), user.getLastName(), link
        );

        confirmMailSender.send(user.getEmail(), "Please confirm your registration with \"Kita Connection\"", html);
        return UserDto.from(user);
    }



    public UserDto getProfile(Long currentId) {
        return UserDto.from(usersRepository.findById(currentId)
                .orElseThrow());

    }

    public UserDto confirm(String code) {

       ConfirmationCode confirmationCode = confirmationCodeRepository.findByCodeAndExpiredDateTimeAfter(code, LocalDateTime.now())
               .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Code: " + code + " not found or is expired"));
       User user = usersRepository.findFirstByCodesContains(confirmationCode)
               .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User by code: " + confirmationCode + " not found"));

       user.setState(User.State.CONFIRMED);
       usersRepository.save(user);

        return UserDto.from(user);
    }
}
