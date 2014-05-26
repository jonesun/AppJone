package com.jone.app.ui.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jone.app.R;
import com.jone.bean.Person;
import com.jone.service.IJoneService;

import java.util.LinkedList;
import java.util.List;

public class JoneAidlClientActivity extends Activity {
    private static final String TAG = JoneAidlClientActivity.class.getSimpleName();
    private TextView txtInfo;
    private IJoneService joneService = null;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            System.out.println("sssssssssssssssssssssssssssssssssss");
            joneService = IJoneService.Stub.asInterface(iBinder);
            try {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append( "System: " + joneService.sayHello() + "/n");
                stringBuffer.append("System: " + joneService.say("111111111") + "/n");
                Log.d(TAG, stringBuffer.toString());
                txtInfo.setText(stringBuffer);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            joneService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jone_aidl_client);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.jone_aidl_client, menu);
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

    private int index = 0;
    public void addPerson(View view){
        Person person = new Person(index, "Person" + index, System.currentTimeMillis());
        try {
            joneService.savePerson(person);
            List<Person> persons = joneService.getAllPerson();
            Log.d(TAG, "persons: " + persons.size());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void callAIDL(View view) {
        Intent intent = new Intent("com.jone.service.IJoneService");
        bindService(intent, connection, BIND_AUTO_CREATE);
    }
}
