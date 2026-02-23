package com.example.photolens.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.photolens.R;
import com.example.photolens.RepositoryFactory;
import com.example.photolens.models.User;
import com.example.photolens.repositories.interfaces.UserRepository;
import com.example.photolens.utils.PasswordHasher;
import com.example.photolens.utils.Validator;

/**
 * Экран регистрации нового пользователя
 * Поддерживает полную валидацию данных и регистрацию студентов
 */
public class RegisterActivity extends AppCompatActivity {
    
    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText teacherEmailEditText;
    private Button registerButton;
    private TextView loginTextView;
    private ProgressBar progressBar;
    
    private UserRepository userRepository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Инициализация компонентов
        initializeViews();
        initializeRepositories();
        
        // Установка обработчиков событий
        setupListeners();
    }
    
    /**
     * Инициализация UI компонентов
     */
    private void initializeViews() {
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        teacherEmailEditText = findViewById(R.id.teacherEmailEditText);
        registerButton = findViewById(R.id.registerButton);
        loginTextView = findViewById(R.id.loginTextView);
        progressBar = findViewById(R.id.progressBar);
        
        // Скрываем индикатор загрузки
        progressBar.setVisibility(View.GONE);
        
        // Устанавливаем фокус на поле ФИО
        fullNameEditText.requestFocus();
    }
    
    /**
     * Инициализация репозиториев
     */
    private void initializeRepositories() {
        userRepository = RepositoryFactory.getUserRepository();
    }
    
    /**
     * Установка обработчиков событий
     */
    private void setupListeners() {
        registerButton.setOnClickListener(v -> onRegisterClick());
        loginTextView.setOnClickListener(v -> onLoginClick());
    }
    
    /**
     * Обработка нажатия кнопки "Зарегистрироваться"
     */
    private void onRegisterClick() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String teacherEmail = teacherEmailEditText.getText().toString().trim();
        
        // Валидация всех полей
        if (!validateRegistrationData(fullName, email, password, confirmPassword, teacherEmail)) {
            return;
        }
        
        // Показываем индикатор загрузки
        setLoadingState(true);
        
        // Хешируем пароль и создаем пользователя
        String hashedPassword = PasswordHasher.hashPassword(password);
        User newUser = new User(
            email,
            hashedPassword,
            User.Role.STUDENT, // По умолчанию все новые пользователи - студенты
            teacherEmail.isEmpty() ? null : teacherEmail,
            fullName
        );
        
        // Выполняем регистрацию в отдельном потоке
        new Thread(() -> {
            try {
                boolean success = userRepository.register(newUser);
                
                // Возвращаемся в UI поток
                runOnUiThread(() -> {
                    setLoadingState(false);
                    
                    if (success) {
                        onRegisterSuccess();
                    } else {
                        showError("Ошибка регистрации. Попробуйте еще раз");
                    }
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    handleRegistrationError(e);
                });
            }
        }).start();
    }
    
    /**
     * Валидация данных регистрации
     */
    /**
     * Валидация данных регистрации
     */
    private boolean validateRegistrationData(String fullName, String email, String password,
                                             String confirmPassword, String teacherEmail) {
        // Проверка ФИО
        if (!Validator.isNotEmpty(fullName)) {
            fullNameEditText.setError("Введите ФИО");
            fullNameEditText.requestFocus();
            return false;
        }

        // Проверка email
        if (!Validator.isNotEmpty(email)) {
            emailEditText.setError("Введите email");
            emailEditText.requestFocus();
            return false;
        }

        if (!Validator.isValidEmail(email)) {
            emailEditText.setError("Неверный формат email");
            emailEditText.requestFocus();
            return false;
        }

        // Проверка пароля
        String passwordError = Validator.getPasswordError(password);
        if (passwordError != null) {
            passwordEditText.setError(passwordError);
            passwordEditText.requestFocus();
            return false;
        }

        // Проверка совпадения паролей
        if (!Validator.passwordsMatch(password, confirmPassword)) {
            confirmPasswordEditText.setError("Пароли не совпадают");
            confirmPasswordEditText.requestFocus();
            return false;
        }

        // Проверка email учителя (если указан)
        if (Validator.isNotEmpty(teacherEmail) && !Validator.isValidEmail(teacherEmail)) {
            teacherEmailEditText.setError("Неверный формат email учителя");
            teacherEmailEditText.requestFocus();
            return false;
        }

        return true;
    }
    
    /**
     * Обработка успешной регистрации
     */
    private void onRegisterSuccess() {
        Toast.makeText(this, 
            "Регистрация успешна! Войдите используя свои данные", 
            Toast.LENGTH_LONG).show();
        
        // Возвращаемся на экран входа
        finish();
    }
    
    /**
     * Обработка ошибок регистрации
     */
    private void handleRegistrationError(Exception e) {
        String errorMessage = e.getMessage();
        
        if (errorMessage.contains("email уже существует") || 
            errorMessage.contains("уже зарегистрирован")) {
            emailEditText.setError("Этот email уже зарегистрирован");
            emailEditText.requestFocus();
            showError("Пользователь с таким email уже существует");
        } else if (errorMessage.contains("учитель") || errorMessage.contains("не найден")) {
            teacherEmailEditText.setError("Учитель не найден");
            teacherEmailEditText.requestFocus();
            showError("Учитель с указанным email не найден");
        } else {
            showError("Ошибка регистрации: " + errorMessage);
        }
    }
    
    /**
     * Обработка нажатия на "Уже есть аккаунт? Войти"
     */
    private void onLoginClick() {
        finish(); // Возвращаемся на LoginActivity
    }
    
    /**
     * Установка состояния загрузки
     */
    private void setLoadingState(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        registerButton.setEnabled(!isLoading);
        fullNameEditText.setEnabled(!isLoading);
        emailEditText.setEnabled(!isLoading);
        passwordEditText.setEnabled(!isLoading);
        confirmPasswordEditText.setEnabled(!isLoading);
        teacherEmailEditText.setEnabled(!isLoading);
        loginTextView.setEnabled(!isLoading);
    }
    
    /**
     * Показ сообщения об ошибке
     */
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
