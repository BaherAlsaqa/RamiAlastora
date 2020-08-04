package com.ramyhd.ramyalastora.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.ramyhd.ramyalastora.utils.AppSharedPreferences;

/**
 * Created by 12 on 01/03/2017.
 */

public class CustomTitleTextView extends AppCompatTextView {
    AppSharedPreferences appSharedPreferences;

    public CustomTitleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomTitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTitleTextView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        Typeface tf;
        appSharedPreferences = new AppSharedPreferences(context);

        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/TheSans-Plain.ttf");
        setTypeface(tf, 1);

    }

}
