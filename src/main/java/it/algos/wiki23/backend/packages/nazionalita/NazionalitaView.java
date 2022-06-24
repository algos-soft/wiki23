package it.algos.wiki23.backend.packages.nazionalita;

import ch.carnet.kasparscherrer.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.*;
import it.algos.vaad23.backend.boot.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.vaadin.crudui.crud.*;

import java.util.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: lun, 25-apr-2022
 * Time: 18:21
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Nazionalita")
@Route(value = TAG_NAZIONALITA, layout = MainLayout.class)
public class NazionalitaView extends WikiView {


    //--per eventuali metodi specifici
    private NazionalitaBackend backend;

    private TextField searchFieldPlurale;
    protected IndeterminateCheckbox boxBoxPagina;

    protected Checkbox boxDistinctPlurali;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public NazionalitaView(@Autowired final NazionalitaBackend crudBackend) {
        super(crudBackend, Nazionalita.class);
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

        super.gridPropertyNamesList = Arrays.asList("singolare", "plurale", "numBio", "numSingolari", "pagina");
        super.formPropertyNamesList = Arrays.asList( "plurale",  "numBio");
        super.sortOrder = Sort.by(Sort.Direction.ASC, "singolare");


        super.usaBottoneElabora = true;
        super.lastDownload = WPref.downloadNazionalita;
        super.lastElaborazione = WPref.elaboraNazionalita;
        super.durataElaborazione = WPref.elaboraNazionalitaTime;
        super.lastUpload = WPref.uploadNazionalita;
        super.wikiModuloTitle = PATH_MODULO_NAZIONALITA;
        //        super.wikiStatisticheTitle = PATH_STATISTICHE_ATTIVITA;
        super.usaBottoneCategoria = true;
        super.usaBottoneEdit = true;

        super.dialogClazz = NazionalitaDialog.class;
        super.unitaMisuraElaborazione = "secondi";
        super.fixPreferenzeBackend();
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();

        Anchor anchor = new Anchor(VaadCost.PATH_WIKI + PATH_MODULO_NAZIONALITA, PATH_MODULO_NAZIONALITA);
        anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
        Anchor anchor2 = new Anchor(VaadCost.PATH_WIKI + PATH_STATISTICHE_NAZIONALITA, PATH_STATISTICHE_NAZIONALITA);
        anchor2.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
        alertPlaceHolder.add(new Span(anchor, new Label(SEP), anchor2));

        message = "Contiene la tabella di conversione delle attività passate via parametri 'Nazionalità/Cittadinanza/NazionalitàNaturalizzato',";
        message += " da singolare maschile e femminile (usati nell'incipit) al plurale maschile per categorizzare la pagina.";
        addSpanVerde(message);

        message = "Le nazionalità sono elencate all'interno del modulo con la seguente sintassi:";
        message += " [\"nazionalitaforma1\"]=\"nazionalità al plurale\"; [\"nazionalitaforma2\"]=\"nazionalità al plurale\".";
        addSpanVerde(message);

        message = "Indipendentemente da come sono scritte nel modulo, tutte le attività singolari e plurali sono convertite in minuscolo.";
        addSpanRosso(message);

        message = String.format("Le singole pagine di nazionalità vengono create su wiki quando superano le %s biografie.", WPref.sogliaAttNazWiki.get());
        addSpanRossoBold(message);
    }

    protected void fixBottoniTopSpecifici() {
        searchFieldPlurale = new TextField();
        searchFieldPlurale.setPlaceholder("Filter by plurale");
        searchFieldPlurale.setClearButtonVisible(true);
        searchFieldPlurale.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder.add(searchFieldPlurale);

        boxBoxPagina = new IndeterminateCheckbox();
        boxBoxPagina.setLabel("Pagina wiki");
        boxBoxPagina.setIndeterminate(true);
        boxBoxPagina.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout2 = new HorizontalLayout(boxBoxPagina);
        layout2.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout2);

        boxDistinctPlurali = new Checkbox();
        boxDistinctPlurali.setLabel("Distinct plurali");
        boxDistinctPlurali.addValueChangeListener(event -> sincroPlurali());
        HorizontalLayout layout3 = new HorizontalLayout(boxDistinctPlurali);
        layout3.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout3);
    }

    /**
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void sincroFiltri() {
        List<Nazionalita> items = backend.findAll(sortOrder);

        final String textSearch = searchField != null ? searchField.getValue() : VUOTA;
        if (textService.isValid(textSearch)) {
            items = items.stream().filter(naz -> naz.singolare.matches("^(?i)" + textSearch + ".*$")).toList();
        }

        final String textSearchPlurale = searchFieldPlurale != null ? searchFieldPlurale.getValue() : VUOTA;
        if (textService.isValid(textSearchPlurale)) {
            items = items.stream().filter(naz -> naz.plurale.matches("^(?i)" + textSearchPlurale + ".*$")).toList();
        }

        if (boxBoxPagina != null && !boxBoxPagina.isIndeterminate()) {
            items = items.stream().filter(att -> att.pagina == boxBoxPagina.getValue()).toList();
            if (boxBoxPagina.getValue()) {
                sortOrder = Sort.by(Sort.Direction.ASC, "plurale");
            }
            else {
                sortOrder = Sort.by(Sort.Direction.ASC, "singolare");
            }
        }

        if (items != null) {
            grid.setItems((List) items);
            elementiFiltrati = items.size();
            sicroBottomLayout();
        }
    }

    protected void sincroPlurali() {
        List<Nazionalita> items = null;

        if (boxDistinctPlurali != null) {
            if (boxDistinctPlurali.getValue()) {
                items = backend.findNazionalitaDistinctByPlurali();
            }
            else {
                sortOrder = Sort.by(Sort.Direction.ASC, "singolare");
                items = backend.findAll(sortOrder);
            }
        }

        if (items != null) {
            grid.setItems((List) items);
            elementiFiltrati = items.size();
            sicroBottomLayout();
        }
    }

    /**
     * Esegue un azione di apertura di una categoria su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public AEntity wikiCategoria() {
        Nazionalita nazionalita = (Nazionalita) super.wikiCategoria();

        String path = "Categoria:";
        wikiApiService.openWikiPage(path + nazionalita.plurale);

        return null;
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected AEntity wikiPage() {
        Nazionalita attivita = (Nazionalita) super.wikiPage();

        String path = PATH_NAZIONALITA + SLASH;
        String attivitaText = textService.primaMaiuscola(attivita.plurale);
        wikiApiService.openWikiPage(path + attivitaText);

        return null;
    }

    public void updateItem(AEntity entityBean) {
        dialog = appContext.getBean(NazionalitaDialog.class, entityBean, CrudOperation.READ, crudBackend, formPropertyNamesList);
        dialog.open(this::saveHandler, this::annullaHandler);
    }

}// end of crud @Route view class