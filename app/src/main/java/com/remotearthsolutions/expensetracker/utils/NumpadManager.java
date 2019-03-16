package com.remotearthsolutions.expensetracker.utils;

import android.view.View;
import android.widget.EditText;
import com.remotearthsolutions.expensetracker.fragments.NumpadFragment;

public class NumpadManager implements NumpadFragment.Listener {

    private EditText displayEdtxt;
    private boolean isOperationDone;
    private double result = 0;
    private String lastOperation, lastValue = "0";

    public void attachDisplay(EditText inputDigitEdtxt) {
        this.displayEdtxt = inputDigitEdtxt;
    }

    public void attachDeleteButton(View deleteButton) {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = displayEdtxt.getText().toString().length();
                if (len > 0 && !isOperationDone) {
                    displayEdtxt.setText(displayEdtxt.getText().toString().substring(0, len - 1));
                }
            }
        });
    }

    @Override
    public void onNumpadButtonClick(String value) {
        if (isOperationDone) {
            displayEdtxt.setText("");
            isOperationDone = false;
        }

        String str = displayEdtxt.getText().toString();
        if (value.equals(".") && str.contains(".")) {
            displayEdtxt.setText(str);
        } else {
            str += value;
            displayEdtxt.setText(str);
        }
    }

    @Override
    public void onMathOperationButtonClick(String operation) {

        if (isOperationDone && lastOperation != null) {
            lastOperation = operation;
            return;
        }

        String val = displayEdtxt.getText().toString();
        if (val.length() == 0 || val.equals(".")) {
            return;
        }

        double currentVal = Double.parseDouble(val);
        double lastVal = Double.parseDouble(lastValue);

        if (lastOperation == null) {
            result = currentVal;
        } else {
            if (lastOperation.equals("+")) {
                result = lastVal + currentVal;
            } else if (lastOperation.equals("-")) {
                result = lastVal - currentVal;
            } else if (lastOperation.equals("*")) {
                result = lastVal * currentVal;
            } else if (lastOperation.equals("/")) {
                result = lastVal / currentVal;
            }
        }

        if (operation.equals("=")) {
            lastOperation = null;
            lastValue = "0";
        } else {
            lastOperation = operation;
            lastValue = Double.toString(result);
        }

        displayEdtxt.setText(Double.toString(result));
        isOperationDone = true;
    }
}
