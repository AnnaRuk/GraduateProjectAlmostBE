package de.ait.gp.utils;

import de.ait.gp.dto.Gender;
import de.ait.gp.dto.Role;

import static de.ait.gp.dto.Gender.*;
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
        if (role!=null) {
            return role.equals(ADMIN.toString()) ? ADMIN :
                    role.equals(MANAGER.toString()) ? MANAGER : USER;
        }
        return null;
    }
}
