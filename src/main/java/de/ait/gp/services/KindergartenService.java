package de.ait.gp.services;

import de.ait.gp.dto.KindergartenDto;
import de.ait.gp.models.Kindergarten;
import static  de.ait.gp.dto.KindergartenDto.from;
import de.ait.gp.repositories.KindergartenRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KindergartenService {

    private final KindergartenRepository kindergartenRepository;


    public List<KindergartenDto> getAllKindergartens() {
        List<Kindergarten> list = kindergartenRepository.findAll();

        return  from(list);
    }

}
