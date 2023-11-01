package de.ait.gp.controllers.api;

import de.ait.gp.dto.KindergartenDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tags(value = @Tag(name = "Kindergerten"))
@RequestMapping("/api/kindergartens")
public interface KindergartenApi {

    @GetMapping
    List<KindergartenDto> getAllKindergartens();
}
