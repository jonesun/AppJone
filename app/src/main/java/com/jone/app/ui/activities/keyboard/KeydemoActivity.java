package com.jone.app.ui.activities.keyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.jone.app.R;

import java.lang.reflect.Method;

public class KeydemoActivity extends Activity {

    private Context ctx;
    private Activity act;
    private EditText edit;
    private EditText edit1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keydemo);
        ctx = this;
        act = this;

        edit = (EditText) this.findViewById(R.id.edit);
        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                new KeyboardUtil(act, ctx, edit).showKeyboard();
                int inputback = edit.getInputType();
                edit.setInputType(InputType.TYPE_NULL);
                new KeyboardUtil(act, ctx, edit).showKeyboard();
                edit.setInputType(inputback);
                return false;
            }
        });

        edit1 = (EditText) this.findViewById(R.id.edit1);
        edit1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inputback = edit1.getInputType();
                edit1.setInputType(InputType.TYPE_NULL);
                new KeyboardUtil(act, ctx, edit1).showKeyboard();
                edit1.setInputType(inputback);
                return false;
            }
        });

    }

}
