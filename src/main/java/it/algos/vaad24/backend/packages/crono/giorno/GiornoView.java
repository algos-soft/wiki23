package it.algos.vaad24.backend.packages.crono.giorno;

import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.router.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.packages.crono.mese.*;
import it.algos.vaad24.ui.views.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 02-mag-2022
 * Time: 08:26
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Giorni")
@Route(value = TAG_GIORNO, layout = MainLayout.class)
public class GiornoView extends CrudView {


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MeseBackend meseBackend;


    private ComboBox comboMese;

    //--per eventuali metodi specifici
    private GiornoBackend backend;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public GiornoView(@Autowired final GiornoBackend crudBackend) {
        super(crudBackend, Giorno.class);
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

        super.gridPropertyNamesList = Arrays.asList("ordine", "nome", "trascorsi", "mancanti", "mese");
        super.formPropertyNamesList = Arrays.asList("ordine", "nome", "trascorsi", "mancanti", "mese");

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

        addSpan("Usati solo in background. Costruiti hard-code");
        addSpanRosso("Solo hard coded. Non creabili e non modificabili");
    }

    /**
     * Componenti aggiuntivi oltre quelli base <br>
     * Tipicamente bottoni di selezione/filtro <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixBottoniTopSpecifici() {
        super.fixBottoniTopSpecifici();

        comboMese = new ComboBox<>();
        comboMese.setPlaceholder("Mese");
        comboMese.getElement().setProperty("title", "Filtro di selezione");
        comboMese.setClearButtonVisible(true);
        comboMese.setItems(meseBackend.findAll());
        comboMese.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder.add(comboMese);

    }

    /**
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void sincroFiltri() {
        List<Giorno> items = backend.findAll(sortOrder);

        if (comboMese != null && comboMese.getValue() != null) {
            if (comboMese.getValue() instanceof Mese mese) {
                items = items.stream().filter(gio -> gio.mese.nome.equals(mese.nome)).toList();
            }
        }

        if (items != null) {
            grid.setItems((List) items);
            elementiFiltrati = items.size();
            sicroBottomLayout();
        }
    }

}// end of crud @Route view class