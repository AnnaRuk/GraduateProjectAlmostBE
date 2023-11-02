package de.ait.gp.services;

import de.ait.gp.dto.KindergartenDto;
import de.ait.gp.dto.StandardResponseDto;
import de.ait.gp.exceptions.RestException;
import de.ait.gp.mail.KindergartenMailSender;
import de.ait.gp.models.ConfirmationCode;
import de.ait.gp.models.Kindergarten;
import de.ait.gp.models.User;
import de.ait.gp.repositories.ConfirmationCodesRepository;
import de.ait.gp.repositories.KindergartenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import de.ait.gp.repositories.UsersRepository;
import de.ait.gp.dto.user.UserDto;
import de.ait.gp.dto.user.NewUserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UsersService {

   private final UsersRepository usersRepository;
   private final PasswordEncoder passwordEncoder;
   private final KindergartenMailSender mailSender;
   private final ConfirmationCodesRepository confirmationCodesRepository;
   private final KindergartenRepository kindergartenRepository;
   @Value("${base.url}")
   private String baseUrl;

    @Transactional
    public UserDto register(NewUserDto newUser) {

        if (usersRepository.existsByEmail(newUser.getEmail())) {
            throw new RestException(HttpStatus.CONFLICT,
                    "User with email <" + newUser.getEmail() + "> already exists");
        }

        User user = User.builder()
                .email(newUser.getEmail())
                .hashPassword(passwordEncoder.encode(newUser.getHashPassword()))
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .role(User.Role.USER)
                .state(User.State.NOT_CONFIRMED)
                .build();

        usersRepository.save(user);

        String codeValue = UUID.randomUUID().toString();

        ConfirmationCode code = ConfirmationCode.builder()
                .code(codeValue)
                .userCode(user)
                .expiredDateTime(LocalDateTime.now().plusMinutes(60))
                .build();

        confirmationCodesRepository.save(code);

        mailSender.send(user.getEmail(), "Registration", "<a href='" + baseUrl + "/api/users/confirm/" + codeValue + "'>Confirm Registration</a>");

        return UserDto.from(user);
    }



    public UserDto getProfile(Long currentId) {
        return UserDto.from(usersRepository.findById(currentId).orElseThrow());

    }

    @Transactional
    public StandardResponseDto confirm(String confirmCode) {
        ConfirmationCode code = confirmationCodesRepository
                .findFirstByCodeAndExpiredDateTimeAfter(confirmCode, LocalDateTime.now())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Code not found or is expired"));

        User user = usersRepository
           //     .findFirstByCodesContains(code)
                .findById(code.getUserCode().getId())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User by code not found"));

        user.setState(User.State.CONFIRMED);

        usersRepository.save(user);

        return StandardResponseDto.builder()
                .message("User confirmed")
                .build();
    }


    public UserDto updateUser(User user) {

        User findUser = usersRepository.findUserById(user.getId())
                .orElseThrow(() ->  new RestException(HttpStatus.NOT_FOUND,"User wuth id<" + user.getId() + ". not found"));

        findUser.setFirstName(user.getFirstName());
        findUser.setLastName(user.getLastName());
        findUser.setEmail(user.getEmail());
        findUser.setPhone(user.getPhone());
        findUser.setAddress(user.getAddress());
        findUser.setRole(user.getRole());
        findUser.setCodes(user.getCodes());
        findUser.setDateOfBirth(user.getDateOfBirth());
        findUser.setHashPassword(passwordEncoder.encode(user.getHashPassword()));

        usersRepository.save(findUser);

        return UserDto.from(findUser);
    }
}
