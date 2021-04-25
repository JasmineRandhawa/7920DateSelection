package com.example.DateSelection.Trials;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
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
import com.example.DateSelection.Main.Trial;
import com.example.DateSelection.Main.TrialAttempt;
import com.example.DateSelection.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/*
.Date Selection
*/

//This is trial 1 screen for combination small date : old design
public class Trial1_Old extends AppCompatActivity {

    //trial specific variables
    final static int trialNumber = 1; // trial number
    final static int partNumber = 1; // part number
    final static boolean isFirstTrialActivity = true; //is its first trial activity to launch
    final static boolean isLastTrialActivity = false; // is it last trial activity
    final static String designType = "Old"; // trial type
    final static String dateEra = "Small";
    final static String designTypeStr = "Old Design"; // is it old design or new design
    final static String trialTask = " - Date Selection"; // trial task description
    final static String popupMessage = "You are about to use " + designTypeStr + " for " + trialTask + ".";
    final static String instructionsPopUpTitle = "Instructions - Trial "; // trial instructions popup title
    final static String trialCompletePopUpTitle = "Success"; // trial success popup titile
    final static String trialCompletionPopupMessage = "You have successfully completed the Trial " + trialNumber +
            (partNumber == 1 ? (" Part " + partNumber) : "");
    final static String popUButtonText = (isLastTrialActivity ? "EMAIL DATA" : ("GO TO " +
            (partNumber == 1 ? "PART 2" : "NEXT TRIAL"))); // popup button text
    @LayoutRes
    final static int layout = R.layout.activity_old_date_selection; // trial layout

    //carry over data
    static Data data;
    static List<Trial> trials = new ArrayList<>();
    static List<TrialAttempt> listAttempts;

    //computed variables
    final Context context = Trial1_Old.this;  //context of current screen
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    // called on load of trial screen
    protected void onCreate(Bundle savedInstanceState) {

        // map Screen to UI
        super.onCreate(savedInstanceState);
        setContentView(layout);

        nextScreenIntent = new Intent(this, Trial1_New.class);

        TextView textViewTrialNumber = (TextView) findViewById(R.id.textViewTrialNumber);
        textViewTrialNumber.setText("Trial " + trialNumber + " : Part " + partNumber);
        TextView textViewTrialName = (TextView) findViewById(R.id.textViewTrialName);
        textViewTrialName.setText(trialTask + " : " + designType);
        textViewSelectedDate = (TextView) findViewById(R.id.textViewSelectedDate);

        //initialize variable to be computed later.
        InitializeVariables();

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
    private void BindCalendar() {
        selectedDate="";
        textViewSelectedDate.setText("");
        CalendarView calendarView = findViewById(R.id.calView);
        LinearLayout calLayout = findViewById(R.id.calLayout);
        ImageButton calButton =  findViewById(R.id.calButton);
        Button okButton = (Button) findViewById(R.id.okButton);
        calendarView.setVisibility(View.GONE);
        okButton.setVisibility(View.GONE);
        calButton.setVisibility(View.VISIBLE);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            noOfTaps++;
            endTimeInMillis = Calendar.getInstance().getTimeInMillis();
            String dayStr = dayOfMonth<=9?"0"+dayOfMonth:dayOfMonth+"";
            selectedDate = dayStr +"-" +Common.GetMonthName(month+1)+"-"+year;
            String selectedDateStr = dayStr+"-"+(month+1)+"-"+year;
            textViewSelectedDate.setText(Common.GetDayOFWeek(selectedDateStr)+" "+selectedDate);
        });

        calButton.setOnClickListener(view -> {
           calendarView.setVisibility(View.VISIBLE);
           okButton.setVisibility(View.VISIBLE);
           calButton.setVisibility(View.GONE);
           Calendar calendar = Calendar.getInstance();
           calendar.setTimeInMillis(System.currentTimeMillis());
           calendarView.setDate(System.currentTimeMillis());
           String dayStr = calendar.get(Calendar.DAY_OF_MONTH) <=9?("0"+calendar.get(Calendar.DAY_OF_MONTH)):calendar.get(Calendar.DAY_OF_MONTH)+"";
           selectedDate = dayStr+"-"+
                   Common.GetMonthName(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
           String selectedDateStr = dayStr+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
           textViewSelectedDate.setText(Common.GetDayOFWeek(selectedDateStr)+" "+selectedDate);
           startTimeInMillis = Calendar.getInstance().getTimeInMillis();
        });

        okButton.setOnClickListener(view -> {
            dateSelectedByUser = selectedDate;
            textViewSelectedDate.setText("");
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
            calendarView.setVisibility(View.GONE);
            calButton.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.GONE);
        });
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
                int yearDiff =    Common.GetYearDiff(dateToSelect);
                int monthDiff =Common.GetMonthDiff(dateToSelect,  yearDiff);
                noOfTaps = noOfTaps + monthDiff;
                listAttempts.add(new TrialAttempt(successAttempts,
                        yearDiff,timeTaken, noOfTaps, errorCount));
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

                builder.setPositiveButton(popUButtonText, (dialog, which) -> {
                    // save data in list
                    int yearDiff =    Common.GetYearDiff(dateToSelect);
                    int monthDiff =Common.GetMonthDiff(dateToSelect,  yearDiff);
                    noOfTaps = noOfTaps + monthDiff;
                    listAttempts.add(new TrialAttempt(successAttempts,
                            yearDiff,timeTaken, noOfTaps, errorCount));
                    if (!isFirstTrialActivity) {

                        Trial trial = new Trial(trialNumber, designType, new ArrayList<>(),
                                totalAttemptsMadeByUser, successAttempts, failedAttempts);
                        trial.setTrialAttempts(listAttempts);
                        trials.add(trial);
                        data.setTrials(trials);
                        nextScreenIntent.putExtra("Data", data);
                        trials = new ArrayList<>();
                    } else {
                        List<Trial> trials = new ArrayList<>();
                        Trial trial = new Trial(trialNumber, designType, new ArrayList<>(),
                                totalAttemptsMadeByUser, successAttempts, failedAttempts);
                        trial.setTrialAttempts(listAttempts);
                        trials.add(trial);
                        data.setTrials(trials);
                        nextScreenIntent.putExtra("Data", data);
                    }
                    data = new Data();
                    listAttempts = new ArrayList<>();
                    startActivity(nextScreenIntent);
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