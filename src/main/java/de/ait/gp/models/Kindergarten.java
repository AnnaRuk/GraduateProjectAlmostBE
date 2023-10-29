package de.ait.gp.models;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@Entity
@AllArgsConstructor
public class Kindergarten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String title;

    @Column(length = 100)
    //todo
    private String address;

    @OneToOne(mappedBy = "positionKindergarten")
    private User manager; //1:1 = ?

    private String description;

    @Column(nullable = false)
    private Integer capacity;

    private Integer freePlaces;

    @Column(nullable = false)
    private String linkImg;

    @ToString.Exclude
    @OneToMany(mappedBy = "kindergarten")
    private Set<Request> requests;


    @ManyToMany(mappedBy = "kindergartens")
    private Set<User> choosers;  //favourite

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Kindergarten that = (Kindergarten) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
