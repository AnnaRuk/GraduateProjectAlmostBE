package de.ait.gp.controllers;

import de.ait.gp.controllers.api.KindergartensApi;

import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.dto.kindergarten.KindergartensWithCitiesDto;
import de.ait.gp.services.KindergartensService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KindergartensController implements KindergartensApi {


    private final KindergartensService kindergartensService;

    @Override
    public KindergartensWithCitiesDto getAllKindergartens() {
        return kindergartensService.getAllKindergartensWithCities();
    }

    @Override
    public KindergartenDto getKindergartenInfo(Long KindergartenId) {
        return kindergartensService.findKindergarten(KindergartenId);
    }
}
