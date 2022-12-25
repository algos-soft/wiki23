package it.algos.vaad24.backend.packages.geografia.continente;

import com.vaadin.flow.router.*;
import it.algos.vaad24.ui.dialog.*;
import it.algos.vaad24.ui.views.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 03-apr-2022
 * Time: 08:39
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Continenti")
@Route(value = "continente", layout = MainLayout.class)
public class ContinenteView extends CrudView {


    //--per eventuali metodi specifici
    private ContinenteBackend backend;

    //--per eventuali metodi specifici
    private ContinenteDialog dialog;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public ContinenteView(@Autowired final ContinenteBackend crudBackend) {
        super(crudBackend, Continente.class);
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

        super.sortOrder = Sort.by(Sort.Direction.ASC, "ordine");
        super.gridPropertyNamesList = Arrays.asList("ordine", "nome", "abitato");
        super.formPropertyNamesList = Arrays.asList("ordine", "nome", "abitato");
        super.usaRowIndex = false;
        super.usaBottoneDeleteReset = true;
        super.usaBottoneNew = false;
        super.autoCreateColumns = false;
        super.searchFieldName = "nome";
        super.dialogClazz = ContinenteDialog.class;
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();


        addSpan(ASpan.text("Usati solo in background. File originale (CSV) sul server /www.algos.it/vaadin23/config").verde());
        addSpan(ASpan.text("Solo hard coded. Non creabili e non modificabili").rosso());
        addSpan(ASpan.text("Ordinati di default per 'ordine'. Ordinabili anche per 'nome'.").rosso());
    }


}// end of crud @Route view class