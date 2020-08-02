package com.example.eklavya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eklavya.ui.setpassword.SetPasswordFragment;

public class SetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SetPasswordFragment.newInstance())
                    .commitNow();

            final Button spsBtn = findViewById(R.id.setpassbtnsubmit);

            spsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openAdminMenu();

                }

            });

        }
    }

    //open admin menu
    private void openAdminMenu() {
        Intent intent=new Intent(SetPassword.this,AdminMenu.class);
        startActivity(intent);
        Toast.makeText(this,"Password Set Successfully",Toast.LENGTH_LONG).show();
    }
}
