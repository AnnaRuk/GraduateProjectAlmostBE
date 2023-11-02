package de.ait.gp.controllers;

import de.ait.gp.controllers.api.UsersApi;
import de.ait.gp.dto.KindergartenDto;
import de.ait.gp.dto.StandardResponseDto;
import de.ait.gp.dto.user.NewUserDto;
import de.ait.gp.dto.user.UserDto;
import de.ait.gp.models.User;
import de.ait.gp.secutity.details.AuthenticatedUser;
import de.ait.gp.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UsersService usersService;

    @Override
    public UserDto register(NewUserDto newUser){
        return usersService.register(newUser);
    }

    @Override
    public StandardResponseDto getConfirmation(String confirmCode) {
        return usersService.confirm(confirmCode);
    }

    @Override
    public UserDto getProfile(AuthenticatedUser user){
        Long currentId = user.getId();
        return usersService.getProfile(currentId);
    }

    @Override
    public UserDto updateUser(User user) {
        return usersService.updateUser(user);
    }


}
