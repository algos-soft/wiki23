package it.algos.vaad24.backend.boot;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.exception.*;
import it.algos.vaad24.backend.interfaces.*;
import it.algos.vaad24.backend.packages.utility.preferenza.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.*;

import javax.servlet.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: mer, 30-mar-2022
 * Time: 20:55
 * Creazione da code di alcune preferenze di base <br>
 */
@SpringComponent
@Qualifier(QUALIFIER_PREFERENCES_VAAD)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class VaadPref implements AIEnumPref, ServletContextListener {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public PreferenzaBackend backend;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService textService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public LogService logger;

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
        for (Pref pref : Pref.getAllEnums()) {
            crea(pref);
        }
    }

    /**
     * Inserimento di una preferenza del progetto base Vaadin23 <br>
     * Controlla che la entity non esista già <br>
     */
    protected void crea(final Pref pref) {
        crea(pref.getKeyCode(), pref.getType(), pref.getDefaultValue(), pref.getDescrizione(), false, true);
    }

    /**
     * Inserimento di una preferenza del progetto base Vaadin23 <br>
     * Controlla che la entity non esista già <br>
     */
    protected void crea(final String keyCode, final AETypePref type, Object value, final String descrizione,
                        final boolean needRiavvio, final boolean vaad23) {
        Preferenza preferenza = null;
        String message;

        if (textService.isEmpty(keyCode)) {
            logger.error(new WrapLog().exception(new AlgosException("Manca il keyCode")).usaDb());
            return;
        }
        if (type == null) {
            message = String.format("Manca il type nella preferenza %s", keyCode);
            logger.error(new WrapLog().exception(new AlgosException(message)).usaDb());
            return;
        }
        if (textService.isEmpty(descrizione)) {
            message = String.format("Manca la descrizione nella preferenza %s", keyCode);
            logger.error(new WrapLog().exception(new AlgosException(message)).usaDb());
            return;
        }
        if (value == null) {
            message = String.format("Il valore della preferenza %s è nullo", keyCode);
            logger.error(new WrapLog().exception(new AlgosException(message)).usaDb());
            return;
        }

        if (backend.existsByKeyCode(keyCode)) {
            return;
        }

        if (type == AETypePref.enumerationType && value instanceof AITypePref) {
            Object obj = ((AITypePref) value).getPref();
            if (obj instanceof String valueTxt) {
                value = valueTxt;
            }
        }

        preferenza = new Preferenza();
        preferenza.code = keyCode;
        preferenza.type = type;
        preferenza.value = type.objectToBytes(value);
        preferenza.vaad23 = vaad23;
        preferenza.usaCompany = false;
        preferenza.needRiavvio = needRiavvio;
        preferenza.visibileAdmin = false;
        preferenza.descrizione = descrizione;
        preferenza.descrizioneEstesa = descrizione;
        preferenza.enumClazzName = AETypeLog.class.getSimpleName();

        backend.add(preferenza);
    }

}
