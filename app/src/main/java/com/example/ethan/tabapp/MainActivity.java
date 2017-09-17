package com.example.ethan.tabapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.ethan.tabapp.databinding.ActivityMainBinding;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    //private ActivityMainBinding binding;
    private TabValue tabValue;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize process to save value
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String currentTabValue_default = getResources().getString(R.string.currentTabValue_default);
        // load saved values
        tabValue = new TabValue();
        String oldCurrentTabValue = sharedPref.getString(getString(R.string.currentTabValue),
                currentTabValue_default);
        tabValue.loadCurrentTabValue(oldCurrentTabValue);

        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            CalculatorFragment firstFragment = new CalculatorFragment();
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public TabValue GetTabValue(){
        return tabValue;
    }

    public void SaveTabValue(){
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.currentTabValue), tabValue.currentTabString.get());
        editor.apply();
    }


}

