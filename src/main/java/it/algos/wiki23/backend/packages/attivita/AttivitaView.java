package it.algos.wiki23.backend.packages.attivita;

import ch.carnet.kasparscherrer.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.selection.*;
import com.vaadin.flow.router.*;
import it.algos.vaad23.backend.boot.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.vaad23.ui.dialog.*;
import it.algos.vaad23.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.statistiche.*;
import it.algos.wiki23.backend.upload.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.vaadin.crudui.crud.*;

import javax.management.*;
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

    protected IndeterminateCheckbox boxSuperaSoglia;

    protected IndeterminateCheckbox boxEsistePagina;

    protected Checkbox boxDistinctPlurali;

    protected Checkbox boxPagineDaCancellare;

    //--per eventuali metodi specifici
    private AttivitaDialog dialog;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it???s constructor does not need to be annotated with @Autowired annotation <br>
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

        super.gridPropertyNamesList = Arrays.asList("singolare", "plurale", "aggiunta", "numBio", "numSingolari", "superaSoglia", "esistePagina");
        super.formPropertyNamesList = Arrays.asList("plurale", "numBio");
        super.sortOrder = Sort.by(Sort.Direction.ASC, "singolare");

        super.usaBottoneElabora = true;
        super.lastDownload = WPref.downloadAttivita;
        super.durataDownload = WPref.downloadAttivitaTime;
        super.lastElaborazione = WPref.elaboraAttivita;
        super.durataElaborazione = WPref.elaboraAttivitaTime;
        super.lastUpload = WPref.uploadAttivita;
        super.durataUpload = WPref.uploadAttivitaTime;
        super.nextUpload = WPref.uploadAttivitaPrevisto;
        super.wikiModuloTitle = PATH_MODULO_ATTIVITA;
        super.usaBottoneStatistiche = true;
        super.usaBottoneUploadStatistiche = true;
        //        super.wikiStatisticheTitle = PATH_STATISTICHE_ATTIVITA;
        super.usaBottoneEdit = true;
        super.usaBottoneCategoria = true;
        super.usaBottoneUploadPagina = true;

        super.dialogClazz = AttivitaDialog.class;
        super.unitaMisuraDownload = "secondi";
        super.unitaMisuraElaborazione = "minuti";
        super.unitaMisuraUpload = "minuti";
        super.fixPreferenzeBackend();
    }

    /**
     * Costruisce un (eventuale) layout per informazioni aggiuntive come header della view <br>
     * Pu?? essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixAlert() {
        super.fixAlert();

        Anchor anchor = new Anchor(VaadCost.PATH_WIKI + PATH_MODULO_ATTIVITA, PATH_MODULO_ATTIVITA);
        anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
        Anchor anchor2 = new Anchor(VaadCost.PATH_WIKI + PATH_STATISTICHE_ATTIVITA, PATH_STATISTICHE_ATTIVITA);
        anchor2.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
        alertPlaceHolder.add(new Span(anchor, new Label(SEP), anchor2));

        message = "Contiene la tabella di conversione delle attivit?? passate via parametri 'Attivit??/Attivit??2/Attivit??3',";
        message += " da singolare maschile e femminile (usati nell'incipit) al plurale maschile per categorizzare la pagina.";
        addSpanVerde(message);

        message = "Le attivit?? sono elencate all'interno del modulo con la seguente sintassi:";
        message += " [\"attivitaforma1\"]=\"attivit?? al plurale\"; [\"attivitaforma2\"]=\"attivit?? al plurale\".";
        addSpanVerde(message);

        message = "Indipendentemente da come sono scritte nel modulo, tutte le attivit?? singolari e plurali sono convertite in minuscolo.";
        message += " Le voci delle ex-attivit?? (non presenti nel modulo) vengono aggiunte prendendole dal package 'genere'";
        addSpanRosso(message);

        message = String.format("Le singole pagine di attivit?? vengono create su wiki quando superano le %s biografie.", WPref.sogliaAttNazWiki.get());
        addSpanRossoBold(message);
        if (WPref.usaTreAttivita.is()) {
            message = "Ultima elaborazione effettuata considerando valide le voci biografiche con almeno una delle '''3''' attivit??.";
        }
        else {
            message = "Ultima elaborazione effettuata considerando valide le voci biografiche con 1 sola attivit?? principale.";
        }
        addSpanRossoBold(message);
    }

    protected void fixBottoniTopSpecifici() {
        super.fixBottoniTopSpecifici();

        searchFieldPlurale = new TextField();
        searchFieldPlurale.setPlaceholder("Filter by plurale");
        searchFieldPlurale.setClearButtonVisible(true);
        searchFieldPlurale.addValueChangeListener(event -> sincroFiltri());
        topPlaceHolder.add(searchFieldPlurale);

        boxBox = new IndeterminateCheckbox();
        boxBox.setLabel("Aggiunti da Genere");
        boxBox.setIndeterminate(true);
        boxBox.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout = new HorizontalLayout(boxBox);
        layout.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout);

        boxSuperaSoglia = new IndeterminateCheckbox();
        boxSuperaSoglia.setLabel("Supera soglia");
        boxSuperaSoglia.setIndeterminate(true);
        boxSuperaSoglia.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout2 = new HorizontalLayout(boxSuperaSoglia);
        layout2.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout2);

        boxEsistePagina = new IndeterminateCheckbox();
        boxEsistePagina.setLabel("Esiste pagina");
        boxEsistePagina.setIndeterminate(true);
        boxEsistePagina.addValueChangeListener(event -> sincroFiltri());
        HorizontalLayout layout3 = new HorizontalLayout(boxEsistePagina);
        layout3.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout3);

        boxDistinctPlurali = new Checkbox();
        boxDistinctPlurali.setLabel("Distinct plurali");
        boxDistinctPlurali.addValueChangeListener(event -> sincroPlurali());
        HorizontalLayout layout4 = new HorizontalLayout(boxDistinctPlurali);
        layout4.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout4);

        boxPagineDaCancellare = new Checkbox();
        boxPagineDaCancellare.setLabel("Da cancellare");
        boxPagineDaCancellare.addValueChangeListener(event -> sincroCancellare());
        HorizontalLayout layout5 = new HorizontalLayout(boxPagineDaCancellare);
        layout5.setAlignItems(Alignment.CENTER);
        topPlaceHolder.add(layout5);
    }

    /**
     * Pu?? essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void sincroFiltri() {
        List<Attivita> items = backend.findAll(sortOrder);

        final String textSearch = searchField != null ? searchField.getValue() : VUOTA;
        if (textService.isValid(textSearch)) {
            items = items.stream().filter(att -> att.singolare.matches("^(?i)" + textSearch + ".*$")).toList();
        }

        final String textSearchPlurale = searchFieldPlurale != null ? searchFieldPlurale.getValue() : VUOTA;
        if (textService.isValid(textSearchPlurale)) {
            if (boxDistinctPlurali != null && boxDistinctPlurali.getValue()) {
                items = backend.findAttivitaDistinctByPlurali();
            }
            items = items.stream().filter(att -> att.plurale.matches("^(?i)" + textSearchPlurale + ".*$")).toList();
        }

        if (boxBox != null && !boxBox.isIndeterminate()) {
            items = items.stream().filter(att -> att.aggiunta == boxBox.getValue()).toList();
        }

        if (boxSuperaSoglia != null && !boxSuperaSoglia.isIndeterminate()) {
            items = items.stream().filter(att -> att.superaSoglia == boxSuperaSoglia.getValue()).toList();
            if (boxSuperaSoglia.getValue()) {
                sortOrder = Sort.by(Sort.Direction.ASC, "plurale");
            }
            else {
                sortOrder = Sort.by(Sort.Direction.ASC, "singolare");
            }
        }

        if (boxEsistePagina != null && !boxEsistePagina.isIndeterminate()) {
            items = items.stream().filter(att -> att.esistePagina == boxEsistePagina.getValue()).toList();
            if (boxEsistePagina.getValue()) {
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
                items = backend.findAttivitaDistinctByPlurali();
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
     * Esegue un azione di upload, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void upload() {
        backend.uploadAll();
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
        appContext.getBean(StatisticheAttivita.class).upload();
        super.uploadStatistiche();
    }

    /**
     * Esegue un azione di apertura di una categoria su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public AEntity wikiCategoria() {
        Attivita attivita = (Attivita) super.wikiCategoria();

        String path = "Categoria:";
        wikiApiService.openWikiPage(path + attivita.plurale);

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
        String attivitaText = textService.primaMaiuscola(attivita.plurale);
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
                appContext.getBean(UploadAttivita.class).uploadTest(attivita.plurale);
            }
            else {
                message = String.format("L'attivit?? %s non raggiunge il necessario numero di voci biografiche", attivita.singolare);
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
            backend.uploadPagina(attivita.plurale);
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