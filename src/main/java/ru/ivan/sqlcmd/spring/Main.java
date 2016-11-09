package ru.ivan.sqlcmd.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String []args){

        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"context.xml"});
        LabRat rat = (LabRat) context.getBean("rat");
        rat.sayHi();
        System.out.println(rat.getName());
    }
}