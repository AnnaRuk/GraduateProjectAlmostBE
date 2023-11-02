package de.ait.gp.controllers.api;


import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.dto.kindergarten.NewKindergartenDto;
import de.ait.gp.dto.user.NewUserDto;
import de.ait.gp.dto.StandardResponseDto;
import de.ait.gp.dto.user.UpdateUserDto;
import de.ait.gp.dto.user.UserDto;
import de.ait.gp.secutity.details.AuthenticatedUser;
import de.ait.gp.validation.dto.ValidationErrorsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tags(value = @Tag(name = "Users"))
@RequestMapping("/api/users")
public interface UsersApi {

    @Operation(summary = "Adding new user", description = "Available to everyone. The default state is USER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "user has been added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "There is already a user with this email",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    UserDto register(@RequestBody @Valid NewUserDto newUser);



    ///TODO documentstion + test
    @GetMapping("/profile")
    UserDto getProfile(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user);

    ///TODO documentstion + test
    @GetMapping("/confirm/{confirm-code}")
    UserDto confirm(@PathVariable("confirm-code") String code);
    @Operation(summary = "Adding new kindergarten to Manager", description = "Available to Manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "kindergarten has been added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KindergartenDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "There is already a kita with this data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/profile/controlKindergarten")
    KindergartenDto addControlKindergartenToManager(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                                                    @RequestBody @Valid NewKindergartenDto NewKindergarten);

    @Operation(summary = "Getting Kindergarten info from user", description = "Available to manager")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                description = "kindergarten has been added",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = KindergartenDto.class))),
        @ApiResponse(responseCode = "404",
                description = "Not Found",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @GetMapping("/profile/controlKindergarten")
    KindergartenDto getControlKindergarten(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user);

    @PutMapping("/profile")
    UserDto updateUser(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                       @RequestBody UpdateUserDto updateUserDto);
}


