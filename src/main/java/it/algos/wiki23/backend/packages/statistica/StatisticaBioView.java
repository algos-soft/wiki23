package it.algos.wiki23.backend.packages.statistica;

import com.vaadin.flow.router.*;
import it.algos.vaad24.ui.views.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.statistiche.*;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 21-Aug-2022
 * Time: 14:07
 * <p>
 * Vista iniziale e principale di un package <br>
 *
 * @Route chiamata dal menu generale <br>
 * Presenta la Grid <br>
 * Su richiesta apre un Dialogo per gestire la singola entity <br>
 */
@PageTitle("Statistiche")
@Route(value = "statistica", layout = MainLayout.class)
public class StatisticaBioView extends WikiView {


    //--per eventuali metodi specifici
    private StatisticaBioBackend backend;

    /**
     * Costruttore @Autowired (facoltativo) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Inietta con @Autowired il service con la logica specifica e lo passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questa @Route view e la passa alla superclasse <br>
     *
     * @param crudBackend service specifico per la businessLogic e il collegamento con la persistenza dei dati
     */
    public StatisticaBioView(@Autowired final StatisticaBioBackend crudBackend) {
        super(crudBackend, StatisticaBio.class);
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

        super.gridPropertyNamesList = Arrays.asList("ordine", "evento", "bio", "giorni", "anni", "attivita", "nazionalita", "attesa");
        super.formPropertyNamesList = Arrays.asList("ordine", "evento", "bio", "giorni", "anni", "attivita", "nazionalita", "attesa");
        super.usaBottoneDeleteAll = true;
        this.usaBottoneDownload = false;
        this.usaBottoneUploadAll = false;
        this.usaBottonePaginaWiki = false;
        this.usaBottoneSearch = false;
        this.usaBottoneTest = false;
        this.usaInfoDownload = false;
        super.usaRowIndex = false;
        super.riordinaColonne = false;
        super.usaBottoneNew = false;
        super.usaBottoneEdit = false;
        super.usaBottoneDelete = false;
        super.usaBottoneStatistiche = true;
        super.usaBottoneUploadStatistiche = true;
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
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void elabora() {
        super.elabora();
    }

    /**
     * Apre una pagina su wiki, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void viewStatistiche() {
        wikiApiService.openWikiPage(PATH_STATISTICHE);
    }

    /**
     * Esegue un azione di upload delle statistiche, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando DOPO il metodo della superclasse <br>
     */
    public void uploadStatistiche() {
        appContext.getBean(StatisticheBio.class).upload();
        super.uploadStatistiche();
    }


}// end of crud @Route view class