package com.remotearthsolutions.expensetracker.fragments.home;

import android.view.View;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFilterButtonClickListener implements View.OnClickListener {

    private DateFilterButtonClickListener.Callback callback;

    private String selectedDate;
    private Date startDate, endDate;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;

    private int day = 0;
    private int month = 0;
    private int year = 0;
    private int startingOfWeek = -6;
    private int endingOfWeek = 0;

    public DateFilterButtonClickListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        String date = null;
        long startTime = 0, endTime = 0;

        if (v.getId() == R.id.nextDateBtn) {

            if (selectedDate.equals(Constants.KEY_DAILY)) {

                if (day < 0) {
                    day = day + 1;
                }
                date = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, day);
                startTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 0, 0, 0);
                endTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 23, 59, 59);
            } else if (selectedDate.equals(Constants.KEY_WEEKLY)) {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy, Locale.getDefault());
                try {

                    if (endingOfWeek < 0) {
                        endingOfWeek = endingOfWeek + 7;
                        startingOfWeek = startingOfWeek + 7;
                    }
                    startDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, startingOfWeek));
                    endDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, endingOfWeek));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String weekstartdate = simpleDateFormat.format(startDate);
                String weeklastdate = simpleDateFormat.format(endDate);

                date = weekstartdate + " - " + weeklastdate;
                startTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weekstartdate, 0, 0, 0);
                endTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weeklastdate, 29, 59, 59);
            } else if (selectedDate.equals(Constants.KEY_MONTHLY)) {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.MM_yy, Locale.getDefault());
                calendar = Calendar.getInstance();
                if (month < 0) {
                    month = month + 1;
                }

                calendar.add(Calendar.MONTH, month);
                date = simpleDateFormat.format(calendar.getTime());

                int minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                int maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                startTime = getDateTimeInLong(DateTimeUtils.MM_yy, date, minimumDate, 0, 0, 0);
                endTime = getDateTimeInLong(DateTimeUtils.MM_yy, date, maximumDate, 23, 59, 59);

            } else if (selectedDate.equals(Constants.KEY_YEARLY)) {
                selectedDate = Constants.KEY_YEARLY;
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.yyyy, Locale.getDefault());
                calendar = Calendar.getInstance();

                if (year < 0) {
                    year = year + 1;
                }

                calendar.add(Calendar.YEAR, year);
                date = simpleDateFormat.format(calendar.getTime());
                int minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_YEAR);
                int maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
                startTime = getDateTimeInLong(DateTimeUtils.yyyy, date, minimumDate, 0, 0, 0);
                endTime = getDateTimeInLong(DateTimeUtils.yyyy, date, maximumDate, 23, 59, 59);
            }

        } else if (v.getId() == R.id.previousDateBtn) {

            if (selectedDate.equals(Constants.KEY_DAILY)) {
                day = day - 1;

                date = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, day);
                startTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 0, 0, 0);
                endTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 23, 59, 59);

            } else if (selectedDate.equals(Constants.KEY_WEEKLY)) {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy, Locale.getDefault());
                try {
                    startingOfWeek = startingOfWeek - 7;
                    endingOfWeek = endingOfWeek - 7;

                    endDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, endingOfWeek));
                    startDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, startingOfWeek));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String weekstartdate = simpleDateFormat.format(startDate);
                String weeklastdate = simpleDateFormat.format(endDate);

                date = weekstartdate + " - " + weeklastdate;
                startTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weekstartdate, 0, 0, 0);
                endTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weeklastdate, 29, 59, 59);

            } else if (selectedDate.equals(Constants.KEY_MONTHLY)) {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.MM_yy, Locale.getDefault());
                calendar = Calendar.getInstance();
                month = month - 1;

                calendar.add(Calendar.MONTH, month);
                date = simpleDateFormat.format(calendar.getTime());
                int minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                int maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                startTime = getDateTimeInLong(DateTimeUtils.MM_yy, date, minimumDate, 0, 0, 0);
                endTime = getDateTimeInLong(DateTimeUtils.MM_yy, date, maximumDate, 23, 59, 59);

            } else if (selectedDate.equals(Constants.KEY_YEARLY)) {

                selectedDate = Constants.KEY_YEARLY;
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.yyyy, Locale.getDefault());
                calendar = Calendar.getInstance();
                year = year - 1;
                calendar.add(Calendar.YEAR, year);

                date = simpleDateFormat.format(calendar.getTime());
                int minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_YEAR);
                int maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
                startTime = getDateTimeInLong(DateTimeUtils.yyyy, date, minimumDate, 0, 0, 0);
                endTime = getDateTimeInLong(DateTimeUtils.yyyy, date, maximumDate, 23, 59, 59);

            }

        } else if (v.getId() == R.id.dailyRangeBtn) {
            resetDate();
            selectedDate = Constants.KEY_DAILY;

            date = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, day);
            startTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 0, 0, 0);
            endTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 23, 59, 59);

        } else if (v.getId() == R.id.weeklyRangeBtn) {
            resetDate();

            selectedDate = Constants.KEY_WEEKLY;
            simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy, Locale.getDefault());
            try {
                startDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, startingOfWeek));
                endDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, endingOfWeek));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String weekstartdate = simpleDateFormat.format(startDate);
            String weeklastdate = simpleDateFormat.format(endDate);

            date = weekstartdate + " - " + weeklastdate;
            startTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weekstartdate, 0, 0, 0);
            endTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weeklastdate, 29, 59, 59);

        } else if (v.getId() == R.id.monthlyRangeBtn) {
            resetDate();

            selectedDate = Constants.KEY_MONTHLY;

            simpleDateFormat = new SimpleDateFormat(DateTimeUtils.MM_yy, Locale.getDefault());

            calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, month);
            date = simpleDateFormat.format(calendar.getTime());
            int minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
            int maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            startTime = getDateTimeInLong(DateTimeUtils.MM_yy, date, minimumDate, 0, 0, 0);
            endTime = getDateTimeInLong(DateTimeUtils.MM_yy, date, maximumDate, 23, 59, 59);

        } else if (v.getId() == R.id.yearlyRangeBtn) {
            resetDate();

            selectedDate = Constants.KEY_YEARLY;
            SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.yyyy, Locale.getDefault());

            calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, year);
            date = sdf.format(calendar.getTime());
            int minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_YEAR);
            int maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            startTime = getDateTimeInLong(DateTimeUtils.yyyy, date, minimumDate, 0, 0, 0);
            endTime = getDateTimeInLong(DateTimeUtils.yyyy, date, maximumDate, 23, 59, 59);
        }

        callback.onDateChanged(v.getId(), date, startTime, endTime);
    }

    private long getDateTimeInLong(String format, String dateStr, int hour, int min, int sec) {
        Calendar calendarforweeklastday = DateTimeUtils.getCalendarFromDateString(format, dateStr);
        calendarforweeklastday.set(Calendar.HOUR_OF_DAY, hour);
        calendarforweeklastday.set(Calendar.MINUTE, min);
        calendarforweeklastday.set(Calendar.SECOND, sec);
        return calendarforweeklastday.getTimeInMillis();
    }

    private long getDateTimeInLong(String format, String dateStr, int dateType, int hour, int min, int sec) {
        Calendar calendarforweeklastday = DateTimeUtils.getCalendarFromDateString(format, dateStr);
        calendarforweeklastday.set(Calendar.DATE, dateType);
        calendarforweeklastday.set(Calendar.HOUR_OF_DAY, hour);
        calendarforweeklastday.set(Calendar.MINUTE, min);
        calendarforweeklastday.set(Calendar.SECOND, sec);
        return calendarforweeklastday.getTimeInMillis();
    }

    private void resetDate() {
        day = 0;
        month = 0;
        year = 0;
        startingOfWeek = -6;
        endingOfWeek = 0;
    }

    public interface Callback {
        void onDateChanged(int btnId, String date, long startTime, long endTime);
    }
}
