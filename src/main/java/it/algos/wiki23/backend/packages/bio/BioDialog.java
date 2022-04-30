package it.algos.wiki23.backend.packages.bio;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.ui.views.*;
import it.algos.wiki23.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.vaadin.crudui.crud.*;

import java.util.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: gio, 28-apr-2022
 * Time: 11:57
 * <p>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BioDialog extends CrudDialog {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ElaboraService elaboraService;

    private Bio currentItem;

    /**
     * Constructor not @Autowired. <br>
     * Non utilizzato e non necessario <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Se c'è un SOLO costruttore questo diventa automaticamente @Autowired e IntelliJ Idea 'segna' in rosso i
     * parametri <br>
     * Per evitare il bug (solo visivo), aggiungo un costruttore senza parametri <br>
     */
    public BioDialog() {
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
    public BioDialog(final Bio entityBean, final CrudOperation operation, final CrudBackend crudBackend, final List fields) {
        super(entityBean, operation, crudBackend, fields);
        this.currentItem = entityBean;
    }// end of constructor not @Autowired

    @Override
    protected void fixBottom() {
        super.fixBottom();

        Button elaboraButton = new Button();
        elaboraButton.setText("Elabora");
        elaboraButton.getElement().setAttribute("theme", "error");
        elaboraButton.addClickListener(e -> elabora());
        elaboraButton.setIcon(new Icon(VaadinIcon.AMBULANCE));
        bottomPlaceHolder.add(elaboraButton);

    }

    protected void elabora() {
        currentItem = elaboraService.esegue(currentItem);
    }

}// end of crud Dialog class