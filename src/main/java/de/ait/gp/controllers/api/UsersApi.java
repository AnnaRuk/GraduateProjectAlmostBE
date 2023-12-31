package de.ait.gp.controllers.api;


import de.ait.gp.dto.child.ChildDto;
import de.ait.gp.dto.child.ChildListDto;
import de.ait.gp.dto.child.NewChildDto;
import de.ait.gp.dto.child.UpdateChildDto;
import de.ait.gp.dto.dialogue.DialogueListDto;
import de.ait.gp.dto.dialogue.message.NewMessageDto;
import de.ait.gp.dto.kindergarten.*;
import de.ait.gp.dto.StandardResponseDto;
import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.dto.kindergarten.KindergartenListDto;
import de.ait.gp.dto.kindergarten.NewKindergartenDto;
import de.ait.gp.dto.kindergarten.UpdateKindergartenDto;
import de.ait.gp.dto.request.NewRequestDto;
import de.ait.gp.dto.request.RequestListWithChildrenDto;
import de.ait.gp.dto.user.NewUserDto;
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
import org.springframework.security.access.prepost.PreAuthorize;
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


    @Operation(summary = "Getting user's profile info", description = "Available to user/manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PreAuthorize("hasAnyAuthority('USER','MANAGER')")
    @GetMapping("/profile")
    UserDto getProfile(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user);

    @Operation(summary = "Confirming user's profile by code", description = "Available to user/manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
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
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/profile/controlKindergarten")
    KindergartenDto addControlKindergartenToManager(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                                                    @RequestBody @Valid NewKindergartenDto NewKindergarten);

    @Operation(summary = "Getting Kindergarten's info from user", description = "Available to manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KindergartenDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/profile/controlKindergarten")
    KindergartenDto getControlKindergarten(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user);

    @Operation(summary = "Updating user's info", description = "Available to everyone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "There is already a user with this email",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))

    })
    @PreAuthorize("hasAnyAuthority('USER','MANAGER')")
    @PutMapping("/profile")
    UserDto updateUser(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                       @RequestBody @Valid UpdateUserDto updateUserDto);

    @Operation(summary = "Updating control kindergarten's info", description = "Available to Manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KindergartenDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "There is already a Kindergarten with this data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),

    })
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/profile/controlKindergarten")
    KindergartenDto updateControlKindergarten(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                                              @RequestBody @Valid UpdateKindergartenDto updateKindergartenDto);

    @Operation(summary = "Get all favorite kindergartens", description = "Available to User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KindergartenListDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/profile/favorites")
    KindergartenListDto getFavoriteKindergartens(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user);

    @Operation(summary = "Adding kindergarten to User's favorites", description = "Available to User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "kindergarten has been added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KindergartenListDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "This kindergarten has already been added to the user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/profile/favorites")
    @PreAuthorize("hasAuthority('USER')")
    KindergartenListDto addKindergartenToFavorites(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                                                   @RequestBody KindergartenToFavoriteDto kindergartenToFavoriteDto);

    @Operation(summary = "Remove kindergarten from User's favorites", description = "Available to User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "kindergarten was deleted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KindergartenDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class)))
    })
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/profile/favorites")
    KindergartenDto removeKindergartenFromFavorites(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                                                    @RequestBody KindergartenToFavoriteDto deleteKindergarten);

    @Operation(summary = "Get all user's children", description = "Available to User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChildListDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/profile/children")
    ChildListDto getAllChildren(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user);

    @Operation(summary = "Adding new Child to user", description = "Available to User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "child has been added to User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChildListDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "This child with this data already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/profile/children")
    @PreAuthorize("hasAuthority('USER')")
    ChildListDto addNewChildToUser(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                                   @RequestBody @Valid NewChildDto newChildDto);

    @Operation(summary = "Updating Child's info in User", description = "Available to User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "child's info updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChildDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "This child with this data already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })

    @PutMapping("/profile/children")
    @PreAuthorize("hasAuthority('USER')")
    ChildDto updateChildInUser(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                               @RequestBody @Valid UpdateChildDto updateChildDto);

    @Operation(summary = "Getting List of requests", description = "Available to everyone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestListWithChildrenDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PreAuthorize("hasAnyAuthority('USER','MANAGER')")
    @GetMapping("/profile/requests")
    RequestListWithChildrenDto getAllRequests(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user);

    @Operation(summary = "Adding a new request", description = "Available to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "request has been added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestListWithChildrenDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "Request with this data already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/profile/requests")
    RequestListWithChildrenDto addNewRequest(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                                             @RequestBody @Valid NewRequestDto newRequest);

    @Operation(summary = "Rejecting a request", description = "Available to everyone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request rejected",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestListWithChildrenDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PreAuthorize("hasAnyAuthority('USER','MANAGER')")
    @PutMapping("/profile/requests/{request_id}/reject")
    RequestListWithChildrenDto rejectRequestById(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                                                 @PathVariable("request_id") Long requestId);

    @Operation(summary = "Confirming request", description = "Available to everyone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Request confirmed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestListWithChildrenDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/profile/requests/{request_id}/confirm")
    RequestListWithChildrenDto confirmRequestById(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                                                  @PathVariable("request_id") Long requestId);

    @Operation(summary = "Getting List of dialogues", description = "Available to everyone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DialogueListDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PreAuthorize("hasAnyAuthority('USER','MANAGER')")
    @GetMapping("/profile/dialogues")
    DialogueListDto getAllDialogues(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user);

    @Operation(summary = "Adding a new message", description = "Available to everyone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "message has been added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DialogueListDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @PreAuthorize("hasAnyAuthority('USER','MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/profile/dialogues")
    DialogueListDto addNewMessage(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user,
                                  @RequestBody @Valid NewMessageDto newMessage);
}





