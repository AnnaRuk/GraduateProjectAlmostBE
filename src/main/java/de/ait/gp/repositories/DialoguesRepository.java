package de.ait.gp.repositories;

import de.ait.gp.models.Dialogue;
import de.ait.gp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface DialoguesRepository extends JpaRepository<Dialogue, Long> {
    List<Dialogue> findAllByMembersContains(User member);
    @Query(nativeQuery = true, value = """
            select d.*
            from dialogue d
            join user_dialogue ud1 on d.id = ud1.dialogue_id
            join user_dialogue ud2 on d.id = ud2.dialogue_id
            where ud1.user_id = :senderId and ud2.user_id= :recipientId""")
    Dialogue findDialogueByMembers(@Param("senderId") Long  senderId,@Param("recipientId") Long recipientId);

}
