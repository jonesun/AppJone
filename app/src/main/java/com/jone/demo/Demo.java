package com.jone.demo;

/**
 * Created by jone on 2014/5/8.
 */
public class Demo implements IDemo {
    @Override
    public int getResult(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i + 1;
    }

    public static void main(String[] args){
        IDemo demo = (IDemo) JoneInvocationHandler.newInstance(new Demo());
        demo.getResult(22222);
    }
}
