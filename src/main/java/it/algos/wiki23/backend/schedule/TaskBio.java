package it.algos.wiki23.backend.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import it.sauronsoftware.cron4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.time.*;
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
        fixNext();
        if (WPref.usaTaskBio.is()) {
            service.ciclo();
        }
    }

    /**
     * Descrizione: ogni giorno della settimana, 1 minuto dopo mezzanotte
     */
    private static final String PATTERN = "1 0 * * *";


    @Override
    public String getPattern() {
        return PATTERN;
    }

    public void fixNext() {
        LocalDateTime adesso = LocalDateTime.now();
        LocalDateTime prossimo = adesso.plusDays(1);
        WPref.downloadBioPrevisto.setValue(prossimo);
    }

}
