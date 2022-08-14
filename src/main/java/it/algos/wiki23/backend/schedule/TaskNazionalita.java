package it.algos.wiki23.backend.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.upload.*;
import it.sauronsoftware.cron4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Thu, 07-Jul-2022
 * Time: 19:48
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskNazionalita extends AlgosTask {



    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
            if (WPref.usaTaskNazionalita.is()) {
                appContext.getBean(UploadNazionalita.class).uploadAll();
            }
    }


    /**
     * Descrizione: ogni settimana il pomeriggio del martedì
     * * 0 14 * * Tue
     */
    private static final String PATTERN = "0 14 * * Tue";


    @Override
    public String getPattern() {
        return PATTERN;
    }

}
