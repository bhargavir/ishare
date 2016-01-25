package com.cmpe277.sjsu.ishare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cmpe277.sjsu.ishare.animation.Shimmer;
import com.cmpe277.sjsu.ishare.animation.ShimmerTextView;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    ShimmerTextView tv;
    Shimmer shimmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv=(ShimmerTextView) findViewById(R.id.shimmer_tv);
        startAnimations();
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void startAnimations() {
        if (shimmer != null && shimmer.isAnimating()) {
            shimmer.cancel();
        } else {
            shimmer = new Shimmer();
            shimmer.start(tv);
        }

    }


}
