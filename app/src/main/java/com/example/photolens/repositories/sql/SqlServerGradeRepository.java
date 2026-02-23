package com.example.photolens.repositories.sql;

import com.example.photolens.database.DatabaseConnection;
import com.example.photolens.models.Grade;
import com.example.photolens.repositories.interfaces.GradeRepository;
import java.util.List;

public class SqlServerGradeRepository implements GradeRepository {
    private DatabaseConnection dbConnection;
    
    public SqlServerGradeRepository() {
        this.dbConnection = new DatabaseConnection();
    }
    
    @Override
    public List<Grade> getGradesByStudent(int studentId) {
        // Реализовать SQL запрос
        // String query = "SELECT * FROM Grades WHERE student_id = ? ORDER BY date DESC";
        return null;
    }
    
    @Override
    public List<Grade> getGradesByTeacher(int teacherId) {
        // Реализовать SQL запрос
        // String query = "SELECT * FROM Grades WHERE teacher_id = ? ORDER BY date DESC";
        return null;
    }
    
    @Override
    public boolean addGrade(Grade grade) {
        //  Реализовать SQL запрос
        // String query = "INSERT INTO Grades (student_id, teacher_id, subject, date, grade, comment) VALUES (?, ?, ?, ?, ?, ?)";
        return false;
    }
    
    @Override
    public boolean updateGrade(Grade grade) {
        // Реализовать SQL запрос
        // String query = "UPDATE Grades SET subject = ?, date = ?, grade = ?, comment = ? WHERE id = ?";
        return false;
    }
    
    @Override
    public boolean deleteGrade(int gradeId) {
        // Реализовать SQL запрос
        // String query = "DELETE FROM Grades WHERE id = ?";
        return false;
    }
    
    @Override
    public Grade getGradeById(int gradeId) {
        // Реализовать SQL запрос
        // String query = "SELECT * FROM Grades WHERE id = ?";
        return null;
    }
}
