package com.jone.app.ui.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jone.app.Constants;
import com.jone.app.R;
import com.jone.app.utils.PhotoUtils;

public class ImageShowerActivity extends Activity {
    private ImageShowerActivity activity;
    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_shower);
        activity = this;
        ImageView img = (ImageView)findViewById(R.id.large_image);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            imagePath = bundle.getString(Constants.IMAGE_PATH_KEY);
            if(imagePath != null){
                Bitmap bitmap = PhotoUtils.getBitmapFromFile(imagePath);
                img.setImageBitmap(bitmap);
            }
        }

        Button btnCLose = (Button) findViewById(R.id.btnCLose);
        btnCLose.setOnClickListener(new View.OnClickListener() { // 点击返回
            public void onClick(View paramView) {
                System.out.println("点击了关闭按钮");
                activity.finish();
            }
        });
    }
}
