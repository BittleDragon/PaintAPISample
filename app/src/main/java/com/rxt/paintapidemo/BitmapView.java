package com.rxt.paintapidemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        BitmapFactory.Options options = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.windmill, options);
        printPictureInfo(options);
        Log.d(TAG, "图片大小：" + bitmap.getByteCount());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                if (baos.size() != 0) {
                    BitmapFactory.Options options1 = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeStream(new
                            ByteArrayInputStream(baos.toByteArray()), null, options1);
                    printPictureInfo(options1);
                    Log.d(TAG, "图片大小：" + bitmap.getByteCount());
                    savePicture(baos);
                }
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    private void printPictureInfo(BitmapFactory.Options options) {
        Log.d(TAG, String.format(Locale.getDefault(),
                "图片尺寸：%d*%d",
                options.outWidth, options.outHeight));
    }

    private void savePicture(ByteArrayOutputStream baos) {
        String dstFilePath = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator +
                "windmill.jpg";
        Log.d(TAG, "路径: " + dstFilePath);
        File dstFile = new File(dstFilePath);
        if (dstFile.exists()) {
            dstFile.delete();
        }
        try {
            dstFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dstFile);
            fos.write(baos.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                double fileSize = dstFile.length() / 1024d;
                String format = String.format(Locale.getDefault(), "图片实际大小：%.2fkb", fileSize);
                Log.d(TAG, format);
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
