package ru.ivan.sqlcmd.model;

public interface UserActionRepositoryCustom {

    void createAction(String userName, String databaseName, String action);

}
