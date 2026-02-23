package com.example.photolens.activities;

import android.content.Intent;
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
import com.example.photolens.utils.SessionManager;
import com.example.photolens.utils.Validator;

/**
 * Экран авторизации
 * Позволяет пользователю войти в систему или перейти на экран регистрации
 */
public class LoginActivity extends AppCompatActivity {
    
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private ProgressBar progressBar;
    
    private UserRepository userRepository;
    private SessionManager sessionManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Инициализация компонентов
        initializeViews();
        initializeRepositories();
        
        // Проверка существующей сессии
        checkExistingSession();
        
        // Установка обработчиков событий
        setupListeners();
    }
    
    /**
     * Инициализация UI компонентов
     */
    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        progressBar = findViewById(R.id.progressBar);
        
        // Скрываем индикатор загрузки
        progressBar.setVisibility(View.GONE);
        
        // Устанавливаем фокус на поле email
        emailEditText.requestFocus();
    }
    
    /**
     * Инициализация репозиториев и менеджеров
     */
    private void initializeRepositories() {
        userRepository = RepositoryFactory.getUserRepository();
        sessionManager = new SessionManager(this);
    }
    
    /**
     * Проверка существующей сессии
     * Если пользователь уже авторизован, перенаправляем его на соответствующий экран
     */
    private void checkExistingSession() {
        if (sessionManager.isLoggedIn()) {
            String role = sessionManager.getUserRole();
            navigateToMainActivity(role);
        }
    }
    
    /**
     * Установка обработчиков событий
     */
    private void setupListeners() {
        loginButton.setOnClickListener(v -> onLoginClick());
        registerTextView.setOnClickListener(v -> onRegisterClick());
    }
    
    /**
     * Обработка нажатия кнопки "Войти"
     */
    private void onLoginClick() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // Валидация полей
        if (!validateInputs(email, password)) {
            return;
        }

        setLoadingState(true);

        // авторизация в отдельном потоке
        new Thread(() -> {
            try {
                User user = userRepository.login(email, password);


                runOnUiThread(() -> {
                    setLoadingState(false);

                    if (user != null) {
                        onLoginSuccess(user);
                    } else {
                        onLoginFailure();
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    showError("Ошибка подключения: " + e.getMessage());
                });
            }
        }).start();
    }
    
    /**
     * Валидация введенных данных
     */
    private boolean validateInputs(String email, String password) {
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
        
        if (!Validator.isNotEmpty(password)) {
            passwordEditText.setError("Введите пароль");
            passwordEditText.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Обработка успешной авторизации
     */
    private void onLoginSuccess(User user) {
        // Сохраняем сессию
        sessionManager.createLoginSession(
            user.getId(),
            user.getRole(),
            user.getEmail()
        );
        
        // Показываем приветствие
        Toast.makeText(this, 
            "Добро пожаловать, " + user.getFullName() + "!", 
            Toast.LENGTH_SHORT).show();
        
        // Переходим на соответствующий экран
        navigateToMainActivity(user.getRole());
    }
    
    /**
     * Обработка неудачной авторизации
     */
    private void onLoginFailure() {
        showError("Неверный логин или пароль");
        passwordEditText.setText("");
        passwordEditText.requestFocus();
    }
    
    /**
     * Переход на главный экран в зависимости от роли
     */
    private void navigateToMainActivity(String role) {
        Intent intent;
        
        switch (role) {
            case User.Role.STUDENT:
                intent = new Intent(this, StudentMainActivity.class);
                break;
            case User.Role.TEACHER:
                intent = new Intent(this, TeacherMainActivity.class);
                break;
            case User.Role.ADMIN:
                intent = new Intent(this, AdminPanelActivity.class);
                break;
            default:
                showError("Неизвестная роль пользователя");
                return;
        }
        
        startActivity(intent);
        finish(); // Закрываем LoginActivity
    }
    
    /**
     * Обработка нажатия на "Зарегистрироваться"
     */
    private void onRegisterClick() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    
    /**
     * Установка состояния загрузки
     */
    private void setLoadingState(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!isLoading);
        emailEditText.setEnabled(!isLoading);
        passwordEditText.setEnabled(!isLoading);
        registerTextView.setEnabled(!isLoading);
    }
    
    /**
     * Показ сообщения об ошибке
     */
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
