package com.remotearthsolutions.expensetracker.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DatePickerDialogFragment extends DialogFragment implements View.OnClickListener {

    private LinearLayout previousdate, currentdate, selectdate;
    private TextView todayDateTv, yesterdayDateTv;
    private DatePickerDialogFragment.Callback callback;
    private int cDay, cMonth, cYear;
    private Context context;

    private String format;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static DatePickerDialogFragment newInstance(String title) {
        DatePickerDialogFragment frag = new DatePickerDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    public void setCallback(DatePickerDialogFragment.Callback callback) {
        this.callback = callback;
    }

    public void setInitialDate(int cDay, int cMonth, int cYear) {
        this.cDay = cDay;
        this.cMonth = cMonth;
        this.cYear = cYear;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        format = preferences.getString(Constants.PREF_TIME_FORMAT,"dd-MM-yyyy");

        previousdate = view.findViewById(R.id.previousdate);
        currentdate = view.findViewById(R.id.currentdate);
        selectdate = view.findViewById(R.id.selectdate);
        yesterdayDateTv = view.findViewById(R.id.showdyesterday);
        todayDateTv = view.findViewById(R.id.showdtoday);

        yesterdayDateTv.setText(DateTimeUtils.getDate(format, -1));
        todayDateTv.setText(DateTimeUtils.getCurrentDate(format));

        previousdate.setOnClickListener(this);
        currentdate.setOnClickListener(this);
        selectdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.currentdate: {
                String date = DateTimeUtils.getCurrentDate(format);
                callback.onSelectDate(date);
                break;
            }
            case R.id.previousdate: {
                String date = DateTimeUtils.getDate(format, -1);
                callback.onSelectDate(date);
                break;
            }
            case R.id.selectdate: {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                    cYear = year;
                    cMonth = month;
                    cDay = dayOfMonth;
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, cDay);
                    calendar.set(Calendar.MONTH, cMonth);
                    calendar.set(Calendar.YEAR, cYear);
                    DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());

                    callback.onSelectDate(dateFormat.format(calendar.getTime()));
                    dismiss();
                }, cYear, cMonth, cDay);
                datePickerDialog.show();
                break;
            }
        }
    }

    public interface Callback {
        void onSelectDate(String date);
    }
}
