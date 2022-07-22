package it.algos.wiki23.backend.packages.attivita;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.vaad23.ui.views.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.crudui.crud.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: lun, 09-mag-2022
 * Time: 17:14
 * <p>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AttivitaDialog extends CrudDialog {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AttivitaBackend backend;

    private Attivita entityBean;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public HtmlService htmlService;

    /**
     * Constructor not @Autowired. <br>
     * Non utilizzato e non necessario <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Se c'è un SOLO costruttore questo diventa automaticamente @Autowired e IntelliJ Idea 'segna' in rosso i
     * parametri <br>
     * Per evitare il bug (solo visivo), aggiungo un costruttore senza parametri <br>
     */
    public AttivitaDialog() {
    }// end of second constructor not @Autowired

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(PreferenzaDialog.class, operation, itemSaver, itemDeleter, itemAnnulla); <br>
     *
     * @param entityBean  The item to edit; it may be an existing or a newly created instance
     * @param operation   The operation being performed on the item (addNew, edit, editNoDelete, editDaLink, showOnly)
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     * @param fields      da costruire in automatico
     */
    public AttivitaDialog(final Attivita entityBean, final CrudOperation operation, final CrudBackend crudBackend, final List fields) {
        super(entityBean, operation, crudBackend, fields);
        this.entityBean = entityBean;
    }// end of constructor not @Autowired

    /**
     * Titolo del dialogo <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected Component fixHeader() {
        return new H2("Mostra le attività singolari associate a un'attività plurale");
    }

    @Override
    protected void fixBinder() {
        super.fixBinder();

        List<Attivita> lista = backend.findAllByPlurale(entityBean.paragrafo);
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setMargin(false);
        layout.add(htmlService.getSpan(new WrapSpan("Attività singolari associate").color(AETypeColor.blu).lineHeight(AELineHeight.em3)));
        for (Attivita attivita : lista) {
            layout.add(htmlService.getSpan(new WrapSpan(attivita.singolare).color(AETypeColor.verde).lineHeight(AELineHeight.em3)));
        }

        formLayout.add(new H6());
        formLayout.add(layout);
    }

    /**
     * Barra dei bottoni <br>
     * Placeholder (eventuale, presente di default) <br>
     */
    protected void fixBottom() {
        super.fixBottom();
        annullaButton.addClickShortcut(Key.ENTER);
    }

}// end of crud Dialog class