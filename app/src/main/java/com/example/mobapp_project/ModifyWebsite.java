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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModifyWebsite extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public static final String TAG = "ModifyWebsite";

    private String name, url, login, password, idUser, idDocument;
    private int backButtonCount = 0;

    EditText txt_name, txt_url, txt_login, txt_password;
    CheckBox checkBox_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_website);

        Intent i = getIntent();
        if (i != null) {
            if (i.hasExtra("IdDocument")) {
                idDocument = i.getStringExtra("IdDocument");
            }
            if (i.hasExtra("Name")) {
                name = i.getStringExtra("Name");
                txt_name = (EditText) findViewById((R.id.nameWebsiteModify));
                txt_name.setText(name);
            }
            if (i.hasExtra("URL")) {
                url = i.getStringExtra("URL");
                txt_url = (EditText)findViewById((R.id.urlModify));
                txt_url.setText(url);
            }
            if (i.hasExtra("Login")) {
                login = i.getStringExtra("Login");
                txt_login = (EditText)findViewById((R.id.loginWebsiteModify));
                txt_login.setText(login);
            }
            if (i.hasExtra("Password")) {
                password = i.getStringExtra("Password");
                txt_password = (EditText)findViewById((R.id.passwordWebsiteModify));
                txt_password.setText(password);
            }
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        idUser = user.getUid();

        checkBox_show = (CheckBox)findViewById(R.id.checkBox_showPassword_modify);

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
    }

    protected void onStart() {
        super.onStart();
    }

    private void updateText() {
        txt_name = (EditText) findViewById((R.id.nameWebsiteModify));
        name = txt_name.getText().toString().trim();
        txt_name.setText(name);

        txt_url = (EditText)findViewById((R.id.urlModify));
        url = txt_url.getText().toString().trim();
        txt_url.setText(url);

        txt_login = (EditText)findViewById((R.id.loginWebsiteModify));
        login = txt_login.getText().toString().trim();
        txt_login.setText(login);

        txt_password = (EditText)findViewById((R.id.passwordWebsiteModify));
        password = txt_password.getText().toString().trim();
        txt_password.setText(password);
    }

    private void updateWebsite() {
        db.collection("Users").document("Website").collection(idUser).document(idDocument)
                .update("Name", name,
                        "URL", url,
                        "Login", login,
                        "Password", password
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Toast.makeText(getApplicationContext(), "Website modified", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error editing document", e);
                    }
                });
    }

    private void deleteWebsite() {
        db.collection("Users").document("Website").collection(idUser).document(idDocument)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete :
                deleteWebsite();
                Intent i_delete = new Intent(this, MainPage.class);
                startActivity(i_delete);
                break;
            case R.id.btn_update :
                updateText();
                updateWebsite();
                break;
            case R.id.txt_backModify:
                Intent i_back = new Intent(this, MainPage.class);
                startActivity(i_back);
                break;
        }
    }
}