package com.jone.app.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jone.app.Constants;
import com.jone.app.R;
import com.jone.app.ui.activities.fullScreen.videoplay.FullVideoPlayActivity;
import com.jone.app.ui.view.EmoteInputView;
import com.jone.app.ui.view.EmoticonsEditText;
import com.jone.app.utils.PhotoUtils;
import com.jone.app.utils.SystemUtil;
import com.jone.app.utils.Utils;

public class NewNoteActivity extends Activity implements View.OnClickListener{
    protected EmoticonsEditText mEetTextDitorEditer;
    protected EmoteInputView mInputView;

    private ViewGroup layout_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setScreenOrientation(NewNoteActivity.this);
        setContentView(R.layout.activity_new_note);
        initViews();
    }

    private void initViews(){
        mEetTextDitorEditer = (EmoticonsEditText) findViewById(R.id.chat_textditor_eet_editer);
        mInputView = (EmoteInputView) findViewById(R.id.chat_eiv_inputview);

        Button btnAddExpression = (Button) findViewById(R.id.btnAddExpression);
        Button btnAddPhoto = (Button) findViewById(R.id.btnAddPhoto);
        Button btnAddVideo = (Button) findViewById(R.id.btnAddVideo);
        Button btnAddLocation = (Button) findViewById(R.id.btnAddLocation);

        layout_center = (ViewGroup) findViewById(R.id.layout_center);
        layout_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                if (mInputView.isShown()) {
                    mInputView.setVisibility(View.GONE);
                }
            }
        });

        mInputView.setEditText(mEetTextDitorEditer);
        mEetTextDitorEditer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.chat_textditor_eet_editer) {
                    showKeyBoard();
                }
                return false;
            }
        });
        btnAddExpression.setOnClickListener(this);
        btnAddPhoto.setOnClickListener(this);
        btnAddVideo.setOnClickListener(this);
        btnAddLocation.setOnClickListener(this);
    }

    protected void showKeyBoard() {
        if (mInputView.isShown()) {
            mInputView.setVisibility(View.GONE);
        }
        mEetTextDitorEditer.requestFocus();
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .showSoftInput(mEetTextDitorEditer, 0);
    }

    protected void hideKeyBoard() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(NewNoteActivity.this
                                .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.new_note, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddExpression: //添加表情
                mEetTextDitorEditer.requestFocus();
                if (mInputView.isShown()) {
                    hideKeyBoard();
                } else {
                    hideKeyBoard();
                    mInputView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btnAddPhoto: //添加图片
                Intent intent = new Intent(NewNoteActivity.this, SelectPicPopupWindowActivity.class);
                startActivityForResult(intent, PhotoUtils.GET_PHOTO_CODE);
                break;
            case R.id.btnAddVideo://添加视频
                startActivityForResult(new Intent(NewNoteActivity.this,
                        SelectVideoPopupWindowActivity.class), PhotoUtils.GET_VIDEO_CODE);
                break;
            case R.id.btnAddLocation: //添加定位
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PhotoUtils.GET_PHOTO_CODE: //获取图片
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    if(bundle != null){
                        String imagePath = bundle.getString(Constants.IMAGE_PATH_KEY);
                        if(imagePath != null){
                            addImageView(imagePath);
                        }
                    }
                }
                break;
            case PhotoUtils.GET_VIDEO_CODE: //获取视频
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    if(bundle != null){
                        String videoPath = bundle.getString(Constants.IMAGE_PATH_KEY);
                        if(videoPath != null){
                            addVideoImageView(videoPath);
                        }
                    }
                }
                break;
        }
    }

    private void addVideoImageView(final String videoPath){
        Bitmap bitmap =PhotoUtils.getVideoThumbnail(videoPath, 100, 100, MediaStore.Images.Thumbnails.MICRO_KIND);
        ImageView imageView = new ImageView(NewNoteActivity.this);
        imageView.setImageResource(android.R.drawable.ic_menu_slideshow);
        BitmapDrawable bd=new BitmapDrawable(bitmap);
        PhotoUtils.setAndRecycleBackground(imageView, bd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewNoteActivity.this, FullVideoPlayActivity.class);
                intent.putExtra(Constants.IMAGE_PATH_KEY, videoPath);
                startActivity(intent);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        layoutParams.setMargins(16, 16, 16, 16);
        layout_center.addView(imageView, layoutParams);
    }

    private void addImageView(final String imagePath){
        Bitmap bitmap =PhotoUtils.getImageThumbnail(imagePath, 100, 100);
//        bitmap = PhotoUtils.toRoundCorner(bitmap, 20); //加圆角
//        bitmap = PhotoUtils.lomoFilter(bitmap); //滤镜
        ImageView imageView = new ImageView(NewNoteActivity.this);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewNoteActivity.this, ImageShowerActivity.class);
                intent.putExtra(Constants.IMAGE_PATH_KEY, imagePath);
                startActivity(intent);
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        layoutParams.setMargins(16, 16, 16, 16);
        layout_center.addView(imageView, layoutParams);
    }
}
