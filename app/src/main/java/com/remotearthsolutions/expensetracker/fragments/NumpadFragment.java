package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.R;


public class NumpadFragment extends Fragment implements View.OnClickListener {

    private Button addition, subtraction, multiplication, division, one, two, three, four, five, six, seven, eight, nine, zero, dot, equalto;
    private NumpadFragment.Listener listener;

    public void setListener(NumpadFragment.Listener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_numpad, container, false);

        addition = view.findViewById(R.id.addition);
        subtraction = view.findViewById(R.id.subtraction);
        multiplication = view.findViewById(R.id.multiplication);
        division = view.findViewById(R.id.division);
        one = view.findViewById(R.id.digit1);
        two = view.findViewById(R.id.digit2);
        three = view.findViewById(R.id.digit3);
        four = view.findViewById(R.id.digit4);
        five = view.findViewById(R.id.digit5);
        six = view.findViewById(R.id.digit6);
        seven = view.findViewById(R.id.digit7);
        eight = view.findViewById(R.id.digit8);
        nine = view.findViewById(R.id.digit9);
        zero = view.findViewById(R.id.digit0);
        dot = view.findViewById(R.id.dot);
        equalto = view.findViewById(R.id.equalto);

        addition.setOnClickListener(this);
        subtraction.setOnClickListener(this);
        multiplication.setOnClickListener(this);
        division.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        dot.setOnClickListener(this);
        equalto.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.digit1) {
            listener.onNumpadButtonClick("1");
        } else if (v.getId() == R.id.digit2) {
            listener.onNumpadButtonClick("2");
        } else if (v.getId() == R.id.digit3) {
            listener.onNumpadButtonClick("3");
        } else if (v.getId() == R.id.digit4) {
            listener.onNumpadButtonClick("4");
        } else if (v.getId() == R.id.digit5) {
            listener.onNumpadButtonClick("5");
        } else if (v.getId() == R.id.digit6) {
            listener.onNumpadButtonClick("6");
        } else if (v.getId() == R.id.digit7) {
            listener.onNumpadButtonClick("7");
        } else if (v.getId() == R.id.digit8) {
            listener.onNumpadButtonClick("8");
        } else if (v.getId() == R.id.digit9) {
            listener.onNumpadButtonClick("9");
        } else if (v.getId() == R.id.digit0) {
            listener.onNumpadButtonClick("0");
        } else if (v.getId() == R.id.dot) {
            listener.onNumpadButtonClick(".");
        } else if (v.getId() == R.id.addition) {
            listener.onMathOperationButtonClick("+");
        } else if (v.getId() == R.id.subtraction) {
            listener.onMathOperationButtonClick("-");
        } else if (v.getId() == R.id.multiplication) {
            listener.onMathOperationButtonClick("*");
        } else if (v.getId() == R.id.division) {
            listener.onMathOperationButtonClick("/");
        } else if (v.getId() == R.id.equalto) {
            listener.onMathOperationButtonClick("=");
        }
    }

    public interface Listener {
        void onNumpadButtonClick(String value);

        void onMathOperationButtonClick(String operation);
    }
}
