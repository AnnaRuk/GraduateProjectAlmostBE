package de.ait.gp.utils;

import de.ait.gp.dto.Gender;
import de.ait.gp.dto.Role;

public class UserUtils {
    private UserUtils() {
    }

    public static Gender getEnumGender(String gender) {
        return gender.equals(Gender.MALE.toString()) ? Gender.MALE :
                gender.equals(Gender.FEMALE.toString()) ? Gender.FEMALE : Gender.DIVERSE;
    }
    public static Role getEnumRole(String role) {
        return role.equals(Role.ADMIN.toString()) ? Role.ADMIN :
                role.equals(Role.MANAGER.toString()) ? Role.MANAGER : Role.USER;
    }
}
