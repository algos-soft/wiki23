package it.algos.wiki23.backend.schedule;

import com.google.common.util.concurrent.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.sauronsoftware.cron4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.data.mongodb.core.messaging.Task;

import javax.annotation.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 05-Jul-2022
 * Time: 13:32
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ScheduleWiki extends Scheduler {

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;


    @PostConstruct
    public void startBio() throws IllegalStateException {
        if (!isStarted()) {
            super.start();
            schedule(appContext.getBean(TaskBio.class));
            schedule(appContext.getBean(TaskGiorni.class));
            schedule(appContext.getBean(TaskAnni.class));
            schedule(appContext.getBean(TaskAttivita.class));
            schedule(appContext.getBean(TaskNazionalita.class));
        }
    }

    public void schedule(AlgosTask task) {
        if (task != null) {
            schedule(task.getPattern(), task);
        }
    }

}
