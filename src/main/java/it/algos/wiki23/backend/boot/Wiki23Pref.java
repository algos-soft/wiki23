package it.algos.wiki23.backend.boot;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaad24.backend.boot.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.*;

/**
 * Project Wiki23
 * Created by Algos
 * User: gac
 * Date: ven, 29 apr 22
 * <p>
 * Creazione da code di alcune preferenze del progetto <br>
 */
@SpringComponent
@Qualifier(TAG_WIKI23_PREFERENCES)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Wiki23Pref extends VaadPref {

    /**
     * The ContextRefreshedEvent happens after both Vaadin and Spring are fully initialized. At the time of this
     * event, the application is ready to service Vaadin requests <br>
     */
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshEvent() {
        this.inizia();
    }


    /**
     * Executed on container startup <br>
     * Setup non-UI logic here <br>
     * This method is called prior to the servlet context being initialized (when the Web application is deployed). <br>
     * You can initialize servlet context related data here. <br>
     * Metodo eseguito solo quando l'applicazione viene caricata/parte nel server (Tomcat o altri) <br>
     * Eseguito quindi a ogni avvio/riavvio del server e NON a ogni sessione <br>
     */
    public void inizia() {
        super.inizia();
        for (WPref pref : WPref.getAllEnums()) {
            crea(pref);
        }
    }

    /**
     * Inserimento di una preferenza del progetto specifico <br>
     * Controlla che la entity non esista già <br>
     */
    protected void crea(final WPref pref) {
        crea(pref.getKeyCode(), pref.getType(), pref.getDefaultValue(), pref.getDescrizione(), false,false);
    }


}
