package it.algos.vaad24.backend.schedule;

import it.algos.vaad24.backend.boot.*;
import it.algos.vaad24.backend.service.*;
import it.sauronsoftware.cron4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Wed, 28-Dec-2022
 * Time: 19:07
 */
public abstract class VaadSchedule extends Scheduler {


    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public LogService logger;


    /**
     * Aggiunge i task alla superclasse per l'esecuzione <br>
     * Deve essere sovrascritto, invocando il metodo della superclasse DOPO <br>
     */
    public void startSchedule() throws IllegalStateException {
        if (!isStarted()) {
            super.start();
        }

        if (VaadVar.taskList != null && VaadVar.taskList.size() > 0) {
            for (VaadTask task : VaadVar.taskList) {
                schedule(task.getPattern(), task);
            }
        }
    }

}
