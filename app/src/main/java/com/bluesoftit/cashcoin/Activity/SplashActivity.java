package com.bluesoftit.cashcoin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.bluesoftit.cashcoin.R;
import com.startapp.sdk.adsbase.StartAppAd;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private int setProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        StartAppAd.disableSplash();
        progressBar =findViewById(R.id.progressBar);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
            }
        });

        thread.start();

    }

    private void startApp() {
        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
        finish();
    }

    

    private void doWork() {
        for (setProgress=20;setProgress<=100;setProgress=setProgress+20){
            try {
                Thread.sleep(1000);
                progressBar.setProgress(setProgress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}