package it.algos.wiki23.backend.packages.wiki;

import ch.carnet.kasparscherrer.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.selection.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.vaad23.ui.views.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;

import java.time.*;
import java.time.format.*;
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

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DownloadService downloadService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiUtility wikiUtility;

    protected boolean usaBottoneDownload;

    protected Button buttonDownload;

    protected boolean usaBottoneElabora;

    protected Button buttonElabora;

    protected boolean usaBottoneErrori;

    protected Button buttonErrori;

    protected boolean usaBottoneUploadAll;

    protected Button buttonUpload;

    protected boolean usaBottoneDeleteAll;

    protected Button buttonDeleteAll;

    //    protected boolean usaBottoneModulo;
    //
    //    protected Button buttonModulo;

    protected boolean usaBottoneCategoria;

    protected Button buttonCategoria;

    protected boolean usaBottoneStatistiche;

    protected Button buttonStatistiche;

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

    protected String unitaMisuraDownload;

    protected String unitaMisuraElaborazione;

    protected String unitaMisuraUpload;

    protected boolean usaBottoneGiornoAnno;

    protected Button buttonGiornoAnno;

    protected boolean usaBottonePaginaNati;

    protected Button buttonPaginaNati;

    protected boolean usaBottonePaginaMorti;

    protected Button buttonPaginaMorti;

    protected boolean usaBottoneTestNati;

    protected Button buttonTestNati;

    protected boolean usaBottoneTestMorti;

    protected Button buttonTestMorti;

    protected boolean usaBottoneUploadNati;

    protected Button buttonUploadNati;

    protected boolean usaBottoneUploadMorti;

    protected Button buttonUploadMorti;

    protected boolean usaInfoDownload;

    protected WPref lastReset;

    protected WPref durataReset;

    protected WPref lastDownload;

    protected WPref durataDownload;

    protected WPref nextDownload;

    protected WPref lastElaborazione;

    protected WPref durataElaborazione;

    protected WPref lastUpload;

    protected WPref durataUpload;

    protected WPref nextUpload;

    protected WPref lastStatistica;

    protected WikiBackend crudBackend;

    protected VerticalLayout infoPlaceHolder;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DateService dateService;

    protected TextField searchFieldPlurale;

    protected TextField searchFieldWikiTitle;

    protected TextField searchFieldSingolare;

    protected TextField searchFieldParagrafo;

    protected TextField searchFieldLista;

    protected TextField searchFieldPagina;

    protected TextField searchFieldOrdinamento;

    protected TextField searchFieldNome;

    protected TextField searchFieldCognome;

    protected TextField searchFieldNascita;

    protected TextField searchFieldMorte;

    protected TextField searchFieldAttivita;

    protected TextField searchFieldNazionalita;

    protected IndeterminateCheckbox boxSuperaSoglia;

    protected IndeterminateCheckbox boxEsistePagina;

    protected Checkbox boxDistinctPlurali;

    protected Checkbox boxPagineDaCancellare;

    protected HorizontalLayout topPlaceHolder2 = new HorizontalLayout();

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

        this.usaBottoneDeleteAll = false;
        super.usaBottoneDeleteReset = false;
        super.usaBottoneNew = false;
        super.usaBottoneEdit = false;
        super.usaBottoneDelete = false;

        this.usaBottoneDownload = true;
        this.usaBottoneElabora = false;
        this.usaBottoneErrori = false;
        this.usaBottoneUploadAll = true;
        this.usaBottoneUploadPagina = false;
        //        this.usaBottoneModulo = true;
        this.usaBottoneCategoria = false;
        this.usaBottoneStatistiche = false;
        this.usaBottoneUploadStatistiche = false;
        this.usaBottonePaginaWiki = true;
        this.usaBottoneTest = true;
        this.usaInfoDownload = true;
        this.usaBottoneGiornoAnno = false;
        this.usaBottonePaginaNati = false;
        this.usaBottonePaginaMorti = false;
        this.usaBottoneTestNati = false;
        this.usaBottoneTestMorti = false;
        this.usaBottoneUploadNati = false;
        this.usaBottoneUploadMorti = false;

        this.unitaMisuraDownload = VUOTA;
        this.unitaMisuraElaborazione = VUOTA;
        this.unitaMisuraUpload = VUOTA;
    }

    protected void fixPreferenzeBackend() {
        if (crudBackend != null) {
            crudBackend.lastDownload = lastDownload;
            crudBackend.durataDownload = durataDownload;
            crudBackend.lastElabora = lastElaborazione;
            crudBackend.durataElaborazione = durataElaborazione;
            crudBackend.lastUpload = lastUpload;
            crudBackend.durataUpload = durataUpload;
        }
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();

        this.infoPlaceHolder = new VerticalLayout();
        this.infoPlaceHolder.setPadding(false);
        this.infoPlaceHolder.setSpacing(false);
        this.infoPlaceHolder.setMargin(false);
        alertPlaceHolder.add(infoPlaceHolder);
        this.fixInfo();
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void fixInfo() {
        infoPlaceHolder.removeAll();
        if (usaInfoDownload) {
            if (lastReset != null && lastReset.get() instanceof LocalDateTime reset) {
                if (reset.equals(ROOT_DATA_TIME)) {
                    message = "Reset non ancora effettuato";
                }
                else {
                    message = String.format("Ultimo reset effettuato il %s", dateService.get(reset));
                    if (durataReset != null && durataDownload.get() instanceof Integer durata) {
                        message += String.format(" in circa %d %s.", durata, "minuti");
                    }
                }
                addSpanVerdeSmall(message);
            }
            if (lastDownload != null && lastDownload.get() instanceof LocalDateTime download) {
                if (download.equals(ROOT_DATA_TIME)) {
                    message = "Download non ancora effettuato";
                }
                else {
                    message = String.format("Ultimo download effettuato il %s", dateService.get(download));
                    if (durataDownload != null && durataDownload.get() instanceof Integer durata) {
                        if (durata > 0) {
                            message += String.format(" in circa %d %s.", durata, unitaMisuraDownload);
                        }
                    }
                    if (nextDownload != null && nextDownload.get() instanceof LocalDateTime next) {
                        if (!next.equals(ROOT_DATA_TIME)) {
                            message += String.format(" Prossimo download previsto %s.", DateTimeFormatter.ofPattern("EEE, d MMM yyy 'alle' HH:mm").format(next));
                        }
                    }
                }
                addSpanVerdeSmall(message);
            }
            if (lastElaborazione != null && lastElaborazione.get() instanceof LocalDateTime elaborazione) {
                if (elaborazione.equals(ROOT_DATA_TIME)) {
                    message = "Elaborazione non ancora effettuata";
                }
                else {
                    message = String.format("Ultimo elaborazione effettuata il %s", dateService.get(elaborazione));
                    if (durataElaborazione != null && durataElaborazione.get() instanceof Integer durata) {
                        if (durata > 0) {
                            message += String.format(" in circa %d %s.", durata, unitaMisuraElaborazione);
                        }
                    }
                }

                addSpanVerdeSmall(message);
            }

            if (lastStatistica != null && lastStatistica.get() instanceof LocalDateTime statistica) {
                if (statistica.equals(ROOT_DATA_TIME)) {
                    message = "Statistiche non ancora registrate sul server";
                }
                else {
                    message = String.format("Ultime statistiche registrate il %s", dateService.get(statistica));
                }
                addSpanVerdeSmall(message);
            }

            if (lastUpload != null && lastUpload.get() instanceof LocalDateTime upload) {
                if (upload.equals(ROOT_DATA_TIME)) {
                    message = "Upload non ancora effettuato";
                }
                else {
                    message = String.format("Ultimo upload effettuato il %s", dateService.get(upload));
                    if (durataUpload != null && durataUpload.get() instanceof Integer durata) {
                        message += String.format(" in circa %d %s.", durata, unitaMisuraUpload);
                    }
                    if (nextUpload != null && nextUpload.get() instanceof LocalDateTime next) {
                        message += String.format(" Prossimo upload previsto %s.", DateTimeFormatter.ofPattern("EEE, d MMM yyy 'alle' HH:mm").format(next));
                    }
                }
                addSpanVerdeSmall(message);
            }
        }
    }

    /**
     * Costruisce un layout per i componenti al Top della view <br>
     * I componenti possono essere (nell'ordine):
     * Bottoni standard (solo icone) Reset, New, Edit, Delete, ecc.. <br>
     * SearchField per il filtro testuale di ricerca <br>
     * ComboBox e CheckBox di filtro <br>
     * Bottoni specifici non standard <br>
     * <p>
     * Metodo chiamato da CrudView.afterNavigation() <br>
     * Costruisce tutti i componenti in metodi che possono essere sovrascritti <br>
     * Inserisce la istanza in topPlaceHolder della view <br>
     * Aggiunge tutti i listeners dei bottoni, searchField, comboBox, checkBox <br>
     * <p>
     * Non può essere sovrascritto <br>
     */
    @Override
    protected void fixTopLayout() {
        super.fixTopLayout();
        this.topPlaceHolder2 = new HorizontalLayout();
        topPlaceHolder2.setClassName("buttons");
        topPlaceHolder2.setPadding(false);
        topPlaceHolder2.setSpacing(true);
        topPlaceHolder2.setMargin(false);
        topPlaceHolder2.setClassName("confirm-dialog-buttons");
    }

    /**
     * Bottoni standard (solo icone) Reset, New, Edit, Delete, ecc.. <br>
     * Può essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    protected void fixBottoniTopStandard() {
        if (usaBottoneDeleteAll) {
            buttonDeleteAll = new Button();
            buttonDeleteAll.getElement().setAttribute("theme", "error");
            buttonDeleteAll.getElement().setProperty("title", "Delete: cancella tutta la collection");
            buttonDeleteAll.addClickListener(event -> deleteAll());
            buttonDeleteAll.setIcon(new Icon(VaadinIcon.TRASH));
            topPlaceHolder.add(buttonDeleteAll);
        }

        if (usaBottoneDownload) {
            buttonDownload = new Button();
            buttonDownload.getElement().setAttribute("theme", "primary");
            buttonDownload.getElement().setProperty("title", "Download: ricarica tutti i valori dal server wiki");
            buttonDownload.setIcon(new Icon(VaadinIcon.DOWNLOAD));
            buttonDownload.addClickListener(event -> download());
            topPlaceHolder.add(buttonDownload);
        }

        if (usaBottoneElabora) {
            buttonElabora = new Button();
            buttonElabora.getElement().setAttribute("theme", "primary");
            buttonElabora.getElement().setProperty("title", "Elabora: tutte le funzioni del package");
            buttonElabora.setIcon(new Icon(VaadinIcon.PUZZLE_PIECE));
            buttonElabora.addClickListener(event -> elabora());
            topPlaceHolder.add(buttonElabora);
        }

        if (usaBottoneErrori) {
            buttonErrori = new Button();
            buttonErrori.getElement().setAttribute("theme", "error");
            buttonErrori.getElement().setProperty("title", "Errori: tutti gli errori");
            buttonErrori.setIcon(new Icon(VaadinIcon.PUZZLE_PIECE));
            buttonErrori.addClickListener(event -> errori());
            topPlaceHolder.add(buttonErrori);
        }

        if (usaBottoneUploadAll) {
            buttonUpload = new Button();
            buttonUpload.getElement().setAttribute("theme", "error");
            buttonUpload.getElement().setProperty("title", "Upload: costruisce tutte le pagine del package sul server wiki");
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

        if (usaBottoneStatistiche) {
            buttonStatistiche = new Button();
            buttonStatistiche.getElement().setAttribute("theme", "secondary");
            buttonStatistiche.getElement().setProperty("title", "Statistiche: apre la pagina esistente delle statistiche sul server wiki");
            buttonStatistiche.setIcon(new Icon(VaadinIcon.TABLE));
            buttonStatistiche.addClickListener(event -> viewStatistiche());
            topPlaceHolder.add(buttonStatistiche);
        }

        if (usaBottoneUploadStatistiche) {
            buttonUploadStatistiche = new Button();
            buttonUploadStatistiche.getElement().setAttribute("theme", "error");
            buttonUploadStatistiche.getElement().setProperty("title", "Statistiche: costruisce una nuova pagina delle statistiche sul server wiki");
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

        if (usaBottoneGiornoAnno) {
            buttonGiornoAnno = new Button();
            buttonGiornoAnno.getElement().setAttribute("theme", "secondary");
            buttonGiornoAnno.getElement().setProperty("title", "Pagina: lettura della pagina giorno/anno esistente su wiki");
            buttonGiornoAnno.setIcon(new Icon(VaadinIcon.SEARCH));
            buttonGiornoAnno.setEnabled(false);
            buttonGiornoAnno.addClickListener(event -> wikiPageGiornoAnno());
            topPlaceHolder.add(buttonGiornoAnno);
        }

        if (usaBottonePaginaNati) {
            buttonPaginaNati = new Button();
            buttonPaginaNati.getElement().setAttribute("theme", "secondary");
            buttonPaginaNati.getElement().setProperty("title", "Pagina: lettura della pagina nati esistente su wiki");
            buttonPaginaNati.setIcon(new Icon(VaadinIcon.SEARCH));
            buttonPaginaNati.setEnabled(false);
            buttonPaginaNati.addClickListener(event -> wikiPageNati());
            topPlaceHolder.add(buttonPaginaNati);
        }

        if (usaBottonePaginaMorti) {
            buttonPaginaMorti = new Button();
            buttonPaginaMorti.getElement().setAttribute("theme", "secondary");
            buttonPaginaMorti.getElement().setProperty("title", "Pagina: lettura della pagina morti esistente su wiki");
            buttonPaginaMorti.setIcon(new Icon(VaadinIcon.SEARCH));
            buttonPaginaMorti.setEnabled(false);
            buttonPaginaMorti.addClickListener(event -> wikiPageMorti());
            topPlaceHolder.add(buttonPaginaMorti);
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

        if (usaBottoneTestNati) {
            buttonTestNati = new Button();
            buttonTestNati.getElement().setAttribute("theme", "secondary");
            buttonTestNati.getElement().setProperty("title", "Test: scrittura di una voce 'nati' su Utente:Biobot");
            buttonTestNati.setIcon(new Icon(VaadinIcon.SERVER));
            buttonTestNati.setEnabled(false);
            buttonTestNati.addClickListener(event -> testPaginaNati());
            topPlaceHolder.add(buttonTestNati);
        }

        if (usaBottoneTestMorti) {
            buttonTestMorti = new Button();
            buttonTestMorti.getElement().setAttribute("theme", "secondary");
            buttonTestMorti.getElement().setProperty("title", "Test: scrittura di una voce 'morti' su Utente:Biobot");
            buttonTestMorti.setIcon(new Icon(VaadinIcon.SERVER));
            buttonTestMorti.setEnabled(false);
            buttonTestMorti.addClickListener(event -> testPaginaMorti());
            topPlaceHolder.add(buttonTestMorti);
        }

        if (usaBottoneUploadPagina) {
            buttonUploadPagina = new Button();
            buttonUploadPagina.getElement().setAttribute("theme", "error");
            buttonUploadPagina.getElement().setProperty("title", "Upload: scrittura della singola pagina sul server wiki");
            buttonUploadPagina.setIcon(new Icon(VaadinIcon.UPLOAD));
            buttonUploadPagina.setEnabled(false);
            buttonUploadPagina.addClickListener(event -> uploadPagina());
            topPlaceHolder.add(buttonUploadPagina);
        }

        if (usaBottoneUploadNati) {
            buttonUploadNati = new Button();
            buttonUploadNati.getElement().setAttribute("theme", "error");
            buttonUploadNati.getElement().setProperty("title", "Upload: scrittura della singola pagina 'nati' sul server wiki");
            buttonUploadNati.setIcon(new Icon(VaadinIcon.UPLOAD));
            buttonUploadNati.setEnabled(false);
            buttonUploadNati.addClickListener(event -> uploadPaginaNati());
            topPlaceHolder.add(buttonUploadNati);
        }

        if (usaBottoneUploadMorti) {
            buttonUploadMorti = new Button();
            buttonUploadMorti.getElement().setAttribute("theme", "error");
            buttonUploadMorti.getElement().setProperty("title", "Upload: scrittura della singola pagina 'morti' sul server wiki");
            buttonUploadMorti.setIcon(new Icon(VaadinIcon.UPLOAD));
            buttonUploadMorti.setEnabled(false);
            buttonUploadMorti.addClickListener(event -> uploadPaginaMorti());
            topPlaceHolder.add(buttonUploadMorti);
        }

        super.fixBottoniTopStandard();
    }

    protected void fixBottoniTopSpecificiAttivitaNazionalita() {
        searchFieldSingolare = new TextField();
        searchFieldSingolare.setPlaceholder(TAG_ALTRE_BY + "singolare");
        searchFieldSingolare.setWidth(WIDTH_EM);
        searchFieldSingolare.setClearButtonVisible(true);
        searchFieldSingolare.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldSingolare);

        searchFieldParagrafo = new TextField();
        searchFieldParagrafo.setPlaceholder(TAG_ALTRE_BY + "paragrafo");
        searchFieldParagrafo.setWidth(WIDTH_EM);
        searchFieldParagrafo.setClearButtonVisible(true);
        searchFieldParagrafo.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldParagrafo);

        searchFieldLista = new TextField();
        searchFieldLista.setPlaceholder(TAG_ALTRE_BY + "lista");
        searchFieldLista.setWidth(WIDTH_EM);
        searchFieldLista.setClearButtonVisible(true);
        searchFieldLista.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldLista);

        searchFieldPagina = new TextField();
        searchFieldPagina.setPlaceholder(TAG_ALTRE_BY + "pagina");
        searchFieldPagina.setWidth(WIDTH_EM);
        searchFieldPagina.setClearButtonVisible(true);
        searchFieldPagina.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldPagina);

        this.add(topPlaceHolder2);
    }


    protected void fixCheckTopSpecificiAttivitaNazionalita() {
        boxSuperaSoglia = new IndeterminateCheckbox();
        boxSuperaSoglia.setLabel("Soglia");
        boxSuperaSoglia.setIndeterminate(true);
        boxSuperaSoglia.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout2 = new HorizontalLayout(boxSuperaSoglia);
        layout2.setAlignItems(Alignment.CENTER);
        topPlaceHolder2.add(layout2);

        boxEsistePagina = new IndeterminateCheckbox();
        boxEsistePagina.setLabel("Lista");
        boxEsistePagina.setIndeterminate(true);
        boxEsistePagina.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout3 = new HorizontalLayout(boxEsistePagina);
        layout3.setAlignItems(Alignment.CENTER);
        topPlaceHolder2.add(layout3);

        boxDistinctPlurali = new Checkbox();
        boxDistinctPlurali.setLabel("Distinct plurali");
        boxDistinctPlurali.addValueChangeListener(event -> sincroPlurali());
        HorizontalLayout layout4 = new HorizontalLayout(boxDistinctPlurali);
        layout4.setAlignItems(Alignment.CENTER);
        topPlaceHolder2.add(layout4);

        boxPagineDaCancellare = new Checkbox();
        boxPagineDaCancellare.setLabel("Da cancellare");
        boxPagineDaCancellare.addValueChangeListener(event -> sincroCancellare());
        HorizontalLayout layout5 = new HorizontalLayout(boxPagineDaCancellare);
        layout5.setAlignItems(Alignment.CENTER);
        topPlaceHolder2.add(layout5);

        this.add(topPlaceHolder2);
    }

    protected boolean sincroSelection(SelectionEvent event) {
        boolean singoloSelezionato = super.sincroSelection(event);

        if (buttonDeleteAll != null) {
            buttonDeleteAll.setEnabled(!singoloSelezionato);
        }

        if (buttonDownload != null) {
            buttonDownload.setEnabled(!singoloSelezionato);
        }

        if (buttonElabora != null) {
            buttonElabora.setEnabled(!singoloSelezionato);
        }

        if (buttonUpload != null) {
            buttonUpload.setEnabled(!singoloSelezionato);
        }

        if (buttonStatistiche != null) {
            buttonStatistiche.setEnabled(!singoloSelezionato);
        }

        if (buttonUploadStatistiche != null) {
            buttonUploadStatistiche.setEnabled(!singoloSelezionato);
        }

        if (buttonCategoria != null) {
            buttonCategoria.setEnabled(singoloSelezionato);
        }

        if (buttonPaginaWiki != null) {
            buttonPaginaWiki.setEnabled(singoloSelezionato);
        }

        if (buttonGiornoAnno != null) {
            buttonGiornoAnno.setEnabled(singoloSelezionato);
        }

        if (buttonPaginaNati != null) {
            buttonPaginaNati.setEnabled(singoloSelezionato);
        }

        if (buttonPaginaMorti != null) {
            buttonPaginaMorti.setEnabled(singoloSelezionato);
        }

        if (buttonTest != null) {
            buttonTest.setEnabled(singoloSelezionato);
        }

        if (buttonTestNati != null) {
            buttonTestNati.setEnabled(singoloSelezionato);
        }

        if (buttonTestMorti != null) {
            buttonTestMorti.setEnabled(singoloSelezionato);
        }

        if (buttonUploadPagina != null) {
            buttonUploadPagina.setEnabled(singoloSelezionato);
        }

        if (buttonUploadNati != null) {
            buttonUploadNati.setEnabled(singoloSelezionato);
        }

        if (buttonUploadMorti != null) {
            buttonUploadMorti.setEnabled(singoloSelezionato);
        }

        return singoloSelezionato;
    }

    protected void sincroPlurali() {
    }


    protected void sincroCancellare() {
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
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void elabora() {
        crudBackend.elabora();
        refresh();
        fixInfo();
    }


    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void errori() {
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
     * Apre una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void viewStatistiche() {
    }

    /**
     * Esegue un azione di upload delle statistiche, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    public void uploadStatistiche() {
        reload();
    }

    /**
     * Apre una pagina di una categoria su wiki, specifica del programma/package in corso <br>
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
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void wikiPageGiornoAnno() {
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void wikiPageNati() {
    }

    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void wikiPageMorti() {
    }


    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void testPagina() {
        reload();
    }

    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void testPaginaNati() {
        reload();
    }

    /**
     * Scrive una voce di prova su Utente:Biobot/test <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void testPaginaMorti() {
        reload();
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void uploadPagina() {
        reload();
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void uploadPaginaNati() {
        reload();
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void uploadPaginaMorti() {
        reload();
    }

    public void addSpanVerdeSmall(final String message) {
        infoPlaceHolder.add(getSpan(new WrapSpan(message).color(AETypeColor.verde).fontHeight(AEFontHeight.em7)));
    }

    public void addSpanRossoSmall(final String message) {
        infoPlaceHolder.add(getSpan(new WrapSpan(message).color(AETypeColor.rosso).fontHeight(AEFontHeight.em7)));
    }

    public void addSpanRossoBold(final String message) {
        alertPlaceHolder.add(getSpan(new WrapSpan(message).color(AETypeColor.rosso).weight(AEFontWeight.bold)));
    }

    public void fixStatisticheMinuti(final long inizio) {
        long fine = System.currentTimeMillis();
        String message;

        if (lastStatistica != null) {
            lastStatistica.setValue(LocalDateTime.now());
        }
        else {
            logger.warn(new WrapLog().exception(new AlgosException("lastStatistica è nullo")));
            return;
        }

        message = String.format("Check");
        logger.info(new WrapLog().message(message).type(AETypeLog.upload).usaDb());
    }

}
