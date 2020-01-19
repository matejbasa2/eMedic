package com.example.emedic;

import android.annotation.SuppressLint;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emedic.base.fragment.FragmentAdapter;
import com.example.emedic.meizu.MeizuWeekView;
import com.example.emedic.simple.SimpleMonthView;
import com.example.emedic.simple.SimpleWeekView;
import com.google.android.material.tabs.TabLayout;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.TrunkBranchAnnals;
import com.example.emedic.base.activity.BaseActivity;
import com.example.emedic.meizu.MeiZuMonthView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomMonthActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnCalendarLongClickListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnWeekChangeListener,
        CalendarView.OnViewChangeListener,
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnYearViewChangeListener,
        DialogInterface.OnClickListener,
        View.OnClickListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    LinearLayout mPacientControl1;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;

    private AlertDialog mMoreDialog;

    private AlertDialog mFuncDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_month;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);

        mPacientControl1 = findViewById(R.id.pacient_control1);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        //mCalendarView.setRange(2018, 7, 1, 2019, 4, 28);
        mTextCurrentDay = findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMoreDialog == null) {
                    mMoreDialog = new AlertDialog.Builder(CustomMonthActivity.this)
                            .setTitle(R.string.list_dialog_title)
                            .setItems(R.array.list_dialog_items, CustomMonthActivity.this)
                            .create();
                }
                mMoreDialog.show();
            }
        });

        final boolean[] isFocused = {false};
        findViewById(R.id.p1).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(isFocused[0]){
                    mPacientControl1.setVisibility(View.GONE);
                    isFocused[0] = false;
                }
                else if(!isFocused[0]){
                    mPacientControl1.setVisibility(View.VISIBLE);
                    isFocused[0] = true;
                }
                return true;
            }
        });

        final DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mCalendarLayout.expand();
                                break;
                            case 1:
                                boolean result = mCalendarLayout.shrink();
                                Log.e("shrink", " --  " + result);
                                break;
                            case 2:
                                mCalendarView.scrollToPre(false);
                                break;
                            case 3:
                                mCalendarView.scrollToNext(false);
                                break;
                            case 4:
                                //mCalendarView.scrollToCurrent(true);
                                mCalendarView.scrollToCalendar(2018, 12, 30);
                                break;
                            case 5:
                                mCalendarView.setRange(2018, 7, 1, 2019, 4, 28);
//                                mCalendarView.setRange(mCalendarView.getCurYear(), mCalendarView.getCurMonth(), 6,
//                                        mCalendarView.getCurYear(), mCalendarView.getCurMonth(), 23);
                                break;
                            case 6:
                                Log.e("scheme", "  " + mCalendarView.getSelectedCalendar().getScheme() + "  --  "
                                        + mCalendarView.getSelectedCalendar().isCurrentDay());
                                List<Calendar> weekCalendars = mCalendarView.getCurrentWeekCalendars();
                                for (Calendar calendar : weekCalendars) {
                                    Log.e("onWeekChange", calendar.toString() + "  --  " + calendar.getScheme());
                                }
                                new AlertDialog.Builder(CustomMonthActivity.this)
                                        .setMessage(String.format("Calendar Range: \n%s —— %s",
                                                mCalendarView.getMinRangeCalendar(),
                                                mCalendarView.getMaxRangeCalendar()))
                                        .show();
                                break;
                        }
                    }
                };

        findViewById(R.id.iv_func).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFuncDialog == null) {
                    mFuncDialog = new AlertDialog.Builder(CustomMonthActivity.this)
                            .setTitle(R.string.func_dialog_title)
                            .setItems(R.array.func_dialog_items, listener)
                            .create();
                }
                mFuncDialog.show();
            }
        });

        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnCalendarLongClickListener(this, true);
        mCalendarView.setOnWeekChangeListener(this);
        mCalendarView.setOnYearViewChangeListener(this);

        mCalendarView.setOnCalendarInterceptListener(this);

        mCalendarView.setOnViewChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "M" + mCalendarView.getCurDay() + "D");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));


    }

    @SuppressWarnings("unused")
    @Override
    protected void initData() {

        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
        for (int y = 1997; y < 2082; y++) {
            for (int m = 1; m <= 12; m++) {
                map.put(getSchemeCalendar(year, month, 2, 0xFFdf1356, "22").toString(),
                        getSchemeCalendar(year, month, 2, 0xFFdf1356, "22"));
                map.put(getSchemeCalendar(year, month, 3, 0xFFe69138, "11").toString(),
                        getSchemeCalendar(year, month, 3, 0xFFe69138, "11"));
                map.put(getSchemeCalendar(year, month, 4, 0xFFedc56d, "7").toString(),
                        getSchemeCalendar(year, month, 4, 0xFFedc56d, "7"));
                map.put(getSchemeCalendar(year, month, 5, 0xFFe69138, "9").toString(),
                        getSchemeCalendar(year, month, 5, 0xFFe69138, "9"));
                map.put(getSchemeCalendar(year, month, 6, 0xFF40db25, "3").toString(),
                        getSchemeCalendar(year, month, 6, 0xFF40db25, "3"));
                map.put(getSchemeCalendar(year, month, 7, 0xFF13acf0, "0").toString(),
                        getSchemeCalendar(year, month, 7, 0xFF13acf0, "0"));
                map.put(getSchemeCalendar(year, month, 8, 0xFF13acf0, "0").toString(),
                        getSchemeCalendar(year, month, 8, 0xFF13acf0, "0"));
                map.put(getSchemeCalendar(year, month, 16, 0xFFedc56d, "7").toString(),
                        getSchemeCalendar(year, month, 16, 0xFFedc56d, "7"));
                map.put(getSchemeCalendar(year, month, 17, 0xFFdf1356, "22").toString(),
                        getSchemeCalendar(year, month, 17, 0xFFdf1356, "22"));
                map.put(getSchemeCalendar(year, month, 18, 0xFFe69138, "11").toString(),
                        getSchemeCalendar(year, month, 18, 0xFFe69138, "11"));
                map.put(getSchemeCalendar(year, month, 19, 0xFF40db25, "3").toString(),
                        getSchemeCalendar(year, month, 19, 0xFF40db25, "3"));
                map.put(getSchemeCalendar(year, month, 20, 0xFFe69138, "9").toString(),
                        getSchemeCalendar(year, month, 20, 0xFFe69138, "9"));
                map.put(getSchemeCalendar(year, month, 21, 0xFF13acf0, "0").toString(),
                        getSchemeCalendar(year, month, 21, 0xFF13acf0, "0"));
                map.put(getSchemeCalendar(year, month, 22, 0xFF13acf0, "0").toString(),
                        getSchemeCalendar(year, month, 22, 0xFF13acf0, "0"));
            }
        }

        //28560
        mCalendarView.setSchemeDate(map);

        //
        //mCalendarView.setSchemeDate(schemes);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                mCalendarView.setWeekStarWithSun();
                break;
            case 1:
                mCalendarView.setWeekStarWithMon();
                break;
            case 2:
                mCalendarView.setWeekStarWithSat();
                break;
            case 3:
                if (mCalendarView.isSingleSelectMode()) {
                    mCalendarView.setSelectDefaultMode();
                } else {
                    mCalendarView.setSelectSingleMode();
                }
                break;
            case 4:
                mCalendarView.setWeekView(SimpleWeekView.class);
                mCalendarView.setMonthView(SimpleMonthView.class);
                mCalendarView.setWeekBar(EnglishWeekBar.class);
                break;
            case 5:
                mCalendarView.setAllMode();
                break;
            case 6:
                mCalendarView.setOnlyCurrentMode();
                break;
            case 7:
                mCalendarView.setFixMode();
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);
        calendar.setScheme(text);
        mCalendarView.setFixMode();
        mCalendarView.setWeekView(MeizuWeekView.class);
        mCalendarView.setMonthView(MeiZuMonthView.class);
        mCalendarView.setWeekBar(EnglishWeekBar.class);
        mCalendarView.setWeekStarWithMon();
        return calendar;
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
        Toast.makeText(this, String.format("%s : OutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        Log.e("onDateSelected", "  -- " + calendar.getYear() + "  --  " + calendar.getMonth() + "  -- " + calendar.getDay());
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "M" + calendar.getDay() + "D");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mYear = calendar.getYear();
        if (isClick) {
            Toast.makeText(this, getCalendarText(calendar), Toast.LENGTH_SHORT).show();
        }
//        Log.e("lunar "," --  " + calendar.getLunarCalendar().toString() + "\n" +
//        "  --  " + calendar.getLunarCalendar().getYear());
        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
        Log.e("onDateSelected", "  " + mCalendarView.getSelectedCalendar().getScheme() +
                "  --  " + mCalendarView.getSelectedCalendar().isCurrentDay());
        Log.e("leto suhe veje ： ", " -- " + TrunkBranchAnnals.getTrunkBranchYear(calendar.getLunarCalendar().getYear()));
    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {
        Toast.makeText(this, String.format("%s : LongClickOutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalendarLongClick(Calendar calendar) {
        Toast.makeText(this, "Long click" + getCalendarText(calendar), Toast.LENGTH_SHORT).show();
    }

    private static String getCalendarText(Calendar calendar) {
        return String.format("Datum: %s \n Praznik：%s \n Letni čas：%s \n Prestopno leto：%s",
                calendar.getMonth() + ". mesec " + calendar.getDay() + ". dan",
                TextUtils.isEmpty(calendar.getTraditionFestival()) ? "Ni praznika" : calendar.getTraditionFestival(),
                TextUtils.isEmpty(calendar.getSolarTerm()) ? "Ni specificirano" : calendar.getSolarTerm(),
                calendar.getLeapMonth() == 0 ? "Ni prestopno" : String.format("Prestopni mesec - %s", calendar.getLeapMonth()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", "  -- " + year + "  --  " + month);
        Calendar calendar = mCalendarView.getSelectedCalendar();
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "M" + calendar.getDay() + "D");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mYear = calendar.getYear();
    }

    @Override
    public void onViewChange(boolean isMonthView) {
        Log.e("onViewChange", "  ---  " + (isMonthView ? "mesečni pogled" : "tedenski pogled"));
        mCalendarView.setWeekView(MeizuWeekView.class);
        mCalendarView.setMonthView(MeiZuMonthView.class);
        mCalendarView.setWeekBar(EnglishWeekBar.class);
    }


    @Override
    public void onWeekChange(List<Calendar> weekCalendars) {
        for (Calendar calendar : weekCalendars) {
            Log.e("onWeekChange", calendar.toString());
        }
    }

    @Override
    public void onYearViewChange(boolean isClose) {
        Log.e("onYearViewChange", "letni pogled -- " + (isClose ? "zapri" : "odpri"));
    }

    /**
     * @param calendar calendar
     * @return MonthViewWeekView
     * **/
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        Log.e("onCalendarIntercept", calendar.toString());
        int day = calendar.getDay();
        return day == 1 || day == 3 || day == 6 || day == 11 || day == 12 || day == 15 || day == 20 || day == 26;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this, calendar.toString() + "Blokirano, onemogočeno", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
        Log.e("onYearChange", " sprememba leta " + year);
    }

}