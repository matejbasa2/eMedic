package com.example.emedic.login;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.emedic.Doctor.DoctorPanel;
import com.example.emedic.Pacient.PacientPanel;
import com.example.emedic.R;
import com.example.emedic.register.RegisterActivity;
import com.example.emedic.sharedPref.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private String[] fruits = {"matej.basa@gmail.com", "filip.lukic@gmail.com"};
    private AutoCompleteTextView mEmail;


    // widgets
    private EditText mPassword;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fruits);
        mEmail.setThreshold(1); //will start working from first character
        mEmail.setAdapter(adapter);
        mPassword = findViewById(R.id.password);
        mProgressBar = findViewById(R.id.progressBar);
        ImageView imageView = findViewById(R.id.pill);

        imageView.startAnimation(
                AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotation_ccw));

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.link_register).setOnClickListener(this);

        hideSoftKeyboard();

    }




    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void toast_1(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void signIn(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://207.180.220.65:4545/api/api-token-auth/";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", mEmail.getText().toString());
            jsonBody.put("password", mPassword.getText().toString());
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEYY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    toast_1("Wrong username or password");
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        String parsed;
                        try {
                            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        } catch (UnsupportedEncodingException e) {
                            parsed = new String(response.data);
                        }
                        Log.d("Response: ",parsed);
                        String user = "";
                        try {
                            JSONObject obj = new JSONObject(parsed);
                            Log.d("Token", obj.get("token").toString());
                            Log.d("User", obj.get("user").toString());
                            user = obj.get("user").toString();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        new PrefManager().saveToken(obj.get("token").toString(), getApplicationContext());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("Get token call : ", new PrefManager().getToken(getApplicationContext()));
                                }
                            });
                            if(user.equals("zdravnik")){
                                Intent doctor = new Intent(LoginActivity.this, DoctorPanel.class);
                                startActivity(doctor);
                            }
                            else if(user.equals("pacient")){
                                Intent pacient = new Intent(LoginActivity.this, PacientPanel.class);
                                startActivity(pacient);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.link_register:{
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
                break;
            }

            case R.id.email_sign_in_button:{
                signIn();


                /*
                if(mEmail.getText().toString().contains("patient") || mEmail.getText().toString().contains("Pacient")){
                    Intent pacient = new Intent(LoginActivity.this, PatientStatistics.class);
                    startActivity(pacient);
                }
                else if(mEmail.getText().toString().contains("doctor") || mEmail.getText().toString().contains("Doctor")){
                    Intent doctor = new Intent(LoginActivity.this, DoctorPanel.class);
                    startActivity(doctor);
                }
                else if(mEmail.getText().toString().contains("blood") || mEmail.getText().toString().contains("Doctor")){
                    Intent blood = new Intent(LoginActivity.this, BloodSugarLevels.class);
                    startActivity(blood);
                }
                else{
                    Toast.makeText(getBaseContext(),"Napačen vnos! Ponovno preveri vnosno polje (use email: pacient; or use: doctor)",
                            Toast.LENGTH_SHORT).show();
                }*/
                break;
            }
        }
    }
}