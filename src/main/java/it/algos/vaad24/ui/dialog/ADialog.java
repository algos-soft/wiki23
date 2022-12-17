package it.algos.vaad24.ui.dialog;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dialog.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.orderedlayout.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import javax.annotation.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: gio, 28-apr-2022
 * Time: 14:15
 */
public abstract class ADialog extends Dialog {

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

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
    public TextService textService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public HtmlService htmlService;

    protected String titoloDialogo;

    protected String dettaglioFacoltativo;

    protected VerticalLayout topPlaceHolder;

    protected VerticalLayout infoPlaceHolder;

    protected VerticalLayout bodyPlaceHolder;

    protected HorizontalLayout bottomPlaceHolder;


    protected Button annullaButton;

    protected Button confirmButton;

    protected String annullaButtonText;

    protected String confirmButtonText;

    /**
     * Constructor not @Autowired. <br>
     * Non utilizzato e non necessario <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Se c'è un SOLO costruttore questo diventa automaticamente @Autowired e IntelliJ Idea 'segna' in rosso i
     * parametri <br>
     * Per evitare il bug (solo visivo), aggiungo un costruttore senza parametri <br>
     */
    public ADialog() {
    }// end of second constructor not @Autowired


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(CrudDialog.class, operation, itemSaver, itemDeleter, itemAnnulla); <br>
     *
     * @param titoloDialogo obbligatorio
     */
    public ADialog(final String titoloDialogo) {
        this(titoloDialogo, VUOTA);
    }// end of constructor not @Autowired

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(CrudDialog.class, operation, itemSaver, itemDeleter, itemAnnulla); <br>
     *
     * @param titoloDialogo        obbligatorio
     * @param dettaglioFacoltativo opzionale
     */
    public ADialog(final String titoloDialogo, final String dettaglioFacoltativo) {
        this.titoloDialogo = titoloDialogo;
        this.dettaglioFacoltativo = dettaglioFacoltativo;
    }// end of constructor not @Autowired


    /**
     * Performing the initialization in a constructor is not suggested as the state of the UI is not properly set up when the constructor is invoked. <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se viene implementata una sottoclasse, passa di qui per ogni sottoclasse oltre che per questa istanza <br>
     * Se esistono delle sottoclassi, passa di qui per ognuna di esse (oltre a questa classe madre) <br>
     */
    @PostConstruct
    private void postConstruct() {
        //--Preferenze sovrascrivibili
        this.fixPreferenze();

        //--Titolo del dialogo
        this.creaHeader();

        //--Informazioni (eventuali) sotto i titoli
        //--Eventuale descrizione
        this.creaInfo();

        //--Corpo centrale del dialogo
        //--Eventuali componenti di Input
        this.creaBody();

        //--spazio per distanziare i bottoni sottostanti
        this.add(new H3());

        //--Barra dei bottoni, creati e regolati
        this.creaBottom();

    }

    /**
     * Preferenze usate da questo dialogo <br>
     * Primo metodo chiamato dopo AfterNavigationEvent <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void fixPreferenze() {
        this.annullaButtonText = "Annulla";
        this.confirmButtonText = "Conferma";
    }

    /**
     * Titolo del dialogo <br>
     * Eventuale descrizione aggiuntiva (in corpo ridotto) <br>
     */
    protected void creaHeader() {
        topPlaceHolder = new VerticalLayout();
        topPlaceHolder.setPadding(false);
        topPlaceHolder.setSpacing(true);
        topPlaceHolder.setMargin(false);

        topPlaceHolder.add(new H2(titoloDialogo));
        topPlaceHolder.add(new Label(dettaglioFacoltativo));
        this.add(topPlaceHolder);
    }

    public void creaInfo() {
        infoPlaceHolder = new VerticalLayout();
        infoPlaceHolder.setPadding(false);
        infoPlaceHolder.setSpacing(false);
        infoPlaceHolder.setMargin(false);
        this.add(infoPlaceHolder);
    }

    /**
     * Corpo del dialogo <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void creaBody() {
        bodyPlaceHolder = new VerticalLayout();
        bodyPlaceHolder.setPadding(false);
        bodyPlaceHolder.setSpacing(true);
        bodyPlaceHolder.setMargin(false);

        this.add(bodyPlaceHolder);
    }

    /**
     * Barra dei bottoni <br>
     */
    protected void creaBottom() {
        bottomPlaceHolder = new HorizontalLayout();
        bottomPlaceHolder.setClassName("buttons");
        bottomPlaceHolder.setPadding(false);
        bottomPlaceHolder.setSpacing(true);
        bottomPlaceHolder.setMargin(false);
        bottomPlaceHolder.setClassName("confirm-dialog-buttons");

        Label spazioVuotoEspandibile = new Label("");

        annullaButton = new Button();
        annullaButton.setText(annullaButtonText);
        //        annullaButton.getElement().setProperty("title", "Shortcut SHIFT");
        //        annullaButton.getElement().setAttribute("theme", operation == CrudOperation.ADD ? "secondary" : "primary");
        annullaButton.addClickListener(e -> annullaHandler());
        annullaButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
        bottomPlaceHolder.add(annullaButton);

        confirmButton = new Button();
        confirmButton.setText(confirmButtonText);
        confirmButton.getElement().setAttribute("theme", "primary");
        confirmButton.addClickListener(e -> confirmHandler());
        confirmButton.setIcon(new Icon(VaadinIcon.CHECK));
        confirmButton.setEnabled(false);
        bottomPlaceHolder.add(confirmButton);

        bottomPlaceHolder.setFlexGrow(1, spazioVuotoEspandibile);

        //--Controlla la visibilità dei bottoni
        //        saveButton.setVisible(operation == CrudOperation.ADD || operation == CrudOperation.UPDATE);

        this.add(bottomPlaceHolder);
    }


    /**
     * Opens the given item for editing in the dialog. <br>
     * Crea i fields e visualizza il dialogo <br>
     * Gli handler vengono aggiunti qui perché non passano come parametri di appContext.getBean(PreferenzaDialog.class) <br>
     * La view è già pronta, i listener anche e rimane solo da lanciare il metodo open() nella superclasse <br>
     *
     */
    public void open() {
        super.open();
    }


    public void annullaHandler() {
        close();
    }

    public void confirmHandler() {
        close();
    }

}
