package com.example.photolens.repositories.interfaces;

import com.example.photolens.models.Grade;
import java.util.List;

/**
 * Интерфейс репозитория для работы с оценками
 */
public interface GradeRepository {

    /**
     * Получить все оценки студента
     * @param studentId ID студента
     * @return Список оценок студента
     */
    List<Grade> getGradesByStudent(int studentId);

    /**
     * Получить все оценки, выставленные данным учителем
     * @param teacherId ID учителя
     * @return Список оценок
     */
    List<Grade> getGradesByTeacher(int teacherId);

    /**
     * Добавить новую оценку
     * @param grade Объект оценки
     * @return true если добавление успешно
     */
    boolean addGrade(Grade grade);

    /**
     * Обновить существующую оценку
     * @param grade Объект оценки с обновленными данными
     * @return true если обновление успешно
     */
    boolean updateGrade(Grade grade);

    /**
     * Удалить оценку
     * @param gradeId ID оценки
     * @return true если удаление успешно
     */
    boolean deleteGrade(int gradeId);
    Grade getGradeById(int gradeId);

}
