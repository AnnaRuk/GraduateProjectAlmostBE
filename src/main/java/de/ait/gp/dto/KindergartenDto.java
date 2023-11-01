package de.ait.gp.dto;

import de.ait.gp.models.Kindergarten;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class KindergartenDto {
    private String title;
    private String city;
    private String address;
    private Integer capacity;
    private String linkImg;


    public static KindergartenDto from(Kindergarten kindergarten) {
        return KindergartenDto.builder()
                .title(kindergarten.getTitle())
                .city(kindergarten.getCity())
                .address(kindergarten.getAddress())
                .capacity(kindergarten.getCapacity())
                .linkImg(kindergarten.getLinkImg())
                .build();
    }


    public static List<KindergartenDto> from(Collection<Kindergarten> users) {
        return users.stream()
                .map(KindergartenDto::from)
                .collect(Collectors.toList());


    }
}
