package com.example.photolens.database;

import com.example.photolens.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для работы с подключением к SQL Server
 *
 * надо добавленить зависимости mssql-jdbc в build.gradle
 */

public class DatabaseConnection {
    
    private static Connection connection = null;
    
    /**
     * Получить подключение к базе данных
     * 
     * @return Connection объект для работы с БД
     * @throws SQLException если не удалось подключиться
     */
    public Connection getConnection() throws SQLException {
        // Реализовать подключение к SQL Server
        /*
        if (connection == null || connection.isClosed()) {
            try {
                // Загрузка драйвера SQL Server
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                
                // Получение connection string из Config
                String connectionUrl = Config.getConnectionString();
                
                // Установка соединения
                connection = DriverManager.getConnection(connectionUrl);
                
                System.out.println("Подключение к SQL Server успешно установлено");
                
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQL Server JDBC Driver не найден. " +
                    "Добавьте зависимость в build.gradle", e);
            } catch (SQLException e) {
                throw new SQLException("Ошибка подключения к SQL Server: " + 
                    e.getMessage(), e);
            }
        }
        
        return connection;
        */
        
        throw new SQLException("SQL Server подключение не реализовано. " +
            "Используйте Config.USE_MOCK_DATA = true для работы с mock-данными");
    }
    
    /**
     * Закрыть подключение к базе данных
     */
    public void closeConnection() {
        //  Реализовать закрытие подключения
        /*
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Подключение к SQL Server закрыто");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
    }
    
    /**
     * Проверить доступность соединения
     * 
     * @return true если соединение активно
     */
    public boolean isConnected() {
        // Реализовать проверку соединения
        /*
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
        */
        return false;
    }
    
    /**
     * Тестовый метод для проверки подключения к БД
     * Используйте для отладки после настройки Config.java
     * 
     * @return true если подключение успешно
     */
    public boolean testConnection() {
        // Реализовать тестовое подключение
        /*
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Тест подключения к SQL Server прошел успешно");
                System.out.println("  Сервер: " + Config.DB_SERVER);
                System.out.println("  База данных: " + Config.DB_NAME);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Ошибка тестового подключения:");
            System.err.println("  " + e.getMessage());
            return false;
        }
        */
        
        System.out.println("✗ SQL Server подключение не реализовано");
        System.out.println("  Установите Config.USE_MOCK_DATA = false и реализуйте TODO");
        return false;
    }
}
