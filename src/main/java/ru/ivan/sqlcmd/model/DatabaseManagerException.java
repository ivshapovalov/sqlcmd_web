package ru.ivan.sqlcmd.model;

class DatabaseManagerException extends RuntimeException {
    public DatabaseManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}

