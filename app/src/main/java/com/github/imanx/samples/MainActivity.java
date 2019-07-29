package com.github.imanx.samples;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.imanx.fingerPrint.FingerPrint;
import com.github.imanx.fingerPrint.OnCallbackAuthenticationListener;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "FingerPrint";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView txt = findViewById(R.id.txt);


        FingerPrint fingerPrint = new FingerPrint(this);
        if (!fingerPrint.isSupportedDevice()) {
            txt.setText("not support device");
            return;
        }

        if (!fingerPrint.hasEnrolledFinger()) {
            txt.setText("has not Enrolled Finger");
            return;
        }

        fingerPrint.start(new OnCallbackAuthenticationListener() {
            @Override
            public void onSuccess() {
                txt.setText("Authentication Success");
            }

            @Override
            public void onFailed() {
                txt.setText("Authentication Failed");
            }
        });

    }

}
