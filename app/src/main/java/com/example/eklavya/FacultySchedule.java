package com.example.eklavya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eklavya.ui.facultyschedule.FacultyScheduleFragment;

public class FacultySchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_schedule_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FacultyScheduleFragment.newInstance())
                    .commitNow();
        }
    }
}
