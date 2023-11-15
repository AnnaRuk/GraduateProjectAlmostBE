package de.ait.gp.services;

import de.ait.gp.dto.child.*;
import de.ait.gp.dto.dialogue.DialogueDto;
import de.ait.gp.dto.dialogue.DialogueListDto;
import de.ait.gp.dto.dialogue.message.NewMessageDto;
import de.ait.gp.dto.kindergarten.KindergartenDto;
import de.ait.gp.dto.kindergarten.KindergartenListDto;
import de.ait.gp.dto.kindergarten.NewKindergartenDto;
import de.ait.gp.dto.kindergarten.UpdateKindergartenDto;
import de.ait.gp.dto.request.NewRequestDto;
import de.ait.gp.dto.request.RequestDto;
import de.ait.gp.dto.request.RequestListWithChildrenDto;
import de.ait.gp.dto.user.NewUserDto;
import de.ait.gp.dto.user.UpdateUserDto;
import de.ait.gp.dto.user.UserDto;
import de.ait.gp.exceptions.RestException;
import de.ait.gp.mail.ConfirmMailSender;
import de.ait.gp.mail.MailTemplatesUtil;
import de.ait.gp.models.*;
import de.ait.gp.repositories.*;
import de.ait.gp.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static de.ait.gp.dto.RequestStatus.CONFIRMED;
import static de.ait.gp.dto.RequestStatus.REJECTED;
import static de.ait.gp.dto.Role.MANAGER;
import static de.ait.gp.dto.Role.USER;
import static de.ait.gp.dto.kindergarten.KindergartenDto.from;
import static de.ait.gp.models.Kindergarten.from;

@EnableAsync
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final ChildrenRepository childrenRepository;
    private final RequestsRepository requestsRepository;
    private final KindergartensRepository kindergartensRepository;
    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final MessagesRepository messagesRepository;
    private final DialoguesRepository dialoguesRepository;
    private final PasswordEncoder passwordEncoder;
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

        String html = mailTemplatesUtil.createConfirmationMail(user.getFirstName(), user.getLastName(), link);

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

        if (!user.getControlKindergarten().isEmpty()) {
            throw new RestException(HttpStatus.CONFLICT, "Manager with id<" + userId + "> already has a control kindergarten");
        }
        Kindergarten kindergarten = from(newKindergarten, user);
        kindergartensRepository.save(kindergarten);
        return KindergartenDto.from(kindergarten);
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

    public KindergartenListDto getAllFavoriteKindergartens(Long userId) {

        User user = getUserOrThrow(userId);

        List<Kindergarten> kindergartens = kindergartensRepository.findAllByChoosersContainsOrderById(user)
                .stream()
                .toList();

        return KindergartenListDto.builder()
                .kindergartens(from(kindergartens))
                .build();

    }

    public KindergartenListDto addKindergartenToFavorites(Long userId, Long kindergartenId) {

        User user = getUserOrThrow(userId);

        Kindergarten kindergarten = getKindergartenOrThrow(kindergartenId);
        Set<Kindergarten> favorites = kindergartensRepository.findAllByChoosersContainsOrderById(user);

        if (!user.getFavorites().add(kindergarten)) {
            throw new RestException(HttpStatus.CONFLICT, "This kindergarten has already been added to the user");
        }
        favorites.add(kindergarten);
        user.setFavorites(favorites);
        usersRepository.save(user);
        return KindergartenListDto.builder()
                .kindergartens(KindergartenDto.from(
                        favorites.stream()
                                .sorted(Comparator.comparing(Kindergarten::getId))
                                .toList()
                ))
                .build();
    }


    public KindergartenDto removeKindergartenFromFavorites(Long userId, Long kindergartenId) {

        User user = getUserOrThrow(userId);
        Kindergarten kindergarten = getKindergartenOrThrow(kindergartenId);
        Set<Kindergarten> kindergartens = kindergartensRepository.findAllByChoosersContainsOrderById(user);

        if (!kindergartens.remove(kindergarten)) {
            throw new RestException(HttpStatus.BAD_REQUEST, "This kindergarten with id <" + kindergartenId + "> not found in favorite");
        }

        user.setFavorites(kindergartens);
        usersRepository.save(user);
        return from(kindergarten);
    }

    public ChildListDto getAllChildren(Long userId) {
        User user = getUserOrThrow(userId);
        return ChildListDto.builder()
                .children(ChildDto.from(childrenRepository.findAllByParentOrderById(user).stream().toList()))
                .build();
    }

    public ChildListDto addNewChildToUser(Long userId, NewChildDto newChildDto) {
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
        return ChildListDto.builder()
                .children(ChildDto.from(childrenRepository.findAllByParentOrderById(user).stream().toList()))
                .build();
    }

    public ChildDto updateChildInUser(Long userId, UpdateChildDto updateChildDto) {
        User user = getUserOrThrow(userId);
        Child updateChild = childrenRepository.findByParentAndId(user, updateChildDto.getId())
                .orElseThrow(() ->
                        new RestException(HttpStatus.NOT_FOUND,
                                "Child with id<" + updateChildDto.getId() + "> of user with id <" + userId + "> not found"))
                .updateFrom(updateChildDto);


        if (childrenRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                updateChild.getFirstName(),
                updateChild.getLastName(),
                updateChild.getDateOfBirth()
        )) {
            Child child = childrenRepository.findFirstByFirstNameAndLastNameAndDateOfBirth(updateChild.getFirstName(),
                    updateChild.getLastName(),
                    updateChild.getDateOfBirth()).orElseThrow();

            if (child.getParent().getId().intValue() != updateChild.getParent().getId().intValue() ||
                    child.getId().intValue() != updateChild.getId().intValue()) {
                throw new RestException(HttpStatus.CONFLICT, "Child with this data already exists ");
            }
        }
        childrenRepository.save(updateChild);
        return ChildDto.from(updateChild);
    }


    public RequestListWithChildrenDto getAllRequests(Long userId) {
        User user = getUserOrThrow(userId);
        List<Request> activeRequests = new ArrayList<>();
        List<Child> childrenWithRequests = new ArrayList<>();

        if (user.getRole().equals(MANAGER)) {
            Kindergarten controlKindergarten = findKindergartenByManagerIdOrThrow(userId);

            childrenWithRequests = childrenRepository
                    .findChildrenWithRequestsManager(controlKindergarten.getId());

            activeRequests = requestsRepository.findAllByKindergartenOrderByRequestDateTimeAsc(controlKindergarten);

        } else if (user.getRole().equals(USER)) {
            Set<Child> childrenOfUser = childrenRepository.findAllByParentOrderById(user);

            activeRequests = requestsRepository.findAllByChildIsInOrderByRequestDateTimeAsc(childrenOfUser);
            childrenWithRequests = childrenRepository
                    .findChildrenWithRequestsUser(userId);

        }

        return RequestListWithChildrenDto.builder()
                .requests(RequestDto.from(activeRequests))
                .childWithUserList(ChildWithUserDto.from(childrenWithRequests))
                .build();

    }

    public RequestListWithChildrenDto addNewRequest(Long userId, NewRequestDto newRequest) {
        if (requestsRepository.findFirstByChild_IdAndKindergarten_IdAndStatusIsNot(
                newRequest.getChildId(),
                newRequest.getKindergartenId(),
                REJECTED) != null) {
            throw new RestException(HttpStatus.CONFLICT, "Request with this data already exists");
        }
        Child child = getChildOrThrow(newRequest.getChildId());
        Kindergarten kindergarten = getKindergartenOrThrow(newRequest.getKindergartenId());
        if (!userId.equals(child.getParent().getId())) {
            throw new RestException(
                    HttpStatus.NOT_FOUND, "Child with id <" + child.getId() + "> not found in children of user with id <" + userId + ">");
        }
        requestsRepository.save(Request.from(child, kindergarten));
        return getAllRequests(userId);

    }

    public RequestListWithChildrenDto rejectRequestById(Long userId, Long requestId) {
        User user = getUserOrThrow(userId);
        Request request = getRequestOrThrow(requestId);
        if (request.getStatus().equals(REJECTED)) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Request is already rejected");
        }
        if (user.getRole().equals(MANAGER)) {
            if (!kindergartensRepository.findFirstByManager(user).getId().equals(request.getKindergarten().getId())) {
                throw new RestException(HttpStatus.BAD_REQUEST, "There is no available requests for manager with id <" + userId + ">");
            }
        } else if (user.getRole().equals(USER)) {
            if (!user.getChildren().contains(request.getChild())) {
                throw new RestException(HttpStatus.BAD_REQUEST, "There is no available requests for user with id <" + userId + ">");
            }
        }
        request.setStatus(REJECTED);
        requestsRepository.save(request);
        return getAllRequests(userId);
    }

    public RequestListWithChildrenDto confirmRequestById(Long userId, Long requestId) {
        User user = getUserOrThrow(userId);
        Request request = getRequestOrThrow(requestId);
        if (request.getStatus().equals(REJECTED)) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Request is already rejected");
        }
        if (user.getRole().equals(MANAGER)) {
            if (!kindergartensRepository.findFirstByManager(user).getId().equals(request.getKindergarten().getId())) {
                throw new RestException(HttpStatus.BAD_REQUEST, "There is no available requests for manager with id<" + userId + ">");
            }
        }

        request.setStatus(CONFIRMED);
        requestsRepository.save(request);
/*        RequestListWithChildrenDto  list = getAllRequests(userId);
        return RequestListWithChildrenDto.builder()
                .requests(list.getRequests())
                .childWithUserList(list.getChildWithUserList())
                .build();
    }*/
        return getAllRequests(userId);
    }

    public DialogueListDto getAllDialogues(Long userId) {
        User authUser = getUserOrThrow(userId);
        List<Dialogue> dialogues = dialoguesRepository.findAllByMembersContains(authUser);
        return DialogueListDto.builder()
                .dialogues(DialogueDto.from(dialogues, userId))
                .build();
    }

    @Transactional
    public DialogueListDto addNewMessage(Long userId, NewMessageDto newMessage) {
        if (userId.equals(newMessage.getRecipientId())) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Sorry, you can't send a message to yourself");
        }
        User sender = getUserOrThrow(userId);
        User recipient = getUserOrThrow(newMessage.getRecipientId());
        Set<User> members = new HashSet<>();
        members.add(sender);
        members.add(recipient);
        Dialogue dialogue = dialoguesRepository.findDialogueByMembers(sender.getId(), recipient.getId());
        Message message = Message.from(sender, newMessage);
        if (dialogue == null) {
            dialogue = Dialogue.builder()
                    .members(members)
                    .messages(new HashSet<>())
                    .build();
            dialogue = dialoguesRepository.save(dialogue);

            recipient.getDialogues().add(dialogue);
            usersRepository.save(recipient);

            sender.getDialogues().add(dialogue);
            usersRepository.save(sender);
        }
        dialogue = dialoguesRepository.findDialogueByMembers(sender.getId(), recipient.getId());
        message.setDialogue(dialogue);
        message = messagesRepository.save(message);
        dialogue.getMessages().add(message);
        dialoguesRepository.save(dialogue);
        return getAllDialogues(userId);
    }





    public User getUserOrThrow(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User with id <" + userId + "> not found"));
    }

    public Child getChildOrThrow(Long childId) {
        return childrenRepository.findById(childId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Child with id <" + childId + "> not found"));
    }


    private Kindergarten getKindergartenOrThrow(Long kindergartenId) {
        return kindergartensRepository.findById(kindergartenId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Kindergarten with id <" + kindergartenId + "> not found"));

    }

    public Kindergarten findKindergartenByManagerIdOrThrow(Long managerId) {
        return kindergartensRepository.findKindergartenByManager_Id(managerId).orElseThrow(
                () -> new RestException(HttpStatus.NOT_FOUND, "Kindergarten of manager with id <" + managerId + ">  not found")
        );
    }
    public Request getRequestOrThrow(Long requestId) {
        return requestsRepository.findById(requestId).orElseThrow(
                () -> new RestException((HttpStatus.NOT_FOUND), "Request with id <" + requestId + "> not found")
        );

    }

}

