package it.algos.vaad24.backend.packages.crono.secolo;

import ch.carnet.kasparscherrer.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.ui.dialog.*;
import it.algos.vaad24.ui.views.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 01-mag-2022
 * Time: 21:24
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Secoli")
@Route(value = TAG_SECOLO, layout = MainLayout.class)
public class SecoloView extends CrudView {


    //--per eventuali metodi specifici
    private SecoloBackend backend;


    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public SecoloView(@Autowired final SecoloBackend crudBackend) {
        super(crudBackend, Secolo.class);
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

        super.gridPropertyNamesList = Arrays.asList("ordine", "nome", "inizio", "fine", "anteCristo");
        super.formPropertyNamesList = Arrays.asList("ordine", "nome", "inizio", "fine", "anteCristo");

        super.usaRowIndex = false;
        super.usaBottoneDeleteReset = true;
        super.usaReset = true;
        super.usaBottoneNew = false;
        super.usaBottoneEdit = false;
        super.usaBottoneSearch = false;
        super.usaBottoneDelete = false;
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();

        addSpan(ASpan.text("L'anno zero non esiste").blue().bold());
        addSpan(ASpan.text("L'anno 2000 è l'ultimo del XX secolo").blue().bold());
        addSpan(ASpan.text("Usati solo in background. File originale (CSV) sul server /www.algos.it/vaadin23/config").verde());
        addSpan(ASpan.text("Solo hard coded. Non creabili e non modificabili").rosso());
    }


    /**
     * Componenti aggiuntivi oltre quelli base <br>
     * Tipicamente bottoni di selezione/filtro <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixBottoniTopSpecifici() {
        super.fixBottoniTopSpecifici();

        boxBox = new IndeterminateCheckbox();
        boxBox.setLabel("Prima di Cristo");
        boxBox.setIndeterminate(true);
        boxBox.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout = new HorizontalLayout(boxBox);
        layout.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout);
    }

    /**
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void sincroFiltri() {
        List<Secolo> items = backend.findAll(sortOrder);

        if (boxBox != null && !boxBox.isIndeterminate()) {
            items = items.stream().filter(sec -> sec.anteCristo == boxBox.getValue()).toList();
        }

        if (items != null) {
            grid.setItems((List) items);
            elementiFiltrati = items.size();
            sicroBottomLayout();
        }
    }

}// end of crud @Route view class