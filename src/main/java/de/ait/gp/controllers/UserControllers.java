package de.ait.gp.controllers;

import de.ait.gp.controllers.api.UsersApi;
import de.ait.gp.dto.user.NewUserDto;
import de.ait.gp.dto.user.UserDto;
import de.ait.gp.secutity.details.AuthenticatedUser;
import de.ait.gp.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserControllers implements UsersApi {
    private final UsersService usersService;

    @Override
    public UserDto register(NewUserDto newUser){
        return usersService.register(newUser);
    }

    @Override
    public UserDto getProfile(AuthenticatedUser user){
        Long currentId = user.getId();
        return usersService.getProfile(currentId);
    }

    @Override
    public UserDto confirm(String code) {
        return usersService.confirm(code);
    }

}
