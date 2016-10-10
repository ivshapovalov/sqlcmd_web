package ru.ivan.sqlcmd.model;

public class DatabaseManagerException extends RuntimeException {
    public DatabaseManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseManagerException(String message) {
        super(message);
    }
}

