package ru.ivan.sqlcmd.model;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ivan.sqlcmd.model.entity.DatabaseConnection;
import ru.ivan.sqlcmd.model.entity.UserAction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class UserActionRepositoryImpl implements UserActionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DatabaseConnectionRepository databaseConnections;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createAction(String userName,String databaseName, String action) {
        DatabaseConnection databaseConnection = databaseConnections.findByUserNameAndDatabaseName
                (userName, databaseName);
        if (databaseConnection == null) {
            databaseConnection = databaseConnections.save(new DatabaseConnection(userName, databaseName));
        }
        UserAction userAction = new UserAction(databaseConnection, action );
        Session session = (Session) entityManager.getDelegate();
        session.persist(userAction);
    }
}
