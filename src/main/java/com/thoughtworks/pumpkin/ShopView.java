package com.thoughtworks.pumpkin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class ShopView extends View {
    private Context context;

    public ShopView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint light = new Paint();
        light.setColor(Color.parseColor(getResources().getString(R.color.pumpkinColorLight)));
        Paint dark = new Paint();
        dark.setColor(Color.parseColor(getResources().getString(Color.BLUE)));
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        canvas.drawRect(new Rect(10, 10, displayMetrics.widthPixels - 10, displayMetrics.heightPixels - (actionBarHeight() + 10)), light);
        canvas.drawRect(new Rect(100, 100, 200, 200), dark);
    }

    private int actionBarHeight() {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return getResources().getDimensionPixelSize(tv.resourceId);
    }
}
