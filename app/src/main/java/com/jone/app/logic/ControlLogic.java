package com.jone.app.logic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.jone.app.R;
import com.jone.app.ui.activities.ContactActivity;
import com.jone.app.ui.activities.FlashlightActivity;
import com.jone.app.ui.masterDetail.ItemListActivity;
import com.jone.app.utils.SystemUtil;
import com.jone.bean.ControlBean;
import com.Zxing.Demo.CaptureActivity;

import net.micode.fileexplorer.FileExplorerTabActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jone_admin on 14-3-18.
 */
public class ControlLogic {
    private Activity activity;
    public ControlLogic(Activity activity){
        this.activity = activity;
    }
    public List<ControlBean> getControlBeans(){
        List<ControlBean> controlBeans = new ArrayList<>();
        if(!SystemUtil.isPad(activity)){
            controlBeans.add(new ControlBean(0, "查看联系人", R.drawable.ic_menu_contacts, null,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.startActivity(new Intent(activity, ContactActivity.class));
                        }
                    }));
            controlBeans.add(new ControlBean(1, "拨打电话", R.drawable.ic_menu_call, null,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.startActivity(new Intent(Intent.ACTION_DIAL));
                        }
                    }));
            controlBeans.add(new ControlBean(2, "发送信息", R.drawable.ic_menu_send_sms, null,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:")));
                        }
                    }));
        }
        controlBeans.add(new ControlBean(3, "打开相机", R.drawable.ic_menu_camera, null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SystemUtil.takePhoto(activity);
                    }
                }));
        controlBeans.add(new ControlBean(4, "打开相册", R.drawable.ic_menu_gallery, null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SystemUtil.openPhoto(activity);
                    }
                }));
        controlBeans.add(new ControlBean(5, "网络设置", R.drawable.ic_menu_network, null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SystemUtil.setNetwork(activity);
                    }
                }));
        controlBeans.add(new ControlBean(6, "文件管理", R.drawable.ic_menu_file, null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(activity, "正在打开文件管理器...", Toast.LENGTH_LONG).show();
                        activity.startActivity(new Intent(activity, FileExplorerTabActivity.class));
                    }
                }));
        controlBeans.add(new ControlBean(7, "条码扫描", R.drawable.ic_menu_scan, null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.startActivity(new Intent(activity, CaptureActivity.class));
                        activity.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//由左向右滑入的效果
                    }
                }));
//        controlBeans.add(new ControlBean(8, activity.getString(R.string.note), R.drawable.ic_menu_note, null,
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        activity.startActivity(new Intent(activity, ItemListActivity.class));
//                    }
//                }));
        controlBeans.add(new ControlBean(9, "手电筒", R.drawable.ic_menu_flashlight, null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.startActivity(new Intent(activity, FlashlightActivity.class));
                        activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomout); //类似iphone的进入和退出时的效果
                    }
                }));
        return controlBeans;
    }
}
