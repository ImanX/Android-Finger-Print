# FingerPrintProvider
Android Easy Finger Print Provider Authentication

- Need Library:
```Gradle
    compile 'com.github.imanx:fingerprintprovider:0.0.2'
 ```

###Example :
 ```Java
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
 ```

