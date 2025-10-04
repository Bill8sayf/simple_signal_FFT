package com.example.laba1koz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        try {
            Bundle lonelyBundle = new Bundle();

            int amp = InputValidator.validateInteger(EditAmp.getText().toString(), "AMP", -100, 100);
            int freq = InputValidator.validateInteger(EditFreq.getText().toString(), "FREQ", 0, 100000);
            int phase = InputValidator.validateInteger(EditPhase.getText().toString(), "PHASE");
            //int disFreq = InputValidator.validateInteger(EditDisFreq.getText().toString(), "DISFREQ",-1000000,1000000);
            //int dotNum = InputValidator.validateInteger(EditDotNum.getText().toString(), "DOTNUM",0,1000);


            lonelyBundle.putInt("AMP", amp);
            lonelyBundle.putInt("FREQ", freq);
            lonelyBundle.putInt("PHASE", phase);

            float timePeriod;
            int disFreq;
            int dotNum = 2028;

            if (freq != 0) {
                timePeriod = 3.0f / freq;
                disFreq = (int)(dotNum / timePeriod);
            } else {
                timePeriod = 3.0f;
                disFreq = dotNum / 3;
            }

            if (disFreq == 0) disFreq = 1;

            lonelyBundle.putInt("DISFREQ", disFreq);
            lonelyBundle.putInt("DOTNUM", dotNum);
            lonelyBundle.putFloat("TIME_PERIOD", timePeriod);

            Log.d(LOG_TAG, String.format("Параметры: amp=%d, freq=%dГц, time=%.3fсек, disFreq=%dГц, dots=%d", amp, freq, timePeriod, disFreq, dotNum));

            Log.d(LOG_TAG, "Bundle Formed");

            Intent intent = new Intent(this, GraphActivity.class);
            intent.putExtras(lonelyBundle);
            Log.d(LOG_TAG, "Intent Formed");

            startActivity(intent);
            Log.d(LOG_TAG, "Intent Started");

        } catch (
                ValidationException e) {
            showErrorMessage(e.getMessage());
            Log.e(LOG_TAG, "Validation error: " + e.getMessage());
        } catch (
                Exception e) {
            showErrorMessage("Произошла непредвиденная ошибка");
            Log.e(LOG_TAG, "Unexpected error: ", e);
        }
    }

    private void showErrorMessage(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button1);

        EditAmp = findViewById(R.id.ETXT_Amp);
        EditFreq = findViewById(R.id.ETXT_Freq);
        EditPhase = findViewById(R.id.ETXT_Phase);
        //EditDisFreq = findViewById(R.id.ETXT_DisFreq);
        //EditDotNum = findViewById(R.id.ETXT_DotsNum);


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
