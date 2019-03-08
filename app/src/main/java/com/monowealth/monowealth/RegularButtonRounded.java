package com.monowealth.monowealth;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class RegularButtonRounded extends AppCompatButton {

    Typeface quicksand;

    public RegularButtonRounded(Context context) {
        super(context);
        init(context);
    }

    public RegularButtonRounded(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RegularButtonRounded(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        quicksand = Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Regular.ttf");
        setTypeface(quicksand);
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(context.getResources().getDisplayMetrics().widthPixels / 13);
        shape.setColor(Color.WHITE);
        setBackground(shape);
    }
}
