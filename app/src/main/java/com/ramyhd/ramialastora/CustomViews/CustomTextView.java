package com.ramyhd.ramialastora.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.ramyhd.ramialastora.utils.AppSharedPreferences;


/**
 * Created by 12 on 01/03/2017.
 */

public class CustomTextView extends AppCompatTextView {
    AppSharedPreferences appSharedPreferences;

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public CustomTextView(Context context) {
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
