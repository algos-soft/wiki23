package it.algos.wiki23.backend.packages.cognome;

import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.router.*;
import it.algos.vaad23.backend.boot.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.vaad23.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.PATH_WIKI;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.data.domain.*;
import org.vaadin.crudui.crud.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 10-Aug-2022
 * Time: 08:43
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Cognomi")
@Route(value = "cognome", layout = MainLayout.class)
public class CognomeView extends WikiView {


    //--per eventuali metodi specifici
    private CognomeBackend backend;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public CognomeView(@Autowired final CognomeBackend crudBackend) {
        super(crudBackend, Cognome.class);
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

        super.gridPropertyNamesList = Arrays.asList( "numBio");
        super.formPropertyNamesList = Arrays.asList("cognome", "numBio");
        super.sortOrder = Sort.by(Sort.Direction.DESC, "numBio");

        super.lastElaborazione = WPref.elaboraCognomi;
        super.durataElaborazione = WPref.elaboraCognomiTime;

        super.usaBottoneDownload = false;
        super.usaBottoneUploadStatistiche = false;
        super.usaBottonePaginaWiki = false;
        super.usaBottoneEdit = false;
        super.usaBottoneUploadPagina = true;
        super.usaBottoneElabora = true;

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
        int numMongo = WPref.sogliaCognomiMongo.getInt();
        int numMWiki = WPref.sogliaCognomiWiki.getInt();

        message = "Sono elencati i cognomi.";
        message += " BioBot crea una lista di biografati una volta superate le 50 biografie che usano quel cognome.";
        addSpanVerde(message);

        message = "Vedi anche la ";
        Span span = getSpan(new WrapSpan(message).color(AETypeColor.verde).weight(AEFontWeight.bold));
        Anchor anchor = new Anchor(VaadCost.PATH_WIKI + "Categoria:Liste di persone per cognome", "[[categoria:Liste di persone per cognome]]");
        anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
        anchor.getElement().getStyle().set(AEFontHeight.HTML, AEFontHeight.em9.getTag());
        anchor.getElement().getStyle().set(AETypeColor.HTML, AETypeColor.verde.getTag());
        alertPlaceHolder.add(new Span(span, anchor));

        message = String.format("Elabora crea sul database locale mongo tutti i cognomi usati da almeno %d voci.", numMongo);
        addSpanRosso(message);
        message = String.format("Upload crea sul server wiki le pagine per tutti i cognomi usati da almeno %d voci.", numMWiki);
        addSpanRosso(message);
    }


    /**
     * autoCreateColumns=false <br>
     * Crea le colonne normali indicate in this.colonne <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void addColumnsOneByOne() {
        super.addColumnsOneByOne();

        Grid.Column pagina = grid.addColumn(new ComponentRenderer<>(entity -> {
            String wikiTitle = textService.primaMaiuscola(((Cognome) entity).cognome);
            Anchor anchor = new Anchor(PATH_WIKI + PATH_COGNOMI + wikiTitle, wikiTitle);
            anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
            if (((Cognome) entity).esistePagina) {
                anchor.getElement().getStyle().set("color", "green");
            }
            else {
                anchor.getElement().getStyle().set("color", "red");
            }
            return new Span(anchor);
        })).setHeader("pagina").setKey("pagina").setFlexGrow(0).setWidth("18em");


//        Grid.Column daCancellare = grid.addColumn(new ComponentRenderer<>(entity -> {
//            String link = "https://it.wikipedia.org/w/index.php?title=Progetto:Biografie/Attivit%C3%A0/";
//            link += textService.primaMaiuscola(((Attivita) entity).pluraleLista);
//            link += TAG_DELETE;
//            Label label = new Label("no");
//            label.getElement().getStyle().set("color", "green");
//            Anchor anchor = new Anchor(link, "del");
//            anchor.getElement().getStyle().set("color", "red");
//            anchor.getElement().getStyle().set(AEFontWeight.HTML, AEFontWeight.bold.getTag());
//            Span span = new Span(anchor);
//
//            if (((Attivita) entity).esistePaginaLista && !((Attivita) entity).superaSoglia) {
//                return span;
//            }
//            else {
//                return label;
//            }
//        })).setHeader("X").setKey("cancella").setFlexGrow(0).setWidth("8em");

        Grid.Column ordine = grid.getColumnByKey(FIELD_KEY_ORDER);
        Grid.Column numBio = grid.getColumnByKey("numBio");

        grid.setColumnOrder(ordine, pagina, numBio);
    }


    /**
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void sincroFiltri() {
        long inizio = System.currentTimeMillis();
        List<Cognome> items = backend.findAll(sortOrder);
        logger.info(new WrapLog().exception(new AlgosException(String.format("Items %s", dateService.deltaText(inizio)))));

        final String textCognome = searchField != null ? searchField.getValue() : VUOTA;
        if (textService.isValidNoSpace(textCognome)) {
            items = items
                    .stream()
                    .filter(cognome -> cognome.cognome != null ? cognome.cognome.matches("^(?i)" + textCognome + ".*$") : false)
                    .toList();
        }
        else {
            if (textCognome.equals(SPAZIO)) {
                items = items
                        .stream()
                        .filter(cognome -> cognome.cognome == null)
                        .toList();
            }
        }

        if (items != null) {
            grid.setItems((List) items);
            elementiFiltrati = items.size();
            sicroBottomLayout();
        }
    }

}// end of crud @Route view class