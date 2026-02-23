package com.example.photolens.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Утилита для хеширования и проверки паролей
 */
public class PasswordHasher {
    
    /**
     * Хеширует пароль с использованием BCrypt
     * @param plainPassword Пароль в открытом виде
     * @return Хешированный пароль
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    
    /**
     * Проверяет соответствие пароля хешу
     * @param plainPassword Пароль в открытом виде
     * @param hashedPassword Хешированный пароль из базы
     * @return true если пароль совпадает
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
