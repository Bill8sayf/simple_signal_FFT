package com.example.laba1koz;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PackageManagerCompat;

public class GraphActivity extends AppCompatActivity {

    private TextView view;
    private String text;

    private final String LOG_TAG = "GRAPH_ACTIVITY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);
//        Log.d(LOG_TAG, "GraphActivity onCreate");
        view = findViewById(R.id.testView);


        Log.d(LOG_TAG, "onCreate");

        Bundle bundle = getIntent().getExtras();

        Log.d(LOG_TAG, "BundleArrival");

        if (bundle == null) {
            Toast.makeText(this, "Нет данных", Toast.LENGTH_SHORT).show();

            Log.d(LOG_TAG, "BundleEmpty");
            finish();
            return;
        }

        text = String.valueOf(bundle.getInt("AMP"));

        view.setText(text);

        Log.d(LOG_TAG, "Text Printed");
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
