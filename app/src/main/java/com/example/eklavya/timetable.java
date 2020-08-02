package com.example.eklavya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eklavya.ui.timetable.TimetableFragment;

public class timetable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TimetableFragment.newInstance())
                    .commitNow();
        }
    }
}
