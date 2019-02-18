package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databinding.FragmentNumpadBinding;


public class NumpadFragment extends Fragment implements View.OnClickListener {

    private NumpadFragment.Listener listener;
    private FragmentNumpadBinding binding;

    public void setListener(NumpadFragment.Listener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_numpad, container, false);

        binding.addition.setOnClickListener(this);
        binding.subtraction.setOnClickListener(this);
        binding.multiplication.setOnClickListener(this);
        binding.division.setOnClickListener(this);
        binding.digitOne.setOnClickListener(this);
        binding.digitTwo.setOnClickListener(this);
        binding.digitThree.setOnClickListener(this);
        binding.digitFour.setOnClickListener(this);
        binding.digitFive.setOnClickListener(this);
        binding.digitSix.setOnClickListener(this);
        binding.digitSeven.setOnClickListener(this);
        binding.digitEight.setOnClickListener(this);
        binding.digitNine.setOnClickListener(this);
        binding.digitZero.setOnClickListener(this);
        binding.dot.setOnClickListener(this);
        binding.equalto.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.digitOne) {
            listener.onNumpadButtonClick("1");
        } else if (v.getId() == R.id.digitTwo) {
            listener.onNumpadButtonClick("2");
        } else if (v.getId() == R.id.digitThree) {
            listener.onNumpadButtonClick("3");
        } else if (v.getId() == R.id.digitFour) {
            listener.onNumpadButtonClick("4");
        } else if (v.getId() == R.id.digitFive) {
            listener.onNumpadButtonClick("5");
        } else if (v.getId() == R.id.digitSix) {
            listener.onNumpadButtonClick("6");
        } else if (v.getId() == R.id.digitSeven) {
            listener.onNumpadButtonClick("7");
        } else if (v.getId() == R.id.digitEight) {
            listener.onNumpadButtonClick("8");
        } else if (v.getId() == R.id.digitNine) {
            listener.onNumpadButtonClick("9");
        } else if (v.getId() == R.id.digitZero) {
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
