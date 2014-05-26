package com.jone.app.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jone.app.R;

public class StandWeightActivity extends Activity {
    private RadioGroup radioGroupSex;
    private EditText editHeight;
    private Button btnCalculate;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_weight);
        initViews();
    }

    public void initViews(){
//        男性：(身高cm－80)×70﹪=标准体重 女性：(身高cm－70)×60﹪=标准体重
//        标准体重正负10﹪为正常体重
//        标准体重正负10﹪~ 20﹪为体重过重或过轻
//        标准体重正负20﹪以上为肥胖或体重不足
//                超重计算公式
//        超重%=[（实际体重－理想体重）/（理想体重）]×100%
        radioGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
        editHeight = (EditText) findViewById(R.id.editHeight);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        txtResult = (TextView) findViewById(R.id.txtResult);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double resultWeight = 0;
                if(radioGroupSex.getCheckedRadioButtonId() == R.id.radioMan){
                    resultWeight = (Integer.parseInt(editHeight.getText().toString()) - 80) * 0.7;
                }else if(radioGroupSex.getCheckedRadioButtonId() == R.id.radioWoman){
                    resultWeight = (Integer.parseInt(editHeight.getText().toString()) - 70) * 0.6;
                }
                txtResult.setText("标准体重为: " + resultWeight + "kg");
            }
        });
    }
}
