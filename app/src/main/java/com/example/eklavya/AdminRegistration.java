package com.example.eklavya;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eklavya.ui.adminregistration.AdminRegistrationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminRegistration extends AppCompatActivity {

EditText textname,textemail,textpassword,textconfirmpassword;
FirebaseAuth fAuth;
FirebaseFirestore fStore;
String adminID,selecteddept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_registration_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AdminRegistrationFragment.newInstance())
                    .commitNow();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            final Button submitBtn = findViewById(R.id.adminRegistrationSubmit);
            textname=findViewById(R.id.txtName);
            textemail=findViewById(R.id.txtemail);
            textpassword=findViewById(R.id.txtpassword);
            textconfirmpassword=findViewById(R.id.txtconfirmpassword);

            fAuth=FirebaseAuth.getInstance();
            fStore=FirebaseFirestore.getInstance();

            //spinner for department selection



            final Spinner spindept=findViewById(R.id.spinnerDept);
            ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.departmentname,R.layout.support_simple_spinner_dropdown_item);
            adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spindept.setAdapter(adapter1);
        //   spindept.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
            //String selecteddept = spindept.getSelectedItem().toString().trim();


            spindept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selecteddept=adapterView.getItemAtPosition(i).toString();
                    Log.i("BR",selecteddept);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            submitBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   final String name=textname.getText().toString().trim();
                   final String email=textemail.getText().toString().trim();
                   String password=textpassword.getText().toString().trim();
                   String confirmpassword=textconfirmpassword.getText().toString().trim();


                 /*  if (fAuth.getCurrentUser()!=null){
                       Intent intent=new Intent(AdminRegistration.this,AdminMenu.class);
                       startActivity(intent);
                       finish();
                   }*/

                   if(TextUtils.isEmpty(name))
                   {
                       textname.setError("Name is required");
                       return;
                   }
                   if(TextUtils.isEmpty(email))
                   {
                       textemail.setError("Email is required");
                       return;
                   }
                   if(password.length()<6)
                   {
                       textpassword.setError("Password must be greater than 6 character");
                       return;
                   }
                   if(!password.equals(confirmpassword))
                   {
                       textconfirmpassword.setError("Password and Confirm password must be same");
                       return;
                   }

                   //register the user in firebase
                   fAuth.createUserWithEmailAndPassword(email,confirmpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               adminID=fAuth.getCurrentUser().getUid();
                               DocumentReference documentReference=fStore.collection("Admin").document(adminID);
                               Map<String,Object> adminData=new HashMap<>();
                               adminData.put("AdminName",name);
                               adminData.put("AdminEmail",email);
                               adminData.put("Department",selecteddept);

                               documentReference.set(adminData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {

                                   }
                               });

                               Intent intent=new Intent(AdminRegistration.this,AdminMenu.class);
                               startActivity(intent);
                               Toast.makeText(AdminRegistration.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                           }
                       }
                   });


               }

           });


        }
    }



}
