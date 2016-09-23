package ru.ivan.sqlcmd.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Ivan on 23.09.2016.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String cmd = "ping.exe gmail.com";
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));

        String line;
        while ((line = buf.readLine()) != null) {
            System.out.println(line);
        }

    }

}