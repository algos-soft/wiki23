package it.algos.wiki23.backend.packages.anno;

import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.router.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
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

        super.gridPropertyNamesList = Arrays.asList("ordine", "nome", "bioNati", "bioMorti");
        super.formPropertyNamesList = Arrays.asList("code", "descrizione"); // controllare la congruità con la Entity
        super.sortOrder = Sort.by(Sort.Direction.DESC, "ordine");
        super.usaRowIndex = false;

        super.lastElaborazione = WPref.elaboraAnni;
        super.durataElaborazione = WPref.elaboraAnniTime;
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
    }

    /**
     * autoCreateColumns=false <br>
     * Crea le colonne normali indicate in this.colonne <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void addColumnsOneByOne() {
        super.addColumnsOneByOne();

        grid.addColumn(new ComponentRenderer<>(entity -> {
            Label label = new Label(((AnnoWiki) entity).pageNati);
            if (((AnnoWiki) entity).nati) {
                label.getElement().getStyle().set("color", "green");
            }
            else {
                label.getElement().getStyle().set("color", "red");
            }
            return label;
        })).setHeader("Nati").setKey("pageNati").setFlexGrow(0).setWidth("12em");

        grid.addColumn(new ComponentRenderer<>(entity -> {
            Label label = new Label(((AnnoWiki) entity).pageMorti);
            if (((AnnoWiki) entity).morti) {
                label.getElement().getStyle().set("color", "green");
            }
            else {
                label.getElement().getStyle().set("color", "red");
            }
            return label;
        })).setHeader("Morti").setKey("pageMorti").setFlexGrow(0).setWidth("12em");

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

        //        HeaderRow headerRow = grid.prependHeaderRow();
        //        Grid.Column ordine = grid.getColumnByKey("ordine");
        //        Grid.Column nome = grid.getColumnByKey("nome");
        //        Grid.Column bioNati = grid.getColumnByKey("bioNati");
        //        Grid.Column bioMorti = grid.getColumnByKey("bioMorti");
        //        Grid.Column pageNati = grid.getColumnByKey("pageNati");
        //        Grid.Column pageMorti = grid.getColumnByKey("pageMorti");
        //        Grid.Column nati = grid.getColumnByKey("nati");
        //        Grid.Column morti = grid.getColumnByKey("morti");
        //
        //        headerRow.join(ordine, nome).setText("Wiki");
        //        headerRow.join(bioNati, bioMorti).setText("Numero biografie");
        //        headerRow.join(pageNati, pageMorti).setText("Titolo pagine su wikipedia");
        //        headerRow.join(nati, morti).setText("Esistenza pagine");
    }


    /**
     * Apre una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void viewStatistiche() {
        wikiApiService.openWikiPage(PATH_ANNI);
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void wikiPageGiornoAnno() {
        AnnoWiki anno = (AnnoWiki) super.wikiPage();
        wikiApiService.openWikiPage(anno.nome);
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
        AnnoWiki anno = (AnnoWiki) super.wikiPage();
        appContext.getBean(UploadAnni.class).uploadTestNascita(anno.nome);
        reload();
    }


    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void testPaginaMorti() {
        AnnoWiki anno = (AnnoWiki) super.wikiPage();
        appContext.getBean(UploadAnni.class).uploadTestMorte(anno.nome);
        reload();
    }


    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void uploadPaginaNati() {
        String nomeAnno = getNomeAnno();
        appContext.getBean(UploadAnni.class).uploadNascita(nomeAnno);
        reload();
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void uploadPaginaMorti() {
        String nomeAnno = getNomeAnno();
        appContext.getBean(UploadAnni.class).uploadMorte(nomeAnno);
        reload();
    }

    public String getNomeAnno() {
        AnnoWiki anno = (AnnoWiki) super.wikiPage();
        return anno != null ? anno.nome : VUOTA;
    }

}// end of crud @Route view class