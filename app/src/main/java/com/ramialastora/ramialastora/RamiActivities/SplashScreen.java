package com.ramialastora.ramialastora.RamiActivities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ramialastora.ramialastora.R;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;

import java.util.Locale;

import static com.ramialastora.ramialastora.RamiActivities.RamiMain.hideStatus;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private AppSharedPreferences appSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*appSharedPreferences = new  AppSharedPreferences(SplashScreen.this);
        appSharedPreferences.writeString(Constants.applanguage, "ar");
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        String language = appSharedPreferences.readString(Constants.applanguage);
        conf.setLocale(new Locale(language));
        res.updateConfiguration(conf, dm);*/
//        LanguageHelper.changeLocale(this.getResources(), "ar", SplashScreen.this);

        setContentView(R.layout.activity_splash_screen);

        hideStatus(SplashScreen.this);

        logo = findViewById(R.id.logo);

        appSharedPreferences = new AppSharedPreferences(SplashScreen.this);




            Thread mythread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000);
                        startActivity(new Intent(getApplicationContext(), RamiMain.class));
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            mythread.start();

        Thread mythread1 = new Thread() {
            @Override
            public void run() {
                Animation animation = new AlphaAnimation(1, (float) 0.6); //to change visibility from visible to invisible
                animation.setDuration(1000);
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
                animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
                logo.startAnimation(animation); //to start animation
            }
        };
        mythread1.start();



    }
    public static void setLanguage(Context context) {
//        LanguageHelper.changeLocale(context.getResources(), "ar", context);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        String language = "ar";
        conf.setLocale(new Locale(language));
        res.updateConfiguration(conf, dm);
    }
}
