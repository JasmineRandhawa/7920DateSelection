package com.example.DateSelection;

import android.content.Context;
import android.util.AttributeSet;

// month list items class
public class MonthListItem extends androidx.appcompat.widget.AppCompatTextView {
    public MonthListItem(Context context) {
        this(context, null);
    }
    public MonthListItem(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }
}