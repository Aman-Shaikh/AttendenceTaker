package com.example.attendencemanager_miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.WriteBatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;


public class AddStudent extends AppCompatActivity {

    private Button AddStudentBtn;
    private EditText Prefix, First, Last;
    private ProgressBar progressBar;
    private SharedPreferences sp;
    private SharedPreferences.Editor speditor;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private MyDBHandler sqlitedb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // Initialize Firebase Auth and firestore
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        sp = getSharedPreferences("SignIn", MODE_PRIVATE);
        speditor = sp.edit();


        AddStudentBtn = findViewById(R.id.addstudent_btn);
        Prefix = findViewById(R.id.addstudent_prefix);
        First = findViewById(R.id.addstudent_firstno);
        Last = findViewById(R.id.addstudent_lastno);
        progressBar = findViewById(R.id.addstudent_progressbar);

        setListeners();
    }

    private void setListeners() {

        AddStudentBtn.setOnClickListener(v -> {
            if (isValidAddStudentDetails()) {
                addStudent();
            }
        });
    }

    private void addStudent() {
        loading(true);
        String prefix = Prefix.getText().toString();
        String first = First.getText().toString();
        String last = Last.getText().toString();

        ArrayList<String> ids = new ArrayList<>();

        int lastno = Integer.parseInt(last);
        int firstno = Integer.parseInt(first);


        //Storing subjectname and range into sqlite database
        sqlitedb = new MyDBHandler(this);
        Bundle extras = getIntent().getExtras();
        sqlitedb.storeRangeInSQLite(extras.getString("SubjectName"),prefix,first,last);

//        storeRangeInFile(firstno, lastno, prefix);

        for (int i = firstno; i <= lastno; i++) {
            if(i>=1&&i<=9)
            {
                ids.add(prefix +"00"+ i);
            }

            else if(i>=10&&i<=99)
            {
                ids.add(prefix +"0"+ i);
            }
            else
            {
                ids.add(prefix + i);
            }

        }


        WriteBatch batch = fstore.batch();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = formatter.format(date);

//        showToast(currentDate);
//        To Convert date into milli secons
//        String date_string = "26-09-1989";
//
//        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
//        try {
//            Date date2 = formatter2.parse(date_string);
//            date2.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        for (int i = 0; i < ids.size(); i++) {
            String docPath = "/" + extras.getString("SubjectName") + "/" + ids.get(i) + "/" + currentDate;

            DocumentReference df = fstore.collection("Students").document(docPath);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("Date", currentDate);
            userInfo.put("Attendance", "To be Taken");
            batch.set(df, userInfo);
        }



        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                showToast("All Students Added Successfully");
                loading(false);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Could not add Students due to some Error check AddStudents Activity");
                loading(false);
            }
        });
    }

//    private void storeRangeInFile(int firstno, int lastno, String prefix) {
//        // Creating Folder with name AttendanceManager
//        File folder = getExternalFilesDir("AttendanceManager");
//
//        // Creating file with name range.txt
//        File file = new File(folder, "range.txt");
//
//        FileOutputStream fileOutputStream = null;
//        try {
//            fileOutputStream = new FileOutputStream(file);
//            String data = firstno + "," + lastno+","+prefix;
//            fileOutputStream.write(data.getBytes());
//            Toast.makeText(this, "Done" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }



    private Boolean isValidAddStudentDetails() {
        if (Prefix.getText().toString().trim().isEmpty()) {
            showToast("Enter Prefix");
            return false;
        } else if (First.getText().toString().trim().isEmpty()) {
            showToast("Enter First Number");
            return false;
        } else if (Last.getText().toString().trim().isEmpty()) {
            showToast("Enter Last Number");
            return false;
        } else {
            return true;
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            AddStudentBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            AddStudentBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    public void goToMyClasses(View view) {
        startActivity(new Intent(this, MyClasses.class));
        finish();
    }

}