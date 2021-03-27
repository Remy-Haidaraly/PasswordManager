package com.example.mobapp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPassword_page extends AppCompatActivity implements View.OnClickListener {

    Button btn_recover;
    TextView txt_homePage;
    public static final String TAG = "EmailSend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password_page);

        btn_recover = (Button) findViewById((R.id.btn_recover));
        txt_homePage = (TextView) findViewById((R.id.txt_homePage));

        btn_recover.setOnClickListener(this);
        txt_homePage.setOnClickListener(this);
    }

    public void sendPasswordReset() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        EditText txt_email = (EditText)findViewById(R.id.email2);
        String emailAddress = txt_email.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Email not sent.");
                            Toast.makeText(getApplicationContext(), "Email inexistant", Toast.LENGTH_SHORT).show();
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
            case R.id.btn_recover :
                sendPasswordReset();
                break;
            case R.id.txt_homePage :
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
        }
    }
}