package com.example.eklavya;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainLogin extends AppCompatActivity  {

    private RadioButton studentRadio,facultyRadio;
    private Button loginBtn;
EditText usernameEditText,passwordEditText;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        fAuth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        usernameEditText = findViewById(R.id.sfemail);
        passwordEditText = findViewById(R.id.sfpassword);
        studentRadio =findViewById(R.id.radio_student);
        facultyRadio =findViewById(R.id.radio_faculty);
        loginBtn=findViewById(R.id.sfbtnlogin);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Eklavya");


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=usernameEditText.getText().toString().trim();
                String password=passwordEditText.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    usernameEditText.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passwordEditText.setError("Password is required");
                    return;
                }
                if (studentRadio.isChecked()){
                    openStudentMenu();

                }
                else if (facultyRadio.isChecked()){
                    openFacultyMenu();
                }

                //authentic the user
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            if (studentRadio.isChecked()){
                                openStudentMenu();

                            }
                            else if (facultyRadio.isChecked()){
                                openFacultyMenu();
                            }
                            Toast.makeText(MainLogin.this,"Log In Successfully",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(MainLogin.this,"Incorrect email or password",Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

    }


    // open StudentMenu
    public void openStudentMenu(){
        Intent intent=new Intent(this,StudentMenu.class);
        startActivity(intent);
    }


    //open faculty menu
    public void openFacultyMenu(){
        Intent intent=new Intent(this,FacultyMenu.class);
        startActivity(intent);
    }

    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_content,menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //String msg=" ";
        switch (item.getItemId()){
            case R.id.admin:
                openAdminLogin();
                break;
            case R.id.refresh:
                Toast.makeText(this,"Refresh",Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    //open admin login
    public void  openAdminLogin(){
        Intent intent=new Intent(this,com.example.eklavya.ui.login.LoginActivity.class);
        startActivity(intent);
    }


}
