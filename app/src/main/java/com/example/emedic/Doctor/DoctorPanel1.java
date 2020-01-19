package com.example.emedic.Doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.emedic.CustomMonthActivity;
import com.example.emedic.R;


public class DoctorPanel1 extends AppCompatActivity {
    boolean pressed = false;
    public ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_panel);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.doctor_panel_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.emedic_logo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);

        CardView profil = findViewById(R.id.doctor_profile);
        CardView scheduler = findViewById(R.id.doctor_scheduler);
        CardView vsi_pacienti = findViewById(R.id.doctor_patient_groups);

        LinearLayout all_button_container = findViewById(R.id.allButtonContainer);
        editButtons(all_button_container);

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(DoctorPanel1.this, DoctorProfile.class);
                startActivity(profile);
            }
        });

        vsi_pacienti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pacienti = new Intent(DoctorPanel1.this, VsiPacienti.class);
                startActivity(pacienti);
            }
        });

        scheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scheduler = new Intent(DoctorPanel1.this, CustomMonthActivity.class);
                startActivity(scheduler);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.doctor_panel_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_diagnose:
                // DIAGNOSE
                return true;

            case R.id.action_settings:
                Intent settings = new Intent(DoctorPanel1.this, DoctorPanelSettings.class);
                //float offset = mScrollView.getParallaxOffset();
                //settings.putExtra("mOffset", offset);
                startActivityForResult(settings, 1);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle extras = getIntent().getExtras();
                //if (extras != null) {
                    //float offset = extras.getFloat("mOffset");
                    //mScrollView.setParallaxOffset(offset);
                    //offset = mScrollView.getParallaxOffset();
                //}
            }
        }
    }

    private void fade(LinearLayout all_button_container){
        for (int i = 0; i < all_button_container.getChildCount(); i++) {
            final LinearLayout ll = (LinearLayout)all_button_container.getChildAt(i);
            for(int j = 0; j < ll.getChildCount(); j++) {
                CardView cv = (CardView) ll.getChildAt(j);
                cv.setAlpha((float) 0.5);
            }
        }
    }
    private void unfade(LinearLayout all_button_container){
        for (int i = 0; i < all_button_container.getChildCount(); i++) {
            final LinearLayout ll = (LinearLayout)all_button_container.getChildAt(i);
            for(int j = 0; j < ll.getChildCount(); j++) {
                CardView cv = (CardView) ll.getChildAt(j);
                cv.setAlpha((float) 1);
            }
        }
    }


    private void editButtons(final LinearLayout all_button_container){
        for (int i = 0; i < all_button_container.getChildCount(); i++) {
            final LinearLayout ll = (LinearLayout)all_button_container.getChildAt(i);
            for(int j = 0; j < ll.getChildCount(); j++) {
                CardView cv = (CardView) ll.getChildAt(j);
                cv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(pressed==false){
                            pressed=true;
                            fade(all_button_container);
                        } else{
                            pressed=false;
                            unfade(all_button_container);
                        }
                        return true;
                    }
                });

            }
        }
    }





/*


    private void setSingleEvent(final GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            Button but = (Button) mainGrid.getChildAt(i);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button myButton = new Button(PacientPanel.this);
                    myButton.setText("name");
                    GridLayout.LayoutParams param =new GridLayout.LayoutParams();
                    param.height= 400;
                    param.width = 200;
                    param.rightMargin = 5;
                    param.topMargin = 5;
                    int row = mainGrid.getChildCount()/2;
                    int column = mainGrid.getChildCount()%2;
                    System.out.println(mainGrid.getChildCount());
                    param.columnSpec = GridLayout.spec(column,(float)1);
                    param.rowSpec = GridLayout.spec(row,(float)1);
                    myButton.setLayoutParams (param);
                    row++;
                    myButton.setOnClickListener(mThisButtonListener);
                    mainGrid.addView(myButton);
/*
                    Intent intent = new Intent(PacientPanel.this, ActivityOne.class);
                    intent.putExtra("info","Created new  button ");

                    startActivity(intent);


                }


            });



        }



    }


*/
}

