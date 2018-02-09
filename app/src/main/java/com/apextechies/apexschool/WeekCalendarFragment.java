package com.apextechies.apexschool;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apextechies.apexschool.listener.CalenderListener;
import com.apextechies.apexschool.utils.ViewUtils;
import com.apextechies.apexschool.utils.WeekCalendarOptions;
import com.apextechies.apexschool.calender.CalUtil;

import org.joda.time.LocalDateTime;
import org.joda.time.Weeks;

import java.util.Calendar;

public class WeekCalendarFragment extends Fragment {
    //Bundle Keys
    public static final String ARGUMENT_SELECTED_DATE_BACKGROUND = "bg:select:date";
    public static final String ARGUMENT_SELECTED_DATE_HIGHLIGHT_COLOR = "selected:date:highlight:color";
    public static final String ARGUMENT_CURRENT_DATE_TEXT_COLOR = "bg:current:bg";
    public static final String ARGUMENT_CALENDER_BACKGROUND_COLOR = "bg:cal";
    public static final String ARGUMENT_NOW_BACKGROUND = "bg:now";
    public static final String ARGUMENT_PRIMARY_TEXT_COLOR = "primary:text:color";
    public static final String ARGUMENT_DAY_TEXT_SIZE = "primary:day:size";
    public static final String ARGUMENT_DAY_TEXT_STYLE = "day:text:style";
    public static final String ARGUMENT_SECONDARY_TEXT_COLOR = "secondary:text:color";
    public static final String ARGUMENT_SECONDARY_TEXT_SIZE = "secondary:text:size";
    public static final String ARGUMENT_SECONDARY_TEXT_STYLE = "secondary:text:style";
    public static final String ARGUMENT_DAY_HEADER_LENGTH = "day:header:length";
    public static final String ARGUMENT_WEEK_COUNT = "week:count";
    public static final String ARGUMENT_DISPLAY_DATE_PICKER = "display:date:picker";
    public static final String ARGUMENT_EVENT_DAYS = "event:days";
    public static final String ARGUMENT_EVENT_COLOR = "event:color";

    public static final String ARGUMENT_PACKAGE_NAME = "package";
    public static String PACKAGE_NAME_VALUE;
    private static WeekCalendarFragment sWeekCalendarInstance;
    Calendar mPreSelectedDate;
    private LocalDateTime mStartDate, mSelectedDate;
    private TextView mMonthView, mSundayTv, mMondayTv, mTuesdayTv, mWednesdayTv;
    private TextView mThursdayTv, mFridayTv, mSaturdayTv;
    private TextView[] mDayHeaders;
    private ViewPager mViewPager;
    private LinearLayout mBackground, mChangeDate;
    private ViewGroup mFrameDatePicker;
    private CalenderListener mCalenderListener;
    private long[] mEventDays;
    private boolean mDisplayDatePicker = true;
    //initial values of calender property
    private String mSelectorDateIndicatorValue = "bg_red";
    private int mSelectorHighlightColor = -1;
    private int mCurrentDateIndicatorValue = Color.BLACK;
    private int mPrimaryTextColor = Color.WHITE;
    private int mPrimaryTextSize;
    private int mPrimaryTextStyle = -1;
    private String mEventColor = WeekCalendarOptions.EVENT_COLOR_WHITE;
    private int mWeekCount = 53;//one year
    private int mMiddlePoint = mWeekCount / 2;

    /**
     * creating instance of the calender class
     */
    public static synchronized WeekCalendarFragment getsWeekCalendarInstance() {
        return sWeekCalendarInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializing instance
        sWeekCalendarInstance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_calender, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mMonthView = (TextView) view.findViewById(R.id.month_tv);
        mSundayTv = (TextView) view.findViewById(R.id.week_sunday);
        mMondayTv = (TextView) view.findViewById(R.id.week_monday);
        mTuesdayTv = (TextView) view.findViewById(R.id.week_tuesday);
        mWednesdayTv = (TextView) view.findViewById(R.id.week_wednesday);
        mThursdayTv = (TextView) view.findViewById(R.id.week_thursday);
        mFridayTv = (TextView) view.findViewById(R.id.week_friday);
        mSaturdayTv = (TextView) view.findViewById(R.id.week_saturday);
        mBackground = (LinearLayout) view.findViewById(R.id.background);
        mChangeDate = (LinearLayout) view.findViewById(R.id.changeDate);
        mFrameDatePicker = (ViewGroup) view.findViewById(R.id.frame_date_picker);

        mDayHeaders = new TextView[]{mSundayTv, mMondayTv, mTuesdayTv, mWednesdayTv, mThursdayTv
                , mFridayTv, mSaturdayTv};

        PACKAGE_NAME_VALUE = getActivity().getPackageName();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        handleCustomizationArguments();
        // update middle point of view pager
        mMiddlePoint = mWeekCount / 2;
        /*Setting Calender Adapter*/
        setupViewPager();

       /*CalUtil is called*/
        CalUtil mCal = new CalUtil();
        mCal.calculate(getActivity());//date calculation called

        mSelectedDate = mCal.getSelectedDate();//sets selected from CalUtil
        mStartDate = mCal.getStartDate();//sets start date from CalUtil

        //Setting the selected date listener
        if (mCalenderListener != null) {
            mCalenderListener.onSelectDate(mStartDate);
        }

        if (mDisplayDatePicker) {
            // Setting the month name
            mMonthView.setText(mSelectedDate.monthOfYear().getAsShortText()
                    + " " + mSelectedDate.year().getAsShortText().toUpperCase());

            /**
             * For quick selection of a date.Any picker or custom date picker can de used
             */
            mChangeDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCalenderListener != null) {
                        mCalenderListener.onSelectPicker();
                    }
                }
            });
        }
    }

    /**
     * Set set date of the selected week
     */
    public void setDateWeek(Calendar calendar) {
        LocalDateTime ldt = LocalDateTime.fromCalendarFields(calendar);
        AppController.getInstance().setSelected(ldt);
        int nextPage = Weeks.weeksBetween(mStartDate, ldt).getWeeks();
        if (nextPage < 0 || (nextPage == 0 && ldt.isBefore(mStartDate))) {
            // make selected date to previous week on viewpager onSelected:
            // if result is negative or it is 0 weeks but earlier than week start date.
            --nextPage;
        }
        if (nextPage >= -mMiddlePoint && nextPage < mMiddlePoint) {
            mViewPager.setCurrentItem(nextPage + mMiddlePoint);
            if (mCalenderListener != null) {
                mCalenderListener.onSelectDate(ldt);
            }
            WeekFragment fragment = (WeekFragment) mViewPager.getAdapter()
                    .instantiateItem(mViewPager, nextPage + mMiddlePoint);
            fragment.ChangeSelector(ldt);
        }
    }

    /**
     * Set date for the week calendar to start on resume
     *
     * @param calendar
     */
    public void setPreSelectedDate(Calendar calendar) {
        mPreSelectedDate = calendar;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPreSelectedDate != null) {
            setDateWeek(mPreSelectedDate);
            mPreSelectedDate = null;
        }
    }

    /**
     * Notify the selected date main page
     */
    public void getSelectedDate(LocalDateTime mSelectedDate) {
        if (mCalenderListener != null) {
            mCalenderListener.onSelectDate(mSelectedDate);
        }
    }

    /**
     * Set setCalenderListener when user click on a date
     */
    public void setCalenderListener(CalenderListener calenderListener) {
        this.mCalenderListener = calenderListener;
    }

    private void setupViewPager() {
        CalenderAdapter adapter = new CalenderAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mMiddlePoint);
        /*Week change Listener*/
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int weekNumber) {
                int addDays = (weekNumber - mMiddlePoint) * 7;
                mSelectedDate = mStartDate.plusDays(addDays); //add 7 days to the selected date
                if (mDisplayDatePicker) {
                    mMonthView.setText(mSelectedDate.monthOfYear().getAsShortText()
                            + "-" + mSelectedDate.year().getAsShortText().toUpperCase());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void handleCustomizationArguments() {
        if (getArguments() == null) {
            return;
        }
        if (getArguments().containsKey(ARGUMENT_PACKAGE_NAME)) {
            //its for showing the resource value from the parent package
            PACKAGE_NAME_VALUE = getArguments().getString(ARGUMENT_PACKAGE_NAME);
        }
        if (getArguments().containsKey(ARGUMENT_CALENDER_BACKGROUND_COLOR)) {
            mBackground.setBackgroundColor(getArguments().getInt(ARGUMENT_CALENDER_BACKGROUND_COLOR));
        }
        if (getArguments().containsKey(ARGUMENT_SELECTED_DATE_BACKGROUND)) {
            mSelectorDateIndicatorValue = getArguments().getString(ARGUMENT_SELECTED_DATE_BACKGROUND);
        }
        if (getArguments().containsKey(ARGUMENT_SELECTED_DATE_HIGHLIGHT_COLOR)) {
            mSelectorHighlightColor = getArguments().getInt(ARGUMENT_SELECTED_DATE_HIGHLIGHT_COLOR);
        }
        if (getArguments().containsKey(ARGUMENT_CURRENT_DATE_TEXT_COLOR)) {
            mCurrentDateIndicatorValue = getArguments().getInt(ARGUMENT_CURRENT_DATE_TEXT_COLOR);
        }
        if (getArguments().containsKey(ARGUMENT_WEEK_COUNT)) {
            if (getArguments().getInt(ARGUMENT_WEEK_COUNT) > 0)
                mWeekCount = getArguments().getInt(ARGUMENT_WEEK_COUNT);
        }
        if (getArguments().containsKey(ARGUMENT_PRIMARY_TEXT_COLOR)) {
            mMonthView.setTextColor(getArguments().getInt(ARGUMENT_PRIMARY_TEXT_COLOR));
            mPrimaryTextColor = getArguments().getInt(ARGUMENT_PRIMARY_TEXT_COLOR);
        }
        if (getArguments().containsKey(ARGUMENT_DAY_TEXT_SIZE)) {
            mPrimaryTextSize = getArguments().getInt(ARGUMENT_DAY_TEXT_SIZE);
        }
        if (getArguments().containsKey(ARGUMENT_DAY_TEXT_STYLE)) {
            mPrimaryTextStyle = getArguments().getInt(ARGUMENT_DAY_TEXT_STYLE);
        }
        if (getArguments().containsKey(ARGUMENT_SECONDARY_TEXT_COLOR)) {
            mSundayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mMondayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mTuesdayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mWednesdayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mThursdayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mFridayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mSaturdayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
        }
        if (getArguments().containsKey(ARGUMENT_SECONDARY_TEXT_SIZE)) {
            int secondaryTextSize = getArguments().getInt(ARGUMENT_SECONDARY_TEXT_SIZE);
            ViewUtils.setTextSize(secondaryTextSize, mSundayTv, mMondayTv, mTuesdayTv,
                    mWednesdayTv, mThursdayTv, mFridayTv, mSaturdayTv);
        }
        if (getArguments().containsKey(ARGUMENT_SECONDARY_TEXT_STYLE)) {
            int secondaryTextStyle = getArguments().getInt(ARGUMENT_SECONDARY_TEXT_STYLE);
            ViewUtils.setTextType(secondaryTextStyle, mSundayTv, mMondayTv, mTuesdayTv,
                    mWednesdayTv, mThursdayTv, mFridayTv, mSaturdayTv);
        }
        if (getArguments().containsKey(ARGUMENT_DISPLAY_DATE_PICKER)) {
            mDisplayDatePicker = getArguments().getBoolean(ARGUMENT_DISPLAY_DATE_PICKER);
            if (!mDisplayDatePicker) {
                mFrameDatePicker.setVisibility(View.GONE);
            }
        }
        if (getArguments().containsKey(ARGUMENT_NOW_BACKGROUND) && mDisplayDatePicker) {
            Resources resources = getResources();
        }


        mEventDays = (long[]) getArguments().get(ARGUMENT_EVENT_DAYS);
        if (getArguments().containsKey(ARGUMENT_EVENT_COLOR)) {
            mEventColor = getArguments().getString(ARGUMENT_EVENT_COLOR);
        }
        String dayHeaderLength = getArguments().getString(ARGUMENT_DAY_HEADER_LENGTH);
        setHeaderLength(dayHeaderLength);
    }

    private void setHeaderLength(String dayHeaderLength) {
        if (dayHeaderLength == null) {
            return;
        }
        String[] headers;
        if (dayHeaderLength.equals(WeekCalendarOptions.DAY_HEADER_LENGTH_THREE_LETTERS)) {
            headers = getResources().getStringArray(R.array.week_header_three);
        } else if (dayHeaderLength.equals(WeekCalendarOptions.DAY_HEADER_LENGTH_ONE_LETTER)) {
            headers = getResources().getStringArray(R.array.week_header_one);
        } else {
            headers = getResources().getStringArray(R.array.week_header_one);
        }
        int i = 0;
        for (TextView tv : mDayHeaders) {
            tv.setText(headers[i++]);
        }
    }

    /**
     * Adaptor which shows weeks in the view
     */
    private class CalenderAdapter extends FragmentStatePagerAdapter {
        public CalenderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public WeekFragment getItem(int pos) {
            return WeekFragment.newInstance(
                    pos - mMiddlePoint,
                    mSelectorDateIndicatorValue,
                    mCurrentDateIndicatorValue,
                    mPrimaryTextColor,
                    mPrimaryTextSize,
                    mPrimaryTextStyle,
                    mSelectorHighlightColor,
                    mEventDays,
                    mEventColor);
        }

        @Override
        public int getCount() {
            return mWeekCount;
        }
    }
}
