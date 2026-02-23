package com.example.photolens.utils;

import android.util.Patterns;
import java.util.regex.Pattern;

/**
 * Утилита для валидации пользовательского ввода
 */
public class Validator {

    /**
     * Проверяет, что строка не пустая
     * @param text Текст для проверки
     * @return true если строка не пустая и не null
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    /**
     * Проверяет формат email
     * @param email Email для проверки
     * @return true если email валиден
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Проверяет требования к паролю:
     * - Минимум 5 символов
     * - Содержит хотя бы одну букву
     * @param password Пароль для проверки
     * @return true если пароль соответствует требованиям
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 5) {
            return false;
        }
        // Проверка наличия хотя бы одной буквы
        Pattern letterPattern = Pattern.compile("[a-zA-Zа-яА-Я]");
        return letterPattern.matcher(password).find();
    }

    /**
     * Проверяет совпадение паролей
     * @param password Пароль
     * @param confirmPassword Подтверждение пароля
     * @return true если пароли совпадают
     */
    public static boolean passwordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    /**
     * Получить сообщение об ошибке для пароля
     * @param password Пароль для проверки
     * @return Текст ошибки или null если пароль валиден
     */
    public static String getPasswordError(String password) {
        if (password == null || password.isEmpty()) {
            return "Пароль не может быть пустым";
        }
        if (password.length() < 5) {
            return "Пароль должен содержать минимум 5 символов";
        }
        Pattern letterPattern = Pattern.compile("[a-zA-Zа-яА-Я]");
        if (!letterPattern.matcher(password).find()) {
            return "Пароль должен содержать хотя бы одну букву";
        }
        return null;
    }
}
