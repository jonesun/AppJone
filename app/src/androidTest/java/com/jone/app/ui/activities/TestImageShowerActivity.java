package com.jone.app.ui.activities;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.ImageView;

import com.jone.app.R;

/**
 * Created by jone on 2014/5/7.
 */
public class TestImageShowerActivity extends ActivityInstrumentationTestCase2<ImageShowerActivity> {
    private ImageShowerActivity imageShowerActivity;
    private ImageView img;
    private Button btnCLose;
    public TestImageShowerActivity(){
        super(ImageShowerActivity.class);
    }
//    public TestImageShowerActivity(Class<ImageShowerActivity> activityClass) {
//        super(activityClass);
//    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        imageShowerActivity = getActivity();
        img = (ImageView)imageShowerActivity.findViewById(R.id.large_image);
        btnCLose = (Button) imageShowerActivity.findViewById(R.id.btnCLose);
    }

    public void testPreconditions() {
        assertNotNull(imageShowerActivity);
        assertNotNull(img);
        assertNotNull(btnCLose);
    }

    @UiThreadTest
    public void testUI() {
        assertEquals("关闭", btnCLose.getText().toString());
       //模拟点击按钮
        btnCLose.performClick();
    }

    @Override
    public void setActivityIntent(Intent i) {
        super.setActivityIntent(i);
//        i.putExtra(Constants.IMAGE_PATH_KEY, );
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
