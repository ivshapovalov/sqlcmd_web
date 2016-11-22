package ru.ivan.sqlcmd.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.ivan.sqlcmd.model.entity.UserAction;

import java.util.List;

public interface UserActionRepository extends CrudRepository<UserAction, Integer>, UserActionRepositoryCustom {
    @Query(value = "SELECT ua FROM UserAction ua WHERE ua.connection.userName = :userName AND" +
            " ua.connection.dbName = :dbName ")
    List<UserAction> findByUserNameAndDbName(@Param("userName") String userName,@Param
            ("dbName") String dbName);


    @Query(value = "SELECT ua FROM UserAction ua WHERE ua.connection.userName = :userName")
    List<UserAction> findByUserName(@Param("userName") String userName);
}
