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
    private ActivityMainBinding binding;
    private TabValue tabValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // initialize process to save value
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        String currentTabValue_default = getResources().getString(R.string.currentTabValue_default);

        // load saved values
        tabValue = new TabValue();
        String oldCurrentTabValue = sharedPref.getString(getString(R.string.currentTabValue),
                currentTabValue_default);
        tabValue.loadCurrentTabValue(oldCurrentTabValue);
        if (tabValue.AOwesMoney.get()){
            binding.currentTab.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_blue));
        }
        else if (tabValue.BOwesMoney.get()){
            binding.currentTab.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_yellow));
        }

        binding.setTabValue(tabValue);

        // set onClick methods for buttons
        binding.buttonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onNumberClick(0);
            }
        });
        binding.buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onNumberClick(1);
            }
        });
        binding.buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onNumberClick(2);
            }
        });
        binding.buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onNumberClick(3);
            }
        });
        binding.buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onNumberClick(4);
            }
        });
        binding.buttonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onNumberClick(5);
            }
        });
        binding.buttonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onNumberClick(6);
            }
        });
        binding.buttonSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onNumberClick(7);
            }
        });
        binding.buttonEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onNumberClick(8);
            }
        });
        binding.buttonNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onNumberClick(9);
            }
        });
        binding.buttonDecimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onDecimalClick();
            }
        });
        binding.buttonBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onDeleteClick();
            }
        });
        binding.buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onEnterClick();
                // change color of the currentTab
                if (tabValue.AOwesMoney.get()){
                    binding.currentTab.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_blue));
                }
                else if (tabValue.BOwesMoney.get()){
                    binding.currentTab.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_yellow));
                }
                // save values
                editor.putString(getString(R.string.currentTabValue), tabValue.currentTabString.get());
                editor.apply();
            }
        });
        binding.buttonAPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.pendingDirection.set(1);
                binding.buttonAPaid.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_yellow));
                binding.buttonBPaid.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_grey));
        }
        });
        binding.buttonBPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.pendingDirection.set(-1);
                binding.buttonAPaid.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_grey));
                binding.buttonBPaid.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_blue));
            }
        });
    }


}

