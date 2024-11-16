//package com.authentication.util;
//
//import org.mindrot.jbcrypt.BCrypt;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//
//public class PasswordUtil {
//
//    public static String hashPassword(String password) {
//        return BCrypt.hashpw(password, BCrypt.gensalt());
//    }
//
//    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
//        return BCrypt.checkpw(plainPassword, hashedPassword);
//    }
//}
//
