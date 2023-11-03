package de.ait.gp.repositories;

import de.ait.gp.models.Kindergarten;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KindergartensRepository extends JpaRepository<Kindergarten,Long> {
@Query("select distinct city from Kindergarten group by city")
    List<String> findAllCities();
boolean existsByTitleAndCityAndAddress(String title, String city, String address);
Optional<Kindergarten> findFirstByTitleAndCityAndAddress(String title, String city, String address);
Optional<Kindergarten> findKindergartenByManager_Id(Long userId);
}
