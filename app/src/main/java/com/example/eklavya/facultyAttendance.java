package com.example.eklavya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eklavya.ui.facultyattendance.FacultyAttendanceFragment;

public class facultyAttendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_attendance_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FacultyAttendanceFragment.newInstance())
                    .commitNow();
        }
    }
}
