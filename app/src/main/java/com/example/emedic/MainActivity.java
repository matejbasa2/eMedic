package com.example.emedic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emedic.Doctor.DoctorPanel;
import com.example.emedic.Pacient.PacientPanel;
import com.example.emedic.login.LoginActivity;
import com.example.emedic.sharedPref.PrefManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);

        Button logIn = findViewById(R.id.logIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new PrefManager().testSave(getApplicationContext());
                //new PrefManager().saveToken("To je testni token ki sem ga shranil",getApplicationContext());

                Log.d("Token get", new PrefManager().getToken(getApplicationContext()));

                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        Button dr_panel = findViewById(R.id.doctor_panel);
        dr_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new PrefManager().testSave(getApplicationContext());
                //new PrefManager().saveToken("To je testni token ki sem ga shranil",getApplicationContext());

                Log.d("Token get", new PrefManager().getToken(getApplicationContext()));

                Intent doctor = new Intent(MainActivity.this, DoctorPanel.class);
                startActivity(doctor);
            }
        });

        Button pc_panel = findViewById(R.id.pacient_panel);
        pc_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new PrefManager().testSave(getApplicationContext());
                //new PrefManager().saveToken("To je testni token ki sem ga shranil",getApplicationContext());

                Log.d("Token get", new PrefManager().getToken(getApplicationContext()));

                Intent pacient = new Intent(MainActivity.this, PacientPanel.class);
                startActivity(pacient);
            }
        });
    }
}
