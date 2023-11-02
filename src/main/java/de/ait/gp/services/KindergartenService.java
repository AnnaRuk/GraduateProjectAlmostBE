package de.ait.gp.services;

import de.ait.gp.dto.kindergarten.KindergartenBaseDto;
import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.dto.kindergarten.KindergartensWithCitiesDto;
import de.ait.gp.exceptions.RestException;

import de.ait.gp.models.Kindergarten;
import de.ait.gp.models.User;
import de.ait.gp.repositories.KindergartenRepository;
import de.ait.gp.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class KindergartenService {

    private final KindergartenRepository kindergartenRepository;
    private final UsersRepository usersRepository;


    public KindergartensWithCitiesDto getAllKindergartensWithCities() {
        List<KindergartenBaseDto> kindergartenBaseDtoListList = KindergartenBaseDto.from(kindergartenRepository.findAll());
List<String> cities = kindergartenRepository.findAllCities();

        return KindergartensWithCitiesDto.builder()
                .KindergartenBaseDTOList(kindergartenBaseDtoListList)
                .cities(cities)
                .build();
    }

    public KindergartenDto findKindergarten(Long KindergartenId) {

        Kindergarten kindergarten = kindergartenRepository.findById(KindergartenId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "kindergarten with id <" + KindergartenId + "> not found"));

        User manager = usersRepository.findFirstUserByControlKindergartenContains(kindergarten)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "manager of kindergarten with id <" + KindergartenId + "> not found"));


        return KindergartenDto.from(kindergarten, manager.getPhone());
    }

}