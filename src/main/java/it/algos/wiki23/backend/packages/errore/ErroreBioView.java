package it.algos.wiki23.backend.packages.errore;

import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.router.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.core.query.*;
import org.vaadin.crudui.crud.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 26-Aug-2022
 * Time: 11:28
 */
@PageTitle("Errori")
@Route(value = "ErroriBio", layout = MainLayout.class)
public class ErroreBioView extends WikiView {

    //--per eventuali metodi specifici
    private BioBackend backend;

    private ComboBox comboType;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioService bioService;

    //--per eventuali metodi specifici
    private ErroreBioDialog dialog;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public ErroreBioView(@Autowired final BioBackend crudBackend) {
        super(crudBackend, Bio.class);
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

        super.gridPropertyNamesList = Arrays.asList("pageId", "sesso", "errore");
        super.formPropertyNamesList = Arrays.asList("pageId", "wikiTitle", "elaborato", "sesso", "nome", "cognome", "ordinamento",
                "giornoNato",
                "annoNato", "giornoMorto", "annoMorto",
                "giornoNatoOrd", "giornoMortoOrd", "annoNatoOrd", "annoMortoOrd",
                "attivita", "attivita2", "attivita3",
                "nazionalita", "errato", "errore"
        );

        this.usaBottoneDownload = false;
        super.usaBottoneElabora = false;
        super.usaBottoneDeleteAll = false;
        this.usaBottoneUploadAll = false;
        super.usaBottoneNew = false;
        super.usaBottoneDelete = false;
        super.usaBottoneSearch = false;
        super.usaBottonePaginaWiki = false;
        this.usaBottoneTest = false;
        super.dialogClazz = ErroreBioDialog.class;

        super.fixPreferenzeBackend();
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();
        int nulli = backend.countSessoMancante();
        int lunghi = backend.countSessoLungo();
        int errati = backend.countSessoErrato();
        int nazionalita = backend.countNazionalitaGenere();
        int ordinamento = backend.countOrdinamento();

        addSpanVerde(String.format("%s: %s", AETypeBioError.sessoMancante, nulli));
        addSpanVerde(String.format("%s: %s", AETypeBioError.sessoLungo, lunghi));
        addSpanVerde(String.format("%s: %s", AETypeBioError.sessoErrato, errati));
        addSpanVerde(String.format("%s: %s", AETypeBioError.nazionalitaGenere, nazionalita));
        addSpanVerde(String.format("%s: %s", AETypeBioError.mancaOrdinamento, ordinamento));
    }

    @Override
    protected void fixTopLayout() {
        super.fixTopLayout();
        this.fixBottoniTopSpecificiErrori();
    }

    protected void fixBottoniTopSpecificiErrori() {
        comboType = new ComboBox<>();
        comboType.setPlaceholder(TAG_ALTRE_BY + "errore");
        comboType.setWidth("26ex");
        comboType.getElement().setProperty("title", "Filtro di selezione");
        comboType.setClearButtonVisible(true);
        comboType.setItems(AETypeBioError.values());
        comboType.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(comboType);

        this.add(topPlaceHolder2);
    }

    /**
     * autoCreateColumns=false <br>
     * Crea le colonne normali indicate in this.colonne <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void addColumnsOneByOne() {
        super.addColumnsOneByOne();

        Grid.Column pagina = grid.addColumn(new ComponentRenderer<>(entity -> {
            String wikiTitle = textService.primaMaiuscola(((Bio) entity).wikiTitle);
            String link = PATH_WIKI_EDIT + wikiTitle + TAG_EDIT_ZERO;
            Anchor anchor = new Anchor(link, wikiTitle);
            anchor.getElement().getStyle().set("color", "green");
            anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());

            return new Span(anchor);
        })).setHeader("pagina").setKey("wikiTitle").setFlexGrow(0).setWidth("18em");

        Grid.Column ordine = grid.getColumnByKey(FIELD_KEY_ORDER);
        Grid.Column pageId = grid.getColumnByKey("pageId");
        Grid.Column sesso = grid.getColumnByKey("sesso");
        Grid.Column errore = grid.getColumnByKey("errore");

        grid.setColumnOrder(ordine, pageId, pagina, sesso, errore);
    }

    protected void fixItems() {
    }

    protected void sincroFiltri() {
        List<Bio> items;
        items = bioService.fetchErrori();

        if (comboType != null && comboType.getValue() != null) {
            if (comboType.getValue() instanceof AETypeBioError errore) {
                items = items.stream()
                        .filter(bio -> bio.errore.equals(errore))
                        .toList();
            }
        }

        if (items != null) {
            grid.setItems((List) items);
            elementiFiltrati = items.size();
            sicroBottomLayout();
        }
    }

    public void updateItem(AEntity entityBeanSenzaTempl) {
        Bio bioMongoCompleto = null;

        if (entityBeanSenzaTempl instanceof Bio bioSenzaTempl) {
            bioMongoCompleto = backend.findByKey(bioSenzaTempl.pageId);
        }

        if (bioMongoCompleto != null) {
            dialog = appContext.getBean(ErroreBioDialog.class, bioMongoCompleto, CrudOperation.UPDATE, crudBackend, formPropertyNamesList);
            dialog.openBio(this::saveHandler, this::annullaHandler, null, null);
        }
    }


}
