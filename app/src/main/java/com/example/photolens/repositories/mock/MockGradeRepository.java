package com.example.photolens.repositories.mock;

import com.example.photolens.models.Grade;
import com.example.photolens.repositories.interfaces.GradeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockGradeRepository implements GradeRepository {
    private List<Grade> mockGrades;
    private int nextId = 8;
    
    public MockGradeRepository() {
        initMockData();
    }
    
    private void initMockData() {
        mockGrades = new ArrayList<>();
        mockGrades.add(new Grade(1, 4, 2, "Портретная съёмка", "10.02.2026", 4.5, "Хорошая композиция, но нужно поработать со светом"));
        mockGrades.add(new Grade(2, 4, 2, "Пейзажная съёмка", "09.02.2026", 5.0, "Отличная работа! Прекрасное использование естественного света"));
        mockGrades.add(new Grade(3, 4, 2, "Студийная съёмка", "08.02.2026", 4.0, "Неплохо, но обратите внимание на фон"));
        mockGrades.add(new Grade(4, 5, 2, "Портретная съёмка", "10.02.2026", 4.8, "Очень хорошо! Отличная работа с моделью"));
        mockGrades.add(new Grade(5, 5, 2, "Макросъёмка", "07.02.2026", 4.2, "Интересный ракурс, продолжайте в том же духе"));
        mockGrades.add(new Grade(6, 6, 3, "Архитектурная съёмка", "11.02.2026", 4.7, "Великолепная перспектива"));
        mockGrades.add(new Grade(7, 6, 3, "Ночная съёмка", "06.02.2026", 4.9, "Превосходное владение выдержкой"));
    }
    
    @Override
    public List<Grade> getGradesByStudent(int studentId) {
        return mockGrades.stream()
            .filter(grade -> grade.getStudentId() == studentId)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Grade> getGradesByTeacher(int teacherId) {
        return mockGrades.stream()
            .filter(grade -> grade.getTeacherId() == teacherId)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean addGrade(Grade grade) {
        grade.setId(nextId++);
        return mockGrades.add(grade);
    }
    
    @Override
    public boolean updateGrade(Grade grade) {
        for (int i = 0; i < mockGrades.size(); i++) {
            if (mockGrades.get(i).getId() == grade.getId()) {
                mockGrades.set(i, grade);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean deleteGrade(int gradeId) {
        return mockGrades.removeIf(grade -> grade.getId() == gradeId);
    }
    
    @Override
    public Grade getGradeById(int gradeId) {
        return mockGrades.stream()
            .filter(grade -> grade.getId() == gradeId)
            .findFirst()
            .orElse(null);
    }
}
