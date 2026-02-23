package com.example.photolens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.photolens.R;
import com.example.photolens.RepositoryFactory;
import com.example.photolens.models.User;
import com.example.photolens.repositories.interfaces.UserRepository;
import com.example.photolens.utils.SessionManager;

/**
 * Главный экран студента
 * Отображает информацию о студенте, его учителе и предоставляет доступ к симулятору камеры
 */
public class StudentMainActivity extends AppCompatActivity {
    
    private TextView welcomeTextView;
    private TextView teacherTextView;
    private Button startLearningButton;
    private Button logoutButton;
    
    private SessionManager sessionManager;
    private UserRepository userRepository;
    private User currentUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        
        // Инициализация компонентов
        initializeViews();
        initializeRepositories();
        
        // Загрузка данных пользователя
        loadUserData();
        
        // Установка обработчиков событий
        setupListeners();
    }
    
    /**
     * Инициализация UI компонентов
     */
    private void initializeViews() {
        welcomeTextView = findViewById(R.id.welcomeTextView);
        teacherTextView = findViewById(R.id.teacherTextView);
        startLearningButton = findViewById(R.id.startLearningButton);
        logoutButton = findViewById(R.id.logoutButton);
    }
    
    /**
     * Инициализация репозиториев и менеджеров
     */
    private void initializeRepositories() {
        sessionManager = new SessionManager(this);
        userRepository = RepositoryFactory.getUserRepository();
    }
    
    /**
     * Загрузка данных текущего пользователя
     */
    private void loadUserData() {
        int userId = sessionManager.getUserId();
        
        // Загружаем данные в отдельном потоке
        new Thread(() -> {
            try {
                currentUser = userRepository.getUserById(userId);
                
                runOnUiThread(() -> {
                    if (currentUser != null) {
                        updateUI();
                    } else {
                        showError("Ошибка загрузки данных пользователя");
                        logout();
                    }
                });
                
            } catch (Exception e) {
                runOnUiThread(() -> {
                    showError("Ошибка: " + e.getMessage());
                });
            }
        }).start();
    }
    
    /**
     * Обновление интерфейса с данными пользователя
     */
    private void updateUI() {
        // Приветствие
        welcomeTextView.setText("Добро пожаловать, " + currentUser.getFullName() + "!");
        
        // Информация об учителе
        if (currentUser.getTeacherEmail() != null && !currentUser.getTeacherEmail().isEmpty()) {
            // Получаем данные учителя
            new Thread(() -> {
                try {
                    User teacher = userRepository.getUserByEmail(currentUser.getTeacherEmail());
                    runOnUiThread(() -> {
                        if (teacher != null) {
                            teacherTextView.setText("Ваш учитель: " + teacher.getFullName() + 
                                " (" + teacher.getEmail() + ")");
                        } else {
                            teacherTextView.setText("Ваш учитель: " + currentUser.getTeacherEmail());
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        teacherTextView.setText("Ваш учитель: " + currentUser.getTeacherEmail());
                    });
                }
            }).start();
        } else {
            teacherTextView.setText("Учитель не назначен");
        }
    }
    
    /**
     * Установка обработчиков событий
     */
    private void setupListeners() {
        startLearningButton.setOnClickListener(v -> onStartLearningClick());
        logoutButton.setOnClickListener(v -> onLogoutClick());
    }
    
    /**
     * Обработка нажатия кнопки "Начать обучение"
     */
    private void onStartLearningClick() {
        // Заменить активность симулятора камеры
        /*
        Intent intent = new Intent(this, CameraSimulatorActivity.class);
        intent.putExtra("userId", currentUser.getId());
        startActivity(intent);
        */
        
        //заглушка
        Toast.makeText(this, 
            "Симулятор камеры будет здесь\n(интеграция с основным приложением)",
            Toast.LENGTH_LONG).show();
    }
    
    /**
     * Обработка нажатия кнопки "Выход"
     */
    private void onLogoutClick() {
        logout();
    }
    
    /**
     * Выход из системы
     */
    private void logout() {
        sessionManager.logoutUser();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    /**
     * Показ сообщения об ошибке
     */
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onBackPressed() {
        // Блокируем возврат на экран входа
        // Вместо этого предлагаем выйти
        Toast.makeText(this, 
            "Нажмите кнопку 'Выход' для выхода из системы", 
            Toast.LENGTH_SHORT).show();
    }
}
