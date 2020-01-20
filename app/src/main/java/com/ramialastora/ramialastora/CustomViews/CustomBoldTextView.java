package com.ramialastora.ramialastora.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.SyncStateContract;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.ramialastora.ramialastora.interfaces.Constants;
import com.ramialastora.ramialastora.utils.AppSharedPreferences;


/**
 * Created by 12 on 01/03/2017.
 */

public class CustomBoldTextView extends AppCompatTextView {
    AppSharedPreferences appSharedPreferences;

    public CustomBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public CustomBoldTextView(Context context) {
        super(context);

        init(context);
    }

    public void init(Context context) {
        Typeface tf;
        appSharedPreferences = new AppSharedPreferences(context);

        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/thesans_bold.ttf");
        setTypeface(tf, 1);

    }


}
