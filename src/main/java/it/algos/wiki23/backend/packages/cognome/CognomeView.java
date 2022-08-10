package it.algos.wiki23.backend.packages.cognome;

import com.vaadin.flow.router.*;
import it.algos.vaad23.ui.views.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 10-Aug-2022
 * Time: 08:43
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Cognomi")
@Route(value = "cognome", layout = MainLayout.class)
public class CognomeView extends CrudView {


    //--per eventuali metodi specifici
    private CognomeBackend backend;

    //--per eventuali metodi specifici
    private CognomeDialog dialog;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public CognomeView(@Autowired final CognomeBackend crudBackend) {
        super(crudBackend, Cognome.class);
        this.backend = crudBackend;
    }

    /**
     * Preferenze usate da questa 'view' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.gridPropertyNamesList = Arrays.asList("ordine", "code", "descrizione"); // controllare la congruità con la Entity
        super.formPropertyNamesList = Arrays.asList("code", "descrizione"); // controllare la congruità con la Entity
        super.riordinaColonne = true; //se rimane true, uguale al default, si può cancellare
        super.usaBottoneRefresh = false; //se rimane false, uguale al default, si può cancellare
        super.usaBottoneDeleteReset = false; //se rimane false, uguale al default, si può cancellare
        super.usaBottoneNew = true; //se rimane true, uguale al default, si può cancellare
        super.usaBottoneEdit = true; //se rimane true, uguale al default, si può cancellare
        super.usaBottoneDelete = true; //se rimane true, uguale al default, si può cancellare

        super.dialogClazz = CognomeDialog.class;
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();
        addSpanVerde("Prova di colori");
    }

}// end of crud @Route view class