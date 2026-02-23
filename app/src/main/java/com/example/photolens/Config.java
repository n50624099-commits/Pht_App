package com.example.photolens;

/**
 * Конфигурация приложения
 *
 */
public class Config {
    
    // ========== ФЛАГ ИСПОЛЬЗОВАНИЯ MOCK ДАННЫХ ==========
    // true  - использовать захардкоженные данные для демонстрации
    // false - использовать подключение к SQL Server
    public static final boolean USE_MOCK_DATA = true;
    
    // ========== НАСТРОЙКИ SQL SERVER
    
    public static final String DB_SERVER = "SERVER";
    public static final String DB_NAME = "photo_learning_db";
    public static final String DB_USER = "your_username";
    public static final String DB_PASSWORD = "your_password";
    public static final int DB_PORT = 1433;
    
    // Строка подключения будет сформирована автоматически
    // jdbc:sqlserver://SERVER:PORT;databaseName=DATABASE
}
