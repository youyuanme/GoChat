package com.sibozn.gochat.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.sibozn.gochat.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_guidance);
        gotoYindaoye();
    }

    @SuppressLint("NewApi")
    public static void hideSystemUI(View view) {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @SuppressLint("NewApi")
    public static void showSystemUI(View view) {
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void gotoYindaoye() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                String token = getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("token", "");
                if (TextUtils.isEmpty(token)) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

}
