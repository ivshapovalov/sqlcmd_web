package ru.ivan.sqlcmd.model;

import org.springframework.data.repository.CrudRepository;
import ru.ivan.sqlcmd.model.entity.DatabaseConnection;

public interface DatabaseConnectionRepository extends CrudRepository<DatabaseConnection, Integer> {
    DatabaseConnection findByUserNameAndDatabaseName(String userName, String databaseName);
    DatabaseConnection findByUserName(String userName);
}
