package de.ait.gp.repositories;

import de.ait.gp.models.Kindergarten;
import de.ait.gp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
public interface KindergartensRepository extends JpaRepository<Kindergarten, Long> {
    @Query("select distinct k.city from Kindergarten k group by k.city order by k.city asc")
    List<String> findAllCitiesOrderByCityAsc();
    Kindergarten findFirstByManager(User user);

    boolean existsByTitleAndCityAndAddress(String title, String city, String address);

    Optional<Kindergarten> findFirstByTitleAndCityAndAddress(String title, String city, String address);

    Optional<Kindergarten> findKindergartenByManager_Id(Long userId);


    Set<Kindergarten> findAllByChoosersContainsOrderById(User user);


    List<Kindergarten> findAllByOrderByIdAsc();
}
