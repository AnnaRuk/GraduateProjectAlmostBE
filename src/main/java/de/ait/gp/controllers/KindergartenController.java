package de.ait.gp.controllers;

import de.ait.gp.controllers.api.KindergartenApi;

import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.dto.kindergarten.KindergartensWithCitiesDto;
import de.ait.gp.services.KindergartenService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KindergartenController implements KindergartenApi {


    private final KindergartenService kindergartenService;

    @Override
    public KindergartensWithCitiesDto getAllKindergartens() {
        return kindergartenService.getAllKindergartensWithCities();
    }

    @Override
    public KindergartenDto getKindergartenInfo(Long KindergartenId) {
        return kindergartenService.findKindergarten(KindergartenId);
    }
}
