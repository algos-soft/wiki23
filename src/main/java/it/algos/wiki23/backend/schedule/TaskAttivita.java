package it.algos.wiki23.backend.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
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
 * Date: Tue, 05-Jul-2022
 * Time: 13:58
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskAttivita extends AlgosTask {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public  AttivitaBackend attivitaBackend;

    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
        long inizio;

        if (WPref.usaTaskAttivita.is()) {

            //--Statistiche
            inizio = System.currentTimeMillis();
            appContext.getBean(StatisticheAttivita.class).upload();
            loggerElabora(inizio);

            //--Upload
            inizio = System.currentTimeMillis();
            appContext.getBean(UploadAttivita.class).uploadAll();
            loggerUpload(inizio);
        }
    }


    /**
     * Descrizione: ogni settimana la mattina di martedì
     * 0 10 * * Tue
     */
    private static final String PATTERN = AESchedule.dieciMartedi.getPattern();


    @Override
    public String getPattern() {
        return PATTERN;
    }

    public void loggerElabora(long inizio) {
        long fine = System.currentTimeMillis();
        String message;
        long delta = fine - inizio;
        delta = delta / 1000 / 60;

        message = String.format("Task per elaborare le attività in %s minuti", delta);
        logger.info(new WrapLog().type(AETypeLog.elabora).message(message).usaDb());
    }


    public void loggerUpload(long inizio) {
        long fine = System.currentTimeMillis();
        String message;
        long delta = fine - inizio;
        delta = delta / 1000 / 60;

        message = String.format("Task per l'upload delle liste di attività in %s minuti", delta);
        logger.info(new WrapLog().type(AETypeLog.upload).message(message).usaDb());
    }

}
