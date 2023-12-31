package de.ait.gp.models;

import de.ait.gp.dto.kindergarten.NewKindergartenDto;
import de.ait.gp.dto.kindergarten.UpdateKindergartenDto;
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

    @Column(length = 50)
    private String address;
    @Column(length = 30)
    private String postcode;
    @Column(length = 30)
    private String city;

    private String description;

    @Column(nullable = false)
    private Integer capacity;

    private Integer freePlaces;

    @Column(nullable = false)
    private String linkImg;

    @ToString.Exclude
    @OneToMany(mappedBy = "kindergarten")
    private Set<Request> requests;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @ToString.Exclude
    @ManyToMany(mappedBy = "favorites")
    private Set<User> choosers;

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

    public Kindergarten updateFrom(UpdateKindergartenDto updateKindergartenDto) {
        this.setTitle(updateKindergartenDto.getTitle());
        this.setCity(updateKindergartenDto.getCity());
        this.setAddress(updateKindergartenDto.getAddress());
        this.setPostcode(updateKindergartenDto.getPostcode());
        this.setCapacity(updateKindergartenDto.getCapacity());
        this.setDescription(updateKindergartenDto.getDescription());
        this.setLinkImg(updateKindergartenDto.getLinkImg());

        return this;
    }

    public static  Kindergarten from(NewKindergartenDto newKindergarten, User user) {
        return Kindergarten.builder()
                .title(newKindergarten.getTitle())
                .city(newKindergarten.getCity())
                .capacity(newKindergarten.getCapacity())
                .manager(user)
                .postcode(newKindergarten.getPostcode())
                .address(newKindergarten.getAddress())
                .linkImg(newKindergarten.getLinkImg())
                .description(newKindergarten.getDescription())
                .build();
    }
}
