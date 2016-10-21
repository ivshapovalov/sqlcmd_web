package ru.ivan.sqlcmd.model;

class DatabaseManagerException extends RuntimeException {
    DatabaseManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    DatabaseManagerException(String message) {
        super(message);
    }
}

