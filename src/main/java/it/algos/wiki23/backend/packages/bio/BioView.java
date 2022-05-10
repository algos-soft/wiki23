package it.algos.wiki23.backend.packages.bio;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dialog.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.*;
import it.algos.vaad23.backend.boot.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.vaad23.backend.boot.VaadCost.PATH_WIKI;
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
import it.algos.wiki23.ui.dialog.*;
import org.springframework.beans.factory.annotation.*;
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

        super.gridPropertyNamesList = Arrays.asList("pageId", "wikiTitle", "elaborato", "sesso", "nome", "cognome", "giornoNato",
                "annoNato",
                "giornoMorto", "annoMorto",
                "attivita", "attivita2", "attivita3",
                "nazionalita"
        );
        super.formPropertyNamesList = Arrays.asList("pageId", "wikiTitle", "elaborato", "sesso", "nome", "cognome", "giornoNato", "annoNato", "giornoMorto", "annoMorto",
                "attivita", "attivita2", "attivita3",
                "nazionalita"
        );

        super.usaBottoneNew = true;
        super.usaBottoneDelete = true;
        super.usaBottoneSearch = false;
        super.usaBottonePaginaWiki = true;
        super.dialogClazz = BioDialog.class;
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

        categoria = WPref.categoriaBio.getStr();

        Anchor anchor = new Anchor(PATH_CATEGORIA + categoria, "Categoria: " + categoria);
        anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
        alertPlaceHolder.add(new Span(anchor));

        addSpanVerde(String.format("Nella categoria %s sono presenti %s biografie", categoria, textService.format(numBio)));
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
     * @param entityBeanDaRegistrare (nuova o esistente)
     */
    public void updateItem(AEntity entityBeanDaRegistrare) {
        dialog = appContext.getBean(BioDialog.class, entityBeanDaRegistrare, CrudOperation.UPDATE, crudBackend, formPropertyNamesList);
        dialog.openBio(this::saveHandler, this::annullaHandler, this::elaboraHandler);
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
        backend.creaIfNotExist(wrap);
        refresh();
    }

}// end of crud @Route view class