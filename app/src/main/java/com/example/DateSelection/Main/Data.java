package com.example.DateSelection.Main;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    public List<Trial> Trials;

    public List<Trial> getTrials() {
        return Trials;
    }

    public void setTrials(List<Trial> trials) {
        Trials = trials;
    }

}
