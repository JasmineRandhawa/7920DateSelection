package com.example.DateSelection.Main;

import java.io.Serializable;
import java.util.List;

public class Trial implements Serializable, Comparable<Trial> {
    private final int TrialNumber;
    private List<TrialAttempt> TrialAttempts;
    private final int TotalAttempts;
    private final String TrialType;
    private final int TotalSuccessfulAttempts;
    private final int TotalFailureAttempts;


    public Trial(int trialNumber, String trialType, List<TrialAttempt> trialAttempts, int totalAttempts, int totalSuccessfulAttempts,
                 int totalFailureAttempts) {
        this.TrialNumber = trialNumber;
        this.TrialAttempts = trialAttempts;
        this.TrialType = trialType;
        this.TotalAttempts = totalAttempts;
        this.TotalSuccessfulAttempts = totalSuccessfulAttempts;
        this.TotalFailureAttempts = totalFailureAttempts;
    }

    public int getTrialNumber() {
        return TrialNumber;
    }

    public List<TrialAttempt> getTrialAttempts() {
        return TrialAttempts;
    }

    public void setTrialAttempts(List<TrialAttempt>  trialAttempts) {
        this.TrialAttempts = trialAttempts;
    }

    public String getTrialType() {
        return TrialType;
    }

    @Override
    public int compareTo(Trial trial) {
        return (new Integer(this.getTrialNumber())).compareTo(trial.getTrialNumber());
    }

}
