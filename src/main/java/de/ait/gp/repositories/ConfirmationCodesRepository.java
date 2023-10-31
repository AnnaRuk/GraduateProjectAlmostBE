package de.ait.gp.repositories;

import de.ait.gp.models.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationCodesRepository extends JpaRepository<ConfirmationCode, Long> {
  Optional<ConfirmationCode> findByCodeAndExpiredDateTimeAfter(String cod,LocalDateTime now);
}
