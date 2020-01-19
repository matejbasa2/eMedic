package com.example.emedic.Doctor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.emedic.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfile extends AppCompatActivity {

    //widgets
    private CircleImageView mAvatarImage;

    //vars
    private static int RESULT_LOAD_IMAGE = 1;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

        inflater.inflate(R.menu.doctor_profile_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean saved = false;
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent previous = new Intent(DoctorProfile.this, DoctorPanel.class);
                startActivity(previous);
                return true;

            case R.id.edit:
                Drawable save = getResources().getDrawable(R.drawable.ic_done_black_24dp); // The ID of your drawable.
                return true;

            case R.id.clear:
                return true;

            case R.id.save:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        Toolbar myToolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.emedic_logo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
        }



        mAvatarImage = (CircleImageView) findViewById(R.id.doctor_profile_image);

         //Button chooseImage = (Button) findViewById(R.id.text_choose_avatar);
         //chooseImage.setOnClickListener(new View.OnClickListener() {
           //  @Override
            // public void onClick(View v) {

              //   Intent file = new Intent(Intent.ACTION_GET_CONTENT);
                // file.setType('*/*');
                 //startActivityForResult(file, 10);
             //}
         //});
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 10:
                if (resultCode == RESULT_OK) {
                    String path = Objects.requireNonNull(data.getData()).getPath();
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    mAvatarImage.setBackground(null);
                    mAvatarImage.setImageBitmap(bitmap);
                }
        }
    }

    private Bitmap getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        // cursor.close();
        // Convert file path into bitmap image using below line.
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return bitmap;
    }
}

