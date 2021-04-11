package com.example.DateSelection;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class MonthListItem extends androidx.appcompat.widget.AppCompatTextView {
    // The name of the view
    private String name;

    public MonthListItem(Context context) {
        this(context, null);
    }

    public MonthListItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, 0);
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs,
                    R.styleable.CircleImageView);

            this.name = array.getString(R.styleable.CircleImageView_name);
            array.recycle();
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}