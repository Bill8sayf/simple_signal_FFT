package com.example.laba1koz;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PackageManagerCompat;

public class GraphActivity extends AppCompatActivity {

    private GraphView graphView;
    private String text;
    private Button toggleButton;

    private final String LOG_TAG = "GRAPH_ACTIVITY";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);
        Log.d(LOG_TAG, "onCreate");

        graphView = findViewById(R.id.sin_graphView);
        toggleButton = findViewById(R.id.toggleButton);

        Bundle bundle = getIntent().getExtras();

        Log.d(LOG_TAG, "BundleArrival");

        if (bundle == null) {
            Toast.makeText(this, "Нет данных", Toast.LENGTH_SHORT).show();

            Log.d(LOG_TAG, "BundleEmpty");
            finish();
            return;
        }

        int amp = bundle.getInt("AMP");
        int freq = bundle.getInt("FREQ");
        int phase = bundle.getInt("PHASE");
        int disFreq = bundle.getInt("DISFREQ");
        int dotNum = bundle.getInt("DOTNUM");
        float timePeriod = bundle.getFloat("TIME_PERIOD", 1.0f);

        Log.d(LOG_TAG, "Получены параметры: AMP=" + amp + ", FREQ=" + freq);

        graphView.setData(amp, freq, phase, disFreq, dotNum, timePeriod);

        toggleButton.setOnClickListener(v -> {
            graphView.toggleView();

            if (toggleButton.getText().toString().equals("Показать спектр")) {
                toggleButton.setText("Показать сигнал");
            } else {
                toggleButton.setText("Показать спектр");
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
