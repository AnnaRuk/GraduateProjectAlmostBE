package de.ait.gp.repositories;

import de.ait.gp.models.Kindergarten;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KindergartenRepository extends JpaRepository<Kindergarten,Long> {
@Query("select distinct city from Kindergarten group by city")
    List<String> findAllCities();
}
