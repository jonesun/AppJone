package com.jone.app.ui.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.jone.app.R;

/**
 * Created by jone_admin on 14-2-15.
 */
public class LocalCameraFragment extends Fragment implements SurfaceHolder.Callback{
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;  //SurfaceView的控制类
    private SurfaceView mSurfaceView;

    private boolean isPreview = false;
    public LocalCameraFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_local_camera, container, false);
        mSurfaceView = (SurfaceView)rootView.findViewById(R.id.surface);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);           //设置回调
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        return rootView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //初始化摄像头
        InitCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    private void InitCamera()
    {
        if(isPreview)
            return;

        int CameraIndex = FindBackCamera();
        if(CameraIndex == -1){
            CameraIndex = FindFrontCamera();
        }
        mCamera = Camera.open(CameraIndex);

        if(mCamera != null)
        {
            try
            {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                // mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
            } catch (Exception e)
            {
                // TODO: handle exception
            }

            isPreview = true;
        }

    }

    public void openCamera(){
        mCamera.startPreview();
    }

    public void closeCamera(){
        mCamera.stopPreview();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCamera != null)
        {
            mCamera.release();
            mCamera = null;
        }
    }

    @TargetApi(9)
    private int FindFrontCamera(){
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
            if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_FRONT ) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }
    @TargetApi(9)
    private int FindBackCamera(){
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
            if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_BACK ) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }
}
