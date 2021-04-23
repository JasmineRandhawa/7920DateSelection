package com.example.DateSelection.Trials;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.DateSelection.Main.Common;
import com.example.DateSelection.Main.Data;
import com.example.DateSelection.Main.ExportDataActivity;
import com.example.DateSelection.Main.Trial;
import com.example.DateSelection.Main.TrialAttempt;
import com.example.DateSelection.NewDesign.MonthList;
import com.example.DateSelection.NewDesign.WheelList;
import com.example.DateSelection.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/*
.Date Selection
*/

//This is trial 1 screen for combination small date : old design
public class Trial1_New extends AppCompatActivity implements MonthList.OnSelectListener,
        MonthList.OnClickListener, MonthList.OnScrollListener{

    //trial specific variables
    final static int trialNumber = 1; // trial number
    final static int partNumber = 2; // part number
    final static boolean isFirstTrialActivity = false; //is its first trial activity to launch
    final static boolean isLastTrialActivity = false; // is it last trial activity
    final static String trialType = "SmallNew"; // trial type
    final static String dateEra = "Small";
    final static String designType = "New Design"; // is it old design or new design
    final static String trialTask = " - Date Selection"; // trial task description
    final static String popupMessage = "You are about to use " + designType + " for " + trialTask + ".";
    final static String instructionsPopUpTitle = "Instructions - Trial "; // trial instructions popup title
    final static String trialCompletePopUpTitle = "Success"; // trial success popup titile
    final static String trialCompletionPopupMessage = "You have successfully completed the Trial " + trialNumber +
            (partNumber == 1 ? (" Part " + partNumber) : "");
    final static String popUButtonText = (isLastTrialActivity ? "EMAIL DATA" : ("GO TO " +
            (partNumber == 1 ? "PART 2" : "NEXT TRIAL"))); // popup button text
    @LayoutRes
    final static int layout = R.layout.activity_new_date_selection; // trial layout

    //carry over data
    static Data data;
    static List<Trial> trials = new ArrayList<>();
    static List<TrialAttempt> listAttempts;

    //computed variables
    final Context context = Trial1_New.this;  //context of current screen
    static String dateToSelect = ""; // randomly generated date to select
    Intent nextScreenIntent;  //intent of next screen when trial ends
    String dateSelectedByUser = ""; // date selected by user
    long startTimeInMillis; // start Time is time in millisec when user taps date picker
    long endTimeInMillis; // end Time when user selects correct date
    long timeTaken = 0; // time taken by user to successfully complete each attempt in  trial.
    int successAttempts = 0; // number of successful attempts when date user selected matches given date
    int failedAttempts = 0; // number of failed attempts when date selected by user does not match given date
    int totalAttempts = (Common.getTotalSuccessfulAttemptsNeededPerTrial()); // number of successful atemapts needed to complete trial
    int totalAttemptsMadeByUser = 0; // number of attempts to select given date
    int noOfTaps = 0;
    int errorCount;
    TextView textViewSelectedDate;
    String selectedDate ="";
    boolean isFirstVisit =true;

    int yearListRadius = 0, dayListRadius = 0;
    private MonthList monthsList;
    private WheelList daysList, yearsList;
    private String daySelected = "", monthSelected = "", yearSelected = "";
    Calendar calendar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    // called on load of trial screen
    protected void onCreate(Bundle savedInstanceState) {

        // map Screen to UI
        super.onCreate(savedInstanceState);
        setContentView(layout);

        nextScreenIntent = new Intent(this, Trial2_Old.class);

        TextView textViewTrialNumber = (TextView) findViewById(R.id.textViewTrialNumber);
        textViewTrialNumber.setText("Trial " + trialNumber + " : Part " + partNumber);
        TextView textViewTrialName = (TextView) findViewById(R.id.textViewTrialName);
        textViewTrialName.setText(trialTask + " : " + designType);
        textViewSelectedDate = (TextView) findViewById(R.id.textViewSelectedDate);
        calendar = Calendar.getInstance();

        //initialize variable to be computed later.
        InitializeVariables();
        InitiateAnimation();
        InitializeWheelLists();
        SetUpButtons();
        isFirstVisit = true;

        //initialize data
        if (isFirstTrialActivity) {
            data = new Data();
            data.Trials = new ArrayList<>();
            listAttempts = new ArrayList<>();
        } else {
            data = (Data) (getIntent().getSerializableExtra("Data"));
            trials.addAll(data.getTrials());
            listAttempts = new ArrayList<>();
        }


        ShowTrialBeginningPopUp();
        StartNextTrialAttempt(false);
        TextView textViewAttempts = findViewById(R.id.textViewAttempts);
        textViewAttempts.setText(successAttempts + " of " + totalAttempts + " completed");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void BindCalendar()
    {
        LinearLayout callayout = findViewById(R.id.calLayout);
        Button okButton = findViewById(R.id.okButton);
        ImageButton calButton = findViewById(R.id.calButton);
        callayout.setVisibility(View.GONE);
        okButton.setVisibility(View.GONE);
        calButton.setVisibility(View.VISIBLE);
        selectedDate="";
        daySelected="";
        monthSelected="";
        yearSelected="";
        textViewSelectedDate.setText("");
        if(!isFirstVisit) {
            InitiateAnimation();
            SetUpListAdaptors();
        }
   }

     //intialization code
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void SetUpButtons() {
        LinearLayout layout = findViewById(R.id.calLayout);
        Button okButton = findViewById(R.id.okButton);
        ImageButton calButton = findViewById(R.id.calButton);
        calButton.setOnClickListener(v -> {
            startTimeInMillis = Calendar.getInstance().getTimeInMillis();
            layout.setVisibility(View.VISIBLE);
            StartEntryAnimation();

        });

        okButton.setOnClickListener(v -> {
            isFirstVisit=false;
            selectedDate = daySelected + "-" + monthSelected + "-" + yearSelected;
            calButton.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.GONE);
            StartExitAnimation();
            layout.setVisibility(View.GONE);
            dateSelectedByUser = selectedDate;
            // on success
            if (dateToSelect.equals(dateSelectedByUser)) {
                endTimeInMillis = Calendar.getInstance().getTimeInMillis();
                timeTaken = endTimeInMillis - startTimeInMillis;
                SaveData(true);
            }
            //on failure
            else {
                SaveData(false);
            }
        });

       daysList.setOnTouchListener((v, event) -> {
           if(event.getAction() == MotionEvent.ACTION_UP)
           noOfTaps++;
           return false;
       });

        monthsList.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP)
            noOfTaps++;
            return false;
        });

        yearsList.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP)
            noOfTaps++;
            return false;
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
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void SetUpListAdaptors() {

        // set up date list
        daysList.setWheelListRadius(dayListRadius);
        daysList.setWheelListAdaptor(WheelList.GetDaysAdaptor(this));
        daysList.setWheelListListener((dayList, firstItem, displayedItems, totalItems)
                -> refreshCircular(daysList, "day", "Update"));
        daysList.setWheelListAlignment(WheelList.WheelListListener.ItemAllignment.Left);

        // set up month list
        monthsList.setOnSelectListener(this, monthsList);
        monthsList.setOnClickListener(this);
        monthsList.setOnScrollListener(this);

        TextView selectedMonth = null;
        for (int i = 0; i < monthsList.getChildCount(); i++) {
            TextView monthListItem = (TextView) monthsList.getChildAt(i);
            if (monthListItem != null && monthListItem.getText().equals("Apr")) {
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
        yearsList.scrollFirstItemToCenter();
        yearsList.setWheelListListener((yearList, firstItem, displayedItems, totalItems)
                -> refreshCircular(yearList, "year", "Update"));
        yearsList.setWheelListAlignment(WheelList.WheelListListener.ItemAllignment.Right);
        if(isFirstVisit) {
            int dayOfMonth = Common.GetDayOfMonth() - 2;
            daysList.scrollSelectedItemToCenter(dayOfMonth);
            yearsList.scrollSelectedItemToCenter(2021 - 3);
        }
        else
        {
            int dayOfMonth = Common.GetDayOfMonth() +1;
            daysList.scrollSelectedItemToCenter(dayOfMonth);
            yearsList.scrollSelectedItemToCenter(2021);
        }
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
        Animation anim = animate(prevfromLoc[0], prevfromLoc[1], finalLoc[0],-200, null, 450);
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
                layout.setX(280);
                layout.setY(520);
                Button okButton = findViewById(R.id.okButton);
                ImageButton calButton = findViewById(R.id.calButton);
                calButton.setVisibility(View.GONE);
                okButton.setVisibility(View.VISIBLE);
            }
        };
        int[] prevfromLoc = new int[2];
        layout.getLocationOnScreen(prevfromLoc);
        Animation anim = animate(0, 0, 0, -1000, animList, 750);
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
        Animation.AnimationListener animList = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                resize(layout, 0.01f, 0.01f);
            }
        };
        Animation anim = animate(layout.getX(), layout.getY()-300, layout.getX(), -500, animList, 750);
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
        TextView listSelectedItem = (TextView) list.getCenterItem();

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
            String dayOfWeek = Common.GetDayOFWeek(daySelected + "-" +
                    Common.GetMonthIndex(monthSelected) + "-" + yearSelected);
            textViewSelectedDate.setText(dayOfWeek + " " +
                    daySelected + "-" + monthSelected + "-" + yearSelected);
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

    // Save Data on success or  increment error count info on failure
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void SaveData(Boolean hasSuceeded) {
        totalAttemptsMadeByUser++;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        TextView title = new TextView(getApplicationContext());
        title.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Black));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView;
        if (hasSuceeded) {
            successAttempts++;
            dateSelectedByUser = "";

            //successful attempt not equal to total attempts
            if (successAttempts < totalAttempts) {
                listAttempts.add(new TrialAttempt(successAttempts,timeTaken, noOfTaps, errorCount));
                StartNextTrialAttempt(false);
                LayoutInflater l = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View successPopUpView = layoutInflater.inflate(R.layout.activity_success, null);
                ShowSuccessFailureAlert(successPopUpView);
            } else if (successAttempts == totalAttempts) {
                popupView = layoutInflater.inflate(R.layout.activity_trial_popup, null);
                TextView textViewAttempts = findViewById(R.id.textViewAttempts);
                textViewAttempts.setText(successAttempts + " of " + totalAttempts + " completed");
                TextView textViewSuccessAttempts = popupView.findViewById(R.id.textViewSuccessAttempts);
                title.setText(trialCompletePopUpTitle);
                textViewSuccessAttempts.setText(trialCompletionPopupMessage);

                builder.setPositiveButton(popUButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // save data in list
                        listAttempts.add(new TrialAttempt(successAttempts,timeTaken, noOfTaps, errorCount));
                        if (!isFirstTrialActivity) {

                            Trial trial = new Trial(trialNumber, trialType, new ArrayList<>(),
                                    totalAttemptsMadeByUser, successAttempts, failedAttempts);
                            trial.setTrialAttempts(listAttempts);
                            trials.add(trial);
                            data.setTrials(trials);
                            nextScreenIntent.putExtra("Data", data);
                            trials = new ArrayList<>();
                        } else {
                            List<Trial> trials = new ArrayList<>();
                            Trial trial = new Trial(trialNumber, trialType, new ArrayList<>(),
                                    totalAttemptsMadeByUser, successAttempts, failedAttempts);
                            trial.setTrialAttempts(listAttempts);
                            trials.add(trial);
                            data.setTrials(trials);
                            nextScreenIntent.putExtra("Data", data);
                        }
                        data = new Data();
                        listAttempts = new ArrayList<>();
                        startActivity(nextScreenIntent);
                    }
                });
                builder.setCustomTitle(title);
                builder.setView(popupView);
                AlertDialog alert = builder.create();
                alert.getWindow().setLayout(400, 800);
                builder.show();
            }
        } else {
            errorCount++;
            dateSelectedByUser = "";
            failedAttempts++;
            StartNextTrialAttempt(true);
            View failurePopUpView = layoutInflater.inflate(R.layout.activity_failure, null);
            ShowSuccessFailureAlert(failurePopUpView);
        }
    }

    private void ShowSuccessFailureAlert(View view) {
        TextView textViewAttempts = findViewById(R.id.textViewAttempts);
        textViewAttempts.setText(successAttempts + " of " + totalAttempts + " completed");
        int toastDurationInMilliSeconds = 400;
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 400) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.cancel();
            }
        };
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        toastCountDown.start();
    }

    //show trial beginning popup
    private void ShowTrialBeginningPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        TextView title = new TextView(getApplicationContext());
        title.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Black));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.activity_begin_trial, null);
        TextView textViewTrial = popupView.findViewById(R.id.textViewTrial);
        textViewTrial.setText(popupMessage);
        title.setText(instructionsPopUpTitle + trialNumber + " Part " + partNumber);
        builder.setPositiveButton("START", (dialog, which) -> {

        });
        builder.setCustomTitle(title);
        builder.setView(popupView);
        AlertDialog alert = builder.create();
        alert.getWindow().setLayout(400, 800);
        builder.show();
    }

    //start new trial attempt
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void StartNextTrialAttempt(Boolean isFailure) {
        timeTaken = 0;
        noOfTaps = 0;
        dateSelectedByUser = "";
        if (!isFailure) {
            errorCount = 0;
            GenerateRandomDate();
        }
        BindCalendar();
    }

    // initializes the variables to be computed later.
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void InitializeVariables() {
        timeTaken = 0;
        GenerateRandomDate();
        dateSelectedByUser = "";
        dateToSelect = "";
        errorCount = 0;
        successAttempts = 0;
        failedAttempts = 0;
        totalAttemptsMadeByUser = 0;
        noOfTaps = 0;
        BindCalendar();
    }

    //generate random dates
    private void GenerateRandomDate() {
        if (dateEra.equals("Small"))
            dateToSelect = Common.GenerateRandomSmallDate(dateToSelect);
        else if (dateEra.equals("Medium"))
            dateToSelect = Common.GenerateRandomMediumDate(dateToSelect);
        else if (dateEra.equals("Large"))
            dateToSelect = Common.GenerateRandomLargeDate(dateToSelect);
        TextView textViewGivenDate = findViewById(R.id.textViewGivenDate);
        textViewGivenDate.setText(dateToSelect);
    }
}