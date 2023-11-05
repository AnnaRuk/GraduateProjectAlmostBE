package de.ait.gp.repositories;

import de.ait.gp.models.Child;
import de.ait.gp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface ChildrenRepository extends JpaRepository<Child, Long> {
    Set<Child> findAllByParentOrderById(User user);

    boolean existsByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);

    Optional<Child> findFirstByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);

    Optional<Child> findByParentAndId(User user, Long childId);
}
