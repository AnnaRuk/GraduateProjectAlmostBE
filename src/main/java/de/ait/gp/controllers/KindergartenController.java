package de.ait.gp.controllers;

import de.ait.gp.controllers.api.KindergartenApi;
import de.ait.gp.dto.KindergartenDto;
import de.ait.gp.services.KindergartenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KindergartenController implements KindergartenApi {


    private final KindergartenService kindergartenService;

    @Override
    public List<KindergartenDto> getAllKindergartens() {
        return kindergartenService.getAllKindergartens();
    }
}
