package com.example.eklavya;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eklavya.ui.adminusers.AdminUsersFragment;

public class AdminUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_users_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AdminUsersFragment.newInstance())
                    .commitNow();
        }
    }
}
