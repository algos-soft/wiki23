package it.algos.wiki23.backend.packages.anno;

import com.vaadin.flow.component.checkbox.*;
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
import it.algos.wiki23.backend.statistiche.*;
import it.algos.wiki23.backend.upload.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

import org.springframework.data.domain.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 08-Jul-2022
 * Time: 06:34
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Anni")
@Route(value = "annoWiki", layout = MainLayout.class)
public class AnnoWikiView extends WikiView {


    //--per eventuali metodi specifici
    private AnnoWikiBackend backend;

    //--per eventuali metodi specifici
    private AnnoWikiDialog dialog;


    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public AnnoWikiView(@Autowired final AnnoWikiBackend crudBackend) {
        super(crudBackend, AnnoWiki.class);
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

        super.gridPropertyNamesList = Arrays.asList("ordine", "nome", "bioNati", "bioMorti", "natiOk", "mortiOk");
        super.formPropertyNamesList = Arrays.asList("code", "descrizione"); // controllare la congruità con la Entity
        super.sortOrder = Sort.by(Sort.Direction.DESC, "ordine");
        super.usaRowIndex = false;

        super.lastElaborazione = WPref.elaboraAnni;
        super.durataElaborazione = WPref.elaboraAnniTime;
        super.lastStatistica = WPref.statisticaAnni;
        super.lastUpload = WPref.uploadAnni;
        super.durataUpload = WPref.uploadAnniTime;
        super.nextUpload = WPref.uploadAnniPrevisto;
        super.usaBottoneDeleteReset = true;
        super.usaReset = true;
        super.usaBottoneElabora = true;
        super.usaBottoneDownload = false;
        super.usaBottoneStatistiche = true;
        super.usaBottoneUploadStatistiche = true;
        super.usaBottoneNew = false;
        super.usaBottoneEdit = false;
        super.usaBottoneSearch = false;
        super.usaBottoneDelete = false;
        super.usaBottonePaginaWiki = false;
        super.usaBottoneGiornoAnno = true;
        super.usaBottonePaginaNati = true;
        super.usaBottonePaginaMorti = true;
        super.usaBottoneTest = false;
        super.usaBottoneTestNati = true;
        super.usaBottoneTestMorti = true;
        super.usaBottoneUploadNati = true;
        super.usaBottoneUploadMorti = true;

        super.dialogClazz = AnnoWikiDialog.class;
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
        addSpanRossoSmall(String.format("%s: %s", "Pagine da cancellare", backend.countListeDaCancellare()));
    }

    @Override
    protected void fixTopLayout() {
        super.fixTopLayout();
        this.fixBottoniTopSpecificiAnni();
    }

    protected void fixBottoniTopSpecificiAnni() {
        boxPagineDaCancellare = new Checkbox();
        boxPagineDaCancellare.setLabel("Da cancellare");
        boxPagineDaCancellare.addValueChangeListener(event -> sincroCancellare());
        HorizontalLayout layout5 = new HorizontalLayout(boxPagineDaCancellare);
        layout5.setAlignItems(Alignment.CENTER);
        topPlaceHolder2.add(layout5);

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

        Grid.Column paginaNati = grid.addColumn(new ComponentRenderer<>(entity -> {
            String wikiTitle = ((AnnoWiki) entity).pageNati;
            int numVoci = ((AnnoWiki) entity).bioNati;
            Anchor anchor;
            if (((AnnoWiki) entity).esistePaginaNati) {
                if (numVoci == 0) {
                    anchor = new Anchor(PATH_WIKI_EDIT + wikiTitle + TAG_DELETE, wikiTitle);
                    anchor.getElement().getStyle().set("color", "red");
                }
                else {
                    anchor = new Anchor(PATH_WIKI + wikiTitle, wikiTitle);
                    anchor.getElement().getStyle().set("color", "green");
                }
                anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
            }
            else {
                anchor = new Anchor(PATH_WIKI + wikiTitle, wikiTitle);
                anchor.getElement().getStyle().set("color", "red");
            }
            return new Span(anchor);
        })).setHeader("Nati").setKey("pageNati").setFlexGrow(0).setWidth("12em");

        Grid.Column paginaMorti = grid.addColumn(new ComponentRenderer<>(entity -> {
            String wikiTitle = ((AnnoWiki) entity).pageMorti;
            int numVoci = ((AnnoWiki) entity).bioMorti;
            Anchor anchor;
            if (((AnnoWiki) entity).esistePaginaMorti) {
                if (numVoci == 0) {
                    anchor = new Anchor(PATH_WIKI_EDIT + wikiTitle + TAG_DELETE, wikiTitle);
                    anchor.getElement().getStyle().set("color", "red");
                }
                else {
                    anchor = new Anchor(PATH_WIKI + wikiTitle, wikiTitle);
                    anchor.getElement().getStyle().set("color", "green");
                }
                anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
            }
            else {
                anchor = new Anchor(PATH_WIKI + wikiTitle, wikiTitle);
                anchor.getElement().getStyle().set("color", "red");
            }
            return new Span(anchor);
        })).setHeader("Morti").setKey("pageMorti").setFlexGrow(0).setWidth("12em");

        Grid.Column ordine = grid.getColumnByKey("ordine");
        Grid.Column nome = grid.getColumnByKey("nome");
        Grid.Column bioNati = grid.getColumnByKey("bioNati");
        Grid.Column bioMorti = grid.getColumnByKey("bioMorti");
        Grid.Column natiOk = grid.getColumnByKey("natiOk");
        Grid.Column mortiOk = grid.getColumnByKey("mortiOk");

        grid.setColumnOrder(ordine, nome, bioNati, bioMorti, paginaNati, paginaMorti, natiOk, mortiOk);
    }

    /**
     * Costruisce il corpo principale (obbligatorio) della Grid <br>
     * <p>
     * Metodo chiamato da CrudView.afterNavigation() <br>
     * Costruisce un' istanza dedicata con la Grid <br>
     */
    @Override
    protected void fixBodyLayout() {
        super.fixBodyLayout();
    }

    protected void sincroCancellare() {
        List<AnnoWiki> items = null;

        if (boxPagineDaCancellare != null) {
            if (boxPagineDaCancellare.getValue()) {
                items = backend.fetchDaCancellare();
            }
            else {
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
     * Apre una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void viewStatistiche() {
        wikiApiService.openWikiPage(PATH_ANNI);
    }

    /**
     * Esegue un azione di upload delle statistiche, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    @Override
    public void uploadStatistiche() {
        long inizio = System.currentTimeMillis();
        appContext.getBean(StatisticheAnni.class).upload();
        super.fixStatisticheMinuti(inizio);
        super.uploadStatistiche();
    }


    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void wikiPageGiornoAnno() {
        wikiApiService.openWikiPage(getNomeAnno());
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void wikiPageNati() {
        AnnoWiki anno = (AnnoWiki) super.wikiPage();
        wikiApiService.openWikiPage(anno.pageNati);
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void wikiPageMorti() {
        AnnoWiki anno = (AnnoWiki) super.wikiPage();
        wikiApiService.openWikiPage(anno.pageMorti);
    }

    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void testPaginaNati() {
        appContext.getBean(UploadAnni.class).nascita().test().upload(getNomeAnno());
        reload();
    }


    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void testPaginaMorti() {
        appContext.getBean(UploadAnni.class).morte().test().upload(getNomeAnno());
        reload();
    }

    /**
     * Esegue un azione di upload, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void upload() {
        appContext.getBean(UploadAnni.class).uploadAll();
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void uploadPaginaNati() {
        appContext.getBean(UploadAnni.class).nascita().upload(getNomeAnno());
        reload();
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void uploadPaginaMorti() {
        appContext.getBean(UploadAnni.class).morte().upload(getNomeAnno());
        reload();
    }

    public String getNomeAnno() {
        AnnoWiki anno = (AnnoWiki) super.wikiPage();
        return anno != null ? anno.nome : VUOTA;
    }

}// end of crud @Route view class