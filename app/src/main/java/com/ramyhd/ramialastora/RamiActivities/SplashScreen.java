package com.ramyhd.ramialastora.RamiActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ramyhd.ramialastora.R;
import com.ramyhd.ramialastora.interfaces.Constants;
import com.ramyhd.ramialastora.utils.LanguageHelper;

import static com.ramyhd.ramialastora.RamiActivities.RamiMain.hideStatus;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;
    private String newsId, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        hideStatus(SplashScreen.this);

        logo = findViewById(R.id.logo);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            newsId = bundle.getString("news_id");
            type = bundle.getString("type");
            Log.d(Constants.Log+"splash", "newsId = "+newsId);
        }
            Thread mythread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000);
                        if (newsId != null && type.equals("KEY_NEWS")){
                            Intent intent = new Intent(getApplicationContext(), RamiMain.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("data", "from_notification");
                            intent.putExtra("newsId", newsId);
                            startActivity(intent);
                        }else {
                            startActivity(new Intent(getApplicationContext(), RamiMain.class).setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            ));
                        }
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
        LanguageHelper.changeLocale(context.getResources(), "ar", context);
        /*Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        String language = "ar";
        conf.setLocale(new Locale(language));
        res.updateConfiguration(conf, dm);*/
    }
}
