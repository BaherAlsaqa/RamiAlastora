package com.ramialastora.ramialastora.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputEditText;
import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;

/**
 * Created by 12 on 01/03/2017.
 */

public class CustomEditText extends TextInputEditText {
    AppSharedPreferences appSharedPreferences;
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomEditText(Context context) {
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
