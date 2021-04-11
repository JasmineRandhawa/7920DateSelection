package com.example.DateSelection;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity implements MonthList.OnSelectListener,
        MonthList.OnClickListener, MonthList.OnScrollListener {

    int yearListRadius = 0, dayListRadius = 0;
    //class fields
    private MonthList monthsList;
    private WheelList daysList, yearsList;
    private String daySelected = "", monthSelected = "", yearSelected = "";

    //constructor
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_new);
        InitiateAnimation();
        SetUpButtons();
        InitializeWheelLists();
    }

    //intialization code
    private void SetUpButtons() {
        LinearLayout layout = findViewById(R.id.calLayout);

        Button okButton = findViewById(R.id.OkButton);
        TextView selectedDateTextView = findViewById(R.id.selectedDate);
        ImageButton calButton = findViewById(R.id.calButton);
        calButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StartEntryAnimation();
                layout.setVisibility(View.VISIBLE);
                selectedDateTextView.setText(DateClass.GetDayOFWeek(daySelected + "-" + DateClass.GetMonthIndex(monthSelected) + "-" + yearSelected) + " " +
                        daySelected + "-" + monthSelected + "-" + yearSelected);
                calButton.setVisibility(View.GONE);
                okButton.setVisibility(View.VISIBLE);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDateTextView.setText(DateClass.GetDayOFWeek(daySelected + "-" + DateClass.GetMonthIndex(monthSelected) + "-" + yearSelected) + " " +
                        daySelected + "-" + monthSelected + "-" + yearSelected);
                calButton.setVisibility(View.VISIBLE);
                okButton.setVisibility(View.GONE);
                StartExitAnimation();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void InitializeWheelLists() {
        daysList = findViewById(R.id.daysList);
        monthsList = findViewById(R.id.monthsList);
        yearsList = findViewById(R.id.yearsList);
        monthsList.setCircleRadius(135);
        monthsList.setY(-120);
        daysList.setX(-75);
        yearsList.setX(40);
        daysList.setY(15);
        yearsList.setY(15);
        yearListRadius = 275;
        dayListRadius = 300;
        SetUpListAdaptors();
        monthsList.setFirstItemDirection(MonthList.ItemDirection.values()[3]);
        refreshCircular(daysList, "day", "");
        refreshCircular(yearsList, "year", "");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void SetUpListAdaptors() {

        // set up date list
        daysList.setWheelListRadius(dayListRadius);
        daysList.setWheelListAdaptor(WheelList.GetDaysAdaptor(this));
        daysList.scrollFirstListItemToCenter();
        daysList.setWheelListListener((daysList, firstVisibleItem, visibleItemCount, totalItemCount) -> refreshCircular(daysList, "day", "Update"));
        daysList.setWheelListAlignment(WheelListListener.ItemAllignment.Left);

        // set up month list
        monthsList.setOnSelectListener(this, monthsList);
        monthsList.setOnClickListener(this);
        monthsList.setOnScrollListener(this);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        TextView selectedMonth = null;
        for (int i = 0; i < monthsList.getChildCount(); i++) {
            TextView monthListItem = (TextView) monthsList.getChildAt(i);
            if (monthListItem != null && monthListItem.getText().equals(DateClass.GetMonthName(calendar.get(Calendar.MONTH) + 1))) {
                selectedMonth = monthListItem;
                break;
            }
        }
        if (selectedMonth != null) {
            onMonthClick(selectedMonth);
            monthsList.scrollViewToCenter(selectedMonth);
            onScrollEnd(selectedMonth);
        }

        // set up years list
        yearsList.setWheelListRadius(yearListRadius);
        yearsList.setWheelListAdaptor(WheelList.GetYearsAdaptor(this));
        yearsList.scrollFirstListItemToCenter();
        yearsList.setWheelListListener((yearList, firstVisibleItem, visibleItemCount, totalItemCount) -> refreshCircular(yearList, "year", "Update"));
        yearsList.setWheelListAlignment(WheelListListener.ItemAllignment.Right);
        calendar.setTimeInMillis(System.currentTimeMillis());
        daysList.scrollFirstListItemToCenter();
        int dayOfMonth = DateClass.GetDayOfMonth() - 2;
        daysList.moveSelectedItemToCenter(dayOfMonth);
        yearsList.scrollFirstListItemToCenter();
        yearsList.moveSelectedItemToCenter(calendar.get(Calendar.YEAR) - 3);
    }

    //animation code
    private void InitiateAnimation() {
        LinearLayout layout = findViewById(R.id.calLayout);
        resize(layout, 0.01f, 0.01f);
        ImageButton calButton = findViewById(R.id.calButton);
        int[] prevfromLoc = new int[2];
        int[] finalLoc = new int[2];
        layout.getLocationOnScreen(prevfromLoc);
        calButton.getLocationOnScreen(finalLoc);
        Animation anim = animate(prevfromLoc[0], prevfromLoc[1], finalLoc[0], -700, null, 20);
        layout.setAnimation(anim);
        anim.startNow();
        layout.setVisibility(View.GONE);
    }

    private void StartEntryAnimation() {
        LinearLayout layout = findViewById(R.id.calLayout);
        Animation.AnimationListener animList = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                resize(layout, 0.7f, 0.7f);
                layout.setX(-100);
                layout.setY(-10);
            }
        };
        int[] prevfromLoc = new int[2];
        layout.getLocationOnScreen(prevfromLoc);
        Animation anim = animate(prevfromLoc[0], prevfromLoc[1], 0, 0, animList, 750);
        layout.setAnimation(anim);
        anim.startNow();
    }

    private void StartExitAnimation() {
        LinearLayout layout = findViewById(R.id.calLayout);
        ImageButton calButton = findViewById(R.id.calButton);
        int[] prevfromLoc = new int[2];
        int[] finalLoc = new int[2];
        layout.getLocationOnScreen(prevfromLoc);
        calButton.getLocationOnScreen(finalLoc);
        float startX = prevfromLoc[0];
        float startY = prevfromLoc[1];
        float destX = finalLoc[0];
        float destY = -700;
        Animation anim = animate(startX, startY, destX, destY, null, 750);
        layout.setAnimation(anim);
        anim.startNow();
        layout.setVisibility(View.GONE);
    }

    private Animation animate(float x1, float y1, float x2, float y2, Animation.AnimationListener animLis, int speed) {
        Animation anim = new TranslateAnimation(Animation.ABSOLUTE, x1, Animation.ABSOLUTE, x2,
                Animation.ABSOLUTE, y1, Animation.ABSOLUTE, y2);
        anim.setDuration(speed);
        anim.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        if (animLis != null)
            anim.setAnimationListener(animLis);
        return anim;
    }

    //helper methods
    private void resize(LinearLayout view, float scaleX, float scaleY) {
        view.setScaleX(scaleX);
        view.setScaleY(scaleY);

        getWindow().getDecorView().getRootView().requestLayout();
    }

    private void refreshCircular(WheelList list, String fieldName, String display) {
        TextView listSelectedItem = (TextView) list.getListItemAtCenter();

        for (int i = 0; i < list.getChildCount(); i++) {
            TextView listItem = (TextView) list.getChildAt(i);
            listItem.setTextColor(getResources().getColor(R.color.DarkBlue));
            if (listItem != null && listItem != listSelectedItem) {
                listItem.setBackgroundResource(R.drawable.wheel_list_item_design);
            }
        }
        if (listSelectedItem != null) {
            listSelectedItem.setBackgroundResource(R.drawable.selected_item_design);
            listSelectedItem.setTextColor(getResources().getColor(R.color.Red));
            if (fieldName.equals("day"))
                daySelected = (String) listSelectedItem.getText();
            if (fieldName.equals("year"))
                yearSelected = (String) listSelectedItem.getText();
        }
        TextView selectedDateTextView = findViewById(R.id.selectedDate);
        if (!monthSelected.equals("") && display.equals("Update")) {
            String dayOfWeek = DateClass.GetDayOFWeek(daySelected + "-" +
                    DateClass.GetMonthIndex(monthSelected) + "-" + yearSelected);
            selectedDateTextView.setText(dayOfWeek + " " +
                    daySelected + "-" + monthSelected + "-" + yearSelected);
        }
    }

    //events
    @Override
    public void onMonthSelect(View view) {
        monthSelected = ((TextView) view).getText().toString();
    }

    @Override
    public void onMonthClick(View view) {
        monthSelected = ((TextView) view).getText().toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onScrollEnd(View selectedMonthListItem) {

        Animation animation = new RotateAnimation(0, 360,
                selectedMonthListItem.getWidth() / 2, selectedMonthListItem.getHeight() / 2);
        animation.setDuration(250);
        selectedMonthListItem.startAnimation(animation);

        for (int i = 0; i < monthsList.getChildCount(); i++) {
            TextView monthListItem = (TextView) monthsList.getChildAt(i);
            if (monthListItem != null && monthListItem != selectedMonthListItem) {
                monthListItem.setBackgroundResource(R.drawable.month);
                monthListItem.setTextColor(getResources().getColor(R.color.White));
                monthListItem.setElevation(2.0f);
            }
        }
        selectedMonthListItem.setBackgroundResource(R.drawable.selected_item_design);
        ((TextView) selectedMonthListItem).setTextColor(getResources().getColor(R.color.Red));
        ((TextView) selectedMonthListItem).setHeight(135);
        ((TextView) selectedMonthListItem).setPadding(0, 0, 0, 0);
        ((TextView) selectedMonthListItem).setWidth(165);
        ((TextView) selectedMonthListItem).setTextSize(30);
        selectedMonthListItem.setElevation(3.0f);
        monthSelected = ((TextView) selectedMonthListItem).getText().toString();
    }
}

