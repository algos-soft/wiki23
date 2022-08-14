package it.algos.wiki23.backend.packages.cognome;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;
import it.algos.vaad23.backend.boot.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.vaad23.ui.views.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
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

        super.gridPropertyNamesList = Arrays.asList("cognome", "numBio");
        super.formPropertyNamesList = Arrays.asList("cognome", "numBio");
        super.sortOrder = Sort.by(Sort.Direction.DESC, "numBio");

        super.usaBottoneUploadAll = false;
        super.usaBottoneDownload = false;
        super.usaBottoneUploadStatistiche = false;
        super.usaBottonePaginaWiki = false;
        super.usaBottoneTest = false;
        super.usaSelectionGrid = false;

        super.usaBottoneElabora = true;
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

}// end of crud @Route view class