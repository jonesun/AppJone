package com.jone.app.test;

/**
 * Created by jone_admin on 14-2-21.
 */
public class Test {
    String str=new String("good");
    char[]ch={'a','b','c'};
    public static void main(String args[]){
        Test ex = new Test();
        ex.change(ex.str,ex.ch);
        System.out.print(ex.str+" and ");
        System.out.print(ex.ch);
    }
    public void change(String str,char ch[]){
        str="test ok";
        ch[0]='g';
    }
}