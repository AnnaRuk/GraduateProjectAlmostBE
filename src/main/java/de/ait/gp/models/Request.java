package de.ait.gp.models;

import de.ait.gp.dto.RequestStatus;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@Entity
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Column(nullable = false)
    private LocalDateTime requestDateTime;

    @ManyToOne
    @JoinColumn(name = "kindergarten_id", nullable = false)
    private Kindergarten kindergarten;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Request request = (Request) o;
        return getId() != null && Objects.equals(getId(), request.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
    public static Request from(Child child, Kindergarten kindergarten) {
        return  Request.builder()
                .child(child)
                .kindergarten(kindergarten)
                .requestDateTime(LocalDateTime.now())
                .status(RequestStatus.NOT_CONFIRMED)
                .build();
    }
}
