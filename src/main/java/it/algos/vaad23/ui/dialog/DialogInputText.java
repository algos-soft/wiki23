package it.algos.vaad23.ui.dialog;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.function.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: gio, 28-apr-2022
 * Time: 19:51
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DialogInputText extends ADialog {

    protected static final String TITOLO = "Input";

    protected static final String CAPTION = "Testo di input";

    protected TextField textField;

    protected String captionTextField = CAPTION;

    protected Consumer<String> confirmHandler;

    /**
     * Constructor not @Autowired. <br>
     * Non utilizzato e non necessario <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Se c'è un SOLO costruttore questo diventa automaticamente @Autowired e IntelliJ Idea 'segna' in rosso i
     * parametri <br>
     * Per evitare il bug (solo visivo), aggiungo un costruttore senza parametri <br>
     */
    public DialogInputText() {
        this(TITOLO);
    }// end of second constructor not @Autowired

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(CrudDialog.class, operation, itemSaver, itemDeleter, itemAnnulla); <br>
     *
     * @param titoloDialogo obbligatorio
     */
    public DialogInputText(final String titoloDialogo) {
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
    public DialogInputText(final String titoloDialogo, final String dettaglioFacoltativo) {
        super(titoloDialogo, dettaglioFacoltativo);
    }// end of constructor not @Autowired


    @Override
    protected void creaBody() {
        super.creaBody();

        textField = new TextField(captionTextField);
        textField.setWidth("16em");
        textField.setAutofocus(true);
        textField.addValueChangeListener(this::sincro);
        bodyPlaceHolder.add(textField);
    }

    protected void sincro(HasValue.ValueChangeEvent event) {
        confirmButton.setEnabled(textService.isValid((textField.getValue())));
    }

    /**
     * Apertura del dialogo <br>
     */
    public void open(final Consumer<String> confirmHandler) {
        this.confirmHandler = confirmHandler;
        this.getElement().getStyle().set("background-color", "#ffffff");

        super.open();
    }

    public void annullaHandler() {
        close();
    }

    public void confirmHandler() {
        close();
        confirmHandler.accept(textField.getValue());
    }

}
