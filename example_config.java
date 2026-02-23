package com.photolearning.auth;

/**
 * ПРИМЕР конфигурационного файла
 * 
 * ДЛЯ ИСПОЛЬЗОВАНИЯ:
 * 1. Скопируйте этот файл в app/src/main/java/com/photolearning/auth/Config.java
 * 2. Заполните реальные данные подключения к вашему SQL Server
 * 3. Измените USE_MOCK_DATA на false для использования реальной БД
 * 4. НЕ КОММИТЬТЕ Config.java в Git (он в .gitignore)!
 * 
 * ВАЖНО: Этот файл содержит чувствительные данные!
 * Храните учетные данные БД в безопасности!
 */
public class Config {
    
    // ==================== ОСНОВНЫЕ НАСТРОЙКИ ====================
    
    /**
     * Флаг использования mock-данных
     * true - использовать захардкоженные данные (для разработки и демо)
     * false - использовать SQL Server (требуется настройка подключения ниже)
     */
    public static final boolean USE_MOCK_DATA = true;
    
    // ==================== НАСТРОЙКИ SQL SERVER ====================
    // Заполнить при подключении к реальной БД
    
    /**
     * Адрес SQL Server
     * Примеры:
     * - Azure SQL: "your-server.database.windows.net"
     * - Локальный: "localhost" или "192.168.1.100"
     */
    public static final String DB_SERVER = "your-server.database.windows.net";
    
    /**
     * Имя базы данных
     */
    public static final String DB_NAME = "photo_learning_db";
    
    /**
     * Имя пользователя БД
     */
    public static final String DB_USER = "your_username";
    
    /**
     * Пароль пользователя БД
     * ВАЖНО: Не храните пароли в открытом виде в production!
     * Рассмотрите использование Android Keystore или других защищенных хранилищ
     */
    public static final String DB_PASSWORD = "your_secure_password_here";
    
    /**
     * Порт подключения
     * По умолчанию 1433 для SQL Server
     */
    public static final int DB_PORT = 1433;
    
    /**
     * Включить SSL шифрование
     * Рекомендуется true для production
     */
    public static final boolean DB_USE_SSL = true;
    
    // ==================== НАСТРОЙКИ БЕЗОПАСНОСТИ ====================
    
    /**
     * Минимальная длина пароля
     */
    public static final int MIN_PASSWORD_LENGTH = 5;
    
    /**
     * Таймаут подключения к БД (в секундах)
     */
    public static final int CONNECTION_TIMEOUT = 30;
    
    // ==================== ТЕСТОВЫЕ УЧЕТНЫЕ ДАННЫЕ ====================
    // Используются только при USE_MOCK_DATA = true
    
    /**
     * Тестовые аккаунты для входа (mock mode):
     * - student@test.com / pass123 (Студент)
     * - teacher@test.com / pass123 (Учитель)
     * - admin@test.com / admin (Администратор)
     */
}
