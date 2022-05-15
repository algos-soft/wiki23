package it.algos.base;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.login.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.wrapper.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: ven, 29-apr-2022
 * Time: 20:57
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
public abstract class WikiTest extends AlgosTest {

    private static int MAX = 175;

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    @Autowired
    public WikiBotService wikiBotService;

    @Autowired
    public ElaboraService elaboraService;

    @Autowired
    public DidascaliaService didascaliaService;

    @Autowired
    public BioService bioService;

    @Autowired
    public BioBackend bioBackend;

    @Autowired
    public BotLogin botLogin;


    protected final static String PAGINA_INESISTENTE = "Pippooz Belloz";

    protected final static String PAGINA_ESISTENTE_UNO = "Matteo Salvini";

    protected final static String PAGINA_ESISTENTE_DUE = "Matteo Renzi";

    protected final static String PAGINA_ESISTENTE_TRE = "Piozzano";

    protected final static String CATEGORIA_INESISTENTE = "Supercalifragilistichespiralidoso";

    protected final static String CATEGORIA_ESISTENTE_UNO = "Nati nel 1435";

    protected final static String CATEGORIA_ESISTENTE_DUE = "BioBot";

    protected final static String CATEGORIA_ESISTENTE_TRE = "Ambasciatori britannici in Brasile";

    protected WResult previstoRisultato;

    protected WResult ottenutoRisultato;

    protected WrapBio wrapBio;

    protected Bio bio;

    protected List<Long> listaPageIds;

    protected List<WrapBio> listWrapBio;

    protected static final String PAGINA_UNO = "Roman Protasevič";

    protected static final String PAGINA_DUE = "Aldelmo di Malmesbury";

    protected static final String PAGINA_TRE = "Aelfric il grammatico";

    protected static final String PAGINA_QUATTRO = "Elfleda di Whitby";

    protected static final String PAGINA_CINQUE = "Werburga";

    protected static final String PAGINA_SEI = "Bernart Arnaut d'Armagnac";

    protected static final String PAGINA_SETTE = "Gaetano Anzalone";

    protected static final String PAGINA_OTTO = "Colin Campbell (generale)";

    protected static final String PAGINA_NOVE = "Louis Winslow Austin";

    protected static final String PAGINA_DIECI = "Maximilian Stadler";

    protected static final String PAGINA_DISAMBIGUA = "Rossi";

    protected static final String PAGINA_REDIRECT = "Regno di Napoli (1805-1815)";

    //--titolo
    //--pagina valida
    protected static Stream<Arguments> PAGINE_BIO() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("Roman Protasevič", true),
                Arguments.of("Aldelmo di Malmesbury", true),
                Arguments.of("Aelfric il grammatico", true),
                Arguments.of("Werburga", true),
                Arguments.of("Bernart Arnaut d'Armagnac", true),
                Arguments.of("Elfleda di Whitby", true),
                Arguments.of("Gaetano Anzalone", true),
                Arguments.of("Colin Campbell (generale)", true),
                Arguments.of("Louis Winslow Austin", true),
                Arguments.of("Regno di Napoli (1908-1745)", false),
                Arguments.of("Regno di Napoli (1806-1815)", false),
                Arguments.of("Rossi", false)
        );
    }


    //--titolo
    //--pagina esistente
    protected static Stream<Arguments> PAGINE_E_CATEGORIE() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("Roman Protasevič", true),
                Arguments.of("Louis Winslow Austin", true),
                Arguments.of("Categoria:Nati nel 1435", true),
                Arguments.of("Categoria:Nati nel 2387", false),
                Arguments.of("Categoria:BioBot", true),
                Arguments.of("Categoria:Supercalifragilistichespiralidoso", false),
                Arguments.of("Supercalifragilistichespiralidoso", true),
                Arguments.of("Regno di Napoli (1908-1745)", false),
                Arguments.of("Rossi", true)
        );
    }

    //--categoria
    //--esiste
    protected static Stream<Arguments> CATEGORIE() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("Inesistente", false),
                Arguments.of("Nati nel 1435", true),
                Arguments.of("Ambasciatori britannici in Brasile", true),
                Arguments.of("Nati nel 1935", true)
        );
    }

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    protected void setUpAll() {
        MockitoAnnotations.openMocks(this);
        slf4jLogger = LoggerFactory.getLogger("wiki23.admin");

        initMocks();
        fixRiferimentiIncrociati();
    }

    /**
     * Inizializzazione dei service <br>
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void initMocks() {
        super.initMocks();

        assertNotNull(wikiBotService);
        assertNotNull(elaboraService);
        assertNotNull(didascaliaService);
        assertNotNull(bioService);
        assertNotNull(bioBackend);
        assertNotNull(botLogin);
    }

    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
        super.fixRiferimentiIncrociati();

        elaboraService.wikiBotService = wikiBotService;
    }

    /**
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    protected void setUpEach() {
        super.setUpEach();

        previstoRisultato = null;
        ottenutoRisultato = null;
        wrapBio = null;
        bio = null;
        listaPageIds = null;
        listWrapBio = null;
    }

    protected void printRisultato(WResult result) {
        List lista = result.getLista();
        lista = lista != null && lista.size() > 20 ? lista.subList(0, 10) : lista;

        System.out.println(VUOTA);
        System.out.println("Risultato");
        System.out.println(String.format("Status: %s", result.isValido() ? "true" : "false"));
        System.out.println(String.format("Query: %s", result.getQueryType()));
        System.out.println(String.format("Title: %s", result.getWikiTitle()));
        System.out.println(String.format("User: %s", result.getUserType()));
        System.out.println(String.format("Limit: %d", result.getLimit()));
        System.out.println(String.format("Preliminary url: %s", result.getUrlPreliminary()));
        System.out.println(String.format("Secondary url: %s", result.getUrlRequest()));
        System.out.println(String.format("Preliminary response: %s", result.getPreliminaryResponse()));
        System.out.println(String.format("Token: %s", result.getToken()));
        System.out.println(String.format("Secondary response: %s", result.getResponse()));
        System.out.println(String.format("Message code: %s", result.getCodeMessage()));
        System.out.println(String.format("Message: %s", result.getMessage()));
        System.out.println(String.format("Error code: %s", result.getErrorCode()));
        System.out.println(String.format("Error message: %s", result.getErrorMessage()));
        System.out.println(String.format("Valid message: %s", result.getValidMessage()));
        System.out.println(String.format("Numeric value: %s", textService.format(result.getIntValue())));
        System.out.println(String.format("Cicli: %d", result.getCicli())); ;
        System.out.println(String.format("List value: %s", lista));
        System.out.println(String.format("Map value: %s", result.getMappa()));
        System.out.println(String.format("Risultato ottenuto in %s", dateService.toText(result.getDurata())));
        System.out.println(String.format("Risultato ottenuto in %s", dateService.deltaTextEsatto(result.getInizio(), result.getFine())));
        printWrapBio(result.getWrap());
    }


    protected void printWrapBio(WrapBio wrap) {
        if (wrap != null) {
            System.out.println(VUOTA);
            System.out.println(String.format("Wrap valido: %s", wrap.isValida() ? "true" : "false"));
            System.out.println(String.format("Wrap title: %s", wrap.getTitle()));
            System.out.println(String.format("Wrap pageid: %s", wrap.getPageid()));
            System.out.println(String.format("Wrap type: %s", wrap.getType()));
            System.out.println(String.format("Wrap templBio: %s", textService.isValid(wrap.getTemplBio()) ? "valido" : "non esiste"));
            if (textService.isValid(wrap.getTemplBio())) {
                System.out.println(String.format("templBio: %s", getMax(wrap.getTemplBio())));
            }
        }
    }

    protected void printDidascalia(final String sorgente, final String sorgente2, final String sorgente3, final String ottenuto) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("Nome: %s", sorgente2));
        System.out.println(String.format("Cognome: %s", sorgente3));
        System.out.println(String.format("Didascalia: %s", ottenuto));
    }

    protected String getMax(String message) {
        message = message.length() < MAX ? message : message.substring(0, Math.min(MAX, message.length()));
        if (message.contains(CAPO)) {
            message = message.replaceAll(CAPO, SPAZIO);
        }

        return message;
    }

}