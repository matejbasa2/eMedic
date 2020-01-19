package com.example.emedic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emedic.Pacient.PacientKartoteka;

public class DisplayRecord extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_record);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(PacientKartoteka.EXTRA_MESSAGE);
        Log.d("TAG", message);
        String prikaz = message.replace(",",": ");
        Log.d("TAG", prikaz);
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(prikaz);
    }
}
