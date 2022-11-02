package it.algos.wiki23.backend.packages.genere;

import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.*;
import it.algos.vaad23.backend.boot.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;

import java.util.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: dom, 24-apr-2022
 * Time: 10:17
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Genere")
@Route(value = TAG_GENERE, layout = MainLayout.class)
public class GenereView extends WikiView {


    //--per eventuali metodi specifici
    private GenereBackend backend;

    private TextField searchFieldPluraleMaschile;

    private TextField searchFieldPluraleFemminile;

    private ComboBox comboType;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public GenereView(@Autowired final GenereBackend crudBackend) {
        super(crudBackend, Genere.class);
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

        super.gridPropertyNamesList = Arrays.asList("singolare", "pluraleMaschile", "pluraleFemminile", "type");
        super.formPropertyNamesList = Arrays.asList("singolare", "pluraleMaschile", "pluraleFemminile", "type");

        super.sortOrder = Sort.by(Sort.Direction.ASC, "singolare");
        super.lastDownload = WPref.downloadGenere;
        super.wikiModuloTitle = PATH_MODULO_GENERE;

        super.usaBottoneUploadAll = false;
        //        super.usaBottoneStatistiche = false;
        super.usaBottoneUploadStatistiche = false;
        super.usaBottonePaginaWiki = false;
        super.usaBottoneTest = false;
        super.usaBottoneUploadPagina = false;

        super.fixPreferenzeBackend();
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();

        Anchor anchor = new Anchor(VaadCost.PATH_WIKI + PATH_MODULO_GENERE, PATH_MODULO_GENERE);
        anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
        alertPlaceHolder.add(new Span(anchor));

        message = "Contiene la tabella di conversione delle attività passate via parametri 'Attività/Attività2/Attività3',";
        message += " da singolare maschile e femminile (usati nell'incipit) al plurale maschile e femminile.";
        addSpanVerde(message);
        message = "Usate per le intestazioni dei paragrafi nelle liste di antroponimi previste nel [Progetto:Antroponimi].";
        addSpanVerde(message);

        message = "Le attività sono elencate all'interno del modulo con la seguente sintassi:";
        message += " [\"attivita singolare maschile\"] = \"attività plurale maschile\";";
        message += " [\"attivita singolare femminile\"] = \"attività plurale femminile\".";
        addSpanVerde(message);

        message = "Indipendentemente da come sono scritte nel modulo, tutte le attività singolari e plurali sono convertite in minuscolo.";
        message += " Le voci delle ex-attività vengono aggiunte al package attività";
        addSpanRosso(message);

        message = "Il package 'genere' deve essere aggiornato prima di regolare 'attività'.";
        addSpanRossoBold(message);
    }


    protected void fixBottoniTopSpecifici() {
        searchFieldPluraleMaschile = new TextField();
        searchFieldPluraleMaschile.setPlaceholder(TAG_ALTRE_BY + "maschile");
        searchFieldPluraleMaschile.setWidth(WIDTH_EM);
        searchFieldPluraleMaschile.setClearButtonVisible(true);
        searchFieldPluraleMaschile.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder.add(searchFieldPluraleMaschile);

        searchFieldPluraleFemminile = new TextField();
        searchFieldPluraleFemminile.setPlaceholder(TAG_ALTRE_BY + "femminile");
        searchFieldPluraleFemminile.setWidth(WIDTH_EM);
        searchFieldPluraleFemminile.setClearButtonVisible(true);
        searchFieldPluraleFemminile.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder.add(searchFieldPluraleFemminile);

        comboType = new ComboBox<>();
        comboType.setPlaceholder(TAG_ALTRE_BY + "genere");
        comboType.getElement().setProperty("title", "Filtro di selezione");
        comboType.setClearButtonVisible(true);
        comboType.setItems(AETypeGenere.values());
        comboType.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder.add(comboType);
    }

    /**
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void sincroFiltri() {
        List<Genere> items = backend.findAll(sortOrder);

        final String textSearch = searchField != null ? searchField.getValue() : VUOTA;
        if (textService.isValid(textSearch)) {
            items = items.stream()
                    .filter(gen -> gen.singolare.matches("^(?i)" + textSearch + ".*$"))
                    .toList();
        }

        final String textSearchPluraleMaschile = searchFieldPluraleMaschile != null ? searchFieldPluraleMaschile.getValue() : VUOTA;
        if (textService.isValid(textSearchPluraleMaschile)) {
            items = items.stream()
                    .filter(gen -> textService.isValid(gen.pluraleMaschile))
                    .filter(gen -> gen.pluraleMaschile.matches("^(?i)" + textSearchPluraleMaschile + ".*$"))
                    .toList();
        }

        final String textSearchPluraleFemminile = searchFieldPluraleFemminile != null ? searchFieldPluraleFemminile.getValue() : VUOTA;
        if (textService.isValid(textSearchPluraleFemminile)) {
            items = items.stream()
                    .filter(gen -> textService.isValid(gen.pluraleFemminile))
                    .filter(gen -> gen.pluraleFemminile.matches("^(?i)" + textSearchPluraleFemminile + ".*$"))
                    .toList();
        }

        if (comboType != null && comboType.getValue() != null) {
            if (comboType.getValue() instanceof AETypeGenere genere) {
                items = items.stream()
                        .filter(gen -> gen.type.equals(genere))
                        .toList();
            }
        }

        if (items != null) {
            grid.setItems((List) items);
            elementiFiltrati = items.size();
            sicroBottomLayout();
        }
    }

}// end of crud @Route view class