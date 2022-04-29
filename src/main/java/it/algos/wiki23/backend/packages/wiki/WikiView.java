package it.algos.wiki23.backend.packages.wiki;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.data.selection.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.vaad23.ui.views.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.service.*;
import org.springframework.beans.factory.annotation.*;

import java.time.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 22-apr-2022
 * Time: 08:01
 */
public abstract class WikiView extends CrudView {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiApiService wikiApiService;

    protected boolean usaBottoneDownload;

    protected Button buttonDownload;

    protected boolean usaBottoneUpload;

    protected Button buttonUpload;

//    protected boolean usaBottoneModulo;
//
//    protected Button buttonModulo;

    protected boolean usaBottoneCategoria;

    protected Button buttonCategoria;

//    protected boolean usaBottoneStatistiche;
//
//    protected Button buttonStatistiche;

    protected boolean usaBottoneUploadStatistiche;

    protected Button buttonUploadStatistiche;


    protected boolean usaBottonePaginaWiki;

    protected Button buttonPaginaWiki;

    protected boolean usaBottoneTest;

    protected Button buttonTest;

    protected boolean usaBottoneUploadPagina;

    protected Button buttonUploadPagina;

    //    protected String parametri;

    protected String wikiModuloTitle;

    protected String wikiStatisticheTitle;

    //    protected String alfabetico;
    //
    //    protected String singolare;
    //
    //    protected String plurale;

    protected boolean usaInfoDownload;

    protected WPref lastDownload;

    protected WPref lastElaborazione;

    protected WPref durataElaborazione;

    protected WPref lastUpload;


    protected WPref durataUpload;

    protected WikiBackend crudBackend;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DateService dateService;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public WikiView(final WikiBackend crudBackend, final Class entityClazz) {
        super(crudBackend, entityClazz);
        this.crudBackend = crudBackend;
    }


    /**
     * Preferenze usate da questa 'view' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaBottoneDeleteReset = false;
        super.usaBottoneNew = false;
        super.usaBottoneEdit = false;
        super.usaBottoneDelete = false;

        this.usaBottoneDownload = true;
        this.usaBottoneUpload = true;
//        this.usaBottoneModulo = true;
        this.usaBottoneCategoria = false;
//        this.usaBottoneStatistiche = true;
        this.usaBottoneUploadStatistiche = true;
        this.usaBottonePaginaWiki = true;
        this.usaBottoneTest = true;
        this.usaInfoDownload = true;
    }

    protected void fixPreferenzeBackend() {
        if (crudBackend != null) {
            crudBackend.lastDownload = lastDownload;
        }
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();

        if (usaInfoDownload) {
            if (lastDownload != null && lastDownload.get() instanceof LocalDateTime download) {
                if (download.equals(ROOT_DATA_TIME)) {
                    message = "Download non ancora effettuato";
                }
                else {
                    message = String.format("Ultimo download effettuato il %s", dateService.get(download));
                }
                addSpanVerdeSmall(message);
            }
            if (lastElaborazione != null && lastElaborazione.get() instanceof LocalDateTime elaborazione) {
                if (elaborazione.equals(ROOT_DATA_TIME)) {
                    message = "Elaborazione non ancora effettuata";
                }
                else {
                    message = String.format("Ultimo elaborazione effettuata il %s", dateService.get(elaborazione));
                }
                addSpanVerdeSmall(message);
            }
            if (lastUpload != null && lastUpload.get() instanceof LocalDateTime upload) {
                if (upload.equals(ROOT_DATA_TIME)) {
                    message = "Upload non ancora effettuato";
                }
                else {
                    message = String.format("Ultimo upload effettuato il %s", dateService.get(upload));
                }
                addSpanVerdeSmall(message);
            }
        }
    }


    /**
     * Bottoni standard (solo icone) Reset, New, Edit, Delete, ecc.. <br>
     * Può essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    protected void fixBottoniTopStandard() {
        if (usaBottoneDownload) {
            buttonDownload = new Button();
            buttonDownload.getElement().setAttribute("theme", "primary");
            buttonDownload.getElement().setProperty("title", "Download: ricarica tutti i valori dal server wiki");
            buttonDownload.setIcon(new Icon(VaadinIcon.DOWNLOAD));
            buttonDownload.addClickListener(event -> download());
            topPlaceHolder.add(buttonDownload);
        }

        if (usaBottoneUpload) {
            buttonUpload = new Button();
            buttonUpload.getElement().setAttribute("theme", "error");
            buttonUpload.getElement().setProperty("title", "Upload: costruisce tutte le pagine di attività sul server wiki");
            buttonUpload.setIcon(new Icon(VaadinIcon.UPLOAD));
            buttonUpload.addClickListener(event -> upload());
            topPlaceHolder.add(buttonUpload);
        }

//        if (usaBottoneModulo) {
//            buttonModulo = new Button();
//            buttonModulo.getElement().setAttribute("theme", "secondary");
//            buttonModulo.getElement().setProperty("title", "Modulo: lettura del modulo originario su wiki");
//            buttonModulo.setIcon(new Icon(VaadinIcon.LIST));
//            buttonModulo.addClickListener(event -> wikiModulo());
//            topPlaceHolder.add(buttonModulo);
//        }

//        if (usaBottoneStatistiche) {
//            buttonStatistiche = new Button();
//            buttonStatistiche.getElement().setAttribute("theme", "secondary");
//            buttonStatistiche.getElement().setProperty("title", "Statistiche: lettura della pagina su wiki");
//            buttonStatistiche.setIcon(new Icon(VaadinIcon.TABLE));
//            buttonStatistiche.addClickListener(event -> statistiche());
//            topPlaceHolder.add(buttonStatistiche);
//        }

        if (usaBottoneUploadStatistiche) {
            buttonUploadStatistiche = new Button();
            buttonUploadStatistiche.getElement().setAttribute("theme", "error");
            buttonUploadStatistiche.getElement().setProperty("title", "Statistiche: costruisce la pagina delle statistiche sul server wiki");
            buttonUploadStatistiche.setIcon(new Icon(VaadinIcon.TABLE));
            buttonUploadStatistiche.addClickListener(event -> uploadStatistiche());
            topPlaceHolder.add(buttonUploadStatistiche);
        }

        if (usaBottoneCategoria) {
            buttonCategoria = new Button();
            buttonCategoria.getElement().setAttribute("theme", "secondary");
            buttonCategoria.getElement().setProperty("title", "Categoria: apertura della pagina su wiki");
            buttonCategoria.setIcon(new Icon(VaadinIcon.RECORDS));
            buttonCategoria.setEnabled(false);
            buttonCategoria.addClickListener(event -> wikiCategoria());
            topPlaceHolder.add(buttonCategoria);
        }

        if (usaBottonePaginaWiki) {
            buttonPaginaWiki = new Button();
            buttonPaginaWiki.getElement().setAttribute("theme", "secondary");
            buttonPaginaWiki.getElement().setProperty("title", "Pagina: lettura della pagina esistente su wiki");
            buttonPaginaWiki.setIcon(new Icon(VaadinIcon.SEARCH));
            buttonPaginaWiki.setEnabled(false);
            buttonPaginaWiki.addClickListener(event -> wikiPage());
            topPlaceHolder.add(buttonPaginaWiki);
        }

        if (usaBottoneTest) {
            buttonTest = new Button();
            buttonTest.getElement().setAttribute("theme", "secondary");
            buttonTest.getElement().setProperty("title", "Test: scrittura di una voce su Utente:Biobot");
            buttonTest.setIcon(new Icon(VaadinIcon.SERVER));
            buttonTest.setEnabled(false);
            buttonTest.addClickListener(event -> testPagina());
            topPlaceHolder.add(buttonTest);
        }

        if (usaBottoneUploadPagina) {
            buttonUploadPagina = new Button();
            buttonUploadPagina.getElement().setAttribute("theme", "error");
            buttonUploadPagina.getElement().setProperty("title", "Upload: scrittura della singola voce sul server wiki");
            buttonUploadPagina.setIcon(new Icon(VaadinIcon.UPLOAD));
            buttonUploadPagina.setEnabled(false);
            buttonUploadPagina.addClickListener(event -> uploadPagina());
            topPlaceHolder.add(buttonUploadPagina);
        }

        super.fixBottoniTopStandard();
    }

    protected boolean sincroSelection(SelectionEvent event) {
        boolean singoloSelezionato = super.sincroSelection(event);

        if (buttonDownload != null) {
            buttonDownload.setEnabled(!singoloSelezionato);
        }
        if (buttonUpload != null) {
            buttonUpload.setEnabled(!singoloSelezionato);
        }
//        if (buttonModulo != null) {
//            buttonModulo.setEnabled(!singoloSelezionato);
//        }
//        if (buttonStatistiche != null) {
//            buttonStatistiche.setEnabled(!singoloSelezionato);
//        }
        if (buttonUploadStatistiche != null) {
            buttonUploadStatistiche.setEnabled(!singoloSelezionato);
        }

        if (buttonCategoria != null) {
            buttonCategoria.setEnabled(singoloSelezionato);
        }
        if (buttonPaginaWiki != null) {
            buttonPaginaWiki.setEnabled(singoloSelezionato);
        }
        if (buttonTest != null) {
            buttonTest.setEnabled(singoloSelezionato);
        }
        if (buttonUploadPagina != null) {
            buttonUploadPagina.setEnabled(singoloSelezionato);
        }

        return singoloSelezionato;
    }

    protected void fixBottomLayout() {
        super.fixBottomLayout();
    }

    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void download() {
        crudBackend.download(wikiModuloTitle);
        reload();
    }

    /**
     * Esegue un azione di upload, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void upload() {
        reload();
    }

    /**
     * Esegue un azione di apertura di un modulo su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void wikiModulo() {
        if (textService.isValid(wikiModuloTitle)) {
            wikiApiService.openWikiPage(wikiModuloTitle);
        }
    }


    /**
     * Esegue un azione di apertura di una pagina wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void statistiche() {
        if (textService.isValid(wikiStatisticheTitle)) {
            wikiApiService.openWikiPage(wikiStatisticheTitle);
        }
    }

    /**
     * Esegue un azione di upload delle statistiche, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void uploadStatistiche() {
        reload();
    }

    /**
     * Esegue un azione di apertura di una categoria su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public AEntity wikiCategoria() {
        AEntity entiyBeanUnicoSelezionato = null;
        Set righeSelezionate;

        if (grid != null) {
            righeSelezionate = grid.getSelectedItems();
            if (righeSelezionate.size() == 1) {
                entiyBeanUnicoSelezionato = (AEntity) righeSelezionate.toArray()[0];
            }
        }

        return entiyBeanUnicoSelezionato;
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected AEntity wikiPage() {
        AEntity entiyBeanUnicoSelezionato = null;
        Set righeSelezionate;

        if (grid != null) {
            righeSelezionate = grid.getSelectedItems();
            if (righeSelezionate.size() == 1) {
                entiyBeanUnicoSelezionato = (AEntity) righeSelezionate.toArray()[0];
            }
        }

        return entiyBeanUnicoSelezionato;
    }

    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void testPagina() {
        reload();
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void uploadPagina() {
        reload();
    }
    public void addSpanVerdeSmall(final String message) {
        alertPlaceHolder.add(getSpan(new WrapSpan(message).color(AETypeColor.verde).fontHeight(AEFontHeight.em7)));
    }
    public void addSpanRossoBold(final String message) {
        alertPlaceHolder.add(getSpan(new WrapSpan(message).color(AETypeColor.rosso).weight(AEFontWeight.bold)));
    }

}
