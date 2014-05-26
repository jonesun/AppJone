package com.jone.app.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.jone.app.Constants;
import com.jone.app.R;

public class VideoPlayActivity extends Activity {
    private VideoView videoView;
    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        videoView = (VideoView) findViewById(R.id.videoView);
        MediaController mController = new MediaController(this);
        videoView.setMediaController(mController);//设置一个控制条

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            videoPath = bundle.getString(Constants.IMAGE_PATH_KEY);
            if(videoPath != null){
                videoView.setVideoPath(videoPath);
                videoView.start();
            }
        }
    }

}
