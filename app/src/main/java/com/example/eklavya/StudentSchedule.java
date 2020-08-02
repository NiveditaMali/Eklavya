package com.example.eklavya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eklavya.ui.studentschedule.StudentScheduleFragment;

public class StudentSchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_schedule_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StudentScheduleFragment.newInstance())
                    .commitNow();
        }
    }
}
