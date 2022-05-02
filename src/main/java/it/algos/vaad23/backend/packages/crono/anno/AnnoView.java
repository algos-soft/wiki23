package it.algos.vaad23.backend.packages.crono.anno;

import ch.carnet.kasparscherrer.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.secolo.*;
import it.algos.vaad23.ui.views.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 02-mag-2022
 * Time: 16:05
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Anni")
@Route(value = TAG_ANNO, layout = MainLayout.class)
public class AnnoView extends CrudView {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public SecoloBackend secoloBackend;

    //--per eventuali metodi specifici
    private AnnoBackend backend;

    private ComboBox comboSecolo;

    private IndeterminateCheckbox boxBisestile;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public AnnoView(@Autowired final AnnoBackend crudBackend) {
        super(crudBackend, Anno.class);
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

        super.gridPropertyNamesList = Arrays.asList("ordine", "nome", "secolo", "anteCristo", "bisestile");
        super.formPropertyNamesList = Arrays.asList("ordine", "nome", "secolo", "anteCristo", "bisestile");

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

        comboSecolo = new ComboBox<>();
        comboSecolo.setPlaceholder("Secolo");
        comboSecolo.getElement().setProperty("title", "Filtro di selezione");
        comboSecolo.setClearButtonVisible(true);
        comboSecolo.setItems(secoloBackend.findAll());
        comboSecolo.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder.add(comboSecolo);

        boxBox = new IndeterminateCheckbox();
        boxBox.setLabel("Prima di Cristo");
        boxBox.setIndeterminate(false);
        boxBox.setValue(false);
        boxBox.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout = new HorizontalLayout(boxBox);
        layout.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout);

        boxBisestile = new IndeterminateCheckbox();
        boxBisestile.setLabel("Bisestile");
        boxBisestile.setIndeterminate(true);
        boxBisestile.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout2 = new HorizontalLayout(boxBisestile);
        layout2.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout2);
    }

    /**
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void sincroFiltri() {
        List<Anno> items = backend.findAll(sortOrder);

        if (comboSecolo != null && comboSecolo.getValue() != null) {
            if (comboSecolo.getValue() instanceof Secolo secolo) {
                items = items.stream().filter(anno -> anno.secolo.nome.equals(secolo.nome)).toList();
            }
        }

        if (boxBox != null && !boxBox.isIndeterminate()) {
            items = items.stream().filter(anno -> anno.anteCristo == boxBox.getValue()).toList();
        }

        if (boxBisestile != null && !boxBisestile.isIndeterminate()) {
            items = items.stream().filter(anno -> anno.bisestile == boxBisestile.getValue()).toList();
        }

        if (items != null) {
            grid.setItems((List) items);
            elementiFiltrati = items.size();
            sicroBottomLayout();
        }
    }

}// end of crud @Route view class