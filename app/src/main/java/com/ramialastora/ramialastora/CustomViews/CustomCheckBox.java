package com.ramialastora.ramialastora.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;

/**
 * Created by 12 on 01/03/2017.
 */

public class CustomCheckBox extends AppCompatCheckBox {
    AppSharedPreferences appSharedPreferences;
    public CustomCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomCheckBox(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        Typeface tf;

        appSharedPreferences=new AppSharedPreferences(context);
        if(appSharedPreferences.readString(Constants.language).equals("ar")){
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/TheSans-Plain.ttf");
        }else {
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Nunito-Regular.ttf");
        }
        setTypeface(tf ,1);

    }
}
