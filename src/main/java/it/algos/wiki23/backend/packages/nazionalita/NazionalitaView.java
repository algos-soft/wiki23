package it.algos.wiki23.backend.packages.nazionalita;

import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.router.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.vaad23.backend.boot.VaadCost.PATH_WIKI;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.ui.dialog.*;
import it.algos.vaad23.ui.views.*;
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

        super.gridPropertyNamesList = Arrays.asList("singolare", "pluraleParagrafo", "numBio", "numSingolari", "superaSoglia");
        super.formPropertyNamesList = Arrays.asList("pluraleParagrafo", "numBio");
        super.sortOrder = Sort.by(Sort.Direction.ASC, "singolare");

        super.usaBottoneElabora = true;
        super.lastDownload = WPref.downloadNazionalita;
        super.durataDownload = WPref.downloadNazionalitaTime;
        super.lastElaborazione = WPref.elaboraNazionalita;
        super.durataElaborazione = WPref.elaboraNazionalitaTime;
        super.lastUpload = WPref.uploadNazionalita;
        super.durataUpload = WPref.uploadNazionalitaTime;
        super.nextUpload = WPref.uploadNazionalitaPrevisto;
        super.lastStatistica = WPref.statisticaNazionalita;
        super.wikiModuloTitle = PATH_MODULO_NAZIONALITA;
        super.usaBottoneStatistiche = true;
        super.usaBottoneUploadStatistiche = true;
        //        super.wikiStatisticheTitle = PATH_STATISTICHE_ATTIVITA;
        super.usaBottoneEdit = true;
        super.usaBottoneCategoria = true;
        super.usaBottoneUploadPagina = true;
        super.usaBottoneSearch = false;

        super.dialogClazz = NazionalitaDialog.class;
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

        String modulo = PATH_WIKI + PATH_MODULO;

        Anchor anchor1 = new Anchor(modulo + PATH_PLURALE + NAZ_LOWER, PATH_PLURALE + NAZ_LOWER);
        anchor1.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());

        Anchor anchor2 = new Anchor(modulo + PATH_LINK + NAZ_LOWER, PATH_LINK + NAZ_LOWER);
        anchor2.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());

        Anchor anchor3 = new Anchor(PATH_WIKI + PATH_STATISTICHE_NAZIONALITA, PATH_STATISTICHE_NAZIONALITA);
        anchor3.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
        alertPlaceHolder.add(new Span(anchor1, new Label(SEP), anchor2, new Label(SEP), anchor3));

        message = "1) Tabella nazionalità del parametro 'nazionalità',";
        message += " da singolare maschile e femminile al plurale maschile per la pagina della lista.";
        addSpanVerde(message);

        message = "2) Tabella di conversione dal nome della nazionalità a quello della voce corrispondente, per creare dei piped wikilink";
        addSpanVerde(message);

        message = "Indipendentemente da come sono scritte nel modulo, tutte le nazionalità sono convertite in minuscolo.";
        addSpanRosso(message);

        message = String.format("Le singole pagine di nazionalità vengono create su wiki quando superano le %s biografie.", WPref.sogliaAttNazWiki.get());
        addSpanRossoBold(message);
    }

    @Override
    protected void fixTopLayout() {
        super.fixTopLayout();
        fixBottoniTopSpecificiNazionalita();
    }

    protected void fixBottoniTopSpecificiNazionalita() {
        super.fixBottoniTopSpecificiAttivitaNazionalita();
        super.fixCheckTopSpecificiAttivitaNazionalita();

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
            String wikiTitle = textService.primaMaiuscola(((Nazionalita) entity).pluraleLista);
            Label label = new Label(wikiTitle);
            label.getElement().getStyle().set("color", "red");
            Anchor anchor = new Anchor(PATH_WIKI + PATH_NAZIONALITA + SLASH + wikiTitle, wikiTitle);
            anchor.getElement().getStyle().set("color", "green");
            anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
            Span span = new Span(anchor);

            if (((Nazionalita) entity).esistePaginaLista) {
                return span;
            }
            else {
                return label;
            }
        })).setHeader("pluraleLista").setKey("pluraleLista").setFlexGrow(0).setWidth("18em");

        Grid.Column linkPagina = grid.addColumn(new ComponentRenderer<>(entity -> {
            String wikiTitle = textService.primaMaiuscola(((Nazionalita) entity).linkPaginaNazione);
            Anchor anchor = new Anchor(PATH_WIKI + wikiTitle, wikiTitle);
            anchor.getElement().getStyle().set("color", "green");
            Span span = new Span(anchor);

            return new Span(anchor);
        })).setHeader("linkPagina").setKey("linkPagina").setFlexGrow(0).setWidth("18em");

        Grid.Column daCancellare = grid.addColumn(new ComponentRenderer<>(entity -> {
            String link = "https://it.wikipedia.org/w/index.php?title=Progetto:Biografie/Nazionalit%C3%A0/";
            link += textService.primaMaiuscola(((Nazionalita) entity).pluraleLista);
            link += "&action=delete";
            Label label = new Label("no");
            label.getElement().getStyle().set("color", "green");
            Anchor anchor = new Anchor(link, "del");
            anchor.getElement().getStyle().set("color", "red");
            anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
            Span span = new Span(anchor);

            if (((Nazionalita) entity).esistePaginaLista && !((Nazionalita) entity).superaSoglia) {
                return span;
            }
            else {
                return label;
            }
        })).setHeader("X").setKey("cancella").setFlexGrow(0).setWidth("8em");

        Grid.Column ordine = grid.getColumnByKey(FIELD_KEY_ORDER);
        Grid.Column singolare = grid.getColumnByKey("singolare");
        Grid.Column pluraleParagrafo = grid.getColumnByKey("pluraleParagrafo");
        Grid.Column numBio = grid.getColumnByKey("numBio");
        Grid.Column numSingolari = grid.getColumnByKey("numSingolari");
        Grid.Column superaSoglia = grid.getColumnByKey("superaSoglia");

        grid.setColumnOrder(ordine, singolare, pluraleParagrafo, pluraleLista, linkPagina, numBio, numSingolari, superaSoglia, daCancellare);
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
            items = items.stream().filter(naz -> naz.pluraleLista.matches("^(?i)" + textSearchPlurale + ".*$")).toList();
        }

        if (boxSuperaSoglia != null && !boxSuperaSoglia.isIndeterminate()) {
            items = items.stream().filter(naz -> naz.superaSoglia == boxSuperaSoglia.getValue()).toList();
            if (boxSuperaSoglia.getValue()) {
                sortOrder = Sort.by(Sort.Direction.ASC, "pluraleLista");
            }
            else {
                sortOrder = Sort.by(Sort.Direction.ASC, "singolare");
            }
        }

        if (boxEsistePagina != null && !boxEsistePagina.isIndeterminate()) {
            items = items.stream().filter(naz -> naz.esistePaginaLista == boxEsistePagina.getValue()).toList();
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

    protected void sincroCancellare() {
        List<Nazionalita> items = null;

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
     * Esegue un azione di upload, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void upload() {
        appContext.getBean(UploadNazionalita.class).uploadAll();
    }

    /**
     * Apre una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void viewStatistiche() {
        wikiApiService.openWikiPage(PATH_NAZIONALITA);
    }

    /**
     * Esegue un azione di upload delle statistiche, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    public void uploadStatistiche() {
        long inizio = System.currentTimeMillis();
        appContext.getBean(StatisticheNazionalita.class).upload();
        super.fixStatisticheMinuti(inizio);
        super.uploadStatistiche();
    }

    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void testPagina() {
        Nazionalita nazionalita;
        String message;

        Optional entityBean = grid.getSelectedItems().stream().findFirst();
        if (entityBean.isPresent()) {
            nazionalita = (Nazionalita) entityBean.get();
            if (nazionalita.numBio > WPref.sogliaAttNazWiki.getInt()) {
                appContext.getBean(UploadNazionalita.class).test().upload(nazionalita.pluraleLista);
            }
            else {
                message = String.format("La nazionalita %s non raggiunge il necessario numero di voci biografiche", nazionalita.singolare);
                Avviso.show3000(message).addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            }
        }
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void uploadPagina() {
        Nazionalita nazionalita = getNazionalitaCorrente();

        if (nazionalita != null) {
            backend.uploadPagina(nazionalita.pluraleLista);
            reload();
        }
    }

    /**
     * Esegue un azione di apertura di una categoria su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public AEntity wikiCategoria() {
        Nazionalita nazionalita = (Nazionalita) super.wikiCategoria();

        String path = "Categoria:";
        wikiApiService.openWikiPage(path + nazionalita.pluraleLista);

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
        String attivitaText = textService.primaMaiuscola(attivita.pluraleLista);
        wikiApiService.openWikiPage(path + attivitaText);

        return null;
    }

    public Nazionalita getNazionalitaCorrente() {
        Nazionalita nazionalita = null;

        Optional entityBean = grid.getSelectedItems().stream().findFirst();
        if (entityBean.isPresent()) {
            nazionalita = (Nazionalita) entityBean.get();
        }

        return nazionalita;
    }

    public void updateItem(AEntity entityBean) {
        dialog = appContext.getBean(NazionalitaDialog.class, entityBean, CrudOperation.READ, crudBackend, formPropertyNamesList);
        dialog.open(this::saveHandler, this::annullaHandler);
    }

}// end of crud @Route view class