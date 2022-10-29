package com.example.attendencemanager_miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    private Button SignUpBtn;
    private EditText SignUpName, SignUpEmail, SignUpPass, SignUpCPass;
    private SharedPreferences sp;
    private SharedPreferences.Editor speditor;
    private ProgressBar SignUpProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth and firestore
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        sp = getSharedPreferences("SignIn", MODE_PRIVATE);
        speditor = sp.edit();


        SignUpBtn = findViewById(R.id.SignUpBtn);
        SignUpEmail = findViewById(R.id.SignUpEmail);
        SignUpName = findViewById(R.id.SignUpName);
        SignUpPass = findViewById(R.id.SignUpPass);
        SignUpCPass = findViewById(R.id.SignUpCPass);
        SignUpProgressBar = findViewById(R.id.SignUpProgressBar);

        setListeners();
    }

    private void setListeners() {

        findViewById(R.id.textSignIn).setOnClickListener(v -> onBackPressed());

        SignUpBtn.setOnClickListener(v -> {
            if (isValidSignUpDetails()) {
                signup();
            }
        });
    }

    private void signup() {
        loading(true);
        String email = SignUpEmail.getText().toString();
        String password = SignUpPass.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            DocumentReference df = fstore.collection("Users").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("Name", SignUpName.getText().toString());
                            userInfo.put("Email", SignUpEmail.getText().toString());
                            if (password.startsWith("bvmrocks")) {
                                userInfo.put("isAdmin", true);
                            } else {
                                userInfo.put("isAdmin", false);
                            }
                            df.set(userInfo);


                            sendToProperActivity();

                        } else {
                            // If sign in fails, display a message to the user.

                            showToast("Sign Up Failed");
                            loading(false);
                        }
                    }
                });


    }

    private Boolean isValidSignUpDetails() {
        if (SignUpName.getText().toString().trim().isEmpty()) {
            showToast("Enter Name");
            return false;
        } else if (SignUpEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(SignUpEmail.getText().toString()).matches()) {
            showToast("Enter Valid Email");
            return false;
        } else if (SignUpPass.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else if (SignUpCPass.getText().toString().trim().isEmpty()) {
            showToast("Confirm your Password");
            return false;
        } else if (!SignUpPass.getText().toString().equals(SignUpCPass.getText().toString())) {
            showToast("Password & Confirm Password must be same");
            return false;
        } else {
            return true;
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            SignUpBtn.setVisibility(View.INVISIBLE);
            SignUpProgressBar.setVisibility(View.VISIBLE);
        } else {
            SignUpProgressBar.setVisibility(View.INVISIBLE);
            SignUpBtn.setVisibility(View.VISIBLE);
        }
    }

    private void sendToProperActivity() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
        DocumentReference df;
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();


        df = fstore.collection("Users").document(currentid);

        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                if (ds.exists()) {
                    if (ds.getBoolean("isAdmin") == true) {
                        speditor.putBoolean("KEY_IS_SIGNED_IN", true);
                        speditor.putString("KEY_USER_NAME", ds.getString("Name"));
                        speditor.commit();
                        showToast("Sign up successfully");
                        startActivity(new Intent(getApplicationContext(), TeachersActivity.class));
                        finish();
                    } else {
                        speditor.putBoolean("KEY_IS_SIGNED_IN", true);
                        speditor.putString("KEY_USER_NAME", ds.getString("Name"));
                        speditor.commit();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                } else {
                    showToast("Something Went Wrong data could not be fetched");
                }
            }
        });

    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
