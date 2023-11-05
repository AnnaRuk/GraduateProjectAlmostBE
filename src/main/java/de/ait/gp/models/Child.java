package de.ait.gp.models;

import de.ait.gp.dto.Gender;
import de.ait.gp.dto.child.NewChildDto;
import de.ait.gp.dto.child.UpdateChildDto;
import de.ait.gp.utils.UserUtils;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static de.ait.gp.dto.Gender.NOT_SELECTED;
import static de.ait.gp.utils.TimeDateFormatter.DATE_FORMAT;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@Entity
@AllArgsConstructor
public class Child {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String firstName;

    @Column(length = 20)
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;


    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private User parent;

    @OneToMany(mappedBy = "child")
    private Set<Request> requests;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Child child = (Child) o;
        return getId() != null && Objects.equals(getId(), child.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
    @SneakyThrows
    public Child updateFrom(UpdateChildDto updateChildDto) {
        this.setFirstName(updateChildDto.getFirstName());
        this.setLastName(updateChildDto.getLastName());
        this.setGender(UserUtils.getEnumGender(updateChildDto.getGender()));
        this.setDateOfBirth(LocalDate.parse(updateChildDto.getDateOfBirth(), DATE_FORMAT));
        return this;
    }
    public static Child from(User user, NewChildDto newChild) {
        return Child.builder()
                .firstName(newChild.getFirstName())
                .lastName(newChild.getLastName())
                .gender(newChild.getGender()!=null ? UserUtils.getEnumGender(newChild.getGender()) : NOT_SELECTED)
                .dateOfBirth(LocalDate.parse(newChild.getDateOfBirth(), DATE_FORMAT))
                .parent(user)
                .build();
    }
}
