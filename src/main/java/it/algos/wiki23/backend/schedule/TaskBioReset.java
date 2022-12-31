package it.algos.wiki23.backend.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.schedule.*;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.wiki23.backend.boot.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.statistiche.*;
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
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskBioReset extends VaadTask {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    private DownloadService service;


    public TaskBioReset() {
        super.descrizioneTask = WPref.resetBio.getDescrizione();
        super.typeSchedule = Wiki23Var.typeSchedule.getResetBio();
        super.flagAttivazione = WPref.usaTaskResetBio;
        super.flagPrevisione = WPref.resetBioPrevisto;
        super.nextDays = 15;
    }


    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
        super.execute(taskExecutionContext);

        if (flagAttivazione.is()) {
            super.fixNext();

            //            service.cicloIniziale();
            //            loggerDownload(inizio);
            super.loggerTask();
        }
        else {
            super.loggerNoTask();
        }
    }

}
