package ru.ivan.sqlcmd.integration;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Ivan on 21.09.2016.
 */
public class LogOutputStream extends OutputStream{
    private String log;
    @Override
    public void write(int i) throws IOException {
        log+=String.valueOf((char)i);
    }

    public String getData() {
        return log;
    }
}
