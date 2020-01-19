package com.example.emedic;

import android.content.Context;
import android.content.Intent;

import com.example.emedic.base.activity.BaseActivity;

/**
 * samo koledar
 */

public class CalendarActivity extends BaseActivity {

    public static void show(Context context) {
        context.startActivity(new Intent(context, CalendarActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
