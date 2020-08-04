package com.ramyhd.ramyalastora.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.ramyhd.ramyalastora.interfaces.Constants;
import com.ramyhd.ramyalastora.utils.AppSharedPreferences;

/**
 * Created by 12 on 01/03/2017.
 */

public class CustomMainTextView extends AppCompatTextView {
    AppSharedPreferences appSharedPreferences;
    public CustomMainTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        appSharedPreferences=new AppSharedPreferences(context);
        init(context);
    }

    public CustomMainTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomMainTextView(Context context) {
        super(context);
        init(context);
    }
    public void init(Context context) {
        Typeface tf;

        appSharedPreferences=new AppSharedPreferences(context);

            if (appSharedPreferences.readString(Constants.language).equals("ar")) {
                tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/TheSans-Plain.ttf");
            } else {
                tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Nunito-Regular.ttf");
            }
            setTypeface(tf, 1);

    }

}
