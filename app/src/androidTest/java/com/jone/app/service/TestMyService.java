package com.jone.app.service;

import android.content.Intent;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.jone.app.services.MyService;

/**
 * Created by jone on 2014/5/7.
 */
public class TestMyService extends ServiceTestCase<MyService> {
    public TestMyService(){
        super(MyService.class);
    }
//    public TestMyService(Class<MyService> serviceClass) {
//        super(serviceClass);
//    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getContext().startService(new Intent(getContext(), MyService.class));
    }

    @SmallTest
    public void testSomething() {
        assertEquals(2, 2);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        getContext().stopService(new Intent(getContext(), MyService.class));
    }
}
