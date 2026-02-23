package com.example.photolens.models;

/**
 * Модель оценки/работы студента
 */
public class Grade {
    private int id;
    private int studentId;
    private int teacherId;
    private String subject;        // Предмет
    private String date;          // Дата работы (формат: "dd.MM.yyyy")
    private double grade;         // Оценка (например, 4.5)
    private String comment;       // Комментарий учителя к работе

    // Конструктор по умолчанию
    public Grade() {
    }

    // Полный конструктор
    public Grade(int id, int studentId, int teacherId, String subject, String date, double grade, String comment) {
        this.id = id;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.subject = subject;
        this.date = date;
        this.grade = grade;
        this.comment = comment;
    }

    // Конструктор для создания новой оценки (без id)
    public Grade(int studentId, int teacherId, String subject, String date, double grade, String comment) {
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.subject = subject;
        this.date = date;
        this.grade = grade;
        this.comment = comment;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", subject='" + subject + '\'' +
                ", date='" + date + '\'' +
                ", grade=" + grade +
                '}';
    }
}
