package de.ait.gp.services;

import de.ait.gp.dto.KindergartenDto;
import de.ait.gp.exceptions.RestException;
import de.ait.gp.models.Kindergarten;
import static  de.ait.gp.dto.KindergartenDto.from;

import de.ait.gp.models.User;
import de.ait.gp.repositories.KindergartenRepository;
import de.ait.gp.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KindergartenService {

    private final KindergartenRepository kindergartenRepository;
    private final UsersRepository usersRepository;


    public List<KindergartenDto> getAllKindergartens() {
        List<Kindergarten> list = kindergartenRepository.findAll();

        return  from(list);
    }

    public  KindergartenDto findKindergarten(Long idKindergarten, Long idManager) {

        Kindergarten kindergarten = kindergartenRepository.findById(idKindergarten)
                .orElseThrow(()-> new RestException(HttpStatus.NOT_FOUND, "kindergarten with id<" + idKindergarten + "> not found"));

        User manager = usersRepository.findUserById(idManager)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User with id<" + idManager + ">not found"));


        return KindergartenDto.builder()
                .title(kindergarten.getTitle())
                .linkImg(kindergarten.getLinkImg())
                .description(kindergarten.getDescription())
                .address(kindergarten.getAddress())
                .capacity(kindergarten.getCapacity())
                .phone(manager.getPhone())
                .build();
    }

}
