package com.monowealth.monowealth;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class RegularButton extends AppCompatButton {

    Typeface quicksand;

    public RegularButton(Context context) {
        super(context);
        init();
    }

    public RegularButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RegularButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        quicksand = Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Regular.ttf");
        setTypeface(quicksand);
    }
}
