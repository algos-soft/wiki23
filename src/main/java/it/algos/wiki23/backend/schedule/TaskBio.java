package it.algos.wiki23.backend.schedule;

import static com.fasterxml.jackson.databind.type.LogicalType.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import it.sauronsoftware.cron4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 05-Jul-2022
 * Time: 19:03
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskBio extends AlgosTask {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    private DownloadService service;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    private ResourceService resourceService;


    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
        long inizio = System.currentTimeMillis();

        if (WPref.usaTaskBio.is()) {
            fixNext();
            service.ciclo();
            loggerDownload(inizio);
        }
    }

    /**
     * Descrizione: ogni giorno della settimana, a mezzanotte (una per adesso)
     */
    private static final String PATTERN = "1 1 * * *";


    @Override
    public String getPattern() {
        return PATTERN;
    }

    public void fixNext() {
        LocalDateTime adesso = LocalDateTime.now();
        LocalDateTime prossimo = adesso.plusDays(1);
        WPref.downloadBioPrevisto.setValue(prossimo);
    }

    public void loggerDownload(long inizio) {
        long fine = System.currentTimeMillis();
        String message;
        long delta = fine - inizio;
        delta = delta / 1000 / 60;

        message = String.format("Task per il ciclo download bio in %s minuti", delta);
        logger.info(new WrapLog().type(AETypeLog.bio).message(message).usaDb());
    }

}
