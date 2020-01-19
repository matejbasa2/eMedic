package com.example.emedic.Doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.emedic.R;

public class DoctorPanelSettings extends AppCompatActivity implements View.OnClickListener {
    private Button mMinus;
    private Button mPlus;
    private TextView mFactorText;
    private float offset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_panel_settings);

        Toolbar myToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME | androidx.appcompat.app.ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setLogo(R.mipmap.emedic_logo);

        mFactorText = (TextView) findViewById(R.id.factor_text);
        mMinus = (Button) findViewById(R.id.minus);
        mMinus.setOnClickListener(this);
        mPlus = (Button) findViewById(R.id.plus);
        mPlus.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            offset = extras.getFloat("mOffset");
            //The key argument here must match that used in the other activity
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.doctor_panel_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent previous = new Intent(DoctorPanelSettings.this, DoctorPanel.class);
                startActivity(previous);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
        Intent back = new Intent();
        back.putExtra("mOffset", offset);
        setResult(RESULT_OK, back);
        finish();
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.minus:
                offset = offset - 0.05f;
                mFactorText.setText(Float.toString(offset));
                break;

            case R.id.plus:
                offset = offset + 0.05f;
                mFactorText.setText(Float.toString(offset));
                break;

            default:
                break;
        }
        if (offset * 100 <= 10)
        {
            mMinus.setEnabled(false);
            mPlus.setEnabled(true);
        }
        else
        {
            mMinus.setEnabled(true);
            mPlus.setEnabled(true);
        }
    }
}
