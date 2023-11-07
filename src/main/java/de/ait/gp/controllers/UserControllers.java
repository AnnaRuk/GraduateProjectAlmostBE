package de.ait.gp.controllers;

import de.ait.gp.controllers.api.UsersApi;
import de.ait.gp.dto.child.ChildDto;
import de.ait.gp.dto.child.ChildListDto;
import de.ait.gp.dto.child.NewChildDto;
import de.ait.gp.dto.child.UpdateChildDto;
import de.ait.gp.dto.kindergarten.*;
import de.ait.gp.dto.request.NewRequestDto;
import de.ait.gp.dto.request.RequestListWithChildrenDto;
import de.ait.gp.dto.user.NewUserDto;
import de.ait.gp.dto.user.UpdateUserDto;
import de.ait.gp.dto.user.UserDto;
import de.ait.gp.secutity.details.AuthenticatedUser;
import de.ait.gp.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserControllers implements UsersApi {
    private final UsersService usersService;

    @Override
    public UserDto register(NewUserDto newUser) {
        return usersService.register(newUser);
    }

    @Override
    public UserDto getProfile(AuthenticatedUser user) {
        Long currentId = user.getId();
        return usersService.getProfile(currentId);
    }

    @Override
    public UserDto confirm(String code) {
        return usersService.confirm(code);
    }

    @Override
    public KindergartenDto addControlKindergartenToManager(AuthenticatedUser user, NewKindergartenDto newKindergarten) {
        return usersService.addControlKindergartenToManager(user.getId(), newKindergarten);
    }

    @Override
    public KindergartenDto getControlKindergarten(AuthenticatedUser user) {
        return usersService.getControlKindergarten(user.getId());
    }

    @Override
    public UserDto updateUser(AuthenticatedUser user, UpdateUserDto updateUserDto) {
        return usersService.updateUser(user.getId(), updateUserDto);
    }

    @Override
    public KindergartenDto updateControlKindergarten(AuthenticatedUser user, UpdateKindergartenDto updateKindergartenDto) {
        return usersService.updateControlKindergarten(user.getId(), updateKindergartenDto);
    }

    @Override
    public KindergartenListDto getFavoriteKindergartens(AuthenticatedUser user) {
        return usersService.getAllFavoriteKindergartens(user.getId());
    }

    @Override
    public KindergartenListDto addKindergartenToFavorites(AuthenticatedUser user, KindergartenToFavoriteDto kindergartenToFavoriteDto) {
        return usersService.addKindergartenToFavorites(user.getId(), kindergartenToFavoriteDto.getKindergartenId());
    }

    @Override
    public ChildListDto getAllChildren(AuthenticatedUser user) {
        return usersService.getAllChildren(user.getId());
    }

    @Override
    public ChildListDto addNewChildToUser(AuthenticatedUser user, NewChildDto newChildDto) {
        return usersService.addNewChildToUser(user.getId(), newChildDto);
    }

    @Override
    public ChildDto updateChildInUser(AuthenticatedUser user, UpdateChildDto updateChildDto) {
        return usersService.updateChildInUser(user.getId(), updateChildDto);
    }

    @Override
    public KindergartenDto removeKindergartenFromFavorites(AuthenticatedUser user, KindergartenToFavoriteDto kindergartenFromFavorite) {
        return usersService.removeKindergartenFromFavorites(user.getId(), kindergartenFromFavorite.getKindergartenId());
    }

    @Override
    public RequestListWithChildrenDto getAllRequests(AuthenticatedUser user) {
        return usersService.getAllRequests(user.getId());
    }

    @Override
    public RequestListWithChildrenDto addNewRequest(AuthenticatedUser user, NewRequestDto newRequest) {
        return usersService.addNewRequest(user.getId(), newRequest);
    }

    @Override
    public RequestListWithChildrenDto rejectRequestBuId(AuthenticatedUser user, Long requestId) {
        return usersService.rejectRequestById(user.getId(), requestId);
    }

    @Override
    public RequestListWithChildrenDto confirmRequestBuId(AuthenticatedUser user, Long requestId) {
        return usersService.confirmRequestById(user.getId(), requestId);
    }
}
