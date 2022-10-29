package com.example.attendencemanager_miniproject;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignInActivity extends AppCompatActivity {


    private Button SignInBtn;
    private EditText SignInEmail, SignInPass;
    private ProgressBar progressBar;
    private SharedPreferences sp;
    private SharedPreferences.Editor speditor;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        // Initialize Firebase Auth and firestore
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        sp = getSharedPreferences("SignIn", MODE_PRIVATE);
        speditor = sp.edit();
        if (sp.getBoolean("KEY_IS_SIGNED_IN", false)) {
            sendToProperActivity();
        }

        SignInBtn = findViewById(R.id.SignInBtn);
        SignInEmail = findViewById(R.id.SignInEmail);
        SignInPass = findViewById(R.id.SignInPass);
        progressBar = findViewById(R.id.SignInProgressBar);
        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.textCreateNewAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidSignInDetails()) {
                    signIn();
                }
            }
        });
    }

    private Boolean isValidSignInDetails() {
        if (SignInEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(SignInEmail.getText().toString()).matches()) {
            showToast("Enter Valid Email");
            return false;
        } else if (SignInPass.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else {
            return true;
        }
    }

    private void signIn() {
        loading(true);
        String email = SignInEmail.getText().toString();
        String password = SignInPass.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            showToast("Sign in successfully");
                            sendToProperActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            loading(false);
                            showToast("Unable to Sign in Wrong Credidential");
                        }
                    }
                });
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            SignInBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            SignInBtn.setVisibility(View.VISIBLE);
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