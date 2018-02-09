package com.apextechies.apexschool.calender;

import android.content.Context;

import com.apextechies.apexschool.AppController;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.List;


/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 Ramesh M Nair
 * Edit: Asi Mugrabi
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class CalUtil {

    LocalDateTime mStartDate, selectedDate;


    /**
     * Get the day difference in the selected day and the first day in the week
     */
    public static int mDateGap(String dayName) {
        if (dayName.equals("mon")) {
            return 1;
        } else if (dayName.equals("tue")) {
            return 2;
        } else if (dayName.equals("wed")) {
            return 3;
        } else if (dayName.equals("thu")) {
            return 4;
        } else if (dayName.equals("fri")) {
            return 5;
        } else if (dayName.equals("sat")) {
            return 6;
        } else {
            return 0;
        }
    }

    public static boolean isSameDay(LocalDateTime day1, LocalDateTime day2) {
        return day1 != null && day2 != null
                && day1.getYear() == day2.getYear()
                && day1.getMonthOfYear() == day2.getMonthOfYear()
                && day1.getDayOfMonth() == day2.getDayOfMonth();
    }

    public static boolean isDayInList(LocalDateTime day, List<LocalDateTime> days) {
        if (days == null || day == null) {
            return false;
        }
        for (LocalDateTime isDay : days) {
            if (isSameDay(isDay, day)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initial calculation of the week
     */
    public void calculate(Context mContext) {

        //Initializing JodaTime
        JodaTimeAndroid.init(mContext);

        //Initializing Start with current month
        final LocalDateTime currentDateTime = new LocalDateTime();

        setStartDate(currentDateTime.getYear(), currentDateTime.getMonthOfYear(), currentDateTime.getDayOfMonth());

        int weekGap = CalUtil.mDateGap(currentDateTime.dayOfWeek().getAsText().substring(0, 3).toLowerCase());


        if (weekGap != 0) {
            //if the current date is not the first day of the week the rest of days is added

            //Calendar set to the current date
            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.DAY_OF_YEAR, -weekGap);

            //now the date is weekGap days back
            LocalDateTime ldt = LocalDateTime.fromCalendarFields(calendar);

            setStartDate(ldt.getYear(), ldt.getMonthOfYear(), ldt.getDayOfMonth());

        }
    }


    /*Set The Start day (week)from calender*/
    private void setStartDate(int year, int month, int day) {

        mStartDate = new LocalDateTime(year, month, day, 0, 0, 0);
        selectedDate = mStartDate;

        AppController.getInstance().setDate(selectedDate);

    }


    public LocalDateTime getSelectedDate() {
        return selectedDate;
    }


    public LocalDateTime getStartDate() {
        return mStartDate;
    }
}
