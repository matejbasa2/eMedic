package com.example.emedic.Pacient;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.emedic.BloodSugarLevels;
import com.example.emedic.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class PacientPanel extends AppCompatActivity implements View.OnTouchListener,
        View.OnDragListener{
    boolean pressed = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient_panel);

        FloatingActionButton fab = findViewById(R.id.xclosednd);

        LinearLayout all_button_container = findViewById(R.id.allButtonContainer);
        editButtons(all_button_container);



        CardView kartoteka = findViewById(R.id.kartoteka);
        kartoteka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kartoteka = new Intent(PacientPanel.this, PacientKartoteka.class);
                startActivity(kartoteka);
            }
        });

        CardView blood = findViewById(R.id.blood);
        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blood = new Intent(PacientPanel.this, BloodSugarLevels.class);
                startActivity(blood);
            }
        });
        CardView pritisk = findViewById(R.id.pressure);
        pritisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blood = new Intent(PacientPanel.this, BloodSugarLevels.class);
                startActivity(blood);
            }
        });
        CardView puls = findViewById(R.id.puls);
        puls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blood = new Intent(PacientPanel.this, BloodSugarLevels.class);
                startActivity(blood);
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



    @SuppressLint("RestrictedApi")
    private void fade(LinearLayout all_button_container) {
        for (int i = 0; i < all_button_container.getChildCount(); i++) {
            final LinearLayout ll = (LinearLayout)all_button_container.getChildAt(i);
            for(int j = 0; j < ll.getChildCount(); j++) {
                CardView cv = (CardView) ll.getChildAt(j);
                cv.setAlpha((float) 0.5);
                cv.setOnTouchListener(this);
                cv.setOnDragListener(this);
                FloatingActionButton fab = findViewById(R.id.xclosednd);
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
                FloatingActionButton fab = findViewById(R.id.xclosednd);
                fab.setVisibility(View.INVISIBLE);
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

}

