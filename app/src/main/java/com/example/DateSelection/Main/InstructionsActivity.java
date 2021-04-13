package com.example.DateSelection.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.DateSelection.R;
import com.example.DateSelection.Trials.Trial1_Old;

public class InstructionsActivity extends AppCompatActivity {

    static Data data;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        //intent of next screen when trial ends
        intent = new Intent(this, Trial1_Old.class);

        //initialize data
        data = (Data) (getIntent().getSerializableExtra("Data"));
    }

    public void redirectToTrial1Screen(View view) {
        intent.putExtra("Data", data);
        data = new Data();
        startActivity(intent);
    }
}