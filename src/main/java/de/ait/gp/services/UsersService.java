package de.ait.gp.services;

import de.ait.gp.dto.child.ChildDto;
import de.ait.gp.dto.child.ChildDtoList;
import de.ait.gp.dto.child.NewChildDto;
import de.ait.gp.dto.child.UpdateChildDto;
import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.dto.kindergarten.KindergartenDtoList;
import de.ait.gp.dto.kindergarten.NewKindergartenDto;
import de.ait.gp.dto.kindergarten.UpdateKindergartenDto;
import de.ait.gp.dto.user.NewUserDto;
import de.ait.gp.dto.user.UpdateUserDto;
import de.ait.gp.dto.user.UserDto;
import de.ait.gp.exceptions.RestException;
import de.ait.gp.mail.ConfirmMailSender;
import de.ait.gp.mail.MailTemplatesUtil;
import de.ait.gp.models.Child;
import de.ait.gp.models.ConfirmationCode;
import de.ait.gp.models.Kindergarten;
import de.ait.gp.models.User;
import de.ait.gp.repositories.ChildrenRepository;
import de.ait.gp.repositories.ConfirmationCodeRepository;
import de.ait.gp.repositories.KindergartensRepository;
import de.ait.gp.repositories.UsersRepository;
import de.ait.gp.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static de.ait.gp.dto.kindergarten.KindergartenDto.from;
import static de.ait.gp.models.Kindergarten.from;

@EnableAsync
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final ChildrenRepository childrenRepository;
    private final PasswordEncoder passwordEncoder;
    private final KindergartensRepository kindergartensRepository;
    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final ConfirmMailSender confirmMailSender;
    private final MailTemplatesUtil mailTemplatesUtil;


    @Value("${base.url}")
    private String baseUrl;


    @Transactional
    public UserDto register(NewUserDto newUser) {

        if (usersRepository.existsByEmail(newUser.getEmail())) {
            throw new RestException(HttpStatus.CONFLICT, "User with email <" + newUser.getEmail() + "> already exists");
        }
        User user = User.builder()
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .hashPassword(passwordEncoder.encode(newUser.getPassword()))
                .role(UserUtils.getEnumRole(newUser.getRole()))
                .state(User.State.NOT_CONFIRMED)
                .build();

        usersRepository.save(user);

        String valueCode = UUID.randomUUID().toString();

        ConfirmationCode code = ConfirmationCode.builder()
                .code(valueCode)
                .expiredDateTime(LocalDateTime.now().plusMonths(1))
                .userByCode(user)
                .build();

        confirmationCodeRepository.save(code);

        String link = baseUrl + "/confirm.html?id=" + valueCode;

        String html = mailTemplatesUtil.createConfirmationMail(
                user.getFirstName(), user.getLastName(), link
        );

        confirmMailSender.send(user.getEmail(), "Please confirm your registration with \"Kita Connection\"", html);
        return UserDto.from(user);
    }


    public UserDto getProfile(Long currentId) {
        return UserDto.from(usersRepository.findById(currentId)
                .orElseThrow());

    }

    public UserDto confirm(String code) {

        ConfirmationCode confirmationCode = confirmationCodeRepository.findByCodeAndExpiredDateTimeAfter(code, LocalDateTime.now())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Code: " + code + " not found or is expired"));
        User user = usersRepository.findFirstByCodesContainsOrderById(confirmationCode)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User by code: " + confirmationCode + " not found"));

        user.setState(User.State.CONFIRMED);
        usersRepository.save(user);

        return UserDto.from(user);
    }

    public KindergartenDto addControlKindergartenToManager(Long userId, NewKindergartenDto newKindergarten) {
        if (kindergartensRepository.existsByTitleAndCityAndAddress(
                newKindergarten.getTitle(),
                newKindergarten.getCity(),
                newKindergarten.getAddress()
        )) {
            throw new RestException(HttpStatus.CONFLICT, "Kindergarten with this data already exists");
        }
        User user = getUserOrThrow(userId);
        Kindergarten kindergarten = from(newKindergarten, user);
        kindergartensRepository.save(kindergarten);
        return KindergartenDto.from(kindergarten);
    }

    public User getUserOrThrow(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User with id <" + userId + "> not found"));
    }


    public KindergartenDto getControlKindergarten(Long userId) {
        Kindergarten kindergarten = kindergartensRepository.findKindergartenByManager_Id(userId)
                .orElseThrow(() ->
                        new RestException(HttpStatus.NOT_FOUND, "Kindergarten of manager with id <" + userId + "> not found"));
        return KindergartenDto.from(kindergarten);
    }

    public UserDto updateUser(Long userID, UpdateUserDto updateUserDto) {

        User user = getUserOrThrow(userID);
        if (usersRepository.existsByEmail(updateUserDto.getEmail())) {
            User userWithEmail = usersRepository.findByEmail(updateUserDto.getEmail()).orElseThrow();
            if (userID.intValue() != userWithEmail.getId().intValue()) {
                throw new RestException(HttpStatus.CONFLICT, "User with email <" + updateUserDto.getEmail() + "> already exists");
            }
        }

        usersRepository.save(user.updateFrom(updateUserDto));

        return UserDto.from(user);
    }

    public KindergartenDto updateControlKindergarten(Long userId, UpdateKindergartenDto updateKindergartenDto) {
        String title = updateKindergartenDto.getTitle();
        String city = updateKindergartenDto.getCity();
        String address = updateKindergartenDto.getAddress();

        Kindergarten kindergarten = kindergartensRepository.findKindergartenByManager_Id(userId)
                .orElseThrow(() ->
                        new RestException(HttpStatus.NOT_FOUND, "Kindergarten of manager with id <" + userId + "> not found"));

        if (kindergartensRepository.existsByTitleAndCityAndAddress(title, city, address)) {

            Kindergarten kindergartenWithData = kindergartensRepository.findFirstByTitleAndCityAndAddress(title, city, address)
                    .orElseThrow();

            if (kindergarten.getId().intValue() != kindergartenWithData.getId().intValue()) {
                throw new RestException(HttpStatus.CONFLICT, "Kindergarten with this data already exists");
            }
        }

        kindergartensRepository.save(kindergarten.updateFrom(updateKindergartenDto));

        return KindergartenDto.from(kindergarten);
    }

    public KindergartenDtoList getAllFavoriteKindergartens(Long userId) {

        User user = getUserOrThrow(userId);

        List<Kindergarten> kindergartens = kindergartensRepository.findAllByChoosersContainsOrderById(user)
                .stream()
                .toList();

        return KindergartenDtoList.builder()
                .kindergartens(from(kindergartens))
                .build();

    }

    public KindergartenDtoList addKindergartenToFavorites(Long userId, Long kindergartenId) {

        User user = getUserOrThrow(userId);

        Kindergarten kindergarten = getKindergartenOrThrow(kindergartenId);
        Set<Kindergarten> favorites = kindergartensRepository.findAllByChoosersContainsOrderById(user);

        if (!user.getFavorites().add(kindergarten)) {
            throw new RestException(HttpStatus.CONFLICT, "This kindergarten has already been added to the user");
        }

        user.setFavorites(favorites);
        usersRepository.save(user);
        return KindergartenDtoList.builder()
                .kindergartens(KindergartenDto.from(favorites.stream().toList()))
                .build();
    }

    private Kindergarten getKindergartenOrThrow(Long kindergartenId) {
        return kindergartensRepository.findById(kindergartenId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Kindergarten with id<" + kindergartenId + "> not found"));

    }


    public KindergartenDto removeKindergartenFromFavorites(Long userId, Long kindergartenId) {

        User user = getUserOrThrow(userId);
        Kindergarten kindergarten = getKindergartenOrThrow(kindergartenId);
        Set<Kindergarten> kindergartens = kindergartensRepository.findAllByChoosersContainsOrderById(user);

        if (!kindergartens.remove(kindergarten)) {
            throw new RestException(HttpStatus.BAD_REQUEST, "This kindergarten with id<" + kindergartenId + "> not found in favorite");
        }

        user.setFavorites(kindergartens);
        usersRepository.save(user);
        return from(kindergarten);
    }

    public ChildDtoList getAllChildren(Long userId) {
        User user = getUserOrThrow(userId);
        return ChildDtoList.builder()
                .children(ChildDto.from(childrenRepository.findAllByParentOrderById(user).stream().toList()))
                .build();
    }

    public ChildDtoList addNewChildToUser(Long userId, NewChildDto newChildDto) {
        User user = getUserOrThrow(userId);
        Child newChild = Child.from(user, newChildDto);
        if (childrenRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                newChild.getFirstName(),
                newChild.getLastName(),
                newChild.getDateOfBirth()
        )) {
            throw new RestException(HttpStatus.CONFLICT, "Child with this data already exists ");
        }
        if (!user.getChildren().add(newChild)) {
            throw new RestException(HttpStatus.CONFLICT, "Child with this data already exists in children of User with id<" + userId + ">");
        }
        childrenRepository.save(newChild);
        return ChildDtoList.builder()
                .children(ChildDto.from(childrenRepository.findAllByParentOrderById(user).stream().toList()))
                .build();
    }

    public ChildDto updateChildInUser(Long userId, UpdateChildDto updateChildDto) {
        User user = getUserOrThrow(userId);
        Child updateChild = getChildOrThrow(updateChildDto.getId()).updateFrom(updateChildDto);
        if (childrenRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                updateChild.getFirstName(),
                updateChild.getLastName(),
                updateChild.getDateOfBirth()
        )) {
            Child child = childrenRepository.findByParentAndId(user, updateChildDto.getId())
                    .orElseThrow(() ->
                            new RestException(HttpStatus.NOT_FOUND,
                                    "Child with id<" + updateChildDto.getId() + " of user with id <" + userId + "> not found"));

            if (child.getId().intValue() != updateChild.getId().intValue()) {
                throw new RestException(HttpStatus.CONFLICT, "Child with this data already exists ");
            }
        }
        childrenRepository.save(updateChild);
        return ChildDto.from(updateChild);
    }

    public Child getChildOrThrow(Long childId) {
        return childrenRepository.findById(childId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Child with id<" + childId + "> not found"));
    }
}
