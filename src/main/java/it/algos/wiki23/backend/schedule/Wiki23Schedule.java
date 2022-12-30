package it.algos.wiki23.backend.schedule;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaad24.backend.boot.*;
import it.algos.vaad24.backend.schedule.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;

/**
 * Project vaad24
 * Created by Algos
 * User: gac
 * Date: Thu, 29-Dec-2022
 * Time: 17:47
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Wiki23Schedule extends VaadSchedule {


    /**
     * Performing the initialization in a constructor is not suggested as the state of the UI is not properly set up when the constructor is invoked. <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Se viene implementata una sottoclasse, passa di qui per ogni sottoclasse oltre che per questa istanza <br>
     * Se esistono delle sottoclassi, passa di qui per ognuna di esse (oltre a questa classe madre) <br>
     * <p>
     * Elenca tutti i task di questa applicazione <br>
     */
    @PostConstruct
    @Override
    public void startSchedule() throws IllegalStateException {

        //        VaadVar.taskList.add(appContext.getBean(TaskBioReset.class));
        VaadVar.taskList.add(appContext.getBean(TaskBio.class));
        VaadVar.taskList.add(appContext.getBean(TaskGiorni.class));
        VaadVar.taskList.add(appContext.getBean(TaskAnni.class));
        VaadVar.taskList.add(appContext.getBean(TaskAttivita.class));
        VaadVar.taskList.add(appContext.getBean(TaskNazionalita.class));
        //        VaadVar.taskList.add(appContext.getBean(TaskCognomi.class));
        //        VaadVar.taskList.add(appContext.getBean(TaskNomi.class));
        //        VaadVar.taskList.add(appContext.getBean(TaskElaborazione.class));
        //        VaadVar.taskList.add(appContext.getBean(TaskCognomi.class));
        //        VaadVar.taskList.add(appContext.getBean(TaskCognomi.class));

        super.startSchedule();
    }

}
