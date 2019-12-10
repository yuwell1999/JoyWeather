package com.example.expriement3;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.Preference;
import android.util.Log;
import android.view.View;

import androidx.core.content.FileProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class method {

    public static String getunit(Context context){
        SharedPreferences pref=context.getSharedPreferences("data", context.MODE_PRIVATE);
        String info=pref.getString("weatherunit","");
        if(info.equals("英制"))
            return "i";
        else
            return "m";
    }

    public static void addcitys(Context context,String newcity){
        SharedPreferences pref=context.getSharedPreferences("data", context.MODE_PRIVATE);
        String oldcitys=pref.getString("citys","");
        String newcitys=oldcitys+newcity+",";
        SharedPreferences.Editor  editor=pref.edit();
        editor.putString("citys",newcitys);
        editor.commit();
    }

    public static String getcitys(Context context){
        SharedPreferences pref=context.getSharedPreferences("data", context.MODE_PRIVATE);
        String citys=pref.getString("citys","");
        return citys;
    }

    public static void storenowcity(Context context,String city){
        SharedPreferences pref=context.getSharedPreferences("data", context.MODE_PRIVATE);
        SharedPreferences.Editor  editor=pref.edit();
        editor.putString("nowcity",city);
        editor.commit();
    }

    public static String getnowcity(Context context){
        SharedPreferences pref=context.getSharedPreferences("data", context.MODE_PRIVATE);
        String city=pref.getString("nowcity","");
        return city;
    }
    public static int getResource(String imageName,Context ctx){
        Activity activity=(Activity)ctx;
        int resId = activity.getResources().getIdentifier(imageName, "mipmap", ctx.getPackageName());
        return  resId;
    }

    public static Bitmap getBitmapByView(View headerView) {
        int h = headerView.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(headerView.getWidth(), h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        headerView.draw(canvas);
        return bitmap;
    }

    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 10, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 400) {  //循环判断如果压缩后图片是否大于400kb,大于继续压缩（这里可以设置大些）
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static File bitMap2File(Bitmap bitmap) {
        String path = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            path = Environment.getExternalStorageDirectory() + File.separator;//保存到sd根目录下
        }
        //        File f = new File(path, System.currentTimeMillis() + ".jpg");
        File f = new File(path, "share" + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return f;
        }
    }

    public static void share(View view, Context activity) {
        Bitmap bp = getBitmapByView(view);
        Bitmap bpend = compressImage(bp);
        bp.recycle();
        File file = bitMap2File(bpend);
        bpend.recycle();
        if (file != null && file.exists() && file.isFile()) {//由文件得到uri
            Uri imageUri;//= Uri.fromFile(file);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(activity, "com.example.guannanyang.yapplication.fileProvider", file);
            } else {
                imageUri = Uri.fromFile(file);
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            activity.startActivity(Intent.createChooser(shareIntent, "分享图片"));
        }
    }
}