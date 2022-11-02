package it.algos.wiki23.backend.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.service.*;
import it.sauronsoftware.cron4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 13-Sep-2022
 * Time: 19:55
 * <p>
 * Il download-reset completo (questo task) che cancella (drop) tutta la collection 'bio' viene effettuata SOLO il lunedì
 * Nella giornata di lunedì gli altri task NON devono girare
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)

public class TaskBioReset extends AlgosTask {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    private DownloadService service;


    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
        long inizio = System.currentTimeMillis();

        if (WPref.usaTaskBio.is()) {
//            service.cicloIniziale();
//            loggerDownload(inizio);
        }
    }

    /**
     * Descrizione: ogni settimana, a mezzanotte della domenica/lunedi
     */
    private static final String PATTERN = AESchedule.zeroCinqueLunedi.getPattern();


    @Override
    public String getPattern() {
        return PATTERN;
    }


    public void loggerDownload(long inizio) {
        long fine = System.currentTimeMillis();
        String message;
        long delta = fine - inizio;
        delta = delta / 1000 / 60;

        message = String.format("Task per il ciclo reset bio in %s minuti", delta);
        logger.info(new WrapLog().type(AETypeLog.bio).message(message).usaDb());
    }

}
