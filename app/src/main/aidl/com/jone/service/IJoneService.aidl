package com.jone.service;

import com.jone.bean.Person;
/*
**AIDL使用简单的语法来声明接口,描述其方法以及方法的参数和返回值.这些参数和返回值可以是任何类型,甚至是其他AIDL生成的接口
*其中对于Java编程语言的基本数据类型 (int, long, char, boolean等),String和CharSequence，集合接口类型List和Map，不需要import 语句。
*而如果需要在AIDL中使用其他AIDL接口类型，需要import，即使是在相同包结构下。AIDL允许传递实现Parcelable接口的类，需要import.
*对于非基本数据类型，也不是String和CharSequence类型的，需要有方向指示，包括in、out和inout，in表示由客户端设置，out表示由服务端设置，inout是两者均可设置。
*AIDL只支持接口方法，不能公开static变量。
*/
interface IJoneService{
    String sayHello();
    String say(String someTxt);
    void savePerson(in Person person);
    List<Person> getAllPerson();
}