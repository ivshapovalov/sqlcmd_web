package ru.ivan.sqlcmd.integration;

import ru.ivan.sqlcmd.controller.MainController;

import java.io.IOException;
import java.io.InputStream;

public class ConfigurableInputStream extends InputStream {

    private String line;
    private boolean endLine = false;

    @Override
    public int read() throws IOException {
        if (line.length() == 0) {
            return -1;
        }

        if (endLine) {
            endLine = false;
            return -1;
        }

        char ch = line.charAt(0);
        line = line.substring(1);

        if (ch == MainController.LINE_SEPARATOR) {
            endLine = true;
        }

        return (int)ch;
    }

    public void add(String line) {
        if (this.line == null) {
            this.line = line;
        } else {
            this.line += ""+ MainController.LINE_SEPARATOR+"" + line;
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        line = null;
        endLine = false;
    }
}
