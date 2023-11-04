package de.ait.gp.repositories;

import de.ait.gp.models.ConfirmationCode;
import de.ait.gp.models.Kindergarten;
import de.ait.gp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findFirstByCodesContains(ConfirmationCode code);

    Optional<User> findFirstUserByControlKindergartenContains(Kindergarten controlKindergarten);


}
