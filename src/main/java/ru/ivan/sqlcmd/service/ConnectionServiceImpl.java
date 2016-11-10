package ru.ivan.sqlcmd.service;

import org.springframework.stereotype.Component;
import ru.ivan.sqlcmd.model.DatabaseManager;

@Component
public abstract class ConnectionServiceImpl implements ConnectionService {
    public abstract DatabaseManager getManager();

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {

        DatabaseManager manager = getManager();
        manager.connect(databaseName, userName, password);
        return manager;
}
}
