# FingerPrint
Android Easy Finger Print

- Need Library:
```Gradle
    compile 'com.github.imanx:fingerprintprovider:0.0.5'
 ```

###Example :
 ```Java

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
 ```

