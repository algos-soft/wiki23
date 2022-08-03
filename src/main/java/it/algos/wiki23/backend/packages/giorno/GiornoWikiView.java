package it.algos.wiki23.backend.packages.giorno;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.router.*;
import it.algos.vaad23.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.statistiche.*;
import it.algos.wiki23.backend.upload.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.data.domain.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Thu, 14-Jul-2022
 * Time: 20:04
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Giorni")
@Route(value = "giornoWiki", layout = MainLayout.class)
public class GiornoWikiView extends WikiView {


    //--per eventuali metodi specifici
    private GiornoWikiBackend backend;

    //--per eventuali metodi specifici
    private GiornoWikiDialog dialog;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public GiornoWikiView(@Autowired final GiornoWikiBackend crudBackend) {
        super(crudBackend, GiornoWiki.class);
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
        super.formPropertyNamesList = Arrays.asList("code", "descrizione");

        super.sortOrder = Sort.by(Sort.Direction.ASC, "ordine");
        super.usaRowIndex = false;

        super.lastElaborazione = WPref.elaboraGiorni;
        super.durataElaborazione = WPref.elaboraGiorniTime;
        super.lastUpload = WPref.uploadGiorni;
        super.durataUpload = WPref.uploadGiorniTime;
        super.nextUpload = WPref.uploadGiorniPrevisto;
        super.lastStatistica = WPref.statisticaGiorni;
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

        super.dialogClazz = GiornoWikiDialog.class;
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
            Label label = new Label(((GiornoWiki) entity).pageNati);
            label.getElement().getStyle().set("color", "green");
            return label;
        })).setHeader("Nati").setKey("pageNati").setFlexGrow(0).setWidth("14em");

        grid.addColumn(new ComponentRenderer<>(entity -> {
            Label label = new Label(((GiornoWiki) entity).pageMorti);
            label.getElement().getStyle().set("color", "green");
            return label;
        })).setHeader("Morti").setKey("pageMorti").setFlexGrow(0).setWidth("14em");

    }


    /**
     * Apre una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void viewStatistiche() {
        wikiApiService.openWikiPage(PATH_GIORNI);
    }

    /**
     * Esegue un azione di upload delle statistiche, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    @Override
    public void uploadStatistiche() {
        long inizio = System.currentTimeMillis();
        appContext.getBean(StatisticheGiorni.class).upload();
        super.fixStatisticheMinuti(inizio);
        super.uploadStatistiche();
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void wikiPageGiornoAnno() {
        GiornoWiki giorno = (GiornoWiki) super.wikiPage();
        wikiApiService.openWikiPage(giorno.nome);
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void wikiPageNati() {
        GiornoWiki giorno = (GiornoWiki) super.wikiPage();
        wikiApiService.openWikiPage(giorno.pageNati);
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void wikiPageMorti() {
        GiornoWiki giorno = (GiornoWiki) super.wikiPage();
        wikiApiService.openWikiPage(giorno.pageMorti);
    }

    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void testPaginaNati() {
        GiornoWiki giorno = (GiornoWiki) super.wikiPage();
        reload();
    }


    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void testPaginaMorti() {
        GiornoWiki giorno = (GiornoWiki) super.wikiPage();
        reload();
    }

    /**
     * Esegue un azione di upload, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void upload() {
        appContext.getBean(UploadGiorni.class).uploadAll();
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void uploadPaginaNati() {
        GiornoWiki giorno = (GiornoWiki) super.wikiPage();
        appContext.getBean(UploadGiorni.class).uploadNascita(giorno.nome);
        reload();
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void uploadPaginaMorti() {
        GiornoWiki giorno = (GiornoWiki) super.wikiPage();
        appContext.getBean(UploadGiorni.class).uploadMorte(giorno.nome);
        reload();
    }

}// end of crud @Route view class