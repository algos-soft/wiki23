package it.algos.wiki23.backend.schedule;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.upload.*;
import it.sauronsoftware.cron4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 08-Aug-2022
 * Time: 14:49
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskAnni extends AlgosTask{

//    /**
//     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
//     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
//     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
//     */
//    @Autowired
//    private UploadAnni upload;

    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
        if (WPref.usaTaskAnni.is()) {
            appContext.getBean(UploadAnni.class).uploadAll();
        }
    }


    /**
     * Descrizione: ogni settimana il pomeriggio del martedì
     * * 0 14 * * Tue
     */
    private static final String PATTERN = "0 14 * * Sat";


    @Override
    public String getPattern() {
        return PATTERN;
    }

}

