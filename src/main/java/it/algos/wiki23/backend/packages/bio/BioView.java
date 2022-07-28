package it.algos.wiki23.backend.packages.bio;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dialog.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.vaad23.ui.dialog.*;
import it.algos.vaad23.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.vaadin.crudui.crud.*;

import java.util.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: gio, 28-apr-2022
 * Time: 11:57
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Biografie")
@Route(value = TAG_BIO, layout = MainLayout.class)
public class BioView extends WikiView {


    //--per eventuali metodi specifici
    private BioBackend backend;

    //--per eventuali metodi specifici
    private BioDialog dialog;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public BioView(@Autowired final BioBackend crudBackend) {
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

        super.gridPropertyNamesList = Arrays.asList("pageId", "wikiTitle", "elaborato", "ordinamento", "sesso", "nome", "cognome",
                "giornoNato",
                "annoNato",
                "giornoMorto", "annoMorto",
                "attivita", "attivita2", "attivita3",
                "nazionalita"
        );
        super.formPropertyNamesList = Arrays.asList("pageId", "wikiTitle", "elaborato", "sesso", "nome", "cognome", "ordinamento",
                "giornoNato",
                "annoNato", "giornoMorto", "annoMorto",
                "giornoNatoOrd", "giornoMortoOrd", "annoNatoOrd", "annoMortoOrd",
                "attivita", "attivita2", "attivita3",
                "nazionalita"
        );
        super.sortOrder = Sort.by(Sort.Direction.DESC, "ordinamento");

        super.lastDownload = WPref.downloadBio;
        super.durataDownload = WPref.downloadBioTime;
        super.nextDownload = WPref.downloadBioPrevisto;
        super.lastElaborazione = WPref.elaboraBio;
        super.durataElaborazione = WPref.elaboraBioTime;

        super.usaBottoneElabora = true;
        super.usaBottoneDeleteAll = true;
        super.usaBottoneNew = true;
        super.usaBottoneDelete = true;
        super.usaBottoneSearch = false;
        super.usaBottonePaginaWiki = true;
        super.dialogClazz = BioDialog.class;
        super.unitaMisuraDownload = "minuti";
        super.unitaMisuraElaborazione = "minuti";
        super.fixPreferenzeBackend();
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();
        String categoria;
        int numBio = 0;
        WResult result;
        categoria = WPref.categoriaBio.getStr();
        result = appContext.getBean(QueryInfoCat.class).urlRequest(categoria);
        numBio = result.getIntValue();

        Anchor anchor = new Anchor(PATH_CATEGORIA + categoria, "Categoria: " + categoria);
        anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
        alertPlaceHolder.add(new Span(anchor));

        addSpanVerde(String.format("Nella categoria [%s] sono presenti %s biografie", categoria, textService.format(numBio)));
    }

    @Override
    protected void fixTopLayout() {
        super.fixTopLayout();
        fixBottoniTopSpecificiBio();
    }


    protected void fixBottoniTopSpecificiBio() {
        String widthEM_uno = "16ex";
        String widthEM_due = "21ex";
        String tag = TAG_ALTRE + " by ";

        searchFieldWikiTitle = new TextField();
        searchFieldWikiTitle.setPlaceholder(tag + "wikiTitle");
        searchFieldWikiTitle.setWidth(widthEM_uno);
        searchFieldWikiTitle.setClearButtonVisible(true);
        searchFieldWikiTitle.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldWikiTitle);

        searchFieldOrdinamento = new TextField();
        searchFieldOrdinamento.setPlaceholder(tag + "ordinamento");
        searchFieldOrdinamento.setWidth(widthEM_due);
        searchFieldOrdinamento.setClearButtonVisible(true);
        searchFieldOrdinamento.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldOrdinamento);

        searchFieldNome = new TextField();
        searchFieldNome.setPlaceholder(tag + "nome");
        searchFieldNome.setWidth(widthEM_uno);
        searchFieldNome.setClearButtonVisible(true);
        searchFieldNome.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldNome);

        searchFieldCognome = new TextField();
        searchFieldCognome.setPlaceholder(tag + "cognome");
        searchFieldCognome.setWidth(widthEM_due);
        searchFieldCognome.setClearButtonVisible(true);
        searchFieldCognome.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldCognome);

        searchFieldNascita = new TextField();
        searchFieldNascita.setPlaceholder(tag + "nascita");
        searchFieldNascita.setWidth(widthEM_uno);
        searchFieldNascita.setClearButtonVisible(true);
        searchFieldNascita.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldNascita);

        searchFieldMorte = new TextField();
        searchFieldMorte.setPlaceholder(tag + "morte");
        searchFieldMorte.setWidth(widthEM_uno);
        searchFieldMorte.setClearButtonVisible(true);
        searchFieldMorte.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldMorte);

        searchFieldAttivita = new TextField();
        searchFieldAttivita.setPlaceholder(tag + "attività");
        searchFieldAttivita.setWidth(widthEM_uno);
        searchFieldAttivita.setClearButtonVisible(true);
        searchFieldAttivita.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldAttivita);

        searchFieldNazionalita = new TextField();
        searchFieldNazionalita.setPlaceholder(tag + "nazionalità");
        searchFieldNazionalita.setWidth(widthEM_due);
        searchFieldNazionalita.setClearButtonVisible(true);
        searchFieldNazionalita.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder2.add(searchFieldNazionalita);

        this.add(topPlaceHolder2);
    }

    @Override
    protected void fixBodyLayout() {
        super.fixBodyLayout();

        HeaderRow headerRow = grid.prependHeaderRow();
        Grid.Column pageId = grid.getColumnByKey("pageId");
        Grid.Column wikiTitle = grid.getColumnByKey("wikiTitle");
        Grid.Column elaborato = grid.getColumnByKey("elaborato");
        Grid.Column sesso = grid.getColumnByKey("sesso");
        Grid.Column nome = grid.getColumnByKey("nome");
        Grid.Column cognome = grid.getColumnByKey("cognome");
        Grid.Column giornoNato = grid.getColumnByKey("giornoNato");
        Grid.Column annoNato = grid.getColumnByKey("annoNato");
        Grid.Column giornoMorto = grid.getColumnByKey("giornoMorto");
        Grid.Column annoMorto = grid.getColumnByKey("annoMorto");
        Grid.Column attivita = grid.getColumnByKey("attivita");
        Grid.Column attivita2 = grid.getColumnByKey("attivita2");
        Grid.Column attivita3 = grid.getColumnByKey("attivita3");
        Grid.Column nazionalita = grid.getColumnByKey("nazionalita");
        Grid.Column nazionalita2 = grid.getColumnByKey("nazionalita");

        headerRow.join(pageId, wikiTitle, elaborato).setText("Wiki");
        headerRow.join(sesso, nome, cognome).setText("Anagrafica");
        headerRow.join(giornoNato, annoNato).setText("Nascita");
        headerRow.join(giornoMorto, annoMorto).setText("Morte");
        headerRow.join(attivita, attivita2, attivita3).setText("Attività");
        //        headerRow.join(nazionalita,nazionalita2).setText("Nazionalità");
    }


    /**
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void sincroFiltri() {
        List<Bio> items = backend.findAll(sortOrder);

        final String textWikiTitle = searchFieldWikiTitle != null ? searchFieldWikiTitle.getValue() : VUOTA;
        if (textService.isValidNoSpace(textWikiTitle)) {
            items = items
                    .stream()
                    .filter(bio -> bio.wikiTitle != null ? bio.wikiTitle.matches("^(?i)" + textWikiTitle + ".*$") : false)
                    .toList();
        }
        else {
            if (textWikiTitle.equals(SPAZIO)) {
                items = items
                        .stream()
                        .filter(bio -> bio.wikiTitle == null)
                        .toList();
            }
        }

        final String textSearchOrdine = searchFieldOrdinamento != null ? searchFieldOrdinamento.getValue() : VUOTA;
        if (textService.isValidNoSpace(textSearchOrdine)) {
            items = items
                    .stream()
                    .filter(bio -> bio.ordinamento != null ? bio.ordinamento.matches("^(?i)" + textSearchOrdine + ".*$") : false)
                    .toList();
        }
        else {
            if (textSearchOrdine.equals(SPAZIO)) {
                items = items
                        .stream()
                        .filter(bio -> bio.ordinamento == null)
                        .toList();
            }
        }


        final String textSearchNome = searchFieldNome != null ? searchFieldNome.getValue() : VUOTA;
        if (textService.isValidNoSpace(textSearchNome)) {
            items = items
                    .stream()
                    .filter(bio -> bio.nome != null ? bio.nome.matches("^(?i)" + textSearchNome + ".*$") : false)
                    .toList();
        }
        else {
            if (textSearchNome.equals(SPAZIO)) {
                items = items
                        .stream()
                        .filter(bio -> bio.nome == null)
                        .toList();
            }
        }

        final String textSearchCognome = searchFieldCognome != null ? searchFieldCognome.getValue() : VUOTA;
        if (textService.isValidNoSpace(textSearchCognome)) {
            items = items
                    .stream()
                    .filter(bio -> bio.cognome != null ? bio.cognome.matches("^(?i)" + textSearchCognome + ".*$") : false)
                    .toList();
        }
        else {
            if (textSearchCognome.equals(SPAZIO)) {
                items = items
                        .stream()
                        .filter(bio -> bio.cognome == null)
                        .toList();
            }
        }


        final String textSearchNascita = searchFieldNascita != null ? searchFieldNascita.getValue() : VUOTA;
        if (textService.isValidNoSpace(textSearchNascita)) {
            items = items
                    .stream()
                    .filter(bio -> bio.annoNato != null ? bio.annoNato.matches("^(?i)" + textSearchNascita + ".*$") : false)
                    .toList();
        }
        else {
            if (textSearchNascita.equals(SPAZIO)) {
                items = items
                        .stream()
                        .filter(bio -> bio.annoNato == null)
                        .toList();
            }
        }

        final String textSearchMorte = searchFieldMorte != null ? searchFieldMorte.getValue() : VUOTA;
        if (textService.isValidNoSpace(textSearchMorte)) {
            items = items
                    .stream()
                    .filter(bio -> bio.annoMorto != null ? bio.annoMorto.matches("^(?i)" + textSearchMorte + ".*$") : false)
                    .toList();
        }
        else {
            if (textSearchMorte.equals(SPAZIO)) {
                items = items
                        .stream()
                        .filter(bio -> bio.annoMorto == null)
                        .toList();
            }
        }

        final String textSearchAttivita = searchFieldAttivita != null ? searchFieldAttivita.getValue() : VUOTA;
        if (textService.isValidNoSpace(textSearchAttivita)) {
            items = items
                    .stream()
                    .filter(bio -> bio.attivita != null ? bio.attivita.matches("^(?i)" + textSearchAttivita + ".*$") : false)
                    .toList();
        }
        else {
            if (textSearchAttivita.equals(SPAZIO)) {
                items = items
                        .stream()
                        .filter(bio -> bio.attivita == null)
                        .toList();
            }
        }

        final String textSearchNazionalita = searchFieldNazionalita != null ? searchFieldNazionalita.getValue() : VUOTA;
        if (textService.isValidNoSpace(textSearchNazionalita)) {
            items = items
                    .stream()
                    .filter(bio -> bio.nazionalita != null ? bio.nazionalita.matches("^(?i)" + textSearchNazionalita + ".*$") : false)
                    .toList();
        }
        else {
            if (textSearchNazionalita.equals(SPAZIO)) {
                items = items
                        .stream()
                        .filter(bio -> bio.nazionalita == null)
                        .toList();
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
        downloadService.ciclo();
        refresh();
    }

    /**
     * Apre un dialogo di creazione <br>
     * Proveniente da un click sul bottone New della Top Bar <br>
     * Sempre attivo <br>
     * Passa al dialogo gli handler per annullare e creare <br>
     */
    @Override
    public void newItem() {
        String message;
        try {
            appContext.getBean(DialogNewBio.class).openNew(this::downloadBio);
        } catch (Exception unErrore) {
            message = String.format("Non sono riuscito a costruire un'istanza di %s", DialogInputText.class.getSimpleName());
            logger.error(new WrapLog().exception(new AlgosException(message)).usaDb());
        }
    }

    /**
     * Apre un dialogo di editing <br>
     * Proveniente da un doppio click su una riga della Grid <br>
     * Passa al dialogo gli handler per annullare e modificare <br>
     *
     * @param entityBeanSenzaTempl (nuova o esistente)
     */
    @Override
    public void updateItem(AEntity entityBeanSenzaTempl) {
        Bio bioMongoCompleto = null;

        if (entityBeanSenzaTempl instanceof Bio bioSenzaTempl) {
            bioMongoCompleto = backend.findByKey(bioSenzaTempl.pageId);
        }

        if (bioMongoCompleto != null) {
            dialog = appContext.getBean(BioDialog.class, bioMongoCompleto, CrudOperation.UPDATE, crudBackend, formPropertyNamesList);
            dialog.openBio(this::saveHandler, this::annullaHandler, this::downloadHandler, this::elaboraHandler);
        }
    }


    /**
     * Apre un dialogo di cancellazione<br>
     * Proveniente da un click sul bottone Delete della Top Bar <br>
     * Attivo solo se è selezionata una e una sola riga <br>
     * Passa al dialogo gli handler per annullare e cancellare <br>
     */
    public void deleteItem() {
        Bio bioBeanSenzaTempl;
        Bio bioMongoCompleto;
        Optional entityBeanSenzaTempl = grid.getSelectedItems().stream().findFirst();

        if (entityBeanSenzaTempl.isPresent()) {
            bioBeanSenzaTempl = (Bio) entityBeanSenzaTempl.get();
            bioMongoCompleto = backend.findByKey(bioBeanSenzaTempl.pageId);
            dialog = appContext.getBean(BioDialog.class, bioMongoCompleto, CrudOperation.DELETE, crudBackend, formPropertyNamesList);
            dialog.open(this::saveHandler, this::deleteHandler, this::annullaHandler);
        }
    }

    public void downloadHandler(final Bio bio) {
        grid.setItems(crudBackend.findAll(sortOrder));
        Avviso.show(String.format("%s successfully downloaded", bio)).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    @Override
    public void annullaHandler(AEntity entityBean) {
    }

    public void elaboraHandler(final Bio bio) {
        backend.update(bio);
        grid.setItems(crudBackend.findAll(sortOrder));
        Avviso.show(String.format("%s successfully elaborated", bio)).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    protected void getInput() {
        VerticalLayout layout = new VerticalLayout();
        Dialog dialog = new Dialog();
        TextField field = new TextField("Titolo della pagina sul server wiki");
        field.setWidth("16em");
        field.addValueChangeListener(event -> {
            //            newFormEsegue(field.getValue());
            dialog.close();
        });
        layout.add(field);
        field.setAutofocus(true);

        dialog.setWidth("400px");
        dialog.setHeight("200px");
        HorizontalLayout layout2 = new HorizontalLayout();

        layout2.add(new Button("Annulla", evt -> dialog.close()));
        layout2.add(new Button("Download", evt -> dialog.close()));
        layout.add(layout2);
        dialog.add(layout);
        dialog.open();
    }


    /**
     * Esegue un azione di apertura di una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected AEntity wikiPage() {
        Bio bio = (Bio) super.wikiPage();

        wikiApiService.openWikiPage(bio.wikiTitle);

        return null;
    }

    /**
     * Già controllato che la pagina esista e che sia una biografia (ha il templBio) <br>
     */
    protected void downloadBio(WrapBio wrap) {
        Bio bio = backend.creaIfNotExist(wrap);
        refresh();
    }

}// end of crud @Route view class