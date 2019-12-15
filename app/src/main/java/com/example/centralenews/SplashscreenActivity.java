package com.example.centralenews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class SplashscreenActivity extends Activity {

    private String language = Locale.getDefault().getLanguage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String sources = null;
                fetchSources process = new fetchSources();
                try {
                    sources = process.execute(language).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                intent.putExtra("sources", sources);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
