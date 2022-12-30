package it.algos.wiki23.backend.boot;

import com.vaadin.flow.component.map.configuration.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaad24.backend.boot.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.interfaces.*;
import it.algos.vaad24.backend.packages.geografia.continente.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.packages.anno.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.cognome.*;
import it.algos.wiki23.backend.packages.doppionome.*;
import it.algos.wiki23.backend.packages.errore.*;
import it.algos.wiki23.backend.packages.genere.*;
import it.algos.wiki23.backend.packages.giorno.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.packages.pagina.*;
import it.algos.wiki23.backend.packages.professione.*;
import it.algos.wiki23.backend.packages.statistica.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.*;
import org.springframework.context.event.EventListener;

import javax.servlet.*;
import java.util.*;

/**
 * Project Wiki23
 * Created by Algos
 * User: gac
 * Date: ven, 29 apr 22
 * <p>
 * Questa classe astratta riceve un @EventListener implementato nella superclasse <br>
 * 1) regola alcuni parametri standard del database MongoDB <br>
 * 2) regola le variabili generali dell'applicazione <br>
 * 3) crea i dati di alcune collections sul DB mongo <br>
 * 4) crea le preferenze standard e specifiche dell'applicazione <br>
 * 5) aggiunge le @Route (view) standard e specifiche <br>
 * 6) lancia gli schedulers in background <br>
 * 7) costruisce una versione demo <br>
 * 8) controlla l' esistenza di utenti abilitati all' accesso <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Wiki23Boot extends VaadBoot implements ServletContextListener {

    private String property;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public LogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public QueryService queryService;


    /**
     * The ContextRefreshedEvent happens after both Vaadin and Spring are fully initialized. At the time of this
     * event, the application is ready to service Vaadin requests <br>
     */
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshEvent() {
        this.inizia();
    }


    /**
     * Costruisce alcune istanze generali dell'applicazione e ne mantiene i riferimenti nelle apposite variabili <br>
     * Le istanze (prototype) sono uniche per tutta l' applicazione <br>
     * Vengono create SOLO in questa classe o in una sua sottoclasse <br>
     * La selezione su quale istanza creare tocca a questa sottoclasse xxxBoot <br>
     * Se la sottoclasse non ha creato l'istanza, ci pensa la superclasse <br>
     * Può essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    @Override
    protected void fixVariabiliRiferimentoIstanzeGenerali() {
        /*
         * Classe da usare per lo startup del programma <br>
         * Di default FlowData oppure possibile sottoclasse del progetto <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabiliRiferimentoIstanzeGenerali() del progetto corrente <br>
         */
        VaadVar.istanzaData = appContext.getBean(Wiki23Data.class);

        /*
         * Classe da usare per gestire le versioni <br>
         * Di default FlowVers oppure possibile sottoclasse del progetto <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        VaadVar.versionClazz = Wiki23Vers.class;

        super.fixVariabiliRiferimentoIstanzeGenerali();
    }

    /**
     * Regola le variabili generali dell' applicazione con il loro valore iniziale di default <br>
     * Le variabili (static) sono uniche per tutta l' applicazione <br>
     * Il loro valore può essere modificato SOLO in questa classe o in una sua sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixVariabili() {
        super.fixVariabili();

        /**
         * Nome identificativo minuscolo del progetto corrente <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        VaadVar.projectCurrent = "wiki23";


        /**
         * Nome della classe di partenza col metodo 'main' <br>
         * Spesso coincide (non obbligatoriamente) con projectCurrent + Application <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        VaadVar.projectCurrentMainApplication = PROJECT_CURRENT_MAIN_APPLICATION;

        /**
         * Nome identificativo maiuscolo dell' applicazione <br>
         * Usato (eventualmente) nella barra di menu in testa pagina <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        VaadVar.projectNameUpper = "Wiki23";

        /**
         * Nome identificativo minuscolo del modulo dell' applicazione <br>
         * Usato come parte del path delle varie directory <br>
         * Spesso coincide (non obbligatoriamente) con projectNameIdea <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        VaadVar.projectNameModulo = "wiki23";

        /**
              * Classe da usare per gestire le versioni <br>
              * Di default FlowVers oppure possibile sottoclasse del progetto <br>
              * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
              */
        VaadVar.versionClazz = Wiki23Vers.class;

        /**
         * Versione dell' applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        try {
            property = "algos.wiki23.version";
            VaadVar.projectVersion = Double.parseDouble(Objects.requireNonNull(environment.getProperty(property)));
        } catch (Exception unErrore) {
            String message = String.format("Non ho trovato la property %s nelle risorse", property);
            logger.warn(new WrapLog().exception(unErrore).message(message).usaDb());
        }

        /**
         * Data di rilascio della versione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        try {
            property = "algos.wiki23.version.date";
            VaadVar.projectDate = Objects.requireNonNull(environment.getProperty(property));
        } catch (Exception unErrore) {
            String message = String.format("Non ho trovato la property %s nelle risorse", property);
            logger.warn(new WrapLog().exception(unErrore).message(message).usaDb());
        }

        /**
         * Note di rilascio della versione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        try {
            property = "algos.wiki23.version.note";
            VaadVar.projectNote = Objects.requireNonNull(environment.getProperty(property));
        } catch (Exception unErrore) {
            String message = String.format("Non ho trovato la property %s nelle risorse", property);
            logger.warn(new WrapLog().exception(unErrore).message(message).usaDb());
        }
    }

    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza di una classe @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    @Qualifier(TAG_WIKI23_VERSION)
    public void setVersInstance(final AIVers versInstance) {
        this.versInstance = versInstance;
    }

    @Autowired
    @Qualifier(TAG_WIKI23_PREFERENCES)
    public void setPrefInstance(final AIEnumPref prefInstance) {
        this.prefInstance = prefInstance;
    }

    /**
     * Aggiunge al menu le @Route (view) standard e specifiche <br>
     * Questa classe viene invocata PRIMA della chiamata del browser <br>
     * <p>
     * Nella sottoclasse che invoca questo metodo, aggiunge le @Route (view) specifiche dell' applicazione <br>
     * Le @Route vengono aggiunte ad una Lista statica mantenuta in VaadVar <br>
     * Verranno lette da MainLayout la prima volta che il browser 'chiama' una view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixMenuRoutes() {
        super.fixMenuRoutes();
        VaadVar.menuRouteList.remove(ContinenteView.class);

        VaadVar.menuRouteList.add(GenereView.class);
        VaadVar.menuRouteList.add(AttivitaView.class);
        VaadVar.menuRouteList.add(NazionalitaView.class);
        VaadVar.menuRouteList.add(ProfessioneView.class);
        VaadVar.menuRouteList.add(DoppionomeView.class);
        VaadVar.menuRouteList.add(BioView.class);
        VaadVar.menuRouteList.add(AnnoWikiView.class);
        VaadVar.menuRouteList.add(GiornoWikiView.class);
        VaadVar.menuRouteList.add(CognomeView.class);
        VaadVar.menuRouteList.add(StatisticaBioView.class);
        VaadVar.menuRouteList.add(ErroreBioView.class);
        VaadVar.menuRouteList.add(PaginaView.class);
    }

//    /**
//     * Eventuali task <br>
//     * Sviluppato nelle sottoclassi <br>
//     */
//    @Override
//    public void fixSchedule() {
//        super.fixSchedule();
//        String message;
//
//        message = String.format("Nel modulo %s ci sono otto 'task'", VaadVar.projectNameUpper);
//        logger.info(new WrapLog().message(message).type(AETypeLog.schedule));
//        logger.info(new WrapLog().message("Pippo").type(AETypeLog.schedule));
//        logger.info(new WrapLog().message("Pluto").type(AETypeLog.schedule));
//        logger.info(new WrapLog().message(VUOTA).type(AETypeLog.setup));
//    }

    @Override
    public void fixLogin() {
        queryService.logAsBot();
    }

}
