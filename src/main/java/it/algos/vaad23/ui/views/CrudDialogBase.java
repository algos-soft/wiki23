package it.algos.vaad23.ui.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.datepicker.*;
import com.vaadin.flow.component.datetimepicker.*;
import com.vaadin.flow.component.formlayout.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.timepicker.*;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.converter.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.vaad23.ui.dialog.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.*;
import org.vaadin.crudui.crud.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: Sat, 15-Oct-2022
 * Time: 14:44
 */
@SpringComponent
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CrudDialogBase extends CrudDialog {

    protected final H2 titleField = new H2();

    /**
     * Corpo centrale del Form <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected final FormLayout formLayout = new FormLayout();

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
    public TextService textService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnotationService annotationService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ReflectionService reflectionService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public HtmlService htmlService;

    @Autowired
    public LogService logger;

    protected CrudBackend crudBackend;

    protected String textAnnullaButton = "Annulla";

    protected String textSaveButton = "Registra";

    protected String textDeleteButton = "Cancella";

    protected Button annullaButton = new Button(textAnnullaButton);

    protected Button saveButton = new Button(textSaveButton);

    protected Button deleteButton = new Button(textDeleteButton);

    protected BiConsumer<AEntity, CrudOperation> saveHandler;

    protected Consumer<AEntity> deleteHandler;

    protected Consumer<AEntity> annullaHandler;

    protected CrudOperation operation;

    protected List<String> fields;

    protected boolean usaUnaSolaColonna = true;

    protected HorizontalLayout bottomPlaceHolder;

    /**
     * Constructor not @Autowired. <br>
     * Non utilizzato e non necessario <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Se c'è un SOLO costruttore questo diventa automaticamente @Autowired e IntelliJ Idea 'segna' in rosso i
     * parametri <br>
     * Per evitare il bug (solo visivo), aggiungo un costruttore senza parametri <br>
     */
    public CrudDialogBase() {
    }// end of second constructor not @Autowired

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(CrudDialog.class, operation, itemSaver, itemDeleter, itemAnnulla); <br>
     *
     * @param entityBean  The item to edit; it may be an existing or a newly created instance
     * @param operation   The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     * @param fields      da costruire in automatico
     */
    public CrudDialogBase(final AEntity entityBean, final CrudOperation operation, final CrudBackend crudBackend, final List<String> fields) {
        this(entityBean, operation, crudBackend, fields, true);
    }// end of constructor not @Autowired

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(CrudDialog.class, operation, itemSaver, itemDeleter, itemAnnulla); <br>
     *
     * @param entityBean        The item to edit; it may be an existing or a newly created instance
     * @param operation         The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
     * @param crudBackend       service specifico per la businessLogic e il collegamento con la persistenza dei dati
     * @param fields            da costruire in automatico
     * @param usaUnaSolaColonna di default=true
     */
    public CrudDialogBase(final AEntity entityBean, final CrudOperation operation, final CrudBackend crudBackend, final List<String> fields, final boolean usaUnaSolaColonna) {
        this.currentItem = entityBean;
        this.operation = operation;
        this.crudBackend = crudBackend;
        this.fields = fields;
        this.usaUnaSolaColonna = usaUnaSolaColonna;
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
//    @PostConstruct
    private void postConstruct() {
        //--Titolo placeholder del dialogo
        this.add(fixHeader());

        //--Form placeholder standard per i campi
        this.add(getFormLayout());

        //--Crea un nuovo binder (vuoto) per questo Dialog e questa entityBean (currentItem)
        binder = new BeanValidationBinder(currentItem.getClass());

        //--Crea i fields
        //--Corpo centrale del Dialog
        fixBody();

        // Regola il binder
        // Updates the value in each bound field component
        fixBinder();

        //--spazio per distanziare i bottoni sottostanti
        this.add(new H3());

        //--Barra placeholder dei bottoni, creati e regolati
        this.fixBottom();
    }

    /**
     * Titolo del dialogo <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected Component fixHeader() {
        String tag = switch (operation) {
            case READ -> "Mostra";
            case ADD -> "Nuova";
            case UPDATE -> "Modifica";
            case DELETE -> "Cancella";
        };

        return new H2(String.format("%s %s", tag, currentItem.getClass().getSimpleName().toLowerCase()));
    }

    /**
     * Body placeholder per i campi <br>
     * Normalmente colonna doppia <br>
     */
    protected Div getFormLayout() {
        // Use one column by default
        if (usaUnaSolaColonna) {
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        }
        else {
            formLayout.setResponsiveSteps(
                    // Use one column by default
                    new FormLayout.ResponsiveStep("0", 1),
                    // Use two columns, if layout's width exceeds 500px
                    new FormLayout.ResponsiveStep("500px", 2)
            );
        }

        formLayout.addClassName("no-padding");
        Div div = new Div(formLayout);
        div.addClassName("has-padding");

        return div;
    }


    /**
     * Crea i fields
     * Inizializza le properties grafiche (caption, visible, editable, width, ecc)
     * Aggiunge i fields al binder
     * Aggiunge eventuali fields specifici direttamente al layout grafico (senza binder e senza fieldMap)
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void fixBody() {
        AETypeField type;
        AbstractSinglePropertyField field;
        Class enumClazz;
        Class linkClazz;
        boolean hasFocus = false;
        String caption;
        List enumObjects;
        List items;
        ComboBox combo;
        boolean nullSelectionAllowed;

        try {
            for (String key : fields) {
                type = annotationService.getFormType(currentItem.getClass(), key);
                hasFocus = annotationService.hasFocus(currentItem.getClass(), key);
                caption = annotationService.getCaption(currentItem.getClass(), key); ;
                nullSelectionAllowed = annotationService.nullSelectionAllowed(currentItem.getClass(), key); ;

                field = switch (type) {
                    case text -> new TextField(caption);
                    case integer -> new IntegerField(caption);
                    case lungo -> new TextField(caption);
                    case booleano -> new Checkbox(caption);
                    case enumeration -> {
                        combo = new ComboBox(caption);
                        combo.setClearButtonVisible(nullSelectionAllowed);
                        try {
                            enumClazz = annotationService.getEnumClazz(currentItem.getClass(), key);
                            Object[] elementi = enumClazz.getEnumConstants();
                            if (elementi != null) {
                                enumObjects = Arrays.asList(elementi);
                                combo.setItems(enumObjects);
                            }
                        } catch (Exception unErrore) {
                            logger.error(new WrapLog().exception(unErrore).usaDb());
                        }
                        yield combo;
                    }
                    case link -> {
                        combo = new ComboBox(caption);
                        combo.setClearButtonVisible(nullSelectionAllowed);

                        try {
                            linkClazz = annotationService.getLinkClazz(currentItem.getClass(), key);
                            Object obj = appContext.getBean(linkClazz);
                            if (obj instanceof CrudBackend backend) {
                                items = backend.findAll();
                                if (items != null) {
                                    combo.setItems(items);
                                }
                            }
                        } catch (Exception unErrore) {
                            logger.error(new WrapLog().exception(unErrore).usaDb());
                        }
                        yield combo;
                    }
                    case localDateTime -> new DateTimePicker(caption);
                    case localDate -> new DatePicker(caption);
                    case localTime -> new TimePicker(caption);
                    default -> {
                        logger.error(new WrapLog().exception(new AlgosException("Manca il case dello switch")).usaDb());
                        yield new TextField(key);
                    }
                };

                formLayout.add(field);
                if (type == AETypeField.lungo) {
                    String message = String.format("%s deve contenere solo caratteri numerici", key);
                    StringToLongConverter longConverter = new StringToLongConverter(0L, message);
                    binder.forField(field)
                            .withConverter(longConverter)
                            .bind(key);
                }
                else {
                    binder.forField(field).bind(key);
                }

                if (hasFocus && field instanceof TextField textField) {
                    textField.focus();
                    textField.setAutoselect(true);
                }
            }
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
        }
    }


    /**
     * Legge la entityBean e inserisce nella UI i valori di eventuali fields NON associati al binder
     */
    protected void fixBinder() {
        try {
            binder.bindInstanceFields(this);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
        }

        // Updates the value in each bound field component
        try {
            binder.readBean(currentItem);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
        }
    }

    /**
     * Barra dei bottoni <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected void fixBottom() {
        bottomPlaceHolder = new HorizontalLayout();
        bottomPlaceHolder.setClassName("buttons");
        bottomPlaceHolder.setPadding(false);
        bottomPlaceHolder.setSpacing(true);
        bottomPlaceHolder.setMargin(false);
        bottomPlaceHolder.setClassName("confirm-dialog-buttons");

        Label spazioVuotoEspandibile = new Label("");

        annullaButton.setText(textAnnullaButton);
        //        annullaButton.getElement().setProperty("title", "Shortcut SHIFT");
        annullaButton.getElement().setAttribute("theme", operation == CrudOperation.ADD ? "secondary" : "primary");
        annullaButton.addClickListener(e -> annullaHandler());
        annullaButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
        bottomPlaceHolder.add(annullaButton);

        if (operation == CrudOperation.ADD || operation == CrudOperation.UPDATE) {
            saveButton.setText(textSaveButton);
            saveButton.getElement().setAttribute("theme", operation == CrudOperation.ADD ? "primary" : "secondary");
            saveButton.addClickListener(e -> saveHandler());
            saveButton.setIcon(new Icon(VaadinIcon.CHECK));
            bottomPlaceHolder.add(saveButton);
        }

        if (operation == CrudOperation.DELETE) {
            deleteButton.setText(textDeleteButton);
            deleteButton.getElement().setAttribute("theme", "error");
            deleteButton.addClickListener(e -> deleteHandler());
            deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
            deleteButton.getElement().setProperty("title", "Shortcut SHIFT+D");
            deleteButton.addClickShortcut(Key.KEY_D, KeyModifier.SHIFT);
            bottomPlaceHolder.add(deleteButton);
        }

        switch (operation) {
            case READ -> {}
            case ADD -> {
                annullaButton.getElement().setProperty("title", "Shortcut SHIFT+freccia sinistra");
                annullaButton.addClickShortcut(Key.ARROW_LEFT, KeyModifier.SHIFT);
                saveButton.getElement().setProperty("title", "Shortcut ENTER");
                saveButton.addClickShortcut(Key.ENTER);
            }
            case UPDATE -> {
                annullaButton.getElement().setProperty("title", "Shortcut ENTER");
                annullaButton.addClickShortcut(Key.ENTER);
                saveButton.getElement().setProperty("title", "Shortcut SHIFT+ENTER");
                saveButton.addClickShortcut(Key.ENTER, KeyModifier.SHIFT);
            }
            case DELETE -> {
                annullaButton.getElement().setProperty("title", "Shortcut ENTER");
                annullaButton.addClickShortcut(Key.ENTER);
            }
        }

        bottomPlaceHolder.setFlexGrow(1, spazioVuotoEspandibile);

        //--Controlla la visibilità dei bottoni
        saveButton.setVisible(operation == CrudOperation.ADD || operation == CrudOperation.UPDATE);

        this.add(bottomPlaceHolder);
    }

    /**
     * Opens the given item for editing in the dialog. <br>
     * Crea i fields e visualizza il dialogo <br>
     * Gli handler vengono aggiunti qui perché non passano come parametri di appContext.getBean(PreferenzaDialog.class) <br>
     * La view è già pronta, i listener anche e rimane solo da lanciare il metodo open() nella superclasse <br>
     *
     * @param saveHandler    funzione associata al bottone 'accetta' ('registra', 'conferma')
     * @param annullaHandler funzione associata al bottone 'annulla' (bottone obbligatorio, azione facoltativa)
     */
    public void open(final BiConsumer<AEntity, CrudOperation> saveHandler, final Consumer<AEntity> annullaHandler) {
        this.open(saveHandler, null, annullaHandler);
    }

    /**
     * Opens the given item for editing in the dialog. <br>
     * Crea i fields e visualizza il dialogo <br>
     * Gli handler vengono aggiunti qui perché non passano come parametri di appContext.getBean(PreferenzaDialog.class) <br>
     * La view è già pronta, i listener anche e rimane solo da lanciare il metodo open() nella superclasse <br>
     *
     * @param saveHandler    funzione associata al bottone 'accetta' ('registra', 'conferma')
     * @param deleteHandler  funzione associata al bottone 'delete' (eventuale)
     * @param annullaHandler funzione associata al bottone 'annulla' (bottone obbligatorio, azione facoltativa)
     */
    public void open(final BiConsumer<AEntity, CrudOperation> saveHandler, final Consumer<AEntity> deleteHandler, final Consumer<AEntity> annullaHandler) {
        this.saveHandler = saveHandler;
        this.deleteHandler = deleteHandler;
        this.annullaHandler = annullaHandler;

        super.open();
    }


    public void saveHandler() {
        try {
            if (binder.writeBeanIfValid(currentItem)) {
                binder.writeBean(currentItem);
            }
            else {
                logger.info(new WrapLog().exception(new AlgosException("binder non valido")));
                return;
            }
        } catch (ValidationException error) {
            logger.error(error);
            return;
        }
        crudBackend.update(currentItem);
        switch (operation) {
            case ADD -> Avviso.show("Aggiunto un elemento").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            case UPDATE -> Avviso.show("Registrata la modifica").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            default -> Notification.show("Caso non previsto").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        if (saveHandler != null) {
            saveHandler.accept(currentItem, operation);
        }
        close();
    }

    public void deleteHandler() {
        AEntity item = currentItem;
        if (deleteHandler != null) {
            deleteHandler.accept(item);
        }
        close();
    }

    public void annullaHandler() {
        switch (operation) {
            case ADD -> Avviso.show("Non registrato").addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            case READ -> Avviso.show("Letto").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            case UPDATE -> Avviso.show("Non modificato").addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            case DELETE -> Avviso.show("Non cancellato").addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            default -> Notification.show("Caso non previsto").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        if (annullaHandler != null) {
            annullaHandler.accept(currentItem);
        }
        close();
    }

}// end of crud generic Dialog class