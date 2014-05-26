package com.jone.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jone.bean.Person;
import com.jone.service.IJoneService;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jone_admin on 14-2-27.
 */
public class JoneRemoteService extends Service {
    private final static String TAG = JoneRemoteService.class.getSimpleName();
    private LinkedList<Person> personLinkedList = new LinkedList<>();
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private final IJoneService.Stub stub = new IJoneService.Stub() {
        @Override
        public String sayHello() throws RemoteException {
            Log.d(TAG, "sayHello");
            return "hello";
        }

        @Override
        public String say(String someTxt) throws RemoteException {
            Log.d(TAG, "say: " + someTxt);
            return "say: " + someTxt;
        }

        @Override
        public void savePerson(Person person) throws RemoteException {
            if(person != null){
                personLinkedList.add(person);
            }
        }

        @Override
        public List<Person> getAllPerson() throws RemoteException {
            return personLinkedList;
        }
    };
}
