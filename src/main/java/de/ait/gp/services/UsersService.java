package de.ait.gp.services;

import de.ait.gp.exceptions.RestException;
import de.ait.gp.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import de.ait.gp.repositories.UsersRepository;
import de.ait.gp.dto.user.UserDto;
import de.ait.gp.dto.user.NewUserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UsersService {

   private final UsersRepository usersRepository;
   private final PasswordEncoder passwordEncoder;

    public UserDto register(NewUserDto newUser) {

        if (usersRepository.existsByEmail(newUser.getEmail())){
            throw new RestException(HttpStatus.CONFLICT, "User with email <" + newUser.getEmail() + "> already exists");
        }

        User user = User.builder()
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .hashPassword(passwordEncoder.encode(newUser.getHashPassword()))
                .role(User.Role.USER)
                .build();

        usersRepository.save(user);
        return UserDto.from(user);
    }



    public UserDto getProfile(Long currentId) {
        return UserDto.from(usersRepository.findById(currentId).orElseThrow());

    }
}
