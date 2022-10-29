package com.example.attendencemanager_miniproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddClass extends AppCompatActivity {
    private Button AddClassBtn;
    private EditText SubjectName, ClassYear, ClassSem ,CalenderYear;
    private ProgressBar progressBar;
    private SharedPreferences sp;
    private SharedPreferences.Editor speditor;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        // Initialize Firebase Auth and firestore
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        sp = getSharedPreferences("SignIn", MODE_PRIVATE);
        speditor = sp.edit();


        AddClassBtn = findViewById(R.id.addclass_btn);
        SubjectName = findViewById(R.id.addclass_subjectname);
        ClassYear = findViewById(R.id.addclass_year);
        ClassSem = findViewById(R.id.addclass_semester);
        CalenderYear = findViewById(R.id.addclass_calender_year);
        progressBar = findViewById(R.id.addclass_progressBar);

        setListeners();
    }

    private void setListeners() {

        AddClassBtn.setOnClickListener(v -> {
            if (isValidAddClassDetails()) {
                addClass();
            }
        });
    }

    private void addClass() {
        loading(true);
        String subject = SubjectName.getText().toString();
        String year = ClassYear.getText().toString();
        String sem = ClassSem.getText().toString();
        String calyear = CalenderYear.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference df = fstore.collection("Classes").document(subject);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("TeacherId", user.getUid());
        userInfo.put("TeacherName", sp.getString("KEY_USER_NAME","USER"));
        userInfo.put("TeacherEmail", user.getEmail());
        userInfo.put("Subject",subject);
        userInfo.put("ClassYear",year);
        userInfo.put("ClassSem",sem);
        userInfo.put("CalenderYear",calyear);


        df.set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    showToast("Class Added Successfully");
                    loading(false);
                    Intent i = new Intent(getApplicationContext(),AddStudent.class);

                    i.putExtra("SubjectName",subject);
                    startActivity(i);
                    finish();
                }
                else
                {
                    showToast("Task of adding class is completed but not successfull");
                    loading(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Could not add Class due to some Error check AddClass Activity");
                loading(false);
            }
        });



    }

    private Boolean isValidAddClassDetails() {
        if (ClassYear.getText().toString().trim().isEmpty()) {
            showToast("Enter Class Year");
            return false;
        } else if (ClassSem.getText().toString().trim().isEmpty()) {
            showToast("Enter Class Sem");
            return false;
        }
        else if (SubjectName.getText().toString().trim().isEmpty()) {
            showToast("Enter Subject Name");
            return false;
        }
        else {
            return true;
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            AddClassBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            AddClassBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}