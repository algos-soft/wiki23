package it.algos.integration.service;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.boot.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.packages.utility.log.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 07-mar-2022
 * Time: 22:50
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("service")
@DisplayName("Log service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogServiceTest extends AlgosTest {

    @Autowired
    protected ApplicationContext appContext;

    private WrapLogCompany wrapCompany;

    private WrapLog wrapLog;

    private AlgosException exception;

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private LogService service;

    @Autowired
    //    @Qualifier("Logger")
    protected LoggerRepository repository;

    //--companySigla
    //--userName
    //--addressIP
    //--type
    //--messaggio
    protected static Stream<Arguments> COMPANY() {
        return Stream.of(
                Arguments.of(VUOTA, VUOTA, VUOTA, (AETypeLog) null, VUOTA),
                Arguments.of("crpt", "ugonottiMario", "2001:B07:AD4:2177:9B56:DB51:33E0:A151", AETypeLog.login, "Messaggio con una company"),
                Arguments.of("gaps", "Angelini P.", "5.168.221.253", AETypeLog.login, "Messaggio con una company"),
                Arguments.of("algos", "INFERMIERE", "2001:B07:A56:933F:F17B:8C00:76DE:1E27", AETypeLog.login, "Messaggio con una company"),
                Arguments.of(VUOTA, "Mario Beretta", "23.5678.987", AETypeLog.delete, "Messaggio con una company"),
                Arguments.of("algos", VUOTA, "127.0.0.1", AETypeLog.setup, "VUOTA"),
                Arguments.of("algos", VUOTA, "80.79.58.124", AETypeLog.modifica, "VUOTA"),
                Arguments.of("crpt", "m. Rossi", VUOTA, AETypeLog.upload, "VUOTA"),
                Arguments.of("pap", "Franca Baldini", "51.179.101.238", AETypeLog.checkMenu, "VUOTA")
        );
    }

    /**
     * Execute only once before running all tests <br>
     * Esegue una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpAll() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = logService;

        service.slf4jLogger = LoggerFactory.getLogger("vaad23.admin");
    }

    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixRiferimentiIncrociati() {
        super.fixRiferimentiIncrociati();
    }

    /**
     * Qui passa prima di ogni test <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        wrapCompany = null;
        wrapLog = null;
        service.mailService = mailService;
        VaadVar.usaCompany = false;
        exception = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - Messaggi di solo testo su slf4jLogger")
    void log1() {
        System.out.println("1 - Messaggi di solo testo su slf4jLogger");
        System.out.println(VUOTA);

        sorgente2 = AELogLevel.info.toString();
        sorgente = String.format("Messaggio semplice di %s proveniente dal test", sorgente2);
        previsto = DUE_PUNTI_SPAZIO + textService.fixSizeQuadre(AETypeLog.system.toString(), PAD_SYSTEM) + SEP + sorgente;
        ottenuto = service.info(new WrapLog().message(sorgente));
        assertEquals(previsto, ottenuto);
        printMessaggio(ottenuto);

        sorgente2 = AELogLevel.warn.toString();
        sorgente = String.format("Messaggio semplice di %s proveniente dal test", sorgente2);
        previsto = DUE_PUNTI_SPAZIO + textService.fixSizeQuadre(AETypeLog.system.toString(), PAD_SYSTEM) + SEP + sorgente;
        ottenuto = service.warn(new WrapLog().message(sorgente));
        assertEquals(previsto, ottenuto);
        printMessaggio(ottenuto);

        sorgente2 = AELogLevel.error.toString();
        sorgente = String.format("Messaggio semplice di %s proveniente dal test", sorgente2);
        previsto = DUE_PUNTI_SPAZIO + textService.fixSizeQuadre(AETypeLog.system.toString(), PAD_SYSTEM) + SEP + sorgente;
        ottenuto = service.error(new WrapLog().message(sorgente));
        assertEquals(previsto, ottenuto);
        printMessaggio(ottenuto);

        sorgente2 = AELogLevel.debug.toString();
        sorgente = String.format("Messaggio semplice di %s proveniente dal test", sorgente2);
        previsto = DUE_PUNTI_SPAZIO + textService.fixSizeQuadre(AETypeLog.system.toString(), PAD_SYSTEM) + SEP + sorgente;
        ottenuto = service.debug(new WrapLog().message(sorgente));
        assertEquals(previsto, ottenuto);
        printMessaggio(ottenuto);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Messaggi con typo su slf4jLogger")
    void log2() {
        System.out.println("2 - Messaggi con typo su slf4jLogger");
        System.out.println(VUOTA);

        sorgente2 = AELogLevel.info.toString();
        sorgente = String.format("Messaggio typo di %s proveniente dal test", sorgente2);
        previsto = DUE_PUNTI_SPAZIO + textService.fixSizeQuadre(AETypeLog.checkData.toString(), PAD_SYSTEM) + SEP + sorgente;
        ottenuto = service.info(new WrapLog().type(AETypeLog.checkData).message(sorgente));
        assertEquals(previsto, ottenuto);
        printMessaggio(ottenuto);

        sorgente2 = AELogLevel.info.toString();
        sorgente = String.format("Messaggio typo di %s proveniente dal test", sorgente2);
        previsto = DUE_PUNTI_SPAZIO + textService.fixSizeQuadre(AETypeLog.download.toString(), PAD_SYSTEM) + SEP + sorgente;
        ottenuto = service.info(new WrapLog().type(AETypeLog.download).message(sorgente));
        assertEquals(previsto, ottenuto);
        printMessaggio(ottenuto);

        sorgente2 = AELogLevel.info.toString();
        sorgente = String.format("Messaggio typo di %s proveniente dal test", sorgente2);
        previsto = DUE_PUNTI_SPAZIO + textService.fixSizeQuadre(AETypeLog.preferenze.toString(), PAD_SYSTEM) + SEP + sorgente;
        ottenuto = service.info(new WrapLog().type(AETypeLog.preferenze).message(sorgente));
        assertEquals(previsto, ottenuto);
        printMessaggio(ottenuto);

        sorgente2 = AELogLevel.warn.toString();
        sorgente = String.format("Messaggio typo di %s proveniente dal test", sorgente2);
        previsto = DUE_PUNTI_SPAZIO + textService.fixSizeQuadre(AETypeLog.modifica.toString(), PAD_SYSTEM) + SEP + sorgente;
        ottenuto = service.warn(new WrapLog().type(AETypeLog.modifica).message(sorgente));
        assertEquals(previsto, ottenuto);
        printMessaggio(ottenuto);

        sorgente2 = AELogLevel.error.toString();
        sorgente = String.format("Messaggio typo di %s proveniente dal test", sorgente2);
        previsto = DUE_PUNTI_SPAZIO + textService.fixSizeQuadre(AETypeLog.startup.toString(), PAD_SYSTEM) + SEP + sorgente;
        ottenuto = service.error(new WrapLog().type(AETypeLog.startup).message(sorgente));
        assertEquals(previsto, ottenuto);
        printMessaggio(ottenuto);

        sorgente2 = AELogLevel.debug.toString();
        sorgente = String.format("Messaggio typo di %s proveniente dal test", sorgente2);
        previsto = DUE_PUNTI_SPAZIO + textService.fixSizeQuadre(AETypeLog.startup.toString(), PAD_SYSTEM) + SEP + sorgente;
        ottenuto = service.debug(new WrapLog().type(AETypeLog.startup).message(sorgente));
        assertEquals(previsto, ottenuto);
        printMessaggio(ottenuto);
    }


    @Test
    @Order(3)
    @DisplayName("3 - Messaggi con company and user")
    void log3() {
        VaadVar.usaCompany = true;
        System.out.println("3 - Messaggi con company and user");
        System.out.println(VUOTA);

        sorgente = String.format("Messaggio di %s con company", AELogLevel.info);
        ottenuto = service.info(new WrapLog().message(sorgente).company("crpt"));
        printMessaggio(ottenuto);

        sorgente = String.format("Messaggio di %s con type e company", AELogLevel.info);
        ottenuto = service.info(new WrapLog().type(AETypeLog.password).message(sorgente).company("gaps"));
        printMessaggio(ottenuto);

        sorgente = String.format("Messaggio di %s con type, company e user", AELogLevel.info);
        ottenuto = service.info(new WrapLog().type(AETypeLog.elabora).message(sorgente).company("pap").user("Marcella P."));
        printMessaggio(ottenuto);

        sorgente = String.format("Messaggio di %s con type, company, user e indirizzo IP", AELogLevel.info);
        ottenuto = service.info(new WrapLog().type(AETypeLog.modifica).message(sorgente).company("crpt").user("Facchinetti Mario").address("2001:B07:AD4:2177:9B56:DB51:33E0:A151"));
        printMessaggio(ottenuto);

        sorgente = String.format("Messaggio di %s con type, company, user e indirizzo IP", AELogLevel.warn);
        ottenuto = service.warn(new WrapLog().type(AETypeLog.modifica).message(sorgente).company("crpt").user("Facchinetti Mario").address("2001:B07:AD4:2177:9B56:DB51:33E0:A151"));
        printMessaggio(ottenuto);

    }

    @Test
    @Order(4)
    @DisplayName("4 - Messaggi registrati su mongoDB")
    void log4() {
        sorgente = String.format("Messaggio su mongoDB di %s proveniente dal test", AELogLevel.info);
        ottenuto = service.info(new WrapLog().message(sorgente).usaDb());
        printMessaggio(ottenuto);

        sorgente = String.format("Messaggio su mongoDB tipizzato", AELogLevel.info);
        ottenuto = service.info(new WrapLog().type(AETypeLog.bio).message(sorgente).usaDb());
        printMessaggio(ottenuto);

        sorgente = String.format("Messaggio di errore su mongoDB tipizzato", AELogLevel.info);
        ottenuto = service.error(new WrapLog().type(AETypeLog.bio).message(sorgente).usaDb().exception(new AlgosException("Messaggio generato dall'errore")));
        printMessaggio(ottenuto);
    }


    @Test
    @Order(5)
    @DisplayName("5 - Messaggi comprensivi di StackTrace")
    void stackTrace() {
        System.out.println(sorgente);
        System.out.println("L'errore può essere di sistema oppure un AlgosException generato nel codice");

        service.warn(AETypeLog.export, new AlgosException("service.warn(AETypeLog.export, new AlgosException(\"testo\"));"));
        service.error(AETypeLog.delete, new AlgosException("service.error(AETypeLog.delete, new AlgosException(\"testo\"));"));

        service.warn(new AlgosException("service.warn(new AlgosException(\"testo\"));"));
        service.error(new AlgosException("service.error(new AlgosException(\"testo\"));"));

        service.warn(new AlgosException(VUOTA));
        service.error(new AlgosException(VUOTA));

        service.warn(new AlgosException());
        service.error(new AlgosException());
    }


    @Test
    @Order(6)
    @DisplayName("6 - Messaggi con StackTrace registrati su mongoDB")
    void stackTraceConDb() {
        System.out.println(sorgente);
        System.out.println("L'errore può essere di sistema oppure un AlgosException generato nel codice");

        wrapLog = new WrapLog()
                .message("Messaggio")
                .type(AETypeLog.password)
                .exception(new AlgosException("Niente"))
                .company("crpt")
                .user("Domenichetti")
                .address("2001:B07:6466:70A3:D869:97FA:FB8E:C443")
                .usaDb();

        ottenuto = service.error(wrapLog);
        printMessaggio(ottenuto);
    }

    @Test
    @Order(7)
    @DisplayName("7 - Utilizzo di wrapLog con Fluent API")
    void wrapLog() {
        System.out.println("7 - Utilizzo di wrapLog con Fluent API");
        System.out.println("Creo un wrapLog senza SpringBoot - no @Autowired");
        System.out.println("Chiamo logService usando wrapLog");

        System.out.println(VUOTA);
        wrapLog = new WrapLog();
        ottenuto = service.info(wrapLog);
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        wrapLog = new WrapLog().message("Solo testo");
        ottenuto = service.info(wrapLog);
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        wrapLog = new WrapLog().message("Testo e type").type(AETypeLog.modifica);
        ottenuto = service.info(wrapLog);
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        wrapLog = new WrapLog().type(AETypeLog.modifica).message("Type e testo (uguale come sopra; cambiando l'ordine dei fattori il risultato non cambia)");
        ottenuto = service.info(wrapLog);
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        wrapLog = new WrapLog().type(AETypeLog.password).message("Info o warn il wrap è uguale");
        ottenuto = service.warn(wrapLog);
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        wrapLog = new WrapLog().type(AETypeLog.mongo).message("Anche per error");
        ottenuto = service.error(wrapLog);
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        wrapLog = new WrapLog().exception(new AlgosException("Eccezione"));
        ottenuto = service.error(wrapLog);
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        wrapLog = new WrapLog().type(AETypeLog.elabora).exception(new AlgosException("Eccezione con type"));
        ottenuto = service.error(wrapLog);
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        wrapLog = new WrapLog().exception(new AlgosException("Qualcosa non ha funzionato"));
        ottenuto = service.info(wrapLog);
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        ottenuto = service.info(new WrapLog().exception(new AlgosException("Motivo del malfunzionamento")));
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        ottenuto = service.info(new WrapLog().type(AETypeLog.login).message("Altro testo").exception(new AlgosException("Altri metodi")));
        printMessaggio(ottenuto);
    }

    @Test
    @Order(8)
    @DisplayName("8 - Utilizzo di wrapLog per multiCompany")
    void wrapLogCompany() {
        System.out.println("8 - Utilizzo di wrapLog per multiCompany");
        System.out.println("Creo un wrapLog senza SpringBoot - no @Autowired");
        System.out.println("Chiamo logService usando wrapLog");

        System.out.println(VUOTA);
        wrapLog = new WrapLog().message("Solo testo");
        ottenuto = service.info(wrapLog);
        printMessaggio(ottenuto);

        sorgente = "Marco Pettinelli";
        System.out.println(VUOTA);
        wrapLog = new WrapLog().message(String.format("L'utente %s ha eseguito questa operazione", sorgente));
        ottenuto = service.info(wrapLog);
        printMessaggio(ottenuto);

        System.out.println(VUOTA);
        wrapLog = new WrapLog().message(String.format("L'utente %s ha eseguito questa operazione", sorgente)).user(sorgente);
        ottenuto = service.info(wrapLog);
        printMessaggio(ottenuto);
    }


    @Test
    @Order(9)
    @DisplayName("9 - Utilizzo di wrapLog per multiCompany con error")
    void wrapLogCompanyError() {
        System.out.println("9 - Utilizzo di wrapLog per multiCompany con error");
        System.out.println("Creo un wrapLog senza SpringBoot - no @Autowired");
        System.out.println("Chiamo logService usando wrapLog");

        System.out.println(VUOTA);
        wrapLog = new WrapLog().exception(new AlgosException("Qualcosa non ha funzionato")).company("crpt");
        ottenuto = service.info(wrapLog);
        printMessaggio(ottenuto);

        sorgente = "L'utente Rossi Carlo si è loggato con una password errata";
        System.out.println(VUOTA);
        wrapLog = new WrapLog().exception(new AlgosException("Qualcosa non ha funzionato")).company("crpt").message(sorgente);
        ottenuto = service.error(wrapLog);
        printMessaggio(ottenuto);
    }

    //    @Test
    @Order(8)
    @DisplayName("8 - Info con incolonnamento")
    //--companySigla
    //--userName
    //--addressIP
    //--type
    //--messaggio
    void infoCompanyIncolonnate() {
        System.out.println("8 - Info con incolonnamento");
        System.out.println(VUOTA);
        COMPANY().forEach(this::printWrap);
    }


    //    @Test
    @Order(10)
    @DisplayName("10 - Prova")
    void prova() {
        arrayService.isEmpty(new ArrayList());
        //        service.mail(AETypeLog.login, wrap, sorgente);
    }

    void printMessaggio(String messaggio) {
        System.out.println(String.format("Messaggio: %s", messaggio));
        System.out.println(VUOTA);
    }

    void printWrap(Arguments arg) {
        Object[] mat = arg.get();
        wrapCompany = appContext.getBean(WrapLogCompany.class, mat[0], mat[1], mat[2]);
        System.out.println(wrapCompany.toString());
    }

    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterAll
    void tearDownAll() {
    }

}