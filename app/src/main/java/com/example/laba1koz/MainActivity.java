package com.example.laba1koz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText EditAmp;
    private EditText EditFreq;
    private EditText EditPhase;
    private EditText EditDisFreq;
    private EditText EditDotNum;

    private final String LOG_TAG = "MAIN_ACTIVITY";

    private void BundleSending() {
        Bundle lonelyBundle = new Bundle();

        lonelyBundle.putInt("AMP", InputValidator.validateInteger(EditAmp.getText().toString(), "AMP"));
        lonelyBundle.putInt("FREQ", InputValidator.validateInteger(EditFreq.getText().toString(), "FREQ"));
        lonelyBundle.putInt("PHASE", InputValidator.validateInteger(EditPhase.getText().toString(), "PHASE"));
        lonelyBundle.putInt("DISFREQ", InputValidator.validateInteger(EditDisFreq.getText().toString(), "DISFREQ"));
        lonelyBundle.putInt("DOTNUM", InputValidator.validateInteger(EditDotNum.getText().toString(), "DOTNUM"));

        Log.d(LOG_TAG, "Bundle Formed");

        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtras(lonelyBundle);

        Log.d(LOG_TAG, "Intent Formed");

        startActivity(intent);

        Log.d(LOG_TAG, "Intent Started");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button1);

        EditAmp = findViewById(R.id.ETXT_Amp);
        EditFreq = findViewById(R.id.ETXT_Freq);
        EditPhase = findViewById(R.id.ETXT_Phase);
        EditDisFreq = findViewById(R.id.ETXT_DisFreq);
        EditDotNum = findViewById(R.id.ETXT_DotsNum);


        button.setOnClickListener(v -> {
            Log.d(LOG_TAG, "Button Clicked");

            BundleSending();
        });

        Log.d(LOG_TAG, "MainActivity onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "MainActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "MainActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(LOG_TAG, "MainActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, "MainActivity onDestroy");
    }
}
