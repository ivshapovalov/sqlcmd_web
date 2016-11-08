package ru.ivan.sqlcmd.spring;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String []args){

        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.OFF);


        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"context.xml"});
        LabRat rat = (LabRat) context.getBean("rat");
        rat.sayHi();
        System.out.println(rat.getName());
    }
}