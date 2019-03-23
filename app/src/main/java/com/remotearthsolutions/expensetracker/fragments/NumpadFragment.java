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
            listener.onNumpadButtonClick(getString(R.string.one));
        } else if (v.getId() == R.id.digitTwo) {
            listener.onNumpadButtonClick(getString(R.string.two));
        } else if (v.getId() == R.id.digitThree) {
            listener.onNumpadButtonClick(getString(R.string.three));
        } else if (v.getId() == R.id.digitFour) {
            listener.onNumpadButtonClick(getString(R.string.four));
        } else if (v.getId() == R.id.digitFive) {
            listener.onNumpadButtonClick(getString(R.string.five));
        } else if (v.getId() == R.id.digitSix) {
            listener.onNumpadButtonClick(getString(R.string.six));
        } else if (v.getId() == R.id.digitSeven) {
            listener.onNumpadButtonClick(getString(R.string.seven));
        } else if (v.getId() == R.id.digitEight) {
            listener.onNumpadButtonClick(getString(R.string.eight));
        } else if (v.getId() == R.id.digitNine) {
            listener.onNumpadButtonClick(getString(R.string.nine));
        } else if (v.getId() == R.id.digitZero) {
            listener.onNumpadButtonClick(getString(R.string.zero));
        } else if (v.getId() == R.id.dot) {
            listener.onNumpadButtonClick(getString(R.string.point));
        } else if (v.getId() == R.id.addition) {
            listener.onMathOperationButtonClick(getString(R.string.plus));
        } else if (v.getId() == R.id.subtraction) {
            listener.onMathOperationButtonClick(getString(R.string.subtraction));
        } else if (v.getId() == R.id.multiplication) {
            listener.onMathOperationButtonClick(getString(R.string.multiplication));
        } else if (v.getId() == R.id.division) {
            listener.onMathOperationButtonClick(getString(R.string.division));
        } else if (v.getId() == R.id.equalto) {
            listener.onMathOperationButtonClick(getString(R.string.equalto));
        }
    }

    public interface Listener {
        void onNumpadButtonClick(String value);

        void onMathOperationButtonClick(String operation);
    }
}
