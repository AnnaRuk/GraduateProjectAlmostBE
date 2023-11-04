package de.ait.gp.utils;

import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.exceptions.RestException;
import de.ait.gp.models.Kindergarten;
import de.ait.gp.models.User;
import de.ait.gp.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class KindergartenDtoListMapper {

    private final UsersRepository usersRepository;


    public List<KindergartenDto> toListWithPhones(List<Kindergarten> kindergartens) {

        List<KindergartenDto> kindergartenDtoList = new ArrayList<>();

        for (Kindergarten kindergarten : kindergartens) {
            User manager = usersRepository.findFirstUserByControlKindergartenContains(kindergarten)
                    .orElseThrow(() ->
                            new RestException(HttpStatus.NOT_FOUND, "Manager of kindergarten with id <" + kindergarten.getId() + "> not found"));
            kindergartenDtoList.add(KindergartenDto.from(kindergarten, manager.getPhone()));
        }
        return kindergartenDtoList;
    }
}
