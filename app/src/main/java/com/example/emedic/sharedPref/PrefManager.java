package com.example.emedic.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class PrefManager extends AppCompatActivity {

    public PrefManager(){

    }

    // when you login you get token and save it
    public void saveToken(String token, Context context){
        SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", token);
        editor.apply();
    }

    // returns saved token that will be used as authentication for rest calls
    public String getToken(Context context){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        // in case if token does not exist it returns null
        return pref.getString("token", null);
    }
}
