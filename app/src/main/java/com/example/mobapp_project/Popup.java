package com.example.mobapp_project;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Popup extends Dialog {

    private ArrayList<String> tool1 = new ArrayList<>();
    private ArrayList<String> tool2 = new ArrayList<>();
    private TextView passwordGenerate;
    private TextView lengthNumber;
    private RadioButton optFull;
    private RadioButton optNumbers;
    private Button btn_generate;
    private Button copyText;

    private SeekBar length;
    private  int progress = 0;
    private int option = 0;

    public Popup(Activity activity) {
        super(activity, R.style.Theme_AppCompat_Light_Dialog_Alert);
        setContentView(R.layout.activity_generate_password);

        passwordGenerate = findViewById(R.id.txt_generated_password);
        lengthNumber = findViewById(R.id.txt_length);
        optFull = findViewById(R.id.opt_full);
        optNumbers = findViewById(R.id.opt_numbers);
        btn_generate = findViewById(R.id.btn_generate);
        copyText = findViewById(R.id.btn_copy);
        length = findViewById(R.id.Length);
        length.setMax(30-8);
    }

    public TextView getPasswordGenerate() { return passwordGenerate; }

    public TextView getLengthNumber() { return lengthNumber; }

    public RadioButton getOptFull() { return optFull; }

    public RadioButton getOptNumbers() { return optNumbers; }

    public Button getBtn_generate() { return btn_generate; }

    public Button getCopyText() { return copyText; }

    public SeekBar getLength() { return length; }

}
