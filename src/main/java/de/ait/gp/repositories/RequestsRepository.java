package de.ait.gp.repositories;


import de.ait.gp.dto.RequestStatus;
import de.ait.gp.models.Child;
import de.ait.gp.models.Kindergarten;
import de.ait.gp.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Set;


public interface RequestsRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByKindergartenAndStatusIsNotOrderByRequestDateTimeAsc(Kindergarten kindergarten, RequestStatus status);
    List<Request> findAllByChildIsInAndStatusIsNotOrderByRequestDateTimeAsc(Set<Child> children, RequestStatus status);
    Request findFirstByChild_IdAndKindergarten_IdAndStatusIsNot(Long childId, Long kindergartenId, RequestStatus status);
}
