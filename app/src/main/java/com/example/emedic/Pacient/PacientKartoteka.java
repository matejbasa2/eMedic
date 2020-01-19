package com.example.emedic.Pacient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


//import android.support.v7.app.AppCompatActivity;

import com.example.emedic.DisplayRecord;
import com.example.emedic.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.hdodenhof.circleimageview.CircleImageView;

public class PacientKartoteka extends AppCompatActivity {

    //widgets
    private CircleImageView mAvatarImage;

    //vars
    private static int RESULT_LOAD_IMAGE = 1;

    public static final String EXTRA_MESSAGE = "com.example.emedic.MESSAGE";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.pacient_kartoteka_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent previous = new Intent(PacientKartoteka.this, PacientPanel.class);
                startActivity(previous);
                return true;

            case R.id.action_download:
                export();
                return true;

            case R.id.action_kartoteka:
                izpis();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    //EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient_kartoteka);
        //mEditText = findViewById(R.id.edit_text);

        Toolbar myToolbar = findViewById(R.id.kartoteka_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.emedic_logo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }


    }

    public void izpis(){
        StringBuilder data = new StringBuilder();
        data.append("Ime_Polja,Razlaga\n");
        String line ="";
        StringBuilder finalstring = new StringBuilder();
        InputStream is = getResources().openRawResource(R.raw.a);
        BufferedReader br = new BufferedReader(new InputStreamReader((is)));

        try {
            while ((line = br.readLine()) != null){
                finalstring.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TAG", String.valueOf(finalstring));
        data.append(finalstring);

        Intent intent = new Intent(this, DisplayRecord.class);

        String message = data.toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void export(){
        StringBuilder data = new StringBuilder();
        data.append("Ime_Polja,Razlaga\n");
        //for(int i = 0; i<5; i++){
        //    data.append("\n"+String.valueOf(i)+","+String.valueOf(i*i));
        //}
        String line ="";
        StringBuilder finalstring = new StringBuilder();
        InputStream is = getResources().openRawResource(R.raw.a);
        BufferedReader br = new BufferedReader(new InputStreamReader((is)));

        try {
            while ((line = br.readLine()) != null){
                finalstring.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TAG", String.valueOf(finalstring));
        data.append(finalstring);
        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("kartoteka.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "kartoteka.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.emedic.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Moja kartoteka v formatu CSV:");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}

