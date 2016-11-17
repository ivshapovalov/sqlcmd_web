package ru.ivan.sqlcmd.model;

import org.springframework.data.repository.CrudRepository;
import ru.ivan.sqlcmd.model.entity.UserAction;

import java.util.List;


public interface UserActionRepository extends CrudRepository<UserAction, Integer> {
    List<UserAction> findByUserName(String userName);
}
