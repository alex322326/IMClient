package com.example.administrator.loadbitmapdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //加载图片会解析图片的所有像素的颜色信息，把颜色信息保存在内存中
//    	Bitmap bm = BitmapFactory.decodeFile("sdcard/dog.jpg");

        BitmapFactory.Options opts = new BitmapFactory.Options();
        //opts类中封装了很多成员属性，在加载图片时可以作为参数使用
        //不解析像素的颜色信息，只解析宽高
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile("sdcard/abc.jpg", opts);
        //获取图片的宽高
        int imageWidth = opts.outWidth;
        int imageHeight = opts.outHeight;
        //获取屏幕的宽高
        Display dp = getWindowManager().getDefaultDisplay();
        int screenWidth = dp.getWidth();
        int screenHeight = dp.getHeight();

        //计算缩小的比例
        int scale = 1;
        int scaleWidth = imageWidth / screenWidth;
        int scaleHeight = imageHeight / screenHeight;

        if(scaleWidth >= scaleHeight && scaleWidth > 0){
            scale = scaleWidth;
        }
        else if(scaleHeight > scaleWidth && scaleHeight > 0){
            scale = scaleHeight;
        }

        //再次加载图片，先缩小，后加载
        //指定缩小比例
        opts.inSampleSize = scale;
        opts.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile("sdcard/abc.jpg", opts);

        ImageView iv = (ImageView) findViewById(R.id.iv);

        iv.setImageBitmap(bm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
