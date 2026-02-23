package com.example.photolens.repositories.sql;

import com.example.photolens.database.DatabaseConnection;
import com.example.photolens.models.User;
import com.example.photolens.repositories.interfaces.UserRepository;
import java.util.List;

public class SqlServerUserRepository implements UserRepository {
    private DatabaseConnection dbConnection;
    
    public SqlServerUserRepository() {
        this.dbConnection = new DatabaseConnection();
    }

    @Override
    public User login(String email, String password) {
        // TODO: Реализовать SQL запрос для авторизации
        // String query = "SELECT * FROM Users WHERE email = ?";
        // Использовать PreparedStatement для защиты от SQL-инъекций
        // Проверить пароль с помощью PasswordHasher.checkPassword()
        return null;
    }
    
    @Override
    public boolean register(User user) throws Exception {
        // TODO: Реализовать SQL запрос для регистрации
        // String query = "INSERT INTO Users (email, password_hash, role, teacher_email, full_name) VALUES (?, ?, ?, ?, ?)";
        // Обработать SQLException с кодом 2627 (duplicate key)
        return false;
    }


    @Override
    public User getUserByEmail(String email) {
        // TODO: SQL запрос
        return null;
    }
    @Override
    public List<User> getAllUsers() {
        // TODO: Реализовать SQL запрос
        // String query = "SELECT * FROM Users ORDER BY id";
        return null;
    }
    
    @Override
    public User getUserById(int id) {
        // TODO: Реализовать SQL запрос
        // String query = "SELECT * FROM Users WHERE id = ?";
        return null;
    }
    
    @Override
    public List<User> getStudentsByTeacher(String teacherEmail) {
        // TODO: Реализовать SQL запрос
        // String query = "SELECT * FROM Users WHERE role = 'STUDENT' AND teacher_email = ?";
        return null;
    }
    
    @Override
    public boolean updateUser(User user) {
        // TODO: Реализовать SQL запрос
        // String query = "UPDATE Users SET email = ?, role = ?, teacher_email = ?, full_name = ? WHERE id = ?";
        return false;
    }
    
    @Override
    public boolean deleteUser(int userId) {
        // TODO: Реализовать SQL запрос
        // String query = "DELETE FROM Users WHERE id = ?";
        return false;
    }
    
    @Override
    public boolean emailExists(String email) {
        // TODO: Реализовать SQL запрос
        // String query = "SELECT COUNT(*) FROM Users WHERE email = ?";
        return false;
    }
    
    @Override
    public User findTeacherByEmail(String email) {
        // TODO: Реализовать SQL запрос
        // String query = "SELECT * FROM Users WHERE email = ? AND role = 'TEACHER'";
        return null;
    }
}
