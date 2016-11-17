package ru.ivan.sqlcmd.model;

import java.util.List;

public interface UserActionsDao {
    void log(UserAction userAction);

    List<UserAction> getAllActionsOfUser(String userName);
}
