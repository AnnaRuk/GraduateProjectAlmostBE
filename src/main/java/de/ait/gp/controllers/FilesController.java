package de.ait.gp.controllers;

import de.ait.gp.controllers.api.FilesApi;
import de.ait.gp.dto.StandardResponseDto;
import de.ait.gp.services.FilesService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class FilesController implements FilesApi {

    private final FilesService filesService;

    public StandardResponseDto upload(MultipartFile file) {
        return filesService.upload(file);
    }
}
