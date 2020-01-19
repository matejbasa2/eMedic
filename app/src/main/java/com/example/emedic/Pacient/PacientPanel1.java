package com.example.emedic.Pacient;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.emedic.BloodSugarLevels;
import com.example.emedic.R;


public class PacientPanel1 extends AppCompatActivity {
    boolean pressed = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient_panel);


        LinearLayout all_button_container = findViewById(R.id.allButtonContainer);
        editButtons(all_button_container);

        CardView kartoteka = findViewById(R.id.kartoteka);
        kartoteka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kartoteka = new Intent(PacientPanel1.this, PacientKartoteka.class);
                startActivity(kartoteka);
            }
        });

        CardView blood = findViewById(R.id.blood);
        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blood = new Intent(PacientPanel1.this, BloodSugarLevels.class);
                startActivity(blood);
            }
        });

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

    private View.OnClickListener mThisButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(PacientPanel.this, "Hello !",
                    Toast.LENGTH_LONG).show();


                        setContentView(R.layout.activity_main2);
                        backButton();





        }
    };
    private void backButton(){
        Button b = findViewById(R.id.button5);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_pacient_panel);
            }
        });}


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

