package com.jone.app.ui.activities;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by jone on 2014/5/19.
 */
public class TestStandWeightActivity extends ActivityInstrumentationTestCase2<StandWeightActivity> {
    private Solo solo;
    public TestStandWeightActivity() {
        super(StandWeightActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testUI() throws Exception {
        boolean expected = true;

        //验证男孩180cm的标准体重为70公斤
        solo.clickOnRadioButton(0);
        solo.clickOnEditText(0);
        solo.enterText(0, "180");
        solo.clickOnButton("计算");
        boolean actual1 = solo.searchText("70.0");
        assertEquals("This and/or is are not found", expected, actual1);

        //返回清空editText表单
        //solo.goBack();
        solo.clearEditText(0);

        //验证女孩160cm的标准体重为70公斤
        solo.clickOnRadioButton(1);
        solo.clickOnEditText(0);
        solo.enterText(0, "160");
        solo.clickOnButton("计算");
        boolean actual2 = solo.searchText("54.0");
        assertEquals("This and/or is are not found", expected, actual2);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
