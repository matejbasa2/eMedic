package com.example.emedic.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.example.emedic.R;
import com.example.emedic.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener
{
    private static final String TAG = "RegisterActivity";

    //widgets
    private TextView textReg;
    private EditText mName, mSurname, mEmail, mPassword, mConfirmPassword;
    private ProgressBar mProgressBar;
    private Switch PDswitch;

    //vars


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mName = (EditText) findViewById(R.id.input_name);
        mSurname = (EditText) findViewById(R.id.input_surname);
        textReg = (TextView) findViewById(R.id.textReg);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        PDswitch = (Switch) findViewById(R.id.PDswitch);

        PDswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) textReg.setText("Doktor register");
                else textReg.setText("Pacient register");
            }
        });

        textReg.setText("Pacient register");

        findViewById(R.id.btn_register).setOnClickListener(this);


        hideSoftKeyboard();
    }


    public void registerNewUser(String name, String surname, String email, String password){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL;
            // check if register doctor or pacient based on the position of the switch
            if(PDswitch.isChecked()) URL = "http://207.180.220.65:4545/api/zdravnik-register";
            else URL = "http://207.180.220.65:4545/api/pacient-register";

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("surname", surname);
            jsonBody.put("username", email);
            jsonBody.put("password", password);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEYY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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
                        runOnUiThread(new Runnable() {
                            public void run() {

                            }
                        });
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Redirects the user to the login screen
     */
    private void redirectLoginScreen(){
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        if(mPassword.getText().toString().length() > 6)
        {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("email", mEmail.getText().toString());
            intent.putExtra("password",mPassword.getText().toString());
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Password length must be 7 or more characters",Toast.LENGTH_LONG).show();
        }
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

    public void toast_1(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    // returns true if all the input fields contains valid values
    public boolean validateInputFields(String name, String surname, String email, String pass_1, String pass_2){
        // preveri ali je ime vneseno
        if(name.isEmpty()){
            toast_1("Please enter Name");
            return false;
        }
        // preveri ali je ime vneseno
        if(surname.isEmpty()){
            toast_1("Please enter Surname");
            return false;
        }
        // preveri ali je mail vnesen
        if(email.isEmpty()){
            toast_1("Please enter Email");
            return false;
        }
        if(pass_1.isEmpty() || pass_2.isEmpty()){
            toast_1("Please enter Password");
            return false;
        }
        if(!pass_1.equals(pass_2)){
            toast_1("The passwords does not mach");
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:{
                Log.d(TAG, "onClick: attempting to register.");
                String name = mName.getText().toString();
                String surname = mSurname.getText().toString();
                String email = mEmail.getText().toString();
                String pass_1 = mPassword.getText().toString();
                String pass_2 = mConfirmPassword.getText().toString();
                Log.d("Register details", "Mail: " + email + ", pass_1: " + pass_1 + ", pass_2:" + pass_2);

                if(validateInputFields(name, surname, email, pass_1, pass_2)){
                    registerNewUser(name, surname, email, pass_1);
                }
                break;
            }
        }
    }
}

