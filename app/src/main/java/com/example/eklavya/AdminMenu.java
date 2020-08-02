package com.example.eklavya;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.eklavya.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminMenu extends AppCompatActivity {

    TextView adminName,deptName;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String adminID,admindept;
    AppBarLayout appbar;

    private AppBarConfiguration mAppBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  appbar=findViewById(R.id.admin_menu_appbar);
       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_users, R.id.nav_notice, R.id.nav_timetable,R.id.nav_subject,
                R.id.nav_contactus, R.id.nav_appinfo)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //   adminDept=findViewById(R.id.AdminMenuDept);
        View headerView = navigationView.getHeaderView(0);
        adminName = (TextView) headerView.findViewById(R.id.AdminMenuName);
        deptName = (TextView) headerView.findViewById(R.id.AdminMenuDept);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();


        //asking permission
      //  ActivityCompat.requestPermissions(AdminMenu.this,new String[]{Manifest.permission_group.STORAGE},102);
        //profile

        adminID=fAuth.getCurrentUser().getUid();

        DocumentReference  documentReference=fStore.collection("Admin").document(adminID);



        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    String username=documentSnapshot.getString("AdminName");
                    String deptname=documentSnapshot.getString("Department");
                    Log.i("Ad",username);
                    Log.i("Ad",deptname);
                    adminName.setText(username);
                   deptName.setText(deptname);
                }
            }
        });
        //  Toast.makeText(this,adminID,Toast.LENGTH_LONG).show();
/*
       DocumentReference documentReference=fStore.collection("users").document(adminID);
      documentReference.addSnapshotListener(AdminMenu.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot,FirebaseFirestoreException e) {
                    adminName.setText(documentSnapshot.getString("AdminName"));
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //String msg=" ";
        switch (item.getItemId()){
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(this,"Log Out Successfully",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_change_password:
                Intent intent1=new Intent(this,ChangePassword.class);
                startActivity(intent1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}