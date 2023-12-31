package de.ait.gp.utils;

import de.ait.gp.dto.Gender;
import de.ait.gp.dto.RequestStatus;
import de.ait.gp.dto.Role;
import de.ait.gp.models.User;

import java.util.Set;

import static de.ait.gp.dto.Gender.*;
import static de.ait.gp.dto.RequestStatus.*;
import static de.ait.gp.dto.Role.*;

public class UserUtils {
    private UserUtils() {
    }

    public static Gender getEnumGender(String gender) {
        if (gender != null) {
            return gender.equals(MALE.toString()) ? MALE :
                    gender.equals(FEMALE.toString()) ? FEMALE :
                            gender.equals(DIVERSE.toString()) ? DIVERSE : NOT_SELECTED;
        }
        return null;
    }

    public static Role getEnumRole(String role) {
        if (role != null) {
            return role.equals(ADMIN.toString()) ? ADMIN :
                    role.equals(MANAGER.toString()) ? MANAGER : USER;
        }
        return null;
    }

    public static RequestStatus getEnumRequestStatus(String status) {
        if (status != null) {
            return status.equals(CONFIRMED.toString()) ? CONFIRMED :
                    status.equals(NOT_CONFIRMED.toString()) ? NOT_CONFIRMED : REJECTED;
        }
        return null;
    }
    public static User getRecipient(Set<User> members, Long userId) {
        return members.stream()
                .filter(user -> !user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}
