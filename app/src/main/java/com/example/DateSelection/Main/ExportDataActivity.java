package com.example.DateSelection.Main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.DateSelection.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExportDataActivity extends AppCompatActivity {

    private ClipboardManager myClipboard;
    private ClipData myClip;
    static Data data;
    static List<Trial> trials = new ArrayList<Trial>();
    static String csvData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_data);

        data = (Data) (getIntent().getSerializableExtra("Data"));
        trials.addAll(data.getTrials());

        csvData = GenerateStringData(trials);
        String textViewData = GenerateStringDataForView(trials);
        TextView txtData = findViewById(R.id.txtData);
        txtData.setText(textViewData);
        Button buttonCopy = (Button) findViewById(R.id.buttonCopy);
        Button buttonEmail = (Button) findViewById(R.id.buttonEmail);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        buttonCopy.setOnClickListener(v -> {
            String text = txtData.getText().toString();
            myClip = ClipData.  newPlainText("text", text);
            myClipboard.setPrimaryClip(myClip);
            Toast.makeText(getApplicationContext(), "Text Copied",
                    Toast.LENGTH_SHORT).show();
        });

        buttonEmail.setOnClickListener(v -> sendEmail("jasminerandhawa05@gmail.com"));

        sendEmail("jasminerandhawa05@gmail.com");
    }

     // convert data to string
    private String GenerateStringData(List<Trial> trials) {
        Collections.sort(trials);
        csvData = "";
        csvData = csvData + "Trialno\tDesignType\tYearsAgo\tAttemptNo\tNoOfTaps\tTime\tError\t\n";
        for (Trial trial : trials) {
            List<TrialAttempt> listAttempts = new ArrayList<>();
            listAttempts.addAll(trial.getTrialAttempts());
            for (int index = 0; index < listAttempts.size(); index++) {
                String attempNumber=  (index + 1)+"";
                csvData = csvData + trial.getTrialNumber() + "\t"
                        + trial.getTrialType() + "\t"
                        + listAttempts.get(index).getYearsAgo() + "\t"
                        + attempNumber + "\t"
                        + listAttempts.get(index).getNoOfTaps() + "\t"
                        + listAttempts.get(index).getTimeInMillis() + "\t"
                        + listAttempts.get(index).getErrorCount()+ "\t"
                        + "\n";
            }
            csvData = csvData+ "\n\n";
        }
        return csvData;
    }

    // convert data to string for display purpose
    private String GenerateStringDataForView(List<Trial> trials) {
        Collections.sort(trials);
        String txtViewData = "";
        for (Trial trial : trials) {
            txtViewData = txtViewData + "--------------------------------------------------------------------------------------\n";
            txtViewData = txtViewData + "TrialNo\t\t\t\t\tDesignType\t\t\t\tYearsAgo\t\t\tAttemptNo\t\t\tNoOfTaps\tTime\t\t\t\tError\t\t\t\n";
            List<TrialAttempt> listAttempts = new ArrayList<>();
            listAttempts.addAll(trial.getTrialAttempts());
            for (int index = 0; index < listAttempts.size(); index++) {
                String attempNumber = (index + 1) + "";
                if(Integer.parseInt(attempNumber) <10)
                    attempNumber = attempNumber +" ";
                txtViewData = txtViewData + trial.getTrialNumber() + "\t\t\t\t\t\t\t\t\t"
                        + trial.getTrialType() + "\t\t\t"
                        + listAttempts.get(index).getYearsAgo() + "\t\t\t\t\t\t\t\t"
                        + attempNumber + "\t\t\t\t\t\t\t\t\t"
                        + listAttempts.get(index).getNoOfTaps() + "\t\t\t\t\t\t\t\t"
                        + listAttempts.get(index).getTimeInMillis() + "\t\t\t\t\t\t"
                        + listAttempts.get(index).getErrorCount()+ "\t\t\t\t\t\t\t"
                        + "\n\n";
            }
            txtViewData = txtViewData + "--------------------------------------------------------------------------------------\n\n";
        }

        return txtViewData;
    }

    //Method to email data
    private void sendEmail(String receipientEmail) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/text");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pass Code Data");
        String to[] = {receipientEmail};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_TEXT, csvData);
        startActivityForResult(Intent.createChooser(intent, "Choose an Email client :"), 800);
    }
}


