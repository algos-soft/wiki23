package it.algos.wiki23.backend.packages.pagina;

import ch.carnet.kasparscherrer.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.router.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.vaad23.backend.boot.VaadCost.PATH_WIKI;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

import org.springframework.data.domain.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 21-Sep-2022
 * Time: 17:39
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Pagine da cancellare")
@Route(value = "pagina", layout = MainLayout.class)
public class PaginaView extends WikiView {

    //--per eventuali metodi specifici
    private PaginaBackend backend;

    private IndeterminateCheckbox boxCancellare;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public PaginaView(@Autowired final PaginaBackend crudBackend) {
        super(crudBackend, Pagina.class);
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

        super.gridPropertyNamesList = Arrays.asList("cancella", "voci", "type");
        super.lastElaborazione = WPref.elaboraPagineCancella;
        super.durataElaborazione = WPref.elaboraPagineCancellaTime;
        super.usaBottoneDeleteReset = true;
        super.usaBottoneNew = false;
        super.usaBottoneEdit = false;
        super.usaBottoneDelete = false;
        this.usaBottoneDownload = false;
        this.usaBottoneErrori = false;
        this.usaBottoneUploadAll = false;
        this.usaBottoneTest = false;
        this.usaBottoneExport = false;
        this.usaBottoneSearch = false;
        this.usaBottonePaginaWiki = false;
        this.usaBottoneElabora = true;

        super.unitaMisuraElaborazione = "minuti";
        super.fixPreferenzeBackend();
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();
    }

    protected void fixTopLayout() {
        super.fixTopLayout();

        comboType = new ComboBox<>();
        comboType.setPlaceholder(TAG_ALTRE_BY + "type");
        comboType.setWidth(WIDTH_EM_LARGE);
        comboType.getElement().setProperty("title", "Filtro di selezione");
        comboType.setClearButtonVisible(true);
        comboType.setItems(AETypePaginaCancellare.values());
        comboType.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder.add(comboType);

        boxDistinctPlurali = new IndeterminateCheckbox();
        boxDistinctPlurali.setLabel("Cancellare");
        boxDistinctPlurali.setIndeterminate(false);
        boxDistinctPlurali.setValue(true);
        boxDistinctPlurali.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout = new HorizontalLayout(boxDistinctPlurali);
        layout.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout);
    }

    /**
     * autoCreateColumns=false <br>
     * Crea le colonne normali indicate in this.colonne <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void addColumnsOneByOne() {
        super.addColumnsOneByOne();

        Grid.Column pagineCancellare = grid.addColumn(new ComponentRenderer<>(entity -> {
            String wikiTitle = ((Pagina) entity).pagina;
            Anchor anchor;
            String link = wikiTitle;

            if (((Pagina) entity).cancella) {
                anchor = new Anchor(PATH_WIKI_EDIT + wikiTitle + TAG_DELETE, link);
                anchor.getElement().getStyle().set("color", "red");
            }
            else {
                anchor = new Anchor(PATH_WIKI + wikiTitle, link);
                anchor.getElement().getStyle().set("color", "green");
            }
            anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
            return new Span(anchor);
        })).setHeader("Pagine").setKey("pagine").setFlexGrow(0).setWidth("40em");

        Grid.Column ordine = grid.getColumnByKey(FIELD_KEY_ORDER);
        Grid.Column voci = grid.getColumnByKey("voci");
        Grid.Column type = grid.getColumnByKey("type");
        Grid.Column cancella = grid.getColumnByKey("cancella");

        grid.setColumnOrder(ordine, pagineCancellare, voci, type, cancella);
    }


    /**
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void sincroFiltri() {
        List<Pagina> items = backend.findAll(sortOrder);

        if (comboType != null && comboType.getValue() != null) {
            if (comboType.getValue() instanceof AETypePaginaCancellare type) {
                items = items.stream()
                        .filter(pagina -> pagina.type.equals(type))
                        .toList();
            }
        }

        if (boxDistinctPlurali != null && !boxDistinctPlurali.isIndeterminate()) {
            items = items.stream().filter(pagina -> pagina.cancella == boxDistinctPlurali.getValue()).toList();
            sortOrder = Sort.by(Sort.Direction.ASC, "pagina");
        }

        if (items != null) {
            grid.setItems((List) items);
            elementiFiltrati = items.size();
            sicroBottomLayout();
        }
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void elabora() {
        crudBackend.elabora();
        reload();
    }

}// end of crud @Route view class