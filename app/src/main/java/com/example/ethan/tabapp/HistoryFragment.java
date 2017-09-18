package com.example.ethan.tabapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ethan.tabapp.databinding.CalculatorFragmentBinding;
import com.example.ethan.tabapp.databinding.HistoryFragmentBinding;

/**
 * Created by Ethan on 9/17/2017.
 */

public class HistoryFragment extends Fragment {
    private HistoryFragmentBinding binding;
    private TabValue tabValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.history_fragment, container, false);
        View view = binding.getRoot();

        tabValue = ((MainActivity)getActivity()).GetTabValue();
        binding.setTabValue(tabValue);

        // set onClick methods for buttons
        binding.testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Button pressed", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}
