package com.inspireuser.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;

public class MainActivity extends AppCompatActivity {
    InAppUpdateManager inAppUpdateManager;
    private static int SPLASH_TIME_OUT =1600;
    public static final String TAG="MyTag";
   private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo=(ImageView)findViewById(R.id.logo);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this, com.inspireuser.app.MainActivity2.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.animation);
        logo.startAnimation(myanim);
    }


}
