package de.ait.gp.repositories;

import de.ait.gp.models.Kindergarten;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KindergartenRepository extends JpaRepository<Kindergarten,Long> {

}
