package com.jone.app.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;

import com.jone.app.R;

public class TestGridLayoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_grid_layout);
        GridLayout gridView = (GridLayout) findViewById(R.id.gridView);

        Button btn1 = new Button(this);
        btn1.setWidth(180);
        btn1.setText("shshshshksh");
        GridLayout.Spec rowSpec = GridLayout.spec(0);     //设置它的行和列
        GridLayout.Spec columnSpec = GridLayout.spec(0, 3);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        params.setGravity(Gravity.LEFT);
        gridView.addView(btn1, params);

        Button btn2 = new Button(this);
        btn2.setWidth(80);
        btn2.setText("fhfghcf");
        rowSpec = GridLayout.spec(1);     //设置它的行和列
        columnSpec = GridLayout.spec(0);
         params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        params.setGravity(Gravity.LEFT);
        gridView.addView(btn2, params);

        Button btn3 = new Button(this);
        btn3.setWidth(80);
        btn3.setText("54548");
        rowSpec = GridLayout.spec(1);     //设置它的行和列
        columnSpec = GridLayout.spec(1);
        params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        params.setGravity(Gravity.LEFT);
        gridView.addView(btn3, params);


//        int count=1;
//        for(int i=0;i<6;i++){
//            for(int j=0;j<5;j++) {
//                Button btn = new Button(this);
//                btn.setWidth(40);
//                btn.setText(String.valueOf(count));
//                count++;
//
//                if(i==0&&j==0) {
//                    Button btn1 = new Button(this);
//                    btn1.setWidth(180);
//                    btn1.setText("shshshshksh");
//                    GridLayout.Spec rowSpec = GridLayout.spec(0);     //设置它的行和列
//                    GridLayout.Spec columnSpec = GridLayout.spec(0, 3);
//                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
//                    params.setGravity(Gravity.LEFT);
//                    gridView.addView(btn1, params);
//                }else{
//                    GridLayout.Spec rowSpec = GridLayout.spec(i);     //设置它的行和列
//                    GridLayout.Spec columnSpec = GridLayout.spec(j);
//                    if(i==0&&j>0) {
//                    //
//                    }else{
//                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
//                    params.setGravity(Gravity.LEFT);
//                    gridView.addView(btn, params);
//                    }
//                }
//            }
//        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_grid_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
