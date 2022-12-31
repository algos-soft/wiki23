package it.algos.vaad24.backend.schedule;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.interfaces.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.wrapper.*;
import it.sauronsoftware.cron4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;

import java.time.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Wed, 28-Dec-2022
 * Time: 20:34
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class VaadTask extends Task {

    protected long inizio;

    protected int nextDays = 7;

    protected AESchedule typeSchedule;

    protected String descrizioneTask;

    protected AIGenPref flagAttivazione;

    protected AIGenPref flagPrevisione;

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected LogService logger;


    /**
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
    }


    public boolean execute() {
        inizio = System.currentTimeMillis();

        if (flagAttivazione == null || flagAttivazione.is()) {
            this.fixNext();
            return true;
        }
        else {
            this.loggerNoTask();
            return false;
        }
    }


    protected void fixNext() {
        LocalDateTime adesso = LocalDateTime.now();
        LocalDateTime prossimo = adesso.plusDays(nextDays);
        if (flagPrevisione != null) {
            flagPrevisione.setValue(prossimo);
        }
    }

    public String getPattern() {
        return typeSchedule.getPattern();
    }


    public String getDescrizioneTask() {
        return descrizioneTask;
    }


    public AESchedule getTypeSchedule() {
        return typeSchedule;
    }

    public AIGenPref getFlagAttivazione() {
        return flagAttivazione;
    }

    public void loggerTask() {
        long fine = System.currentTimeMillis();
        String message;
        String clazzName;
        long delta = fine - inizio;
        delta = delta / 1000 / 60;

        clazzName = this.getClass().getSimpleName();
        message = String.format("%s%s%s [%s] eseguita in %s minuti", clazzName, FORWARD, descrizioneTask, getPattern(), delta);

        logger.info(new WrapLog().type(AETypeLog.task).message(message).usaDb());
    }

    public void loggerNoTask() {
        String message;
        String clazzName;

        clazzName = this.getClass().getSimpleName();
        message = String.format("%s%s%s [%s] non eseguita per flag disabilitato", clazzName, FORWARD, descrizioneTask, getPattern());

        logger.info(new WrapLog().type(AETypeLog.task).message(message));
    }

}

