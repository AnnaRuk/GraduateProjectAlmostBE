package de.ait.gp.controllers.api;


import de.ait.gp.dto.StandardResponseDto;
import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.dto.kindergarten.KindergartensWithCitiesDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tags(value = @Tag(name = "Kindergarten"))
@RequestMapping("/api/kindergartens")
public interface KindergartenApi {
    @Operation(summary = "Getting all kindergartens", description = "Available to everyone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =  KindergartensWithCitiesDto.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @GetMapping
    KindergartensWithCitiesDto getAllKindergartens();
    @Operation(summary = "Getting kindergarten", description = "Available to everyone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KindergartenDto.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class)))
    })
    @GetMapping("/{kindergarten-id}")
    KindergartenDto getKindergartenInfo(@PathVariable("kindergarten-id") Long KindergartenId);


}




