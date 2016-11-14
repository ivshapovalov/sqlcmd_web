package ru.ivan.sqlcmd.controller;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ivan.sqlcmd.controller.actions.ErrorAction;
import ru.ivan.sqlcmd.service.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Component
public class ActionResolver {

    @Autowired
    private Service service;

    private List<Action> actions;

    public ActionResolver() {
        actions = new LinkedList<>();

        Reflections reflections = new Reflections(ErrorAction.class.getPackage().getName());
        Set<Class<? extends AbstractAction>> classes =
                reflections.getSubTypesOf(AbstractAction.class);

        for (Class<? extends AbstractAction> aClass : classes) {
            if (aClass.equals(ErrorAction.class)) {
                continue;
            }

            try {
                AbstractAction action = aClass.getConstructor().newInstance(service);
                actions.add(action);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        actions.add(new ErrorAction(service));

    }

    public Action getAction(String url) {
        for (Action action : actions) {
            if (action.canProcess(url)) {
                return action;
            }
        }
        return new NullAction();
    }
}
