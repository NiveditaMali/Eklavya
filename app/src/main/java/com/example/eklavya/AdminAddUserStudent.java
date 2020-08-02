package com.example.eklavya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eklavya.ui.adminadduserstudent.AdminAddUserStudentFragment;

public class AdminAddUserStudent extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_user_student_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AdminAddUserStudentFragment.newInstance())
                    .commitNow();
        }
    }
}
