# Модуль авторизации для Android-приложения по обучению фотографии

## Описание проекта

Полнофункциональный модуль авторизации и управления пользователями для Android-приложения обучения фотографии. Проект реализован на Java с использованием паттерна Repository и готов к интеграции как с mock-данными (для демонстрации), так и с реальным SQL Server.

### Особенности

- ✅ Полная система авторизации/регистрации
- ✅ Три роли пользователей: Студент, Учитель, Администратор
- ✅ Mock-данные для быстрого старта
- ✅ Архитектура готова для SQL Server
- ✅ BCrypt хеширование паролей
- ✅ Валидация всех полей ввода
- ✅ Горизонтальная ориентация экрана
- ✅ Минималистичный UI дизайн

## Требования

- **Android Studio** Electric Eel | 2022.1.1 или новее
- **JDK** 11 или выше
- **Android API Level** 24 (Android 7.0) - 36
- **Gradle** 8.2.0

## Установка и запуск

### Шаг 1: Клонирование репозитория

```bash
git clone https://github.com/your-username/photo-auth-module.git
cd photo-auth-module
```

### Шаг 2: Открыть в Android Studio

1. Откройте Android Studio
2. File → Open → Выберите папку проекта
3. Дождитесь синхронизации Gradle

### Шаг 3: Запуск (с mock-данными)

По умолчанию приложение работает с mock-данными (Config.USE_MOCK_DATA = true)

1. Подключите Android устройство или запустите эмулятор
2. Нажмите Run (Shift + F10)
3. Используйте тестовые аккаунты (см. ниже)

## Тестовые аккаунты

### Студент
- **Email:** student@test.com
- **Пароль:** pass123
- **Возможности:** Просмотр информации о своем учителе, переход к симулятору камеры

### Учитель
- **Email:** teacher@test.com
- **Пароль:** pass123
- **Возможности:** Просмотр таблицы оценок своих студентов, переход к симулятору

### Администратор
- **Email:** admin@test.com
- **Пароль:** admin
- **Возможности:** Управление всеми пользователями (просмотр, редактирование, удаление)

## Структура проекта

```
PhotoAuthModule/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/photolearning/auth/
│   │       │   ├── activities/          # Все экраны приложения
│   │       │   ├── models/              # Модели данных
│   │       │   ├── repositories/        # Паттерн Repository
│   │       │   ├── database/            # Подключение к БД
│   │       │   ├── utils/               # Утилиты
│   │       │   ├── Config.java
│   │       │   └── RepositoryFactory.java
│   │       ├── res/layout/              # XML layouts
│   │       ├── sql/                     # SQL скрипты
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
├── .gitignore
└── README.md
```

## Функционал по ролям

### 👨‍🎓 Студент
- Регистрация с указанием email учителя (опционально)
- Просмотр информации о своем учителе
- Переход к симулятору камеры для обучения
- Выход из системы

### 👨‍🏫 Учитель
- Просмотр таблицы всех своих студентов с их оценками
- Колонки: ФИО, Предмет, Дата, Оценка, Комментарий
- Переход к симулятору камеры
- Выход из системы

### 👨‍💼 Администратор
- Просмотр всех пользователей системы
- Редактирование пользователей (ФИО, email, роль, учитель)
- Удаление пользователей с подтверждением
- Выход из системы

## Подключение к SQL Server (опционально)

### Шаг 1: Настройка базы данных

1. Создайте базу данных на SQL Server
2. Выполните скрипт `/app/src/main/sql/create_tables.sql`
3. (Опционально) Выполните `/app/src/main/sql/insert_test_data.sql`

### Шаг 2: Конфигурация приложения

Отредактируйте файл `Config.java`:

```java
public class Config {
    public static final boolean USE_MOCK_DATA = false;
    
    public static final String DB_SERVER = "your-server.database.windows.net";
    public static final String DB_NAME = "photo_learning_db";
    public static final String DB_USER = "your_username";
    public static final String DB_PASSWORD = "your_password";
}
```

⚠️ **ВАЖНО:** Файл `Config.java` находится в `.gitignore`!

### Шаг 3: Раскомментировать зависимость

В `app/build.gradle`:

```gradle
implementation 'com.microsoft.sqlserver:mssql-jdbc:12.2.0.jre11'
```

## Безопасность

- 🔒 Пароли хешируются с помощью BCrypt
- 🔒 Сессия хранится в SharedPreferences (MODE_PRIVATE)
- 🔒 SQL запросы используют PreparedStatement
- 🔒 Config.java с паролями БД в .gitignore

## Архитектура - Repository Pattern

```
Interface (UserRepository/GradeRepository)
    ↓
    ├── MockRepository (захардкоженные данные)
    └── SqlServerRepository (реальная БД)
```

Переключение через `RepositoryFactory` и флаг `Config.USE_MOCK_DATA`.

## Тестирование

### Сценарий 1: Регистрация нового студента
1. Нажмите "Зарегистрироваться"
2. Заполните все поля (email учителя: teacher@test.com)
3. Нажмите "Зарегистрироваться"
4. Войдите с новыми данными

### Сценарий 2: Редактирование пользователя (Админ)
1. Войдите как admin@test.com / admin
2. Найдите пользователя в таблице
3. Нажмите "Редактировать"
4. Измените роль и сохраните

### Сценарий 3: Просмотр оценок (Учитель)
1. Войдите как teacher@test.com / pass123
2. Проверьте таблицу с оценками студентов

## Технологии

- **Язык:** Java
- **Минимальный SDK:** 24 (Android 7.0)
- **Target SDK:** 36
- **Хеширование:** BCrypt (jbcrypt:0.4)
- **UI:** Android Views
- **База данных:** SQL Server (готово к подключению)

## Поддержка

При проблемах:
1. Проверьте версию Android Studio
2. Убедитесь, что Gradle синхронизирован
3. Проверьте `Config.USE_MOCK_DATA = true`
4. Build → Clean Project

---

**Приятного использования! 📸**
