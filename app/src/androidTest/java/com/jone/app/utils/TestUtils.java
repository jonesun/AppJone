package com.jone.app.utils;

import android.test.AndroidTestCase;

import junit.framework.Assert;

public class TestUtils extends AndroidTestCase {
    private int i1;
    private int i2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        i1 = 1;
        i2 = 3;
    }

    public void testSave() throws Throwable{
        Assert.assertEquals(4, i1 + i2);
    }

    public void testSomethingElse() throws Throwable {
        Assert.assertTrue(i1 + i2 == 4);
    }
}
