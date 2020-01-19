package com.example.emedic.Doctor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.emedic.CustomMonthActivity;
import com.example.emedic.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DoctorPanel extends AppCompatActivity implements View.OnTouchListener,
        View.OnDragListener {
    boolean pressed = false;
    public ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_panel);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);

        FloatingActionButton fab = findViewById(R.id.xclosednd2);

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
                Intent profile = new Intent(DoctorPanel.this, DoctorProfile.class);
                startActivity(profile);
            }
        });

        vsi_pacienti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pacienti = new Intent(DoctorPanel.this, VsiPacienti.class);
                startActivity(pacienti);
            }
        });

        scheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scheduler = new Intent(DoctorPanel.this, CustomMonthActivity.class);
                startActivity(scheduler);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressed=false;
                unfade(all_button_container);
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
                Intent settings = new Intent(DoctorPanel.this, DoctorPanelSettings.class);
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

    @SuppressLint("RestrictedApi")
    private void fade(LinearLayout all_button_container){
        for (int i = 0; i < all_button_container.getChildCount(); i++) {
            final LinearLayout ll = (LinearLayout)all_button_container.getChildAt(i);
            for(int j = 0; j < ll.getChildCount(); j++) {
                CardView cv = (CardView) ll.getChildAt(j);
                cv.setAlpha((float) 0.5);
                cv.setOnTouchListener(this);
                cv.setOnDragListener(this);
                FloatingActionButton fab = findViewById(R.id.xclosednd2);
                fab.setVisibility(View.VISIBLE);
            }
        }
    }
    @SuppressLint("RestrictedApi")
    private void unfade(LinearLayout all_button_container){
        for (int i = 0; i < all_button_container.getChildCount(); i++) {
            final LinearLayout ll = (LinearLayout)all_button_container.getChildAt(i);
            for(int j = 0; j < ll.getChildCount(); j++) {
                CardView cv = (CardView) ll.getChildAt(j);
                cv.setAlpha((float) 1);
                cv.setOnTouchListener(null);
                cv.setOnDragListener(null);
                FloatingActionButton fab = findViewById(R.id.xclosednd2);
                fab.setVisibility(View.INVISIBLE);
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
                        }
                        return true;
                    }
                });

            }
        }
    }



    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            v.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent e) {
        View view = (View) e.getLocalState();
        if (e.getAction() == DragEvent.ACTION_DROP && v instanceof CardView) {
            ViewGroup from = (ViewGroup) view.getParent();
            CardView to = (CardView) v;
            ViewGroup toparent = (ViewGroup) to.getParent();
            if(to.getId()!= view.getId()) {
                int ccount = toparent.getChildCount();
                int indexOfMyView = toparent.indexOfChild(to);
                int indexOffrom = from.indexOfChild(view);
                toparent.removeView(to);
                from.removeView(view);
                if(ccount == 1){
                    System.out.println("test");
                    LinearLayout.LayoutParams fparam =  (LinearLayout.LayoutParams)from.getLayoutParams();
                    LinearLayout.LayoutParams tparam =  (LinearLayout.LayoutParams)to.getLayoutParams();
                    to.measure(0, 0);
                    from.measure(0, 0);
                    int t = to.getMeasuredWidth();
                    int f = from.getMeasuredWidth();
                    tparam.width = f;
                    fparam.width = t;
                    to.setLayoutParams(tparam);
                    from.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));;
                }
                if(indexOffrom == 0){
                    from.addView(to,(indexOffrom));
                    toparent.addView(view, (indexOfMyView));
                } else{
                    toparent.addView(view, (indexOfMyView));
                    from.addView(to,(indexOffrom));

                }
                view.setVisibility(View.VISIBLE);
            }
        }
        view.setVisibility(View.VISIBLE);
        return true;
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

