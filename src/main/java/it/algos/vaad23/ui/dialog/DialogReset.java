package it.algos.vaad23.ui.dialog;

import com.vaadin.flow.component.*;
import com.vaadin.flow.spring.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 01-mag-2022
 * Time: 09:40
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DialogReset extends ADialog {

    protected static final String TITOLO = "Reset";

    protected static final String DESCRIZIONE = "Ripristina nel database i valori di default\nannullando le eventuali modifiche apportate" +
            " successivamente";

    protected Runnable confirmHandler;

    /**
     * Constructor not @Autowired. <br>
     * Non utilizzato e non necessario <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Se c'è un SOLO costruttore questo diventa automaticamente @Autowired e IntelliJ Idea 'segna' in rosso i
     * parametri <br>
     * Per evitare il bug (solo visivo), aggiungo un costruttore senza parametri <br>
     */
    public DialogReset() {
        super(TITOLO, DESCRIZIONE);
    }// end of second constructor not @Autowired

    @Override
    protected void creaBottom() {
        super.creaBottom();

        annullaButton.getElement().setAttribute("theme", "primary");
        annullaButton.addClickShortcut(Key.ENTER);
        confirmButton.getElement().setAttribute("theme", "error");
        confirmButton.setEnabled(true);
    }

    /**
     * Apertura del dialogo <br>
     */
    public void open(final Runnable confirmHandler) {
        this.confirmHandler = confirmHandler;
        super.open();
    }

    public void confirmHandler() {
        close();
        confirmHandler.run();
    }

}
