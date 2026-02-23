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
import com.example.photolens.models.Grade;
import com.example.photolens.models.User;
import com.example.photolens.repositories.interfaces.GradeRepository;
import com.example.photolens.repositories.interfaces.UserRepository;
import com.example.photolens.utils.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherMainActivity extends AppCompatActivity {
    
    private TextView tvTitle;
    private Button btnOpenSimulator;
    private Button btnLogout;
    private TableLayout tableGrades;
    
    private SessionManager sessionManager;
    private UserRepository userRepository;
    private GradeRepository gradeRepository;
    
    private Map<Integer, String> studentNames;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        
        sessionManager = new SessionManager(this);
        userRepository = RepositoryFactory.getUserRepository();
        gradeRepository = RepositoryFactory.getGradeRepository();
        studentNames = new HashMap<>();
        
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }
        
        initializeViews();
        loadStudentGrades();
        setupListeners();
    }

    private void initializeViews() {
        tvTitle = findViewById(R.id.titleTextView);

        btnOpenSimulator = findViewById(R.id.openSimulatorButton);

        btnLogout = findViewById(R.id.logoutButton);

        tableGrades = findViewById(R.id.gradesTableLayout);
    }
    
    private void loadStudentGrades() {
        int teacherId = sessionManager.getUserId();
        
        List<Grade> grades = gradeRepository.getGradesByTeacher(teacherId);
        
        if (grades == null || grades.isEmpty()) {
            Toast.makeText(this, "У вас пока нет выставленных оценок", Toast.LENGTH_SHORT).show();
            return;
        }
        
        createTableHeader();
        
        for (Grade grade : grades) {
            addGradeRow(grade);
        }
    }
    
    private void createTableHeader() {
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        ));
        
        String[] headers = {"ФИО ученика", "Предмет", "Дата", "Оценка", "Комментарий"};
        
        for (String header : headers) {
            TextView tv = new TextView(this);
            tv.setText(header);
            tv.setPadding(16, 16, 16, 16);
            tv.setTextSize(14);
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
            headerRow.addView(tv);
        }
        
        tableGrades.addView(headerRow);
    }
    
    private void addGradeRow(Grade grade) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        ));
        
        String studentName = getStudentName(grade.getStudentId());
        
        String[] data = {
            studentName,
            grade.getSubject(),
            grade.getDate(),
            String.valueOf(grade.getGrade()),
            grade.getComment()
        };
        
        for (String cellData : data) {
            TextView tv = new TextView(this);
            tv.setText(cellData);
            tv.setPadding(16, 12, 16, 12);
            tv.setTextSize(13);
            row.addView(tv);
        }
        
        row.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        
        tableGrades.addView(row);
    }
    
    private String getStudentName(int studentId) {
        if (!studentNames.containsKey(studentId)) {
            User student = userRepository.getUserById(studentId);
            if (student != null) {
                studentNames.put(studentId, student.getFullName());
            } else {
                studentNames.put(studentId, "Неизвестный студент");
            }
        }
        return studentNames.get(studentId);
    }
    
    private void setupListeners() {
        btnOpenSimulator.setOnClickListener(v -> openSimulator());
        btnLogout.setOnClickListener(v -> logout());
    }
    
    private void openSimulator() {
        Toast.makeText(this, "Переход к симулятору камеры (в разработке)", Toast.LENGTH_LONG).show();
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
