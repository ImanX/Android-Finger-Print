package com.github.imanx.fingerPrint;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresPermission;


import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Android Finger Print
 * Created by ImanX on 9/25/16.
 * Copyright Alireza Tarazani All Rights Reserved.
 */
@TargetApi(Build.VERSION_CODES.M)
public class FingerPrint extends FingerprintManager.AuthenticationCallback {

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";


    private String                           applicationPackageName;
    private CancellationSignal               cancellationSignal;
    private KeyStore                         keyStore;
    private FingerprintManager               fingerprintManager;
    private Context                          context;
    private Cipher                           cipher;
    private OnCallbackAuthenticationListener listener;

    public FingerPrint(Context context) {
        this.context = context;
        this.applicationPackageName = context.getPackageName();
        this.fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresPermission(anyOf = {Manifest.permission.USE_FINGERPRINT})
    public void start(OnCallbackAuthenticationListener listener) {


        if (!isGrantedFingerPrintPermission()) {
            return;
        }

        this.listener = listener;
        this.cancellationSignal = new CancellationSignal();


        if (isCipherCreate()) {
            this.fingerprintManager.authenticate(
                    new FingerprintManager.CryptoObject(this.cipher),
                    this.cancellationSignal, 0,
                    this,
                    null
            );

        }

    }

    public void stop() {
        this.cancellationSignal.cancel();
        this.listener = null;
    }

    public boolean isSupportedDevice() {

        if (!isGrantedFingerPrintPermission()) {
            return false;
        }

        return this.fingerprintManager.isHardwareDetected();
    }


    public boolean hasEnrolledFinger() {

        if (!isGrantedFingerPrintPermission()) {
            return false;
        }

        return this.fingerprintManager.hasEnrolledFingerprints();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isGrantedFingerPrintPermission() {
        return context.checkSelfPermission(Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED;

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {

        try {
            this.keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
            this.keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(
                    applicationPackageName,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isCipherCreate() {
        try {
            this.generateKey();
            String transformation = (KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);

            this.cipher = Cipher.getInstance(transformation);
            this.keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(applicationPackageName, null);
            this.cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);

    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();

        if (!this.cancellationSignal.isCanceled()) {
            this.listener.onFailed();
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        if (!this.cancellationSignal.isCanceled()) {
            this.listener.onSuccess();
        }

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
    }
}
