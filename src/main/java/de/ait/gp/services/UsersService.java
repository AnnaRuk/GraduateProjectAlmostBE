package de.ait.gp.services;

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
import de.ait.gp.models.ConfirmationCode;
import de.ait.gp.models.Kindergarten;
import de.ait.gp.models.User;
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

import static de.ait.gp.models.Kindergarten.from;
import static de.ait.gp.dto.kindergarten.KindergartenDto.from;

@EnableAsync
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
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
        User user = usersRepository.findFirstByCodesContains(confirmationCode)
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
        User user = getUser(userId);
        Kindergarten kindergarten = from(newKindergarten, user);
        kindergartensRepository.save(kindergarten);
        return KindergartenDto.from(kindergarten);
    }

    public User getUser(Long userId) {
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

        User user = getUser(userID);
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

            Kindergarten kindergartenWithData = kindergartensRepository
                    .findFirstByTitleAndCityAndAddress(title, city, address)
                    .orElseThrow();

            if (kindergarten.getId().intValue() != kindergartenWithData.getId().intValue()) {
                throw new RestException(HttpStatus.CONFLICT, "Kindergarten with this data already exists");
            }
        }

        kindergartensRepository.save(kindergarten.updateFrom(updateKindergartenDto));

        return KindergartenDto.from(kindergarten);
    }

    public KindergartenDtoList getAllFavoriteKindergartens(Long id) {

        User user = getUser(id);

        List<Kindergarten> kindergartens = kindergartensRepository.findAllByChoosersContains(user);

        List<KindergartenDto> kindergartenDtoList = from(kindergartens);

        return KindergartenDtoList.builder()
                .kindergartens(kindergartenDtoList)
                .build();

    }

    public KindergartenDto addKindergartenToFavorites(Long userId, Long kindergartenId) {

        User user = getUser(userId);

        Kindergarten kindergarten = getKindergarten(kindergartenId);
        Set<Kindergarten> kindergartens = kindergartensRepository.findAllByChoosersId(userId);

        if (!kindergartens.add(kindergarten)) {
            throw new RestException(HttpStatus.CONFLICT, "This kindergarten has already been added to the user");
        }

        user.setFavorities(kindergartens);
        usersRepository.save(user);
        return from(kindergarten);
    }

    private Kindergarten getKindergarten(Long kindergartenId) {
        return kindergartensRepository.findById(kindergartenId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Kindergarten with id<" + kindergartenId + "> not found"));

    }


    public KindergartenDto deleteKindergartenFromFavorites(Long userId, Long kindergartenId) {

        User user = getUser(userId);
        Kindergarten kindergarten = getKindergarten(kindergartenId);
        Set<Kindergarten> kindergartens = kindergartensRepository.findAllByChoosersId(userId);

        if (!kindergartens.remove(kindergarten)) {
            throw new RestException(HttpStatus.BAD_REQUEST, "This kindergarten with id<" + kindergartenId + "> not found in favorite");
        }

        user.setFavorities(kindergartens);
        usersRepository.save(user);
        return KindergartenDto.from(kindergarten);
    }
}
