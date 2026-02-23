package com.example.photolens;

import com.example.photolens.repositories.interfaces.GradeRepository;
import com.example.photolens.repositories.interfaces.UserRepository;
import com.example.photolens.repositories.mock.MockGradeRepository;
import com.example.photolens.repositories.mock.MockUserRepository;
import com.example.photolens.repositories.sql.SqlServerGradeRepository;
import com.example.photolens.repositories.sql.SqlServerUserRepository;

/**
 * Фабрика для создания репозиториев
 * Автоматически выбирает реализацию на основе Config.USE_MOCK_DATA
 */
public class RepositoryFactory {
    
    /**
     * Получить репозиторий пользователей
     * @return MockUserRepository если USE_MOCK_DATA = true, иначе SqlServerUserRepository
     */
    public static UserRepository getUserRepository() {
        if (Config.USE_MOCK_DATA) {
            return new MockUserRepository();
        } else {
            return new SqlServerUserRepository();
        }
    }
    
    /**
     * Получить репозиторий оценок
     * @return MockGradeRepository если USE_MOCK_DATA = true, иначе SqlServerGradeRepository
     */
    public static GradeRepository getGradeRepository() {
        if (Config.USE_MOCK_DATA) {
            return new MockGradeRepository();
        } else {
            return new SqlServerGradeRepository();
        }
    }
}
