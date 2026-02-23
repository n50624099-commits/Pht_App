package com.example.photolens.repositories.interfaces;

import com.example.photolens.models.User;
import java.util.List;

public interface UserRepository {
    User login(String email, String password);
    boolean register(User user) throws Exception;
    List<User> getAllUsers();
    User getUserById(int id);
    User getUserByEmail(String email);
    List<User> getStudentsByTeacher(String teacherEmail);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    boolean emailExists(String email);


    User findTeacherByEmail(String email);
}