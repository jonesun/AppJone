package com.jone.app.ui.activities;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jone.app.Constants;
import com.jone.app.R;
import com.jone.app.utils.FileUtils;
import com.jone.app.utils.PhotoUtils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class SelectPicPopupWindowActivity extends Activity implements OnClickListener {

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private LinearLayout layout;
    private Intent intent;
    private boolean isCut = false;

    private String cameraImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic_popup_window);
        intent = getIntent();
        setIsCut();
        btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

        layout = (LinearLayout) findViewById(R.id.pop_layout);

        // 添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
//                        Toast.LENGTH_SHORT).show();
            }
        });
        // 添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
    }

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    private void setIsCut(){
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            isCut = bundle.getBoolean("isCut", false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imagePath = null;
        switch (requestCode) {
            case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:
                if (data == null) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (data.getData() == null) {
                        return;
                    }
                    if (!FileUtils.isSdcardExist()) {
                        Toast.makeText(SelectPicPopupWindowActivity.this, "SD卡不可用,请检查", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Uri uri = data.getData();
                    String[] proj = { MediaStore.Images.Media.DATA };
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                            imagePath = cursor.getString(column_index);
                            if(isCut){
                                PhotoUtils.startPhotoZoom(SelectPicPopupWindowActivity.this, Uri.fromFile(FileUtils.createNewFile(imagePath)), 100);//裁切
                            }else {
                                goResult(imagePath);
                            }
                        }
                    }
                }
                break;
            case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    imagePath = cameraImagePath;
                    if(isCut){
                        PhotoUtils.startPhotoZoom(SelectPicPopupWindowActivity.this, Uri.fromFile(FileUtils.createNewFile(imagePath)), 100);//裁切
                    }else {
                        goResult(imagePath);
                    }
                }
                break;
            case PhotoUtils.PHOTO_REQUEST_CUT: //裁切后
                if (resultCode == RESULT_OK) {
                    imagePath = PhotoUtils.getCutPhotoPath(data);
                    intent.putExtra(Constants.IMAGE_PATH_KEY, imagePath);
                }
                goResult(imagePath);
                break;
        }
    }

    private void goResult(String imagePath){
        intent.putExtra(Constants.IMAGE_PATH_KEY, imagePath);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                cameraImagePath = PhotoUtils.takePicture(SelectPicPopupWindowActivity.this);
                Log.e("cameraImagePath", cameraImagePath);
                break;
            case R.id.btn_pick_photo:
                PhotoUtils.selectPhoto(SelectPicPopupWindowActivity.this);
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }

    }

}
