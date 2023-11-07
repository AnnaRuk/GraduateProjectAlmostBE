package de.ait.gp.repositories;

import de.ait.gp.models.Child;
import de.ait.gp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChildrenRepository extends JpaRepository<Child, Long> {
    Set<Child> findAllByParentOrderById(User user);

    boolean existsByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);

    Optional<Child> findFirstByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);

    Optional<Child> findByParentAndId(User user, Long childId);
    @Query(nativeQuery = true, value = "select distinct child.* from Child child join Request  r on child.id = r.child_id where not r.status = :request_status and r.kindergarten_id = :kindergartenId")
    List<Child> findChildrenWithActiveRequestsManager(@Param("kindergartenId") Long kindergartenId, @Param("request_status") String status);
    @Query(value = "select distinct child.* from Child child join request r on child.id = r.child_id where not r.status = :request_status and child.parent_id = :parentId", nativeQuery = true)
    List<Child> findChildrenWithActiveRequestsUser(@Param("parentId") Long parentId, @Param("request_status") String status);
}
