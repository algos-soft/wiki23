package it.algos.wiki23.backend.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.schedule.*;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.wiki23.backend.boot.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.statistiche.*;
import it.algos.wiki23.backend.upload.*;
import it.sauronsoftware.cron4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.time.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Thu, 07-Jul-2022
 * Time: 19:48
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskNazionalita extends VaadTask {


    public TaskNazionalita() {
        super.descrizioneTask = WPref.uploadNazionalita.getDescrizione();
        super.typeSchedule = Wiki23Var.typeSchedule.getNazionalita();
        super.flagAttivazione = WPref.usaTaskNazionalita;
        super.flagPrevisione = WPref.uploadNazionalitaPrevisto;
    }


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public NazionalitaBackend nazionalitaBackend;

    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
        super.execute(taskExecutionContext);

        if (flagAttivazione.is()) {
            super.fixNext();

            //--Statistiche
            inizio = System.currentTimeMillis();
            appContext.getBean(StatisticheNazionalita.class).upload();
            loggerElabora(inizio);

            //--Upload
            inizio = System.currentTimeMillis();
            appContext.getBean(UploadNazionalita.class).uploadAll();
            loggerUpload(inizio);
            super.loggerTask();
        }
        else {
            super.loggerNoTask();
        }
    }


    public void loggerElabora(long inizio) {
        long fine = System.currentTimeMillis();
        String message;
        long delta = fine - inizio;
        delta = delta / 1000 / 60;

        message = String.format("Task per elaborare le nazionalità in %s minuti", delta);
        logger.info(new WrapLog().type(AETypeLog.elabora).message(message).usaDb());
    }

    public void loggerUpload(long inizio) {
        long fine = System.currentTimeMillis();
        String message;
        long delta = fine - inizio;
        delta = delta / 1000 / 60;

        message = String.format("Task per l'upload delle liste di nazionalità in %s minuti", delta);
        logger.info(new WrapLog().type(AETypeLog.upload).message(message).usaDb());
    }

}
