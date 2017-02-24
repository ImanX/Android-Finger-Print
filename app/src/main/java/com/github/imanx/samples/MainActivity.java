package com.github.imanx.samples;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.imanx.fingerPrint.FingerPrint;
import com.github.imanx.fingerPrint.OnCallbackAuthenticationListener;
import com.github.imanx.sample.R;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "Finger Print";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FingerPrint fingerPrint = new FingerPrint(this);
        if (!fingerPrint.isSupportedDevice()) {
            return;
        }

        if (!fingerPrint.hasEnrolledFinger()) {
            return;
        }

        fingerPrint.start(new OnCallbackAuthenticationListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "Authentication Success");
            }

            @Override
            public void onFailed() {
                Log.i(TAG, "Authentication Failed");
            }
        });

    }

}
