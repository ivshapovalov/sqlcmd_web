package ru.ivan.sqlcmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ServiceFactory {

    @Autowired
    @Qualifier ("impl")
    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {

        this.service = service;
    }
}
