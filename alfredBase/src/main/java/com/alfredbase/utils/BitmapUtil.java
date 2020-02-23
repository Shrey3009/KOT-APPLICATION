package com.alfredbase.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.RGB_565);
        view.draw(new Canvas(bitmap));

        return bitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleWidth);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static Bitmap rotate(int rotate, float scale, Bitmap img) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        matrix.postScale(scale, scale);
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    public static Bitmap getResizedBitmap(Bitmap bigimage, float scale) {
        // 获取这个图片的宽和高
        int width = bigimage.getWidth();
        int height = bigimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scale, scale);
        Bitmap bitmap = Bitmap.createBitmap(bigimage, 0, 0, width, height, matrix, true);
        return bitmap;
    }


    public static Bitmap getTableBitmap(int rotate, int scale, Bitmap img) {
        Matrix matrix = new Matrix();
        if (rotate != 0)
            matrix.postRotate(rotate);

        switch (scale) {
            case 1:
                matrix.postScale((float) (2.0 / 3), (float) (2.0 / 3));
                break;
            case 2:
                matrix.postScale((float) (5.0 / 6), (float) (5.0 / 6));
                break;
            case 3:
                matrix.postScale(1, 1);
                break;
        }

        int width = 0;
        try {
            width = img.getWidth();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        int height = 0;
        try {
            height = img.getHeight();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (height == 0 || width == 0) {
            return null;
        }
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    public static Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }

    public static void saveImageToGallery(String imageName, Context context, Bitmap bmp) {
        // 首先保存图片 - Saving Images
        File appDir = new File(Environment.getExternalStorageDirectory(), "C360Engage");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = imageName + System.currentTimeMillis() +".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库 - Putting the image into gallery
        try
        {
            String path = file.getAbsolutePath();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis()); // DATE HERE
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Files.FileColumns.DATA, path);

            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

//        MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新 - gallery notification
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(appDir)));
    }
}
