package com.example.mobapp_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GetUser";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    FirebaseUser user;

    ListView listView;
    CollectionReference websitesCollection;

    private List<String> websitesList = new ArrayList<>();

    private String idUser, idDocument;

    private String name, url, login, password;

    private int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        this.getUser();
        user = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();

        idUser = user.getUid();
        Log.d(TAG, "Id User : " + idUser);

        websitesCollection = db.collection("Users").document("Website").collection(idUser);
        Log.d(TAG, "Websites collection id : " + websitesCollection);
        websitesCollection.orderBy("Name");

        listView = (ListView)findViewById(R.id.listView);

        showList();
        selectElement();
    }

    private void showList() {
        websitesCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                websitesList.clear();

                for(DocumentSnapshot snapshot : value) {
                    websitesList.add(snapshot.getString("Name"));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, websitesList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);

                        TextView textView=(TextView) view.findViewById(android.R.id.text1);

                        /*YOUR CHOICE OF COLOR*/
                        textView.setTextColor(0xFF808080);

                        return view;
                    }
                };

                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });
    }

    private void selectElement() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                name = (listView.getItemAtPosition(position).toString());

                showItems(name);
            }
        });
    }

    private void showWebsite() {
        Intent i = new Intent(this, ModifyWebsite.class);

        i.putExtra("IdDocument", idDocument);
        if (name != null) {
            i.putExtra("Name", name);
        }
        if (url != null) {
            i.putExtra("URL", url);
        }
        if (login != null) {
            i.putExtra("Login", login);
        }
        if (password != null) {
            i.putExtra("Password", password);
        }

        startActivity(i);
    }

    private void showItems(String name) {
        websitesCollection
                .whereEqualTo("Name", name)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for(DocumentSnapshot snapshot : value) {
                    idDocument = snapshot.getId();
                    url = snapshot.getString("URL");
                    login = snapshot.getString("Login");
                    password = snapshot.getString("Password");

                    showWebsite();
                }
            }
        });
    }

    private void getUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        TextView nameUser = findViewById(R.id.nameUser);
        if (user != null) {
            Log.d(TAG, "User connected");
            // Name, email address
            String name = user.getDisplayName();
            nameUser.setText(name);
        } else {
            Log.d(TAG, "User not connected");
        }
    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1)
        {
            mAuth.signOut();
            Intent i_MainActivity = new Intent(this, MainActivity.class);
            startActivity(i_MainActivity);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to disconnect.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addWebsite:
                Intent i = new Intent(this, CreateWebsite.class);
                startActivity(i);
                break;
            case R.id.btn_logout:
                mAuth.signOut();
                Intent i_MainActivity = new Intent(this, MainActivity.class);
                startActivity(i_MainActivity);
                break;
        }
    }
}