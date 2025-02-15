package com.nebarrow.weathertracker.util;

import com.nebarrow.weathertracker.exception.InvalidPasswordException;
import org.mindrot.jbcrypt.BCrypt;

public class HashPasswordUtil {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static void checkPassword(String password, String hashedPassword) {
        if (!BCrypt.checkpw(password, hashedPassword)) {
            throw new InvalidPasswordException(password.formatted("Password {} is incorrect"));
        }
    }
}
