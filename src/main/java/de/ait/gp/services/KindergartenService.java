package de.ait.gp.services;

import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.dto.kindergarten.KindergartensWithCitiesDto;
import de.ait.gp.exceptions.RestException;
import de.ait.gp.models.Kindergarten;
import de.ait.gp.models.User;
import de.ait.gp.repositories.KindergartensRepository;
import de.ait.gp.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class KindergartenService {

    private final KindergartensRepository kindergartensRepository;
    private final UsersRepository usersRepository;


    public KindergartensWithCitiesDto getAllKindergartensWithCities() {
        List<Kindergarten> kindergartenList = kindergartensRepository.findAll();
        List<KindergartenDto> kindergartenDtoList = new ArrayList<>();
        for (Kindergarten kindergarten : kindergartenList) {
            User manager = usersRepository.findFirstUserByControlKindergartenContains(kindergarten)
                    .orElseThrow(() ->
                            new RestException(HttpStatus.NOT_FOUND, "Manager of kindergarten with id <" + kindergarten.getId() + "> not found"));
            kindergartenDtoList.add(KindergartenDto.from(kindergarten, manager.getPhone()));
        }
        List<String> cities = kindergartensRepository.findAllCities();

        return KindergartensWithCitiesDto.builder()
                .KindergartenDTOList(kindergartenDtoList)
                .cities(cities)
                .build();
    }

    public KindergartenDto findKindergarten(Long KindergartenId) {

        Kindergarten kindergarten = kindergartensRepository.findById(KindergartenId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "kindergarten with id <" + KindergartenId + "> not found"));

        User manager = usersRepository.findFirstUserByControlKindergartenContains(kindergarten)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "manager of kindergarten with id <" + KindergartenId + "> not found"));


        return KindergartenDto.from(kindergarten, manager.getPhone());
    }

}
