package com.example.ethan.tabapp;

import android.content.Intent;
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

        // for now just set currentTab as 0. this should be read in later.
        tabValue = new TabValue();

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
                if (tabValue.AOwesMoney.get()){
                    binding.currentTab.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_blue));
                }
                else if (tabValue.BOwesMoney.get()){
                    binding.currentTab.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_yellow));
                }
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

