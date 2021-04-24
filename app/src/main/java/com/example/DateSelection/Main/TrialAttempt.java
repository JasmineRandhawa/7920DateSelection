package com.example.DateSelection.Main;

import java.io.Serializable;

public class TrialAttempt implements Serializable {

    private long TimeInMillis;
    private int AttemptNo;
    private int YearsAgo;
    private int NoOfTaps;
    private int ErrorCount;

    //constructor for TrialAttempt class
    public TrialAttempt(int attemptNo, int yearsAgo,long timeInMillis, int noOfTaps, int errorCount) {
        this.TimeInMillis = timeInMillis;
        this.YearsAgo = yearsAgo;
        this.NoOfTaps = noOfTaps;
        this.ErrorCount = errorCount;
        this.AttemptNo = attemptNo;
    }

    // Getters and Setters for TrialAttempt class fields
    public long getTimeInMillis() {
        return TimeInMillis;
    }

    public int getNoOfTaps() {
        return NoOfTaps;
    }

    public int getErrorCount() {
        return ErrorCount;
    }

    public int getYearsAgo() {
        return YearsAgo;
    }

}
