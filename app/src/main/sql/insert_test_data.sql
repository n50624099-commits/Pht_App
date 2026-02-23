-- =============================================
-- Скрипт вставки тестовых данных
-- Обучение фотографии
-- =============================================

-- ВАЖНО: Пароли должны быть захешированы с помощью BCrypt
-- Для примера используются плейсхолдеры
-- Реальные хеши нужно сгенерировать в приложении

-- Вставка администратора
INSERT INTO Users (email, password_hash, role, teacher_email, full_name) VALUES
('admin@test.com', '$2a$10$YourBCryptHashHere', 'ADMIN', NULL, 'Администратор Системы');

-- Вставка учителей
INSERT INTO Users (email, password_hash, role, teacher_email, full_name) VALUES
('teacher@test.com', '$2a$10$YourBCryptHashHere', 'TEACHER', NULL, 'Петрова Мария Ивановна'),
('teacher2@test.com', '$2a$10$YourBCryptHashHere', 'TEACHER', NULL, 'Сидоров Петр Николаевич');

-- Вставка студентов
INSERT INTO Users (email, password_hash, role, teacher_email, full_name) VALUES
('student@test.com', '$2a$10$YourBCryptHashHere', 'STUDENT', 'teacher@test.com', 'Иванов Иван Петрович'),
('student2@test.com', '$2a$10$YourBCryptHashHere', 'STUDENT', 'teacher@test.com', 'Кузнецова Анна Сергеевна'),
('student3@test.com', '$2a$10$YourBCryptHashHere', 'STUDENT', 'teacher2@test.com', 'Смирнов Алексей Владимирович');

-- Вставка тестовых оценок
-- Получаем ID пользователей для внешних ключей
DECLARE @student1_id INT = (SELECT id FROM Users WHERE email = 'student@test.com');
DECLARE @student2_id INT = (SELECT id FROM Users WHERE email = 'student2@test.com');
DECLARE @teacher1_id INT = (SELECT id FROM Users WHERE email = 'teacher@test.com');

INSERT INTO Grades (student_id, teacher_id, subject, date, grade, comment) VALUES
(@student1_id, @teacher1_id, 'Портретная съёмка', '2026-02-10', 4.5, 'Хорошая композиция, но поработайте со светом'),
(@student1_id, @teacher1_id, 'Пейзажная съёмка', '2026-02-09', 5.0, 'Отличная работа! Прекрасное использование золотого часа'),
(@student1_id, @teacher1_id, 'Макросъёмка', '2026-02-08', 4.0, 'Хорошо, но немного не хватает резкости'),
(@student2_id, @teacher1_id, 'Портретная съёмка', '2026-02-11', 4.8, 'Прекрасная работа с естественным светом'),
(@student2_id, @teacher1_id, 'Уличная фотография', '2026-02-10', 4.3, 'Интересный ракурс, но композицию можно улучшить');

PRINT 'Тестовые данные успешно вставлены!';

-- =============================================
-- ИНСТРУКЦИЯ ПО ГЕНЕРАЦИИ ХЕШЕЙ ПАРОЛЕЙ:
-- =============================================
-- 
-- 1. В Android приложении используйте:
--    String hash = PasswordHasher.hashPassword("ваш_пароль");
--
-- 2. Замените '$2a$10$YourBCryptHashHere' на реальные хеши
--
-- 3. Для тестовых аккаунтов с паролем "pass123" и "admin"
--    сгенерируйте хеши и вставьте сюда
--
-- =============================================
