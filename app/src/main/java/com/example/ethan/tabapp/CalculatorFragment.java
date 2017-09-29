package com.example.ethan.tabapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.ethan.tabapp.databinding.CalculatorFragmentBinding;
import java.util.Calendar;

/**
 * Created by Ethan on 9/16/2017.
 */

public class CalculatorFragment extends Fragment {
    private CalculatorFragmentBinding binding;
    private TabValue tabValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.calculator_fragment, container, false);
        View view = binding.getRoot();

        tabValue = ((MainActivity)getActivity()).GetTabValue();
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
        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.onDeleteClick();
            }
        });
        binding.buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // adds the entry to the database
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM-dd h:mm a");
                String dateTimeString = dateTimeFormat.format(c.getTime());
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                Log.d("myTag","new entry added. year is " + String.valueOf(year) + ". month is " + String.valueOf(month));

                ((MainActivity)getActivity()).GetDBManager().insert(year,
                        month,
                        dateTimeString,
                        binding.calculatorComment.getText().toString(),
                        tabValue.pendingTab,
                        tabValue.pendingDirection.get());
                // does the math and saves the result to sharedPref
                tabValue.onEnterClick();
                ((MainActivity)getActivity()).SaveTabValue();
                // change color of the currentTab
                if (tabValue.AOwesMoney.get()){
                    binding.currentTab.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_blue));
                }
                else if (tabValue.BOwesMoney.get()){
                    binding.currentTab.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_yellow));
                }

                binding.calculatorComment.setText("");
            }
        });
        binding.buttonAPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.pendingDirection.set(1);
                setABPaidButtonColor();
            }
        });
        binding.buttonBPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.pendingDirection.set(-1);
                setABPaidButtonColor();
            }
        });
        binding.calculatorComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.calculatorComment.getWindowToken(), 0);
                return true;
            }
        });

        if (tabValue.AOwesMoney.get()){
            binding.currentTab.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_blue));
        }
        else if (tabValue.BOwesMoney.get()){
            binding.currentTab.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_yellow));
        }

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        setABPaidButtonColor();
    }

    // helper function that sets the colors of the APaid and BPaid buttons accordingly to pendingDirection
    private void setABPaidButtonColor(){
        if (tabValue.pendingDirection.get() == 1){
            binding.buttonAPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_yellow));
            binding.buttonBPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_grey));
        }
        else{
            binding.buttonAPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_grey));
            binding.buttonBPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_blue));
        }
    }
}
