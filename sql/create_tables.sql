-- ====================================
-- SQL Server таблицы для приложения
-- Обучение фотографии - система авторизации
-- ====================================

-- Удаление существующих таблиц (если нужно пересоздать)
-- DROP TABLE IF EXISTS Grades;
-- DROP TABLE IF EXISTS Users;

-- ====================================
-- Таблица пользователей
-- ====================================
CREATE TABLE Users (
    id INT PRIMARY KEY IDENTITY(1,1),
    email NVARCHAR(255) UNIQUE NOT NULL,
    password_hash NVARCHAR(255) NOT NULL,
    role NVARCHAR(50) NOT NULL CHECK (role IN ('STUDENT', 'TEACHER', 'ADMIN')),
    teacher_email NVARCHAR(255),
    full_name NVARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Users_Teacher FOREIGN KEY (teacher_email) REFERENCES Users(email) ON DELETE NO ACTION ON UPDATE CASCADE
);

-- ====================================
-- Таблица оценок/работ
-- ====================================
CREATE TABLE Grades (
    id INT PRIMARY KEY IDENTITY(1,1),
    student_id INT NOT NULL,
    teacher_id INT NOT NULL,
    subject NVARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    grade DECIMAL(3,2) NOT NULL CHECK (grade >= 0 AND grade <= 5),
    comment NVARCHAR(1000),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Grades_Student FOREIGN KEY (student_id) REFERENCES Users(id) ON DELETE CASCADE,
    CONSTRAINT FK_Grades_Teacher FOREIGN KEY (teacher_id) REFERENCES Users(id) ON DELETE NO ACTION
);

-- ====================================
-- Индексы для оптимизации запросов
-- ====================================
CREATE INDEX idx_users_email ON Users(email);
CREATE INDEX idx_users_role ON Users(role);
CREATE INDEX idx_users_teacher_email ON Users(teacher_email);
CREATE INDEX idx_grades_student ON Grades(student_id);
CREATE INDEX idx_grades_teacher ON Grades(teacher_id);
CREATE INDEX idx_grades_date ON Grades(date);

-- ====================================
-- Триггер для автоматического обновления updated_at
-- ====================================
CREATE TRIGGER trg_users_update 
ON Users
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE Users 
    SET updated_at = GETDATE()
    FROM Users u
    INNER JOIN inserted i ON u.id = i.id;
END;
GO

CREATE TRIGGER trg_grades_update 
ON Grades
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE Grades 
    SET updated_at = GETDATE()
    FROM Grades g
    INNER JOIN inserted i ON g.id = i.id;
END;
GO

-- ====================================
-- Комментарии к таблицам
-- ====================================

-- EXEC sp_addextendedproperty 
-- @name = N'MS_Description', 
-- @value = 'Таблица пользователей системы. Поддерживает роли: STUDENT, TEACHER, ADMIN',
-- @level0type = N'SCHEMA', @level0name = 'dbo',
-- @level1type = N'TABLE',  @level1name = 'Users';

-- EXEC sp_addextendedproperty 
-- @name = N'MS_Description', 
-- @value = 'Таблица оценок и работ студентов',
-- @level0type = N'SCHEMA', @level0name = 'dbo',
-- @level1type = N'TABLE',  @level1name = 'Grades';

PRINT 'Таблицы успешно созданы!';
PRINT 'Следующий шаг: запустите insert_test_data.sql для добавления тестовых данных';
