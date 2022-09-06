package it.algos.base;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.server.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.interfaces.*;
import it.algos.vaad23.backend.packages.utility.log.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.*;

import java.lang.reflect.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 07-mar-2022
 * Time: 19:47
 * Classe astratta per i test <br>
 *
 * @see https://www.baeldung.com/parameterized-tests-junit-5
 */
public abstract class AlgosTest {

    public static final String SEP_RIGA = "====================";

    protected static final String CONTENUTO = "contenuto";

    protected static final String CONTENUTO_DUE = "mariolino";

    /**
     * The constant ARRAY_STRING.
     */
    protected static final String[] ARRAY_SHORT_STRING = {CONTENUTO};

    /**
     * The constant LIST_STRING.
     */
    protected static final List<String> LIST_SHORT_STRING = new ArrayList(Arrays.asList(ARRAY_SHORT_STRING));

    protected static final String[] ARRAY_SHORT_STRING_DUE = {CONTENUTO_DUE};

    protected static final List<String> LIST_SHORT_STRING_DUE = new ArrayList(Arrays.asList(ARRAY_SHORT_STRING_DUE));

    protected static final LocalDateTime LOCAL_DATE_TIME_UNO = LocalDateTime.of(2014, 10, 21, 7, 42);

    protected static final LocalDateTime LOCAL_DATE_TIME_DUE = LocalDateTime.of(2014, 10, 5, 7, 4);

    public Logger slf4jLogger;

    protected boolean previstoBooleano;

    protected boolean ottenutoBooleano;

    protected String sorgente;

    protected String sorgente2;

    protected String sorgente3;

    protected String previsto;

    protected String previsto2;

    protected String previsto3;

    protected String ottenuto;

    protected String ottenuto2;
    protected String ottenuto3;

    protected int sorgenteIntero;

    protected int previstoIntero;

    protected int ottenutoIntero;

    protected long sorgenteLong = 0;

    protected long previstoLong = 0;

    protected long ottenutoLong = 0;

    protected double sorgenteDouble = 0;

    protected double previstoDouble = 0;

    protected double ottenutoDouble = 0;

    protected AResult previstoRisultato;

    protected AResult ottenutoRisultato;

    protected Span span;

    protected Class clazz;

    protected Class sorgenteClasse;

    protected Class previstoClasse;

    protected Class ottenutoClasse;

    protected Field sorgenteField;

    protected Field ottenutoField;

    protected String[] sorgenteMatrice;

    protected String[] previstoMatrice;

    protected String[] ottenutoMatrice;

    protected List<String> sorgenteArray;

    protected List<String> previstoArray;

    protected List<String> ottenutoArray;

    protected Map<String, String> mappaSorgente;

    protected Map<String, String> mappaPrevista;

    protected Map<String, String> mappaOttenuta;

    protected List<Field> listaFields;

    protected List<String> listaStr;

    protected List<AEntity> listaBean;

    protected Map<String, List<String>> mappa;

    protected String message;

    protected byte[] bytes;

    protected StreamResource streamResource;

    protected long inizio;

    @InjectMocks
    protected TextService textService;

    @InjectMocks
    protected DateService dateService;

    @InjectMocks
    protected LogService logService;

    @InjectMocks
    protected MailService mailService;

    @InjectMocks
    protected AnnotationService annotationService;

    @InjectMocks
    protected ArrayService arrayService;

    @InjectMocks
    protected ClassService classService;

    @InjectMocks
    protected ReflectionService reflectionService;

    @InjectMocks
    protected FileService fileService;

    @InjectMocks
    protected ResourceService resourceService;

    @InjectMocks
    protected HtmlService htmlService;

    @InjectMocks
    protected LoggerBackend loggerBackend;

    @InjectMocks
    protected UtilityService utilityService;

    @InjectMocks
    protected WebService webService;

    @InjectMocks
    public RegexService regexService;


    //--tag
    //--esiste nella enumeration
    protected static Stream<Arguments> TYPES() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of(VUOTA, false),
                Arguments.of("system", true),
                Arguments.of("setup", true),
                Arguments.of("login", true),
                Arguments.of("pippoz", false),
                Arguments.of("startup", true),
                Arguments.of("checkMenu", true),
                Arguments.of("checkData", true),
                Arguments.of("preferenze", true),
                Arguments.of("newEntity", true),
                Arguments.of("edit", true),
                Arguments.of("newEntity", true),
                Arguments.of("modifica", true),
                Arguments.of("delete", true),
                Arguments.of("deleteAll", true),
                Arguments.of("mongoDB", true),
                Arguments.of("debug", true),
                Arguments.of("info", true),
                Arguments.of("warn", true),
                Arguments.of("error", true),
                Arguments.of("info", true),
                Arguments.of("wizard", true),
                Arguments.of("wizarddoc", false),
                Arguments.of("wizardDoc", true),
                Arguments.of("info", true),
                Arguments.of("import", true),
                Arguments.of("export", true),
                Arguments.of("download", true),
                Arguments.of("update", true),
                Arguments.of("Update", false),
                Arguments.of("info", true),
                Arguments.of("elabora", true),
                Arguments.of("reset", true),
                Arguments.of("utente", true),
                Arguments.of("password", true),
                Arguments.of("cicloBio", true)
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

        slf4jLogger = LoggerFactory.getLogger("vaad23.admin");

        initMocks();
        fixRiferimentiIncrociati();
    }

    /**
     * Inizializzazione dei service <br>
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void initMocks() {
        assertNotNull(textService);
        assertNotNull(slf4jLogger);
        assertNotNull(logService);
        assertNotNull(mailService);
        assertNotNull(dateService);
        assertNotNull(annotationService);
        assertNotNull(arrayService);
        assertNotNull(classService);
        assertNotNull(reflectionService);
        assertNotNull(fileService);
        assertNotNull(resourceService);
        assertNotNull(utilityService);
        assertNotNull(htmlService);
        assertNotNull(webService);
        assertNotNull(loggerBackend);
        assertNotNull(regexService);
    }


    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
        mailService.textService = textService;
        dateService.textService = textService;
        arrayService.textService = textService;
        resourceService.textService = textService;
        logService.fileService = fileService;
        logService.textService = textService;
        logService.loggerBackend = loggerBackend;
        resourceService.fileService = fileService;
        fileService.logger = logService;
        fileService.textService = textService;
        arrayService.logger = logService;
        logService.utilityService = utilityService;
        logService.slf4jLogger = slf4jLogger;
        utilityService.fileService = fileService;
        utilityService.textService = textService;
        htmlService.textService = textService;
        loggerBackend.fileService = fileService;
        loggerBackend.textService = textService;
        resourceService.webService = webService;
        resourceService.logger = logService;
    }

    /**
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    protected void setUpEach() {
        sorgente = VUOTA;
        sorgente2 = VUOTA;
        sorgente3 = VUOTA;
        previsto = VUOTA;
        previsto2 = VUOTA;
        previsto3 = VUOTA;
        ottenuto = VUOTA;
        ottenuto2 = VUOTA;
        ottenuto3 = VUOTA;
        sorgenteIntero = 0;
        previstoIntero = 0;
        ottenutoIntero = 0;
        sorgenteLong = 0;
        previstoLong = 0;
        ottenutoLong = 0;
        sorgenteDouble = 0;
        previstoDouble = 0;
        ottenutoDouble = 0;
        clazz = null;
        previstoRisultato = null;
        ottenutoRisultato = null;
        sorgenteClasse = null;
        previstoClasse = null;
        ottenutoClasse = null;
        sorgenteField = null;
        ottenutoField = null;
        sorgenteMatrice = null;
        previstoMatrice = null;
        ottenutoMatrice = null;
        sorgenteArray = null;
        previstoArray = null;
        ottenutoArray = null;
        mappaSorgente = null;
        mappaPrevista = null;
        mappaOttenuta = null;
        listaFields = null;
        listaStr = null;
        listaBean = null;
        mappa = null;
        bytes = null;
        streamResource = null;
        span = null;
        inizio = System.currentTimeMillis();
        message = VUOTA;
    }


    protected void printError(final AlgosException unErrore) {
        System.out.println(VUOTA);
        System.out.println("Errore");
        if (unErrore.getCause() != null) {
            System.out.println(String.format("Cause %s %s", FORWARD, unErrore.getCause().getClass().getSimpleName()));
        }
        System.out.println(String.format("Message %s %s", FORWARD, unErrore.getMessage()));
        if (unErrore.getEntityBean() != null) {
            System.out.println(String.format("EntityBean %s %s", FORWARD, unErrore.getEntityBean().toString()));
        }

        //@todo rimettere in AlgosException
        //        if (unErrore.getClazz() != null) {
        //            System.out.println(String.format("Clazz %s %s", FORWARD, unErrore.getClazz().getSimpleName()));
        //        }
        //        if (textService.isValid(unErrore.getMethod())) {
        //            System.out.println(String.format("Method %s %s()", FORWARD, unErrore.getMethod()));
        //        }
    }

    protected void printError(final Exception unErrore) {
        System.out.println(VUOTA);
        System.out.println("Errore");
        if (unErrore == null) {
            return;
        }

        if (unErrore instanceof AlgosException erroreAlgos) {
            System.out.println(String.format("Class %s %s", FORWARD, erroreAlgos.getClazz()));
            System.out.println(String.format("Method %s %s", FORWARD, erroreAlgos.getMethod()));
            System.out.println(String.format("Message %s %s", FORWARD, erroreAlgos.getMessage()));
        }
        else {
            System.out.println(String.format("Class %s %s", FORWARD, unErrore.getCause() != null ? unErrore.getCause().getClass().getSimpleName() : VUOTA));
            System.out.println(String.format("Message %s %s", FORWARD, unErrore.getMessage()));
            System.out.println(String.format("Cause %s %s", FORWARD, unErrore.getCause()));
        }
    }

    protected void print(String sorgente, String ottenuto) {
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
    }

    protected void print(long sorgenteLong, String ottenuto) {
        System.out.println(String.format("%d%s%s", sorgenteLong, FORWARD, ottenuto));
    }

    protected void printLista(final List lista) {
        int cont = 0;
        System.out.println(VUOTA);

        if (lista != null) {
            if (lista.size() > 0) {
                System.out.println(String.format("La lista contiene %d elementi", lista.size()));
                for (Object obj : lista) {
                    cont++;
                    System.out.print(cont);
                    System.out.print(PARENTESI_TONDA_END);
                    System.out.print(SPAZIO);
                    System.out.println(obj);
                }
            }
            else {
                System.out.println("Non ci sono elementi nella lista");
            }
        }
        else {
            System.out.println("Manca la lista");
        }
    }

    //    protected void printVuota(List<String> lista, String message) {
    //        System.out.println(VUOTA);
    //        print(lista, message);
    //    }

    protected void print(List<String> lista, String message) {
        int k = 0;
        if (lista != null && lista.size() > 0) {
            System.out.println(String.format("Ci sono %d elementi nella lista %s", lista.size() - 1, message));
        }
        else {
            System.out.println("La lista è vuota");
        }
        System.out.println(VUOTA);
        if (arrayService.isAllValid(lista)) {
            for (String stringa : lista) {
                System.out.print(k++);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);
                System.out.println(stringa);
            }
        }
    }


    protected void printMappa(Map<String, List<String>> mappa, String titoloMappa) {
        List<String> lista;
        String riga;
        String message = "esclusi i titoli";
        if (arrayService.isAllValid(mappa)) {
            System.out.println(VUOTA);
            System.out.println(String.format("Ci sono %d elementi nella mappa %s %s", mappa.size(), titoloMappa, message));
            System.out.println(VUOTA);
            for (String key : mappa.keySet()) {
                lista = mappa.get(key);
                if (arrayService.isAllValid(lista)) {
                    System.out.print(key);
                    System.out.print(FORWARD);
                    riga = VUOTA;
                    for (String value : lista) {
                        riga += value;
                        riga += VIRGOLA;
                        riga += SPAZIO;
                    }
                    riga = textService.levaCoda(riga, VIRGOLA).trim();
                    System.out.println(riga);
                }
            }
        }
    }

    protected void printTag(AIType enumTag) {
        System.out.println(String.format("%s%s%s", enumTag, FORWARD, enumTag.getTag()));
    }

    protected void printSpan(Span span) {
        System.out.println(span != null ? span.getElement().toString() : VUOTA);
        System.out.println(VUOTA);
    }

    protected String getTime() {
        return dateService.deltaTextEsatto(inizio);
    }

    protected void printRisultato(AResult result) {
        List lista = result.getLista();
        lista = lista != null && lista.size() > 20 ? lista.subList(0, 10) : lista;

        System.out.println(VUOTA);
        System.out.println("Risultato");
        System.out.println(String.format("Status: %s", result.isValido() ? "true" : "false"));
        System.out.println(String.format("Method: %s", result.getMethod()));
        //        System.out.println(String.format("Title: %s", result.getWikiTitle()));
        System.out.println(String.format("Target: %s", result.getTarget()));
        System.out.println(String.format("Type: %s", result.getType()));
        //        System.out.println(String.format("Preliminary url: %s", result.getUrlPreliminary()));
        //        System.out.println(String.format("Secondary url: %s", result.getUrlRequest()));
        //        System.out.println(String.format("Preliminary response: %s", result.getPreliminaryResponse()));
        //        System.out.println(String.format("Token: %s", result.getToken()));
        //        System.out.println(String.format("Secondary response: %s", result.getResponse()));
        System.out.println(String.format("Message code: %s", result.getCodeMessage()));
        System.out.println(String.format("Message: %s", result.getMessage()));
        System.out.println(String.format("Error code: %s", result.getErrorCode()));
        System.out.println(String.format("Error message: %s", result.getErrorMessage()));
        System.out.println(String.format("Valid message: %s", result.getValidMessage()));
        System.out.println(String.format("Numeric value: %s", textService.format(result.getIntValue())));
        System.out.println(String.format("List value: %s", lista));
        System.out.println(String.format("Map value: %s", result.getMappa()));
        System.out.println(String.format("Risultato ottenuto in %s", dateService.deltaText(inizio)));
    }

    protected String getSimpleName(final Class clazz) {
        return clazz != null ? clazz.getSimpleName() : "(manca la classe)";
    }
    protected void startTime() {
        inizio = System.currentTimeMillis();
    }
    protected void printTime() {
        System.out.println(dateService.deltaText(inizio));
    }
    protected void printTimeEsatto() {
        System.out.println(dateService.deltaTextEsatto(inizio));
    }


}
