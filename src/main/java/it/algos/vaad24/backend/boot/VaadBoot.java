package it.algos.vaad24.backend.boot;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.exception.*;
import it.algos.vaad24.backend.interfaces.*;
import it.algos.vaad24.backend.packages.utility.log.*;
import it.algos.vaad24.backend.packages.utility.nota.*;
import it.algos.vaad24.backend.packages.utility.preferenza.*;
import it.algos.vaad24.backend.packages.utility.versione.*;
import it.algos.vaad24.backend.schedule.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.utility.*;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.vaad24.wizard.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.*;
import org.springframework.core.env.*;

import javax.annotation.*;
import javax.servlet.*;
import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 06-mar-2022
 * Time: 08:03
 * <p>
 * Running logic after the Spring context has been initialized <br>
 * Executed on container startup, before any browse command <br>
 * Any class that use the @EventListener annotation, will be executed before the application is up and its
 * onContextRefreshEvent method will be called
 * <p>
 * Questa classe astratta riceve un @EventListener dalla sottoclasse concreta alla partenza del programma <br>
 * Deve essere creata una sottoclasse (obbligatoria) per l' applicazione specifica che: <br>
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
public  class VaadBoot implements ServletContextListener {

    protected boolean allDebugSetup;
    private String property;

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    public AIVers versInstance;

    //    /**
    //     * Istanza di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) <br>
    //     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
    //     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
    //     */
    //    public AIData dataInstance;


    /**
     * Istanza di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    public AIEnumPref prefInstance;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata dal framework SpringBoot/Vaadin usando il metodo setter() <br>
     * al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public Environment environment;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected LogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected MongoService mongoService;

    @Autowired
    protected PreferenceService preferenceService;

    /**
     * Constructor with @Autowired on setter. Usato quando ci sono sottoclassi. <br>
     * Per evitare di avere nel costruttore tutte le property che devono essere iniettate e per poterle aumentare <br>
     * senza dover modificare i costruttori delle sottoclassi, l'iniezione tramite @Autowired <br>
     * viene delegata ad alcuni metodi setter() che vengono qui invocati con valore (ancora) nullo. <br>
     * Al termine del ciclo init() del costruttore il framework SpringBoot/Vaadin, inietterà la relativa istanza <br>
     */
    public VaadBoot() {
        LogService.debug(": [flow     ] - VaadBoot, init()");

        //        this.setMongo(mongo);
        //        this.setLogger(logger)
        //        this.setEnvironment(environment);
        this.setVersInstance(versInstance);
        //        this.setDataInstance(dataInstance);
        this.setPrefInstance(prefInstance);
    }// end of constructor with @Autowired on setter


    /**
     * Performing the initialization in a constructor is not suggested as the state of the UI is not properly set up when the constructor is invoked. <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti <br>
     * L'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se viene implementata una istanza di sottoclasse, passa di qui per ogni istanza <br>
     */
    @PostConstruct
    private void postConstruct() {
        LogService.debug(": [flow     ] - VaadBoot.postConstruct()");
        this.inizia();
    }

        /**
         * The ContextRefreshedEvent happens after both Vaadin and Spring are fully initialized. At the time of this
         * event, the application is ready to service Vaadin requests <br>
         */
        @EventListener(ContextRefreshedEvent.class)
        public void onContextRefreshEvent() {
//            this.inizia();
        }

    /**
     * Primo ingresso nel programma <br>
     * <p>
     * 1) regola alcuni parametri standard del database MongoDB <br>
     * 2) regola le variabili generali dell'applicazione <br>
     * 3) crea le preferenze standard e specifiche dell'applicazione <br>
     * 4) crea i dati di alcune collections sul DB mongo <br>
     * 5) aggiunge al menu le @Route (view) standard e specifiche <br>
     * 6) lancia gli schedulers in background <br>
     * 7) costruisce una versione demo <br>
     * 8) controllare l' esistenza di utenti abilitati all' accesso <br>
     * <p>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void inizia() {
        logger.setUpIni();

        this.fixEnvironment();
        this.fixDBMongo();
        this.fixVariabili();
        this.fixVariabiliRiferimentoIstanzeGenerali();
        this.fixPreferenze();
        this.fixMenuRoutes();
        this.fixData();
        this.fixVersioni();
        this.fixSchedule();
        this.fixLogin();

        logger.setUpEnd();
    }


    /**
     * Controllo di alcune regolazioni
     */
    public void fixEnvironment() {
        String message;
        String databaseName;
        String autoIndexCreation;
        String allDebugSetupTxt;

        if (environment == null) {
            logger.error(new WrapLog().exception(new AlgosException("Manca la property 'environment'")).usaDb());
            return;
        }

        allDebugSetupTxt = environment.getProperty("algos.vaadin23.all.debug.setup");
        if (allDebugSetupTxt != null && allDebugSetupTxt.length() > 0 && allDebugSetupTxt.equals(VERO)) {
            allDebugSetup = true;
        }

        databaseName = environment.getProperty("spring.data.mongodb.database");
        message = String.format("Database mongo in uso: %s", databaseName);
        if (allDebugSetup) {
            logger.info(new WrapLog().message(message).type(AETypeLog.setup));
        }

        autoIndexCreation = environment.getProperty("spring.data.mongodb.auto-index-creation");
        message = String.format("Auto creazione degli indici (per la classi @Document): %s", autoIndexCreation);
        if (allDebugSetup) {
            logger.info(new WrapLog().message(message).type(AETypeLog.setup));
        }
    }

    /**
     * Regola alcuni parametri standard del database MongoDB <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixDBMongo() {
        if (allDebugSetup) {
            mongoService.getMaxBlockingSortBytes();//@todo da controllare
            mongoService.fixMaxBytes();//@todo da controllare
        }
    }

    /**
     * Costruisce alcune istanze generali dell'applicazione e ne mantiene i riferimenti nelle apposite variabili <br>
     * Le istanze (prototype) sono uniche per tutta l' applicazione <br>
     * Vengono create SOLO in questa classe o in una sua sottoclasse <br>
     * La selezione su quale istanza creare tocca a questa sottoclasse xxxBoot <br>
     * Se la sottoclasse non ha creato l'istanza, ci pensa la superclasse <br>
     * Può essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    protected void fixVariabiliRiferimentoIstanzeGenerali() {
        /**
         * Istanza da usare per lo startup del programma <br>
         * Di default VaadData oppure possibile sottoclasse del progetto xxxData <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabiliRiferimentoIstanzeGenerali() del progetto corrente <br>
         */
        VaadVar.istanzaData = VaadVar.istanzaData == null ? appContext.getBean(VaadData.class) : VaadVar.istanzaData;

        /**
         * Classe da usare per gestire le versioni <br>
         * Di default FlowVers oppure possibile sottoclasse del progetto <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        VaadVar.versionClazz = VaadVers.class;
    }

    /**
     * Regola le variabili generali dell' applicazione con il loro valore iniziale di default <br>
     * Le variabili (static) sono uniche per tutta l' applicazione <br>
     * Il loro valore può essere modificato SOLO in questa classe o in una sua sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixVariabili() {

        /**
         * Nome identificativo minuscolo del progetto base vaadin23 <br>
         * Deve essere regolato in backend.boot.VaadBoot.fixVariabili() del progetto base <br>
         */
        VaadVar.projectVaadin24 = PROJECT_VAADIN24;

        /**
         * Nome identificativo minuscolo del modulo base vaad23 <br>
         * Deve essere regolato in backend.boot.VaadBoot.fixVariabili() del progetto base <br>
         */
        VaadVar.moduloVaadin24 = MODULO_VAADIN24;

        /**
         * Nome identificativo minuscolo del modulo dell' applicazione <br>
         * Usato come parte del path delle varie directory <br>
         * Spesso coincide (non obbligatoriamente) con projectNameIdea <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        VaadVar.projectNameModulo = MODULO_VAADIN24;

        /**
         * Lista dei moduli di menu da inserire nel Drawer del MainLayout per le gestione delle @Routes. <br>
         * Regolata dall'applicazione durante l'esecuzione del 'container startup' (non-UI logic) <br>
         * Usata da ALayoutService per conto di MainLayout allo start della UI-logic <br>
         */
        VaadVar.menuRouteList = new ArrayList<>();

        //        /**
        //         * Classe da usare per lo startup del programma <br>
        //         * Di default FlowData oppure possibile sottoclasse del progetto <br>
        //         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() <br>
        //         */
        //        VaadVar.dataClazz = VaadData.class;
        //
        //        /**
        //         * Classe da usare per gestire le versioni <br>
        //         * Di default FlowVers oppure possibile sottoclasse del progetto <br>
        //         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
        //         */
        //        VaadVar.versionClazz = VaadVers.class;

        /**
         * Versione dell' applicazione base vaadflow14 <br>
         * Usato solo internamente <br>
         * Deve essere regolato in backend.boot.VaadBoot.fixVariabili() del progetto corrente <br>
         */
        VaadVar.vaadin23Version = Double.parseDouble(Objects.requireNonNull(environment.getProperty("algos.vaad24.version")));

        /**
         * Controlla se l' applicazione è multi-company oppure no <br>
         * Di default uguale a false <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         * Se usaCompany=true anche usaSecurity deve essere true <br>
         */
        VaadVar.usaCompany = false;

        /**
         * Controlla se l' applicazione usa il login oppure no <br>
         * Se si usa il login, occorre la classe SecurityConfiguration <br>
         * Se non si usa il login, occorre disabilitare l'Annotation @EnableWebSecurity di SecurityConfiguration <br>
         * Di default uguale a false <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         * Se usaCompany=true anche usaSecurity deve essere true <br>
         * Può essere true anche se usaCompany=false <br>
         */
        VaadVar.usaSecurity = false;


        /*
         * Nome identificativo minuscolo del progetto corrente <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        try {
            property = "algos.project.name";
            VaadVar.projectCurrent = Objects.requireNonNull(environment.getProperty(property)).toLowerCase();
        } catch (Exception unErrore) {
            String message = String.format("Non ho trovato la property %s nelle risorse", property);
            logger.warn(new WrapLog().exception(unErrore).message(message).usaDb());
            VaadVar.projectCurrent = "simple";
        }

        /*
         * Nome identificativo maiuscolo dell' applicazione <br>
         * Usato (eventualmente) nella barra di menu in testa pagina <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        try {
            property = "algos.project.name";
            VaadVar.projectNameUpper = Objects.requireNonNull(environment.getProperty(property));
        } catch (Exception unErrore) {
            String message = String.format("Non ho trovato la property %s nelle risorse", property);
            logger.warn(new WrapLog().exception(unErrore).message(message).usaDb());
            VaadVar.projectNameUpper = "Simple";
        }

        /*
         * Nome identificativo minuscolo del progetto corrente <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        try {
            property = "algos.project.nameModulo";
            VaadVar.projectNameModulo = Objects.requireNonNull(environment.getProperty(property)).toLowerCase();
        } catch (Exception unErrore) {
            String message = String.format("Non ho trovato la property %s nelle risorse", property);
            logger.warn(new WrapLog().exception(unErrore).message(message).usaDb());
            VaadVar.projectCurrent = "simple";
        }

        /*
         * Versione dell' applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        try {
            property = "algos.project.version";
            VaadVar.projectVersion = Double.parseDouble(Objects.requireNonNull(environment.getProperty(property)));
        } catch (Exception unErrore) {
            String message = String.format("Non ho trovato la property %s nelle risorse", property);
            logger.warn(new WrapLog().exception(unErrore).message(message).usaDb());
        }

        /*
         * Data di rilascio della versione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        try {
            property = "algos.project.version.date";
            VaadVar.projectDate = Objects.requireNonNull(environment.getProperty(property));
        } catch (Exception unErrore) {
            String message = String.format("Non ho trovato la property %s nelle risorse", property);
            logger.warn(new WrapLog().exception(unErrore).message(message).usaDb());
        }

        /*
         * Note di rilascio della versione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         * Deve essere regolato in backend.boot.xxxBoot.fixVariabili() del progetto corrente <br>
         */
        try {
            property = "algos.project.version.note";
            VaadVar.projectNote = Objects.requireNonNull(environment.getProperty(property));
        } catch (Exception unErrore) {
            String message = String.format("Non ho trovato la property %s nelle risorse", property);
            logger.warn(new WrapLog().exception(unErrore).message(message).usaDb());
        }

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
    protected void fixMenuRoutes() {
        //        VaadVar.menuRouteList.add(HelloWorldView.class);
        //        VaadVar.menuRouteList.add(AboutView.class);
        //        VaadVar.menuRouteList.add(AddressFormView.class);
        //        VaadVar.menuRouteList.add(CarrelloFormView.class);
        //        VaadVar.menuRouteList.add(ContinenteView.class);
        VaadVar.menuRouteList.add(WizardView.class);
        VaadVar.menuRouteList.add(UtilityView.class);
        VaadVar.menuRouteList.add(NotaView.class);
        VaadVar.menuRouteList.add(VersioneView.class);
        VaadVar.menuRouteList.add(LoggerView.class);
        VaadVar.menuRouteList.add(PreferenzaView.class);
    }


    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    @Qualifier(QUALIFIER_VERSION_VAAD)
    public void setVersInstance(final AIVers versInstance) {
        this.versInstance = versInstance;
    }

    //    /**
    //     * Set con @Autowired di una property chiamata dal costruttore <br>
    //     * Istanza di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) <br>
    //     * Chiamata dal costruttore di questa classe con valore nullo <br>
    //     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
    //     */
    //    @Autowired
    //    @Qualifier(QUALIFIER_DATA_VAAD)
    //    public void setDataInstance(final AIData dataInstance) {
    //        this.dataInstance = dataInstance;
    //    }


    /**
     * Set con @Autowired di una property chiamata dal costruttore <br>
     * Istanza di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) <br>
     * Chiamata dal costruttore di questa classe con valore nullo <br>
     * Iniettata dal framework SpringBoot/Vaadin al termine del ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    @Qualifier(QUALIFIER_PREFERENCES_VAAD)
    public void setPrefInstance(final AIEnumPref prefInstance) {
        this.prefInstance = prefInstance;
    }


    /**
     * Inizializzazione dei database di vaadinFlow <br>
     * Inizializzazione dei database del programma specifico <br>
     */
    protected void fixData() {
        //        this.dataInstance.inizia();
    }

    /**
     * Inizializzazione delle versioni standard di vaadinFlow <br>
     * Inizializzazione delle versioni del programma specifico <br>
     */
    protected void fixVersioni() {
        this.versInstance.inizia();
    }

    /**
     * Inizializzazione delle versioni standard di vaadinFlow <br>
     * Inizializzazione delle versioni del programma specifico <br>
     */
    public void fixPreferenze() {
        this.prefInstance.inizia();
    }

    /**
     * Eventuale collegamento <br>
     * Sviluppato nelle sottoclassi <br>
     */
    public void fixLogin() {
    }

    /**
     * Eventuali task <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void fixSchedule() {
        String message;
        String clazzName;
        AESchedule type;
        String desc;
        String pattern;
        String nota;
        String flagText;
        AIGenPref flagTask;
        int nextDays = 0;

        if (VaadVar.taskList != null && VaadVar.taskList.size() > 0) {
            for (VaadTask task : VaadVar.taskList) {
                clazzName = task.getClass().getSimpleName();
                desc = task.getDescrizioneTask();
                type = task.getTypeSchedule();
                pattern = type.getPattern();
                nota = type.getNota();
                nextDays = task.getTypeSchedule().getGiorniNext();
                flagTask = task.getFlagAttivazione();
                if (flagTask == null) {
                    flagText = TASK_NO_FLAG + TASK_FLAG_SEMPRE_ATTIVA;
                }
                else {
                    flagTask.setPreferenceService(preferenceService);
                    if (flagTask.is()) {
                        flagText = flagTask.getKeyCode() + TASK_FLAG_ATTIVA;
                    }
                    else {
                        flagText = flagTask.getKeyCode() + TASK_FLAG_DISATTIVA;
                    }
                }
                message = String.format("%s [%s] %s (+%s)%s%s; eseguita %s", clazzName, pattern, flagText, nextDays, FORWARD, desc, nota);
                logger.info(new WrapLog().message(message).type(AETypeLog.schedule));
            }
        }
        else {
            message = String.format("Nel modulo %s non ci sono 'task'", VaadVar.projectNameUpper);
            logger.info(new WrapLog().message(message).type(AETypeLog.schedule));
        }

        logger.info(new WrapLog().message(VUOTA).type(AETypeLog.setup));
    }


}
