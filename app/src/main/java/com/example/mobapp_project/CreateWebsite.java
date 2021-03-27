package com.example.mobapp_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateWebsite extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String TAG = "CreateWebsite";

    EditText txt_name, txt_url, txt_login, txt_password;
    CheckBox checkBox_show;
    private String name, url, login, password, id;
    private int backButtonCount = 0;

    Popup popup;
    private int option = 0;
    private int progressVal = 0;
    private String copiedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_website);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        id = user.getUid();

        popup = new Popup(this);

        txt_name = (EditText)findViewById(R.id.nameWebsite);

        txt_url = (EditText)findViewById(R.id.url);

        txt_login = (EditText)findViewById(R.id.loginWebsite);

        txt_password = (EditText)findViewById(R.id.passwordWebsite);

        checkBox_show = (CheckBox)findViewById(R.id.checkBox_showPassword_create);

        checkBox_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    //show password
                    txt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    //hide password
                    txt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        popup.getLength().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b)
            {
                progressVal = progressValue + 8;
                popup.getLengthNumber().setText("Length : " + progressVal);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    protected void onStart() {
        super.onStart();
    }

    private void createWebsite () {
        name = txt_name.getText().toString().trim();
        url = txt_url.getText().toString().trim();
        login = txt_login.getText().toString().trim();
        password = txt_password.getText().toString().trim();

        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("Name", name);
        data.put("URL", url);
        data.put("Login", login);
        data.put("Password", password);

        db.collection("Users").document("Website").collection(id)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Website created", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent i_back = new Intent(this, MainPage.class);
            startActivity(i_back);
        } else {
            Toast.makeText(this, "Warning: changes will not be saved", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    private boolean error() {
        if(!popup.getOptFull().isChecked() && !popup.getOptNumbers().isChecked()) {
            Toast.makeText(this,"Error. Please select your option",Toast.LENGTH_LONG).show();
            return false;
        }
        if (progressVal > 30 || progressVal < 8) {
            Toast.makeText(this,"Error. Please select your length between 8 and 30",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private String generatePassword(int length,int choix) {
        char [] full = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz~`!,@#$%^&*()-=+[]{}<>?0123456789".toCharArray();
        char [] number = "0123456789".toCharArray();
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        if(choix == 1) {
            for (int i = 0; i < length; i++) {
                char c = full[r.nextInt(full.length)];
                sb.append(c);
            }
        } else {
            for (int i = 0; i < length; i++) {
                char c = number[r.nextInt(number.length)];
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_generatePassword:
                popup.getBtn_generate().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (error()) {
                            if (popup.getOptFull().isChecked()) {
                                option = 1;
                                copiedPassword = generatePassword(progressVal, option);
                                popup.getPasswordGenerate().setText("The generated password is : " + copiedPassword);
                            }

                            if (popup.getOptNumbers().isChecked()) {
                                option = 2;
                                copiedPassword = generatePassword(progressVal, option);
                                popup.getPasswordGenerate().setText("The generated password is : " + copiedPassword);
                            }
                        }
                    }
                });
                popup.getCopyText().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        txt_password.setText(copiedPassword);
                        popup.hide();
                    }
                });
                popup.show();
                break;
            case R.id.btn_createWebsite :
                createWebsite();
                Intent i = new Intent(this, MainPage.class);
                startActivity(i);
                break;
            case R.id.txt_back:
                Intent i_back = new Intent(this, MainPage.class);
                startActivity(i_back);
                break;
        }
    }
}