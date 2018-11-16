package com.rxt.paintapidemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Desc:图片显示view
 * Company: xuehai
 * Copyright: Copyright (c) 2018
 *
 * @author raoxuting
 * @since 2018/11/16 17/07
 */
public class BitmapView extends View {


    public BitmapView(Context context) {
        super(context);
        Bitmap bitmap;
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.windmill);
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
        } else {

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
