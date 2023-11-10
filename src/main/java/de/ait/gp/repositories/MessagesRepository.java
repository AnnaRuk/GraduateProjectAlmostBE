package de.ait.gp.repositories;

import de.ait.gp.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MessagesRepository extends JpaRepository<Message, Long> {
}
