package ru.ivan.sqlcmd.model;

import org.springframework.data.repository.CrudRepository;
import ru.ivan.sqlcmd.model.entity.DatabaseConnection;

public interface DatabaseConnectionRepository extends CrudRepository<DatabaseConnection, Integer> {
    DatabaseConnection findByUserNameAndDbName(String userName, String dbName);
    DatabaseConnection findByUserName(String userName);
}
