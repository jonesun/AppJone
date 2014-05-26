package com.jone.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jone.app.Constants;
import com.jone.app.R;
import com.jone.app.utils.FileUtils;
import com.jone.app.utils.PhotoUtils;

public class SelectVideoPopupWindowActivity extends Activity implements View.OnClickListener{

    private Button btn_take_video, btn_pick_video, btn_cancel;
    private LinearLayout layout;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video_popup_window);
        intent = getIntent();
        btn_take_video = (Button) this.findViewById(R.id.btn_take_video);
        btn_pick_video = (Button) this.findViewById(R.id.btn_pick_video);
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

        layout = (LinearLayout) findViewById(R.id.pop_layout);

        // 添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
//                        Toast.LENGTH_SHORT).show();
            }
        });
        // 添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_take_video.setOnClickListener(this);
        btn_pick_video.setOnClickListener(this);
    }

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String videoPath = null;
        switch (requestCode) {
            case PhotoUtils.INTENT_REQUEST_CODE_ALBUM_VIDEO:
                if (data == null) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (data.getData() == null) {
                        return;
                    }
                    if (!FileUtils.isSdcardExist()) {
                        Toast.makeText(SelectVideoPopupWindowActivity.this, "SD卡不可用,请检查", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Uri uri = data.getData();
                    String[] proj = { MediaStore.Video.Media.DATA };
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                            videoPath = cursor.getString(column_index);
                            Log.i("videoPath", videoPath);
                        }
                    }
                }
                break;
            case PhotoUtils.INTENT_REQUEST_CODE_VIDEO:
                if (resultCode == RESULT_OK) {
                    Uri videoUri = data.getData();
                    String[] projection = { MediaStore.Video.Media.DATA};
                    Cursor cursor = managedQuery(videoUri, projection, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                            videoPath = cursor.getString(column_index);
                            Log.i("videoPath", videoPath);
                        }
                    }
//                    int recordedVideoFileSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
//                    Log.i("videoSize", ""+recordedVideoFileSize);
                }
                break;
        }
        intent.putExtra(Constants.IMAGE_PATH_KEY, videoPath);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_video:
                PhotoUtils.takeVideoDefault(SelectVideoPopupWindowActivity.this);
                break;
            case R.id.btn_pick_video:
                PhotoUtils.selectVideo(SelectVideoPopupWindowActivity.this);
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }

    }

}
