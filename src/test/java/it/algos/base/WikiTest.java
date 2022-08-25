package it.algos.base;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import it.algos.vaad23.backend.service.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.login.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.wrapper.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.data.domain.*;

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

    public static int MAX = 175;

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    @Autowired
    protected MongoService mongoService;

    @Autowired
    public WikiBotService wikiBotService;

    @Autowired
    public ElaboraService elaboraService;

    @Autowired
    public DidascaliaService didascaliaService;

    @Autowired
    public QueryService queryService;

    @Autowired
    public BioService bioService;

    @Autowired
    public DownloadService downloadService;

    @Autowired
    public BioBackend bioBackend;

    @Autowired
    public AttivitaBackend attivitaBackend;

    @Autowired
    public NazionalitaBackend nazionalitaBackend;

    @Autowired
    public BotLogin botLogin;

    @Autowired
    public WikiUtility wikiUtility;

    @Autowired
    public GiornoBackend giornoBackend;

    @Autowired
    public AnnoBackend annoBackend;

    protected final static long BIO_SALVINI_PAGEID = 132555;

    protected final static long BIO_RENZI_PAGEID = 134246;


    protected final static String PAGINA_INESISTENTE = "Pippooz Belloz";

    protected final static String BIO_SALVINI = "Matteo Salvini";

    protected final static String BIO_RENZI = "Matteo Renzi";

    protected final static String PAGINA_ESISTENTE_TRE = "Piozzano";

    protected final static String CATEGORIA_INESISTENTE = "Supercalifragilistichespiralidoso";

    protected final static String CATEGORIA_ESISTENTE_BREVE = "Ambasciatori britannici in Brasile";

    protected final static String CATEGORIA_ESISTENTE_MEDIA = "Nati nel 1435";

    protected final static String CATEGORIA_ESISTENTE_LUNGA = "Nati nel 1938";

    protected final static String CATEGORIA_PRINCIPALE_BIOBOT = "BioBot";


    protected WResult previstoRisultato;

    protected WResult ottenutoRisultato;

    protected WrapBio wrapBio;

    protected Bio bio;

    protected List<Long> listaPageIds;

    protected List<WrapBio> listWrapBio;

    protected List<Bio> listBio;

    protected List<WrapDidascalia> listWrapDidascalia;

    protected List<WrapLista> listWrapLista;

    protected List<WrapTime> listMiniWrap;

    protected LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaLista;

    protected AETypeUser typeUser;

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

    protected static final String PAGINA_UNDICI = "Muhammad ibn Ali al-Taqi al-Jawad";

    protected long pageId;

    public static final String SINGOLARE = "singolare";

    public static final String PLURALE = "plurale";

    protected LinkedHashMap<String, List<WrapLista>> mappaWrap;

    //--titolo
    //--pagina valida
    protected static Stream<Arguments> PAGINE_BIO() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("Roberto il Forte", true),
//                Arguments.of("Claude de Chastellux", true),
//                Arguments.of("John Murphy (politico statunitense)", true),
//                Arguments.of("Meena Keshwar Kamal", true),
//                Arguments.of("Werburga", true),
//                Arguments.of("Roman Protasevič", true),
//                Arguments.of("Aldelmo di Malmesbury", true),
//                Arguments.of("Aelfric il grammatico", true),
//                Arguments.of("Bernart Arnaut d'Armagnac", true),
//                Arguments.of("Elfleda di Whitby", true),
//                Arguments.of("Gaetano Anzalone", true),
//                Arguments.of("Colin Campbell (generale)", true),
//                Arguments.of("Louis Winslow Austin", true),
//                Arguments.of("San Nicanore", true),
//                Arguments.of("Regno di Napoli (1908-1745)", false),
//                Arguments.of("Regno di Napoli (1806-1815)", false),
//                Arguments.of("Rossi", false),
//                Arguments.of("Bartolomeo Giuseppe Amico di Castell'Alfero", true),
//                Arguments.of("Lucio Anneo Seneca", true),
//                Arguments.of("Bodhidharma", true),
                Arguments.of("Ashur-uballit I", true)
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
                Arguments.of("2741616|27416167", false),
                Arguments.of("Categoria:Nati nel 2387", false),
                Arguments.of("Categoria:BioBot", true),
                Arguments.of("Categoria:Supercalifragilistichespiralidoso", false),
                Arguments.of("Supercalifragilistichespiralidoso", true),
                Arguments.of("Regno di Napoli (1908-1745)", false),
                Arguments.of("Rossi", true)
        );
    }

    //--categoria
    //--esiste come anonymous
    //--esiste come user, admin
    //--esiste come bot
    protected static Stream<Arguments> CATEGORIE() {
        return Stream.of(
                Arguments.of(null, false, false, false),
                Arguments.of(VUOTA, false, false, false),
                Arguments.of("Inesistente", false, false, false),
                Arguments.of("Nati nel 1435", true, true, true),
                Arguments.of("2741616|27416167", false, false, false),
                Arguments.of("Ambasciatori britannici in Brasile", true, true, true),
                Arguments.of("Nati nel 1935", false, true, true)
        );
    }

    //--nome attività
    //--typeLista
    protected static Stream<Arguments> ATTIVITA() {
        return Stream.of(
                Arguments.of(VUOTA, AETypeLista.listaBreve),
                Arguments.of(VUOTA, AETypeLista.nazionalitaSingolare),
                Arguments.of("abati e badesse", AETypeLista.attivitaPlurale),
                Arguments.of("bassisti", AETypeLista.attivitaPlurale),
                Arguments.of("allevatori", AETypeLista.attivitaPlurale),
                Arguments.of("diplomatici", AETypeLista.attivitaPlurale),
                Arguments.of("soprano", AETypeLista.attivitaSingolare),
                Arguments.of("romanziere", AETypeLista.attivitaSingolare),
                Arguments.of("accademici", AETypeLista.attivitaPlurale)

        );
    }


    //--nome nazionalità
    //--typeLista
    protected static Stream<Arguments> NAZIONALITA() {
        return Stream.of(
//                Arguments.of(VUOTA, AETypeLista.listaBreve),
//                Arguments.of(VUOTA, AETypeLista.nazionalitaSingolare),
//                Arguments.of("azeri", AETypeLista.nazionalitaPlurale),
//                Arguments.of("arabi", AETypeLista.nazionalitaPlurale),
//                Arguments.of("libanesi", AETypeLista.nazionalitaPlurale),
                Arguments.of("afghani", AETypeLista.nazionalitaPlurale),
//                Arguments.of("mongoli", AETypeLista.nazionalitaPlurale),
//                Arguments.of("assiri", AETypeLista.nazionalitaPlurale),
                Arguments.of("capoverdiani", AETypeLista.nazionalitaPlurale)

        );
    }

    //--direzione
    //--property
    protected static Stream<Arguments> SORT() {
        return Stream.of(
                Arguments.of(Sort.Direction.ASC, SINGOLARE),
                Arguments.of(Sort.Direction.DESC, SINGOLARE),
                Arguments.of(Sort.Direction.ASC, PLURALE),
                Arguments.of(Sort.Direction.DESC, PLURALE)
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
        assertNotNull(attivitaBackend);
        assertNotNull(botLogin);
        assertNotNull(queryService);
        assertNotNull(downloadService);
        assertNotNull(wikiUtility);
        assertNotNull(giornoBackend);
        assertNotNull(annoBackend);
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
        wikiUtility.queryService = queryService;
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
        listBio = null;
        listWrapDidascalia = null;
        listWrapLista = null;
        listMiniWrap = null;
        mappaWrap = null;
        mappaLista = null;
        typeUser = null;
        pageId = 0L;
    }

    protected void printRisultato(WResult result) {
        List lista = result.getLista();
        lista = lista != null && lista.size() > 20 ? lista.subList(0, 10) : lista;
        String newText = result.getNewtext();
        newText = newText.length() < MAX ? newText : newText.substring(0, Math.min(MAX, newText.length()));
        String content = result.getContent();
        content = content.length() < MAX ? content : content.substring(0, Math.min(MAX, content.length()));

        System.out.println("Risultato");
        System.out.println(String.format("Status: %s", result.isValido() ? "true" : "false"));
        System.out.println(String.format("Modificata: %s", result.isModificata() ? "true" : "false"));
        System.out.println(String.format("Query: %s", result.getQueryType()));
        System.out.println(String.format("Title: %s", result.getWikiTitle()));
        System.out.println(String.format("PageId: %s", result.getPageid()));
        System.out.println(String.format("Namespace: %s", result.getNameSpace()));
        System.out.println(String.format("Type: %s", result.getTypePage()));
        System.out.println(String.format("User: %s", result.getUserType()));
        System.out.println(String.format("Limit: %d", result.getLimit()));
        System.out.println(String.format("Summary: %s", result.getSummary()));
        System.out.println(String.format("Preliminary url: %s", result.getUrlPreliminary()));
        System.out.println(String.format("Secondary url: %s", result.getUrlRequest()));
        System.out.println(String.format("Get request url: %s", result.getGetRequest()));
        System.out.println(String.format("Cookies: %s", result.getCookies()));
        System.out.println(String.format("Preliminary response: %s", result.getPreliminaryResponse()));
        System.out.println(String.format("Token: %s", result.getToken()));
        System.out.println(String.format("Post: %s", result.getPost()));
        System.out.println(String.format("New text: %s", newText));
        System.out.println(String.format("Secondary response: %s", result.getResponse()));
        System.out.println(String.format("Message code: %s", result.getCodeMessage()));
        System.out.println(String.format("Message: %s", result.getMessage()));
        System.out.println(String.format("Error code: %s", result.getErrorCode()));
        System.out.println(String.format("Error message: %s", result.getErrorMessage()));
        System.out.println(String.format("Valid message: %s", result.getValidMessage()));
        System.out.println(String.format("NewTimeStamp: %s", result.getNewtimestamp()));
        System.out.println(String.format("Numeric value: %s", textService.format(result.getIntValue())));
        System.out.println(String.format("Cicli: %d", result.getCicli())); ;
        System.out.println(String.format("List value: %s", lista));
        System.out.println(String.format("Map value: %s", result.getMappa()));
        System.out.println(String.format("Content value: %s", content));
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

    protected void printMiniWrap(List<WrapTime> listaMiniWrap) {
        if (listaMiniWrap != null) {
            System.out.println(VUOTA);
            System.out.println(String.format("Pageid (wikiTitle) and last timestamp"));
            System.out.println(VUOTA);
            for (WrapTime wrap : listaMiniWrap) {
                System.out.println(String.format("%s (%s)%s%s", wrap.getPageid(), wrap.getWikiTitle(), FORWARD, wrap.getLastModifica()));
            }
        }
    }

    protected void printWrapBio(List<WrapBio> listaWrapBio) {
        if (listaWrapBio != null) {
            System.out.println(VUOTA);
            System.out.println(String.format("Wrap pageid e wikiTitle"));
            System.out.println(VUOTA);
            for (WrapBio wrap : listaWrapBio) {
                System.out.println(String.format("%s (%s)", wrap.getPageid(), wrap.getTitle()));
            }
        }
    }

    protected void printBio(List<Bio> listaBio, String titolo) {
        int cont = 0;
        String message = textService.isValid(titolo) ? String.format(" di %s", titolo) : VUOTA;
        if (listaBio != null) {
            System.out.println(String.format("Nella lista ci sono %d biografie%s", listaBio.size(), message));
            System.out.println(VUOTA);
            for (Bio bio : listaBio) {
                cont++;
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);
                System.out.println(bio.wikiTitle);
            }
        }
    }

    protected void printBio(List<Bio> listaBio) {
        printBio(listaBio, VUOTA);
    }


    protected void printDidascalia(final String sorgente, final String sorgente2, final String sorgente3, final String ottenuto) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("Nome: %s", sorgente2));
        System.out.println(String.format("Cognome: %s", sorgente3));
        System.out.println(String.format("Didascalia: %s", ottenuto));
    }

    protected void printBotLogin() {
        System.out.println(VUOTA);
        System.out.println("Valori attuali del singleton BotLogin");
        System.out.println(String.format("Valido: %s", botLogin.isValido() ? "true" : "false"));
        System.out.println(String.format("Bot: %s", botLogin.isBot() ? "true" : "false"));
        System.out.println(String.format("Userid: %d", botLogin.getUserid()));
        System.out.println(String.format("Username: %s", botLogin.getUsername()));
        System.out.println(String.format("UserType: %s", botLogin.getUserType()));
    }

    protected void printLong(List<Long> listaLong, int max) {
        if (listaLong != null) {
            System.out.println(VUOTA);
            System.out.println(String.format("Pageid"));
            System.out.println(VUOTA);
            for (Long pageId : listaLong.subList(0, Math.min(max, listaLong.size()))) {
                System.out.println(String.format("%s", pageId));
            }
        }
    }

    protected void printString(List<String> listaString, int max) {
        int cont = 0;

        if (listaString != null) {
            System.out.println(VUOTA);
            System.out.println(String.format("Valori:"));
            for (String riga : listaString.subList(0, Math.min(max, listaString.size()))) {
                cont++;
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);
                System.out.println(String.format("%s", riga));
            }
        }
    }

    protected String getMax(String message) {
        message = message.length() < MAX ? message : message.substring(0, Math.min(MAX, message.length()));
        if (message.contains(CAPO)) {
            message = message.replaceAll(CAPO, SPAZIO);
        }

        return message;
    }


    protected void printBioLista(List<Bio> listaBio) {
        String message;
        int max = 10;
        int tot = listaBio.size();
        int iniA = 0;
        int endA = Math.min(max, tot);
        int iniB = tot - max > 0 ? tot - max : 0;
        int endB = tot;

        if (listaBio != null) {
            message = String.format("Faccio vedere una lista delle prime e delle ultime %d biografie", max);
            System.out.println(message);
            message = "Ordinate per forzaOrdinamento";
            System.out.println(message);
            message = "Ordinamento, wikiTitle, nome, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printBioBase(listaBio.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printBioBase(listaBio.subList(iniB, endB), iniB);
        }
    }

    protected void printBioBase(List<Bio> listaBio, final int inizio) {
        int cont = inizio;

        for (Bio bio : listaBio) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.ordinamento));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.wikiTitle));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(bio.nome) ? bio.nome : KEY_NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(bio.cognome) ? bio.cognome : KEY_NULL));
            System.out.print(SPAZIO);

            System.out.println(SPAZIO);
        }
    }


    protected void printWrapDidascalia(List<WrapDidascalia> lista) {
        int cont = 0;
        String value;

        if (lista != null) {
            for (WrapDidascalia wrap : lista) {
                cont++;
                value = wrap.getWikiTitle();

                System.out.print(TAB);
                System.out.print(TAB);
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);

                System.out.print(textService.setQuadre(value));
                System.out.print(SPAZIO);

                System.out.println(VUOTA);
            }
        }
    }


    protected void printMappaDidascalia(String tag, String attivita, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie) {
        int cont = 0;
        LinkedHashMap<String, List<String>> mappaSub;

        if (mappaDidascalie != null) {
            message = String.format("Didascalie per %s %s", tag, attivita);
            System.out.println(message);
            message = "Nazionalità/Attività, primoCarattere, didascalia";
            System.out.println(message);
            System.out.println(VUOTA);

            for (String key : mappaDidascalie.keySet()) {
                mappaSub = mappaDidascalie.get(key);
                cont++;
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);

                System.out.print(key);
                System.out.print(SPAZIO);

                System.out.println(VUOTA);

                printMappaSubDidascalie(mappaSub);
                System.out.println(VUOTA);
            }
        }
    }


    protected void printMappaSubDidascalie(LinkedHashMap<String, List<String>> mappaSub) {
        int cont = 0;
        List lista;

        if (mappaSub != null) {
            for (String key : mappaSub.keySet()) {
                lista = mappaSub.get(key);
                cont++;
                System.out.print(TAB);
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);

                System.out.print(key);
                System.out.print(SPAZIO);

                System.out.println(VUOTA);

                printDidascalia(lista);
            }
        }
    }

    protected void printDidascalia(List<String> lista) {
        int cont = 0;

        if (lista != null) {
            for (String didascalia : lista) {
                cont++;

                System.out.print(TAB);
                System.out.print(TAB);
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);

                System.out.print(didascalia);
                System.out.print(SPAZIO);

                System.out.println(VUOTA);
            }
        }
    }

    protected void printAllSingolari(String plurale, List<String> listaSingolari, String tag) {
        System.out.println(String.format("Ci sono %d %s singolari per %s", listaSingolari.size(), tag, plurale));
        System.out.println(VUOTA);

        for (String singolare : listaSingolari) {
            System.out.println(singolare);
        }
    }


    protected void printMappaSub(LinkedHashMap<String, List<WrapDidascalia>> mappaSub) {
        int cont = 0;
        List lista;

        if (mappaSub != null) {
            for (String key : mappaSub.keySet()) {
                lista = mappaSub.get(key);
                cont++;
                System.out.print(TAB);
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);

                System.out.print(textService.setQuadre(key));
                System.out.print(SPAZIO);

                System.out.println(VUOTA);

                printWrapDidascalia(lista);
            }
        }
    }


    protected void printWrapLista(List<WrapLista> listWrapLista) {
        int max = 15;
        if (listWrapLista != null) {
            message = String.format("Faccio vedere una lista delle prime %d didascalie", max);
            System.out.println(message);
            message = "Paragrafo, paragrafoLink, sottoParagrafo, didascaliaBreve";
            System.out.println(message);
            System.out.println(VUOTA);
            printSub(listWrapLista.subList(0, Math.min(max, listWrapLista.size())));
        }
    }

    protected void printSub(List<WrapLista> listWrapLista) {
        for (WrapLista wrap : listWrapLista) {
            System.out.print(wrap.titoloParagrafo);
            System.out.print(SEP);
            if (textService.isValid(wrap.titoloParagrafoLink)) {
                System.out.print(wrap.titoloParagrafoLink);
                System.out.print(SEP);
            }
            if (textService.isValid(wrap.titoloSottoParagrafo)) {
                System.out.print(wrap.titoloSottoParagrafo);
                System.out.print(SEP);
            }
            System.out.println(wrap.didascaliaBreve);
        }
        System.out.println(VUOTA);
    }

    protected void printMappaWrap(LinkedHashMap<String, List<WrapLista>> mappaWrap) {
        List<WrapLista> lista;

        if (mappaWrap != null) {
            message = String.format("Faccio vedere una mappa delle didascalie");
            System.out.println(VUOTA);
            for (String paragrafo : mappaWrap.keySet()) {
                System.out.print("==");
                System.out.print(paragrafo);
                System.out.print("==");
                System.out.print(CAPO);
                lista = mappaWrap.get(paragrafo);
                printSub(lista);
            }
        }
    }

    protected void printMappa(Map<String, List<String>> mappa) {
        List<String> lista;

        if (mappa != null) {
            message = String.format("Faccio vedere una mappa delle didascalie");
            System.out.println(message);
            System.out.println(VUOTA);
            for (String paragrafo : mappa.keySet()) {
                System.out.print("==");
                System.out.print(paragrafo);
                System.out.print("==");
                System.out.print(CAPO);
                lista = mappa.get(paragrafo);
                for (String didascalia : lista) {
                    System.out.println(didascalia);
                }
                System.out.println(VUOTA);
            }
        }
    }

}