package com.example.eklavya;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eklavya.ui.changepassword.ChangePasswordFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ChangePassword extends AppCompatActivity {

    EditText etoldpass,etnewpass,etconfirmnewpass;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ChangePasswordFragment.newInstance())
                    .commitNow();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            fAuth=FirebaseAuth.getInstance();
            final Button cpsBtn = findViewById(R.id.changepassbtnsubmit);
            etoldpass=findViewById(R.id.old_password);
            etnewpass=findViewById(R.id.new_password);
            etconfirmnewpass=findViewById(R.id.new_confirmpassword);

            cpsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String oldpass,newpass,confirmnewpass;
                    oldpass=etoldpass.getText().toString();
                    newpass=etnewpass.getText().toString();
                    confirmnewpass=etconfirmnewpass.getText().toString();

                    if (oldpass.equals("")){
                        etoldpass.setError("Password is Required");
                        return;                    }
                    else if (newpass.equals("")){
                        etnewpass.setError("Password is Required");
                    }
                    else if (confirmnewpass.equals("")){
                        etconfirmnewpass.setError("Password is Required");
                    }
                    else if(newpass.length()<6 )
                    {
                        etnewpass.setError("Password must be greater than 6 character");
                        return;
                    }
                    else if(!newpass.equals(confirmnewpass))
                    {
                        etconfirmnewpass.setError("Password and Confirm password must be same");
                        return;
                    }
                    else{
                        final FirebaseUser user=fAuth.getCurrentUser();
                        AuthCredential credential= EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()),oldpass);

                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(confirmnewpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(ChangePassword.this,"Password changed",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(ChangePassword.this,"Password not changed",Toast.LENGTH_SHORT).show();

                                        }
                                        }
                                    });
                                }
                            }
                        });
                    }
                  // openAdminMenu();

                }

            });
        }
    }

  /*  private void openAdminMenu() {
        Intent intent=new Intent(ChangePassword.this,AdminMenu.class);
        startActivity(intent);
        Toast.makeText(this,"Password Changed Successfully",Toast.LENGTH_LONG).show();
    }

   */
}
