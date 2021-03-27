package com.example.mobapp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignIn_page extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,30}$");
            //at least 1 digit, 1 lower case letter, 1 upper case letter, 1 special character,
            //no white spaces, at least 8 characters

    public static final String TAG = "CreatingUser";

    private String email, username, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);
        mAuth = FirebaseAuth.getInstance();
    }

    private boolean validateEmail() {
        EditText txt_email = (EditText)findViewById((R.id.email));
        email = txt_email.getText().toString().trim();

        if (email.isEmpty()) {
            txt_email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txt_email.setError("Please enter a validate email address.");
            return false;
        }
        else {
            txt_email.setError(null);
            return true;
        }
    }

    private boolean validatePseudo() {
        EditText txt_username = (EditText)findViewById(R.id.pseudo);
        username = txt_username.getText().toString().trim();

        if (username.isEmpty()) {
            txt_username.setError("Field can't be empty");
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        EditText txt_password = (EditText)findViewById(R.id.password_signIn);
        password = txt_password.getText().toString();

        if (password.isEmpty()) {
            txt_password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            txt_password.setError("Password too weak.\nPlease choose a password with at least " +
                    "one digit, one lower case letter, one upper case letter, one special character " +
                    "six characters and without white spaces.");
            return false;
        } else {
            txt_password.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        EditText txt_confirmPassword = (EditText)findViewById(R.id.confirmPassword);
        confirmPassword = txt_confirmPassword.getText().toString();

        if (confirmPassword.isEmpty()) {
            txt_confirmPassword.setError("Please, confirm your password");
            return false;
        } else if (!password.equals(confirmPassword)) {
            txt_confirmPassword.setError("Please, enter the same password");
            return false;
        } else {
            txt_confirmPassword.setError(null);
            return true;
        }
    }

    private void registerUser() {
        if (validateEmail() == false || validatePseudo() == false ||
                validatePassword() == false || validateConfirmPassword() == false) { return; }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                }
                            });

                    Intent intent = new Intent(SignIn_page.this, MainPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signIn_signIn :
                this.registerUser();
                break;
            case R.id.txt_alreadyAnAccount :
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }
    }
}