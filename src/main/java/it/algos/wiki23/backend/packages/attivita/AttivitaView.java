package it.algos.wiki23.backend.packages.attivita;

import ch.carnet.kasparscherrer.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.data.selection.*;
import com.vaadin.flow.router.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import static it.algos.vaad24.backend.boot.VaadCost.PATH_WIKI;
import it.algos.vaad24.backend.entity.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.ui.dialog.*;
import it.algos.vaad24.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.statistiche.*;
import it.algos.wiki23.backend.upload.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.vaadin.crudui.crud.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 18-apr-2022
 * Time: 21:25
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Attivita")
@Route(value = TAG_ATTIVITA, layout = MainLayout.class)
public class AttivitaView extends WikiView {


    //--per eventuali metodi specifici
    private AttivitaBackend backend;


    //--per eventuali metodi specifici
    private AttivitaDialog dialog;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public AttivitaView(@Autowired final AttivitaBackend crudBackend) {
        super(crudBackend, Attivita.class);
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

        super.gridPropertyNamesList = Arrays.asList("singolare", "pluraleParagrafo", "type", "aggiunta", "numBio", "numSingolari", "superaSoglia");
        super.formPropertyNamesList = Arrays.asList("singolare", "pluraleParagrafo", "pluraleLista", "linkPaginaAttivita", "numBio", "superaSoglia", "esistePaginaLista");
        super.sortOrder = Sort.by(Sort.Direction.ASC, "pluraleLista");

        super.usaBottoneElabora = true;
        super.lastDownload = WPref.downloadAttivita;
        super.durataDownload = WPref.downloadAttivitaTime;
        super.lastElaborazione = WPref.elaboraAttivita;
        super.durataElaborazione = WPref.elaboraAttivitaTime;
        super.lastUpload = WPref.uploadAttivita;
        super.durataUpload = WPref.uploadAttivitaTime;
        super.nextUpload = WPref.uploadAttivitaPrevisto;
        super.lastStatistica = WPref.statisticaAttivita;
        super.wikiModuloTitle = PATH_MODULO_ATTIVITA;

        super.usaBottoneStatistiche = true;
        super.usaBottoneUploadStatistiche = true;
        super.usaBottoneEdit = true;
        super.usaBottoneCategoria = true;
        super.usaBottoneUploadPagina = true;
        super.usaBottoneSearch = false;

        super.dialogClazz = AttivitaDialog.class;
        super.unitaMisuraDownload = "secondi";
        super.unitaMisuraElaborazione = "minuti";
        super.unitaMisuraUpload = "minuti";
        super.fixPreferenzeBackend();
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();
        addSpanRossoSmall(String.format("%s: %s", "Liste da cancellare", backend.countAttivitaDaCancellare()));

        String modulo = PATH_WIKI + PATH_MODULO;

        Anchor anchor1 = new Anchor(modulo + PATH_PLURALE + ATT_LOWER, PATH_PLURALE + ATT_LOWER);
        anchor1.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());

        Anchor anchor2 = new Anchor(modulo + PATH_EX + ATT_LOWER, PATH_EX + ATT_LOWER);
        anchor2.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());

        Anchor anchor3 = new Anchor(modulo + PATH_LINK + ATT_LOWER, PATH_LINK + ATT_LOWER);
        anchor3.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());

        Anchor anchor4 = new Anchor(PATH_WIKI + PATH_STATISTICHE_ATTIVITA, PATH_STATISTICHE_ATTIVITA);
        anchor4.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
        alertPlaceHolder.add(new Span(anchor1, new Label(SEP), anchor2, new Label(SEP), anchor3, new Label(SEP), anchor4));

        message = "1) Tabella attività dei parametri 'Attività/Attività2/Attività3',";
        message += " da singolare maschile e femminile al plurale maschile per la pagina della lista.";
        addSpanVerde(message);

        message = "2) Tabella attività che sono ammesse nei parametri Attività/Attività2/Attività3 anche col prefisso \"ex\"";
        addSpanVerde(message);

        message = "3) Tabella di conversione dal nome dell'attività a quello della voce corrispondente, per creare dei piped wikilink";
        addSpanVerde(message);

        message = "Indipendentemente da come sono scritte nel modulo, tutte le attività singolari e plurali sono convertite in minuscolo.";
        addSpanRosso(message);

        message = String.format("Le singole pagine di attività vengono create su wiki quando superano le %s biografie.", WPref.sogliaAttNazWiki.get());
        addSpanRossoBold(message);
        if (WPref.usaTreAttivita.is()) {
            message = "Ultima elaborazione effettuata considerando la validità delle voci biografiche in base ad una qualsiasi delle '''3''' attività.";
        }
        else {
            message = "Ultima elaborazione effettuata considerando la validità delle voci biografiche sola in base all'attività principale.";
        }
        addSpanRossoBold(message);
    }

    @Override
    protected void fixTopLayout() {
        super.fixTopLayout();
        this.fixBottoniTopSpecificiAttivita();
    }


    protected void fixBottoniTopSpecificiAttivita() {
        super.fixBottoniTopSpecificiAttivitaNazionalita();

        comboType = new ComboBox<>();
        comboType.setPlaceholder(TAG_ALTRE_BY + "genere");
        comboType.setWidth(WIDTH_EM);
        comboType.getElement().setProperty("title", "Filtro di selezione");
        comboType.setClearButtonVisible(true);
        comboType.setItems(AETypeGenere.values());
        comboType.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(comboType);

        boxBox = new IndeterminateCheckbox();
        boxBox.setLabel("Ex");
        boxBox.setIndeterminate(true);
        boxBox.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout = new HorizontalLayout(boxBox);
        layout.setAlignItems(Alignment.CENTER);
        topPlaceHolder2.add(layout);

        fixCheckTopSpecificiAttivitaNazionalita();

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

        Grid.Column pluraleLista = grid.addColumn(new ComponentRenderer<>(entity -> {
            String wikiTitle = textService.primaMaiuscola(((Attivita) entity).pluraleLista);
            Anchor anchor = new Anchor(PATH_WIKI + PATH_ATTIVITA + SLASH + wikiTitle, wikiTitle);
            anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
            if (((Attivita) entity).esistePaginaLista) {
                anchor.getElement().getStyle().set("color", "green");
            }
            else {
                anchor.getElement().getStyle().set("color", "red");
            }
            return new Span(anchor);
        })).setHeader("pluraleLista").setKey("pluraleLista").setFlexGrow(0).setWidth("18em");

        Grid.Column linkPagina = grid.addColumn(new ComponentRenderer<>(entity -> {
            String wikiTitle = textService.primaMaiuscola(((Attivita) entity).linkPaginaAttivita);
            Anchor anchor = new Anchor(PATH_WIKI + wikiTitle, wikiTitle);
            anchor.getElement().getStyle().set("color", "green");
            anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());

            return new Span(anchor);
        })).setHeader("linkPagina").setKey("linkPagina").setFlexGrow(0).setWidth("18em");

        Grid.Column daCancellare = grid.addColumn(new ComponentRenderer<>(entity -> {
            String link = "https://it.wikipedia.org/w/index.php?title=Progetto:Biografie/Attivit%C3%A0/";
            link += textService.primaMaiuscola(((Attivita) entity).pluraleLista);
            link += TAG_DELETE;
            Label label = new Label("no");
            label.getElement().getStyle().set("color", "green");
            Anchor anchor = new Anchor(link, "del");
            anchor.getElement().getStyle().set("color", "red");
            anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
            Span span = new Span(anchor);

            if (((Attivita) entity).esistePaginaLista && !((Attivita) entity).superaSoglia) {
                return span;
            }
            else {
                return label;
            }
        })).setHeader("X").setKey("cancella").setFlexGrow(0).setWidth("8em");

        Grid.Column ordine = grid.getColumnByKey(FIELD_KEY_ORDER);
        Grid.Column singolare = grid.getColumnByKey("singolare");
        Grid.Column pluraleParagrafo = grid.getColumnByKey("pluraleParagrafo");
        Grid.Column type = grid.getColumnByKey("type");
        Grid.Column aggiunta = grid.getColumnByKey("aggiunta");
        Grid.Column numBio = grid.getColumnByKey("numBio");
        Grid.Column numSingolari = grid.getColumnByKey("numSingolari");
        Grid.Column superaSoglia = grid.getColumnByKey("superaSoglia");

        grid.setColumnOrder(ordine, singolare, pluraleParagrafo, pluraleLista, linkPagina, type, aggiunta, numBio, numSingolari, superaSoglia, daCancellare);
    }

    /**
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void sincroFiltri() {
        List<Attivita> items = backend.findAll(sortOrder);

        final String textSearchSingolare = searchFieldSingolare != null ? searchFieldSingolare.getValue() : VUOTA;
        if (textService.isValid(textSearchSingolare)) {
            items = items.stream().filter(att -> att.singolare.matches("^(?i)" + textSearchSingolare + ".*$")).toList();
        }

        final String textSearchParagrafo = searchFieldParagrafo != null ? searchFieldParagrafo.getValue() : VUOTA;
        if (textService.isValid(textSearchParagrafo)) {
            items = items.stream().filter(att -> att.pluraleParagrafo.matches("^(?i)" + textSearchParagrafo + ".*$")).toList();
        }

        final String textSearchLista = searchFieldLista != null ? searchFieldLista.getValue() : VUOTA;
        if (textService.isValid(textSearchLista)) {
            items = items.stream().filter(att -> att.pluraleLista.matches("^(?i)" + textSearchLista + ".*$")).toList();
        }

        final String textSearchPagina = searchFieldPagina != null ? searchFieldPagina.getValue() : VUOTA;
        if (textService.isValid(textSearchPagina)) {
            items = items.stream().filter(att -> att.linkPaginaAttivita.matches("^(?i)" + textSearchPagina + ".*$")).toList();
        }

        if (comboType != null && comboType.getValue() != null) {
            if (comboType.getValue() instanceof AETypeGenere genere) {
                items = items.stream()
                        .filter(gen -> gen.type.equals(genere))
                        .toList();
            }
        }

        if (boxBox != null && !boxBox.isIndeterminate()) {
            items = items.stream().filter(att -> att.aggiunta == boxBox.getValue()).toList();
        }

        if (boxSuperaSoglia != null && !boxSuperaSoglia.isIndeterminate()) {
            items = items.stream().filter(att -> att.superaSoglia == boxSuperaSoglia.getValue()).toList();
            if (boxSuperaSoglia.getValue()) {
                sortOrder = Sort.by(Sort.Direction.ASC, "pluraleLista");
            }
            else {
                sortOrder = Sort.by(Sort.Direction.ASC, "singolare");
            }
        }

        if (boxEsistePagina != null && !boxEsistePagina.isIndeterminate()) {
            items = items.stream().filter(att -> att.esistePaginaLista == boxEsistePagina.getValue()).toList();
            if (boxEsistePagina.getValue()) {
                sortOrder = Sort.by(Sort.Direction.ASC, "linkPagina");
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

    @Override
    protected boolean sincroSelection(SelectionEvent event) {
        boolean singoloSelezionato = super.sincroSelection(event);
        boolean numVociNecessarie = false;

        Attivita attivita = getAttivitaCorrente();
        if (attivita != null) {
            numVociNecessarie = attivita.numBio > WPref.sogliaAttNazWiki.getInt();
        }

        if (buttonUploadPagina != null) {
            buttonUploadPagina.setEnabled(singoloSelezionato && numVociNecessarie);
        }

        return singoloSelezionato;
    }

    protected void sincroPlurali() {
        List<Attivita> items = null;

        if (boxDistinctPlurali != null) {
            if (boxDistinctPlurali.getValue()) {
                items = backend.findAttivitaDistinctByPluraliSortPagina();
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


    protected void sincroCancellare() {
        List<Attivita> items = null;

        if (boxPagineDaCancellare != null) {
            if (boxPagineDaCancellare.getValue()) {
                items = backend.findPagineDaCancellare();
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
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void download() {
        backend.download();
        reload();
    }

    /**
     * Esegue un azione di upload, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void upload() {
        appContext.getBean(UploadAttivita.class).uploadAll();
    }

    /**
     * Apre una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void viewStatistiche() {
        wikiApiService.openWikiPage(PATH_ATTIVITA);
    }

    /**
     * Esegue un azione di upload delle statistiche, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    public void uploadStatistiche() {
        long inizio = System.currentTimeMillis();
        appContext.getBean(StatisticheAttivita.class).upload();
        super.fixStatisticheMinuti(inizio);
        super.uploadStatistiche();
    }

    /**
     * Esegue un azione di apertura di una categoria su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public AEntity wikiCategoria() {
        Attivita attivita = (Attivita) super.wikiCategoria();

        String path = "Categoria:";
        wikiApiService.openWikiPage(path + attivita.pluraleParagrafo);

        return null;
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected AEntity wikiPage() {
        Attivita attivita = (Attivita) super.wikiPage();

        String path = PATH_ATTIVITA + SLASH;
        String attivitaText = textService.primaMaiuscola(attivita.pluraleParagrafo);
        wikiApiService.openWikiPage(path + attivitaText);

        return null;
    }


    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void testPagina() {
        Attivita attivita;
        String message;

        Optional entityBean = grid.getSelectedItems().stream().findFirst();
        if (entityBean.isPresent()) {
            attivita = (Attivita) entityBean.get();
            if (attivita.numBio > WPref.sogliaAttNazWiki.getInt()) {
                appContext.getBean(UploadAttivita.class).test().upload(attivita.pluraleParagrafo);
            }
            else {
                message = String.format("L'attività %s non raggiunge il necessario numero di voci biografiche", attivita.singolare);
                Avviso.show3000(message).addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            }
        }
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void uploadPagina() {
        Attivita attivita = getAttivitaCorrente();

        if (attivita != null) {
            backend.uploadPagina(attivita.pluraleLista);
            reload();
        }
    }

    public void updateItem(AEntity entityBean) {
        dialog = appContext.getBean(AttivitaDialog.class, entityBean, CrudOperation.READ, crudBackend, formPropertyNamesList);
        dialog.open(this::saveHandler, this::annullaHandler);
    }

    public Attivita getAttivitaCorrente() {
        Attivita attivita = null;

        Optional entityBean = grid.getSelectedItems().stream().findFirst();
        if (entityBean.isPresent()) {
            attivita = (Attivita) entityBean.get();
        }

        return attivita;
    }

}// end of crud @Route view class