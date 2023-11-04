package de.ait.gp.services;

import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.dto.kindergarten.KindergartensWithCitiesDto;
import de.ait.gp.exceptions.RestException;
import de.ait.gp.models.Kindergarten;
import de.ait.gp.repositories.KindergartensRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.ait.gp.dto.kindergarten.KindergartenDto.from;


@Service
@RequiredArgsConstructor
public class KindergartensService {

    private final KindergartensRepository kindergartensRepository;



    public KindergartensWithCitiesDto getAllKindergartensWithCities() {
        List<Kindergarten> kindergartenList = kindergartensRepository.findAll();
        List<KindergartenDto> kindergartenDtoList = from(kindergartenList);
        List<String> cities = kindergartensRepository.findAllCities();

        return KindergartensWithCitiesDto.builder()
                .KindergartenDTOList(kindergartenDtoList)
                .cities(cities)
                .build();
    }

    public KindergartenDto findKindergarten(Long KindergartenId) {

        Kindergarten kindergarten = kindergartensRepository.findById(KindergartenId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "kindergarten with id <" + KindergartenId + "> not found"));

        return from(kindergarten);
    }

}
