package ir.imansoft.ocr.dictionary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.imanx.fingerPrint.FingerPrintProvider;
import com.github.imanx.fingerPrint.OnCallbackFingerPrintListener;


public class SampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FingerPrintProvider fingerPrintProvider = new FingerPrintProvider(getApplicationContext());


        if (!fingerPrintProvider.isSupportDevice()) {
            //NOTE: Device not Supported FingerPrint.
            return;
        }

        if (!fingerPrintProvider.hasRegisterFingerPrint()) {
            //NOTE: Device supported FingerPrint but user not registered yourself FingerPrint.
            return;
        }


        fingerPrintProvider.startAuthenticate(new OnCallbackFingerPrintListener() {
            @Override
            public void onAuthenticateSuccess() {
                Log.i("TAG Finger Print", "Success Authentication");
            }

            @Override
            public void onAuthenticateFailure(int errCode) {
                Log.i("TAG Finger Print", "Failure Authentication");
            }
        });
    }
}
