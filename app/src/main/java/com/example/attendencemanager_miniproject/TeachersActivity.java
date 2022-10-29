package com.example.attendencemanager_miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class TeachersActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor speditor;
    private TextView nametextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);

        sp = getSharedPreferences("SignIn", MODE_PRIVATE);

        speditor = sp.edit();
        Toolbar toolbar = findViewById(R.id.teacher_home_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle("");
        nametextview = findViewById(R.id.teacher_home_textName);
        nametextview.setText("Welcome "+sp.getString("KEY_USER_NAME","User"));
        setListeners();

    }

    private void setListeners() {

        findViewById(R.id.teacher_home_imageSignOut).setOnClickListener(v -> signOut());

    }

    private void signOut() {
        showToast("Signing Out...");
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        speditor.putBoolean("KEY_IS_SIGNED_IN", false);
                        speditor.commit();
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                        finish();
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void goToAddClass(View view) {
        startActivity(new Intent(this,AddClass.class));
    }
    public void goToMyClasses(View view) {startActivity(new Intent(this,MyClasses.class));
    }

}