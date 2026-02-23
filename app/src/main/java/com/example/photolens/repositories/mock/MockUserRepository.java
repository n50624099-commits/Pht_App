package com.example.photolens.repositories.mock;

import com.example.photolens.models.User;
import com.example.photolens.repositories.interfaces.UserRepository;
import com.example.photolens.utils.PasswordHasher;

import java.util.ArrayList;
import java.util.List;
public class MockUserRepository implements UserRepository {
    private static List<User> mockUsers; // Статическое поле, чтобы данные сохранялись между экранами
    private static int nextId = 7;

    public MockUserRepository() {
        if (mockUsers == null) {
            initMockData();
        }
    }

    @Override
    public User getUserByEmail(String email) {
        for (User user : mockUsers) {
            if (user.getEmail().equalsIgnoreCase(email)) return user;
        }
        return null;
    }
    private void initMockData() {
        mockUsers = new ArrayList<>();
        // Используем BCrypt для соответствия логике LoginActivity
        mockUsers.add(new User(1, "admin@test.com", PasswordHasher.hashPassword("admin"), "ADMIN", null, "Администратор"));
        mockUsers.add(new User(2, "teacher@test.com", PasswordHasher.hashPassword("pass123"), "TEACHER", null, "Петрова Мария Ивановна"));
        mockUsers.add(new User(4, "student@test.com", PasswordHasher.hashPassword("pass123"), "STUDENT", "teacher@test.com", "Иванов Иван"));
    }

    @Override
    public List<User> getStudentsByTeacher(String teacherEmail) {
        List<User> students = new ArrayList<>();
        if (teacherEmail == null) return students;

        for (User user : mockUsers) {
            // Проверяем роль и соответствие email учителя
            if (User.Role.STUDENT.equals(user.getRole()) &&
                    teacherEmail.equalsIgnoreCase(user.getTeacherEmail())) {
                students.add(user);
            }
        }
        return students;
    }

    @Override
    public User login(String email, String password) {
        for (User user : mockUsers) {
            if (user.getEmail().equalsIgnoreCase(email) && PasswordHasher.checkPassword(password, user.getPasswordHash())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean register(User user) throws Exception {
        for (User u : mockUsers) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) return false;
        }
        user.setId(nextId++);
        mockUsers.add(user);
        return true;
    }

    @Override
    public boolean emailExists(String email) {
        for (User u : mockUsers) {
            if (u.getEmail().equalsIgnoreCase(email)) return true;
        }
        return false;
    }

    @Override
    public List<User> getAllUsers() { return new ArrayList<>(mockUsers); }

    @Override
    public User getUserById(int id) {
        for (User u : mockUsers) if (u.getId() == id) return u;
        return null;
    }

    // Другие методы...
    @Override
    public boolean deleteUser(int userId) {
        return mockUsers.removeIf(u -> u.getId() == userId);
    }

    @Override
    public boolean updateUser(User user) {
        for (int i = 0; i < mockUsers.size(); i++) {
            if (mockUsers.get(i).getId() == user.getId()) {
                mockUsers.set(i, user);
                return true;
            }
        }
        return false;
    }
    @Override
    public User findTeacherByEmail(String email) {
        for (User user : mockUsers) {
            if (user.getEmail().equalsIgnoreCase(email) && "TEACHER".equals(user.getRole())) {
                return user;
            }
        }
        return null;
    }
}