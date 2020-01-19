package com.example.emedic;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;








/* Potreben razred, ki se kiče v initChart() in pretvori (long) v (Date) vrednost po Y osi  */
class DateAxisValueFormatter extends ValueFormatter {
    private DateFormat mDataFormat;
    private Date mDate;
    public  DateAxisValueFormatter() {
        this.mDataFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        this.mDate = new Date();
    }
    @Override
    public String getFormattedValue(float value) {
        int index = (int)value;
        if(index < BloodSugarLevels.dataList.size()+1) {
            if(index > 0 )
                index--;
            Entry en = BloodSugarLevels.dataList.get(index);
            mDate.setTime((long)en.getData());
        } else {
            mDate.getTime();
        }
        return mDataFormat.format(mDate);
    }
}



public class BloodSugarLevels extends AppCompatActivity  {
    Dialog dialog;
    static ArrayList<Entry> dataList = new ArrayList<>();
    private LineChart mChart;
    private Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(this);
        setContentView(R.layout.activity_blood_sugar_levels);
        Toolbar toolbar = findViewById(R.id.toolbar);
        initChart();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addDataButton();
        searchSugarButton();
        deleteSugarButton();
        dangerButton();
        editBuddon();
    }


    /* Inicializira graf in določi način prikazovanja podatkov.
     * Dodamo tudi poslušalec na dotik, ki pridobi vrednosti točke, ki smo izbrali*/
    private void initChart(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new DateAxisValueFormatter());
        xAxis.setLabelRotationAngle((float) 25);

        LineDataSet set1 = new LineDataSet(dataList, "Količine sladkorja");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                date.setTime((long) dataList.get((int)h.getX()-1).getData());
                cal.setTime(date);
                int sec = cal.get(Calendar.SECOND);
                int min = cal.get(Calendar.MINUTE);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH)+1;
                int year = cal.get(Calendar.YEAR);
                final EditText datainput = findViewById(R.id.dataInput);
                String date_string = day + "/" + month + "/" + year + " " + hour + ":" + min + ":" + sec;
                datainput.setText(date_string);
            }
            @Override
            public void onNothingSelected() {

            }
        });
        mChart.getDescription().setText("");
        mChart.setData(data);
        if(dataList.size() > 10) {
            mChart.setVisibleXRangeMaximum(10f);
            mChart.moveViewToX(dataList.size());
        }
    }


    /* Po dodajanju ali brisanju vrednosti naredimo ponovni izris z to metodo */
    private void refreshChartData(){
        LineDataSet set1 = new LineDataSet(dataList, "Nivoji sladkorja");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.moveViewToX(dataList.size());
        if(dataList.size() > 10) {
            mChart.setVisibleXRangeMaximum(10f);
        }
    }


    private void addDataButton(){
        Button adddatabutton = findViewById(R.id.addDataButton);
        final EditText datainput = findViewById(R.id.dataInput);
        adddatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    float data = Float.valueOf(datainput.getText().toString());
                    date = new Date();
                    Entry ent = new Entry(dataList.size()+1,data,date.getTime());
                    dataList.add(ent);
                    refreshChartData();
                }catch (Exception e){
                    Toast.makeText(BloodSugarLevels.this, "Prosim vnesite naravno ali realno število",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void searchSugarButton(){
        Button adddatabutton = findViewById(R.id.searchSugarLevel);
        final EditText datainput = findViewById(R.id.dataInput);
        adddatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = datainput.getText().toString();
                if (!data.matches("\\d{1,2}[/]\\d{1,2}[/]\\d{4}")) {
                    Toast.makeText(BloodSugarLevels.this, "Prosim vnesite pravilni format: 21/10/2019",
                            Toast.LENGTH_LONG).show();
                } else {
                    String[] array = data.split("/");
                    Date date = new Date();
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
                    boolean found = false;
                    for (int i = 0; i < dataList.size(); i++) {
                        date.setTime((long) dataList.get(i).getData());
                        cal.setTime(date);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        if (day == Integer.parseInt(array[0]) && month == Integer.parseInt(array[1])-1 && year == Integer.parseInt(array[2])) {
                            mChart.moveViewToX(i);
                            found = true;
                            Toast.makeText(BloodSugarLevels.this, "Našel!",
                                    Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                    if (!found) {
                        Toast.makeText(BloodSugarLevels.this, "Datum ne obstaja",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }




    private void deleteSugarButton() {
        Button adddatabutton = findViewById(R.id.deleteSugarButton);
        final EditText datainput = findViewById(R.id.dataInput);
        adddatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = datainput.getText().toString();
                if (!data.matches("\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}")) {
                    Toast.makeText(BloodSugarLevels.this, "Prosim vnesite pravilni format: 21/10/2019 13:43:30",
                            Toast.LENGTH_LONG).show();
                } else {
                    String[] date_array = data.split("/");
                    String[] time_array = date_array[2].split(" ");
                    date_array[2] = time_array[0];
                    time_array = time_array[1].split(":");
                    System.out.println("ARRAY: " + time_array.length);
                    Date date = new Date();
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
                    boolean found = false;
                    for (int i = 0; i < dataList.size(); i++) {
                        date.setTime((long) dataList.get(i).getData());
                        cal.setTime(date);
                        int sec = cal.get(Calendar.SECOND);
                        int min = cal.get(Calendar.MINUTE);
                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        if (sec == Integer.parseInt(time_array[2]) && min == Integer.parseInt(time_array[1]) && hour == Integer.parseInt(time_array[0]) && day == Integer.parseInt(date_array[0]) && month == Integer.parseInt(date_array[1]) - 1 && year == Integer.parseInt(date_array[2]) ) {
                            dataList.remove(i);
                            for (int j = i; j < dataList.size(); j++) {
                                float current_x = dataList.get(j).getX();
                                dataList.get(j).setX(current_x-1);
                            }

                            refreshChartData();
                            found = true;
                            Toast.makeText(BloodSugarLevels.this, "Izbrisan!",Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    if (!found) {
                        Toast.makeText(BloodSugarLevels.this, "Čas ali datum ne obstaja",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void dangerButton() {
        Button adddatabutton = findViewById(R.id.dangerButton);
        final EditText datainput = findViewById(R.id.dataInput);
        adddatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = datainput.getText().toString();
                if (!data.matches("\\d+|(\\d+.\\d+)")) {
                    Toast.makeText(BloodSugarLevels.this, "Prosim vnesite celo ali decimalno število: 12.3 ali 12",
                            Toast.LENGTH_LONG).show();
                }else{
                    LimitLine danger_line = new LimitLine(Float.parseFloat(data),"Danger zone");
                    YAxis leftAxis = mChart.getAxisLeft();
                    leftAxis.removeAllLimitLines();
                    leftAxis.addLimitLine(danger_line);
                    mChart.notifyDataSetChanged();
                    mChart.invalidate();
                }
            }
        });
    }


    private void editBuddon() {
        Button editbutton = findViewById(R.id.editButton);
        final int[] index = {0};
        final EditText datainput = findViewById(R.id.dataInput);
        dialog.setContentView(R.layout.edit_data_popup);
        EditText dataedit = (EditText) dialog.findViewById(R.id.dataEditInput);
        editbutton.setOnClickListener(view -> {
            TextView x = dialog.findViewById(R.id.xclose);
            x.setOnClickListener(v -> {
                dialog.dismiss();
            });
            Button preklici = dialog.findViewById(R.id.cancelEdit);
            preklici.setOnClickListener(v -> {
                dialog.dismiss();
            });
            Button potrdi = dialog.findViewById(R.id.confirmEdit);


            String data = datainput.getText().toString();
            if (!data.matches("\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}")) {
                Toast.makeText(BloodSugarLevels.this, "Prosim vnesite pravilni format: 21/10/2019 13:43:30",
                        Toast.LENGTH_LONG).show();
            } else {
                String[] date_array = data.split("/");
                String[] time_array = date_array[2].split(" ");
                date_array[2] = time_array[0];
                time_array = time_array[1].split(":");
                System.out.println("ARRAY: " + time_array.length);
                Date date = new Date();
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
                boolean found = false;
                for (int i = 0; i < dataList.size(); i++) {
                    date.setTime((long) dataList.get(i).getData());
                    cal.setTime(date);
                    int sec = cal.get(Calendar.SECOND);
                    int min = cal.get(Calendar.MINUTE);
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int month = cal.get(Calendar.MONTH);
                    int year = cal.get(Calendar.YEAR);
                    if (sec == Integer.parseInt(time_array[2]) && min == Integer.parseInt(time_array[1]) && hour == Integer.parseInt(time_array[0]) && day == Integer.parseInt(date_array[0]) && month == Integer.parseInt(date_array[1]) - 1 && year == Integer.parseInt(date_array[2]) ) {
                        found = true;
                        index[0] = i;
                        potrdi.setOnClickListener(v -> {
                            Entry e = dataList.get(index[0]);
                            String edit = dataedit.getText().toString();
                            if (edit.matches("\\d+|(\\d+.\\d+)")) {
                                e.setY(Float.valueOf(edit));
                                dataList.set(index[0],e);
                                refreshChartData();
                                Toast.makeText(BloodSugarLevels.this, "Posodobljeno!",
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }else {
                                Toast.makeText(BloodSugarLevels.this, "Prosim vnesite celo ali decimalno število: 12.3 ali 12",
                                        Toast.LENGTH_LONG).show();
                            }

                        });
                        dialog.show();
                        break;
                    }
                }
                if (!found) {
                    Toast.makeText(BloodSugarLevels.this, "Čas ali datum ne obstaja",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}