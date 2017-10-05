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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Ethan on 9/16/2017.
 */

public class CalculatorFragment extends Fragment {
    private int pendingDirection; // 1 if A paid, -1 if B paid
    private int pendingFactor; // 2 if paying for both, 1 if paying for one
    private DecimalFormat decimalFormat;

    private Button buttonAPaid, buttonBPaid;
    private View calculateBackground;
    private TextView textViewCurrentTab, textViewAOwesMoney, textViewBOwesMoney;
    private RadioButton radioButtonForOne, radioButtonForBoth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator_fragment, container, false);
        pendingDirection = 1;
        pendingFactor = 2;

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        decimalFormat = new DecimalFormat("#.##");

        Button buttonEnter = (Button)(getActivity().findViewById(R.id.buttonEnter));
        Button buttonReset = (Button)(getActivity().findViewById(R.id.buttonReset));
        buttonAPaid = (Button)(getActivity().findViewById(R.id.buttonAPaid));
        buttonBPaid = (Button)(getActivity().findViewById(R.id.buttonBPaid));
        textViewCurrentTab = (TextView)(getActivity().findViewById(R.id.textView_currentTab));
        textViewAOwesMoney = (TextView)(getActivity().findViewById(R.id.textView_AOwesMoney));
        textViewBOwesMoney = (TextView)(getActivity().findViewById(R.id.textView_BOwesMoney));
        final EditText editTextPendingTab = (EditText)(getActivity().findViewById(R.id.editText_pendingTab));
        final EditText editTextComment = (EditText)(getActivity().findViewById(R.id.editText_comment));
        calculateBackground = getActivity().findViewById(R.id.calculator_background);
        radioButtonForOne = (RadioButton)(getActivity().findViewById(R.id.radioButton_forOne));
        radioButtonForBoth = (RadioButton)(getActivity().findViewById(R.id.radioButton_forBoth));

        updateCurrentTabTextView();

        // TODO: set up response to radio button, update enter button and database accordingly
        // TODO: make sure edittext for pending doesnt go above 2 decimal places


        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextPendingTab.getText().toString().trim().length() == 0){
                    Toast.makeText(getActivity(), "Error: amount cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editTextComment.getText().toString().trim().length() == 0){
                    Toast.makeText(getActivity(), "Error: comment cannot empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // adds the entry to the database
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a");
                String dateTimeString = dateTimeFormat.format(c.getTime());
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                double pendingAmount = Double.parseDouble(editTextPendingTab.getText().toString().trim());

                ((MainActivity) getActivity()).GetDBManager().insert(year,
                        month,
                        dateTimeString,
                        editTextComment.getText().toString().trim(),
                        pendingAmount,
                        pendingDirection,
                        pendingFactor);
                // does the math and saves the result to sharedPref
                ((MainActivity) getActivity()).ChangeTabValue(pendingAmount * pendingDirection / pendingFactor);
                // change display and color of the currentTab
                updateCurrentTabTextView();

                Toast.makeText(getActivity(), "Entry added.", Toast.LENGTH_SHORT).show();
                editTextPendingTab.setText("");
                editTextComment.setText("");
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPendingTab.setText("");
                radioButtonForBoth.setChecked(true);
                radioButtonForOne.setChecked(false);
                pendingFactor = 2;
                editTextComment.setText("");
            }
        });
        buttonAPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingDirection = 1;
                setABPaidButtonColor();
            }
        });
        buttonBPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingDirection = -1;
                setABPaidButtonColor();
            }
        });
        // hides the keyboard after user hits "enter" on the comment editText
        editTextPendingTab.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
                return true;
            }
        });
        editTextComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
                return true;
            }
        });
        radioButtonForOne.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pendingFactor = 1;
            }
        });
        radioButtonForBoth.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                pendingFactor = 2;
            }
        });
    }


        @Override
    public void onResume(){
        super.onResume();
        setABPaidButtonColor();
    }

    // helper function that sets the color of the currentTab based on who owes money
    private void updateCurrentTabTextView(){
        double tab = ((MainActivity)getActivity()).GetCurrentTab();
        textViewCurrentTab.setText("$" + valueToString(Math.abs(tab)));
            if (tab > 0.0) { // B owes money
            textViewCurrentTab.setTextColor(ContextCompat.getColor(
                    getActivity().getApplicationContext(), R.color.color_blue));
            textViewBOwesMoney.setVisibility(View.VISIBLE);
            textViewAOwesMoney.setVisibility(View.INVISIBLE);
        } else if (tab < 0.0) { // A owes money
            textViewCurrentTab.setTextColor(ContextCompat.getColor(
                    getActivity().getApplicationContext(), R.color.color_yellow));
            textViewAOwesMoney.setVisibility(View.VISIBLE);
            textViewBOwesMoney.setVisibility(View.INVISIBLE);
        }
        else{ // currentTab is 0
            textViewCurrentTab.setTextColor(ContextCompat.getColor(
                    getActivity().getApplicationContext(), R.color.color_grey));
            textViewBOwesMoney.setVisibility(View.INVISIBLE);
            textViewAOwesMoney.setVisibility(View.INVISIBLE);
        }
    }

    // helper function that sets the colors of the APaid and BPaid buttons accordingly to pendingDirection
    // TODO set the background color of the background box too
    private void setABPaidButtonColor(){
        if (pendingDirection == 1){ // A is paying
            buttonAPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_yellow));
            buttonBPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_grey));
            calculateBackground.setBackgroundColor(ContextCompat.getColor(
                    getActivity().getApplicationContext(), R.color.color_lightYellow));
            radioButtonForOne.setText("for Ethan");
        }
        else{ // B is paying
            buttonAPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_grey));
            buttonBPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_blue));
            calculateBackground.setBackgroundColor(ContextCompat.getColor(
                    getActivity().getApplicationContext(), R.color.color_lightBlue));
            radioButtonForOne.setText("for Grace");
        }
    }

    // helper method. takes in a double and converts it into appropirate string to display
    private String valueToString(double value) {
        // if the double value is an int, then display just the interger
        if ((value % 1) == 0) {
            return String.valueOf((int) value);
        }
        // otherwise display only the first 2 decimal places
        else{
            return String.valueOf(decimalFormat.format(value));
        }
    }
}
