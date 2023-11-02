package de.ait.gp.models;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account")
@AllArgsConstructor
public class User {

    public enum Role {
        ADMIN, USER, MANAGER
    }
    public enum Gender {
        MALE, FEMALE, DIVERSE
    }
    public enum State {
       NOT_CONFIRMED, CONFIRMED, DELETED ,BANNED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String firstName;

    @Column(length = 20)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String hashPassword;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;

    private String address;
    private String postcode;
    private String city;


    private LocalDate dateOfBirth;

    private String phone;

    @OneToMany(mappedBy = "manager")
    private Set<Kindergarten> controlKindergarten;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    private Set<Child> children;


    @ManyToMany
    @JoinTable(
            name = "favorities",
            joinColumns = @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "kindergarten_id", nullable = false, referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "kindergarten_id"} )
    )
    private Set<Kindergarten> favorities; /////favourite


    @OneToMany(mappedBy = "userByCode")
    @ToString.Exclude
    private Set<ConfirmationCode> codes;


    @OneToMany(mappedBy = "requestSender")
    private Set<Request> requests;


    @ManyToMany
    @JoinTable(
            name = "user_dialogue",
            joinColumns = @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "dialogue_id", nullable = false, referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "dialogue_id"} )
    )
    private Set<Dialogue> dialogues;

    @OneToMany(mappedBy = "sender")
    private Set<Message> messages;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
