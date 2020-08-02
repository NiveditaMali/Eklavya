package com.example.eklavya.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.eklavya.AdminMenu;
import com.example.eklavya.AdminRegistration;
import com.example.eklavya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    FirebaseAuth fAuth;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);


        final EditText usernameEditText = findViewById(R.id.sfemail);
        final EditText passwordEditText = findViewById(R.id.sfpassword);
        final Button loginButton = findViewById(R.id.sfbtnlogin);
        final Button registerButton = findViewById(R.id.btnRegister);
        final TextView forgotTextView = findViewById(R.id.forgotpassword);


        fAuth=FirebaseAuth.getInstance();


        //login button

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());


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

                //authentic the user

               fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(LoginActivity.this,AdminMenu.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this,"Log In Successfully",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Incorrect email or password",Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

        //Registration button

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, AdminRegistration.class);
                startActivity(intent);

                /*
                AdminRegistrationFragment arf=new AdminRegistrationFragment();
                FragmentManager fm=getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.containerlogin,arf).commit();

                 */
            }
        });

        //forgotpassword

        forgotTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(LoginActivity.this, AdminForgotPassword.class);
               startActivity(intent);*/
                final EditText resetMail=new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter Your email to Received Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link

                        String mail=resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this,"Reset link sent to your Email ",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,"Error! Reset link is not sent ",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the dialog
                    }
                });

                passwordResetDialog.create().show();


            }
        });

    }


}
