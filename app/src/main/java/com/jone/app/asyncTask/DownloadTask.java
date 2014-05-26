package com.jone.app.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by jone_admin on 14-2-18.
 */
public class DownloadTask extends AsyncTask<Void, Integer, Boolean> {
    private Context context;
    private ProgressDialog progressDialog;
    public DownloadTask(Context context){
        this.context = context;
    }


    @Override //这个方法会在后台任务开始执行之间调用，用于进行一些界面上的初始化操作，比如显示一个进度条对话框等
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "加载中", "请等待");
        progressDialog.show();
    }

    @Override //这个方法中所有代码都会在子线程中运行，我们应该在这里去处理所有的耗时任务。
    //任务一旦完成就可以通过return语句来将任务的执行结果进行返回，如果AsyncTask的第三个泛型参数指定的是Void,就可以不返回任务执行结果。
    //注意，在这个方法中是不可以进行UI操作的,如果需要更新UI元素,比如反馈当前任务的执行进度,可以调用publishProgress(Progress...)方法来完成
    protected Boolean doInBackground(Void... voids) {
        try{
            while (true){
                int downloadPercent = 0;
                downloadPercent = doWork();
                publishProgress(downloadPercent);
                if(downloadPercent >= 100){
                    break;
                }
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override //当在后台任务中调用了publishProgress(Progress...)方法后,这个方法就很快会被调用,方法中携带的参数就是在后台任务中传递过来的。
    //在这个方法中可以对UI进行操作,利用参数中的数值就可以对界面元素进行相应的更新
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setMessage("当前下载进度: " + values[0] + "%");
    }

    @Override //当后台任务执行完毕并通过return语句进行返回时,这个方法就很快会被调用.
    //返回的数据会作为参数传递到此方法中,可以利用返回的数据来进行一些UI操作,比如提醒任务执行的结果,以及关闭掉进度条对话框等.
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
        if(aBoolean){
            Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
        }
    }


    // 该程序模拟填充长度为100的数组
    private int[] data = new int[100];
    private int hasData = 0;
    public int doWork() {
        // 为数组元素赋值
        data[hasData++] = (int) (Math.random() * 100);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return hasData;
    }
}
