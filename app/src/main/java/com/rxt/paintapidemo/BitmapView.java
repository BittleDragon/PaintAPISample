package com.rxt.paintapidemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * Desc:图片显示view
 * Company: xuehai
 * Copyright: Copyright (c) 2018
 *
 * @author raoxuting
 * @since 2018/11/16 17/07
 */
public class BitmapView extends View {

    public static final String TAG = "BitmapView";

    Bitmap bitmap;
    Paint paint;

    public BitmapView(Context context) {
        this(context, null);
    }

    public BitmapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);

        getBitmapResource();
//        drawableToBitmap();
    }

    private void getBitmapResource() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.windmill);
        String pictureInfo = String.format(Locale.getDefault(),
                "图片尺寸：%d*%d，图片大小：%dkb",
                bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount());
        Log.d(TAG, pictureInfo);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                if (baos.size() != 0) {
//                    BitmapFactory.Options options1 = new BitmapFactory.Options();
                    Bitmap bitmap2 = BitmapFactory.decodeStream(new
                            ByteArrayInputStream(baos.toByteArray()));
                    pictureInfo = String.format(Locale.getDefault(),
                            "图片尺寸：%d*%d，图片大小：%dkb",
                            bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getByteCount());
                    Log.d(TAG, pictureInfo);
                }
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawableToBitmap() {
        Drawable drawable = getResources().getDrawable(R.mipmap.windmill);
        int originWidth = drawable.getIntrinsicWidth();
        int originHeight = drawable.getIntrinsicHeight();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
        } else {
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                    Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            bitmap = Bitmap.createBitmap(originWidth, originHeight, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.draw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }
}
