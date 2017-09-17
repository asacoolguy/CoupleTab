package com.example.ethan.tabapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ethan.tabapp.databinding.CalculatorFragmentBinding;


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
                    binding.currentTab.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_blue));
                }
                else if (tabValue.BOwesMoney.get()){
                    binding.currentTab.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_yellow));
                }
                // save values
                ((MainActivity)getActivity()).SaveTabValue();
            }
        });
        binding.buttonAPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.pendingDirection.set(1);
                binding.buttonAPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_yellow));
                binding.buttonBPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_grey));
            }
        });
        binding.buttonBPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabValue.pendingDirection.set(-1);
                binding.buttonAPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_grey));
                binding.buttonBPaid.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.color_blue));
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
}
