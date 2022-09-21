package it.algos.wiki23.backend.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
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
 * Date: Mon, 08-Aug-2022
 * Time: 14:49
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskGiorni extends AlgosTask {


    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
        long inizio;

        if (WPref.usaTaskGiorni.is()) {
            fixNext();

            //--Le statistiche comprendono anche l'elaborazione
            appContext.getBean(StatisticheGiorni.class).upload();

            inizio = System.currentTimeMillis();
            appContext.getBean(UploadGiorni.class).uploadAll();
            loggerUpload(inizio);
        }
    }

    /**
     * Descrizione: alle 2 di notte escluso il lunedì
     */
    private static final String PATTERN = AESchedule.dueNoLunedi.getPattern();


    @Override
    public String getPattern() {
        return PATTERN;
    }

    public void fixNext() {
        LocalDateTime adesso = LocalDateTime.now();
        LocalDateTime prossimo = adesso.plusDays(1);
        WPref.uploadGiorniPrevisto.setValue(prossimo);
    }

    public void loggerUpload(long inizio) {
        long fine = System.currentTimeMillis();
        String message;
        long delta = fine - inizio;
        delta = delta / 1000 / 60;

        message = String.format("Task per l'upload delle liste di giorni in %s minuti", delta);
        logger.info(new WrapLog().type(AETypeLog.upload).message(message).usaDb());
    }

}

