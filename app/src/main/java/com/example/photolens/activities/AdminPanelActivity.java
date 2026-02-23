package com.example.photolens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.photolens.R;
import com.example.photolens.RepositoryFactory;
import com.example.photolens.models.User;
import com.example.photolens.repositories.interfaces.UserRepository;
import com.example.photolens.utils.SessionManager;

import java.util.List;

public class AdminPanelActivity extends AppCompatActivity {

    private TextView titleTextView;
    private Button logoutButton;
    private TableLayout usersTableLayout;

    private SessionManager sessionManager;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        sessionManager = new SessionManager(this);
        userRepository = RepositoryFactory.getUserRepository();

        // Проверка прав
        if (!sessionManager.isLoggedIn() || !"ADMIN".equals(sessionManager.getUserRole())) {
            redirectToLogin();
            return;
        }

        initializeViews();
        loadAllUsers();
        setupListeners();
    }

    private void initializeViews() {
        // Имена исправлены под твой XML
        titleTextView = findViewById(R.id.titleTextView);
        logoutButton = findViewById(R.id.logoutButton);
        usersTableLayout = findViewById(R.id.usersTableLayout);
    }

    private void loadAllUsers() {
        new Thread(() -> {
            List<User> users = userRepository.getAllUsers();
            runOnUiThread(() -> {
                if (users == null || users.isEmpty()) {
                    Toast.makeText(this, "Пользователи не найдены", Toast.LENGTH_SHORT).show();
                    return;
                }
                usersTableLayout.removeAllViews();
                createTableHeader();
                for (User user : users) {
                    addUserRow(user);
                }
            });
        }).start();
    }

    private void createTableHeader() {
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        String[] headers = {"ID", "Email", "ФИО", "Роль", "Действия"};
        for (String h : headers) {
            TextView tv = new TextView(this);
            tv.setText(h);
            tv.setPadding(10, 10, 10, 10);
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
            headerRow.addView(tv);
        }
        usersTableLayout.addView(headerRow);
    }

    private void addUserRow(User user) {
        TableRow row = new TableRow(this);
        addCell(row, String.valueOf(user.getId()));
        addCell(row, user.getEmail());
        addCell(row, user.getFullName());
        addCell(row, user.getRole());

        Button btnEdit = new Button(this);
        btnEdit.setText("Edit");
        btnEdit.setOnClickListener(v -> showEditDialog(user));
        row.addView(btnEdit);

        usersTableLayout.addView(row);
    }

    private void addCell(TableRow row, String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(10, 10, 10, 10);
        row.addView(tv);
    }

    private void showEditDialog(User user) {
        // Здесь должен быть твой диалог
        Toast.makeText(this, "Редактирование: " + user.getFullName(), Toast.LENGTH_SHORT).show();
    }

    private void setupListeners() {
        logoutButton.setOnClickListener(v -> logout());
    }

    private void logout() {
        sessionManager.logout();
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}