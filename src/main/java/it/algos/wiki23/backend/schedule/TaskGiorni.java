package it.algos.wiki23.backend.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.schedule.*;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.statistiche.*;
import it.algos.wiki23.backend.upload.*;
import it.sauronsoftware.cron4j.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.time.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 08-Aug-2022
 * Time: 14:49
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskGiorni extends VaadTask {


    public TaskGiorni() {
        super.descrizione = "Upload di tutte le liste per giorno";
        super.typeSchedule = AESchedule.dueNoLunedi;
    }

    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
        if (WPref.usaTaskGiorni.is()) {
            fixNext();

            //--Le statistiche comprendono anche l'elaborazione
            //--L'elaborazione comprende le info per la view
            //--Le statistiche comprendono le info per la view
            appContext.getBean(StatisticheGiorni.class).upload();

            //--L'upload comprende anche le info per la view
            inizio = System.currentTimeMillis();
            appContext.getBean(UploadGiorni.class).uploadAll();
            super.loggerTask();
        }
        else {
            super.loggerNoTask();
        }
    }



    public void fixNext() {
        LocalDateTime adesso = LocalDateTime.now();
        LocalDateTime prossimo = adesso.plusDays(1);
        WPref.uploadGiorniPrevisto.setValue(prossimo);
    }

}

