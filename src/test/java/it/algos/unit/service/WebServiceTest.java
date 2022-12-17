package it.algos.unit.service;

import it.algos.base.*;
import it.algos.vaad24.backend.service.*;
import static it.algos.vaad24.backend.service.WebService.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: mer, 06-apr-2022
 * Time: 06:49
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@DisplayName("Web service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WebServiceTest extends AlgosTest {


    public static final String URL_WEB_GAC = URL_BASE_ALGOS + "hellogac.html";


    public static final String URL_CONTINENTI = URL_BASE_VAADIN23 + "continenti";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    private static final String TAG_WIKI = "https://it.wikipedia.org/wiki/";

    private static final String UNKNOWN_HOST = "java.net.UnknownHostException: htp";

    private static final String URL_ERRATO = "htp://www.altos.it/hellogac.html";

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private WebService service;

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
        service = webService;
    }


    /**
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    @Test
    @Order(1)
    @DisplayName("1 - Legge un indirizzo URL errato (inesistente)")
    public void leggeErrato() {
        sorgente = URL_ERRATO;

        //        ottenutoRisultato = service.legge(sorgente);
        //        assertNotNull(ottenutoRisultato);
        //        assertTrue(ottenutoRisultato.isErrato());
        //        assertTrue(textService.isEmpty(ottenutoRisultato.getWikiTitle()));
        //        assertEquals(sorgente, ottenutoRisultato.getUrlRequest());
        //        assertTrue(ottenutoRisultato.getErrorCode().equals(UNKNOWN_HOST));
        //        assertTrue(ottenutoRisultato.getErrorMessage().equals(UNKNOWN_HOST));
        //        assertTrue(textService.isEmpty(ottenutoRisultato.getValidMessage()));
        //        assertTrue(textService.isEmpty(ottenutoRisultato.getResponse()));
        //        assertTrue(ottenutoRisultato.getIntValue() == 0);
        //
        //        System.out.println(String.format("Non ha trovato il domain '%s' richiesto", ottenutoRisultato.getUrlRequest()));
        //        System.out.println("Genera un messaggio di errore:");
        //        System.out.println(ottenutoRisultato.getErrorMessage());
        //
        //        ottenuto = service.leggeWebTxt(sorgente);
        //        assertNotNull(ottenuto);
        //        assertTrue(textService.isEmpty(ottenuto));
    }

    @Test
    @Order(2)
    @DisplayName("2 - Legge un indirizzo URL generico")
    public void leggeGac() {
        sorgente = URL_WEB_GAC;
        previsto = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";

        //        ottenutoRisultato = service.legge(sorgente);
        //        assertNotNull(ottenutoRisultato);
        //        assertTrue(ottenutoRisultato.isValido());
        //        assertTrue(textService.isEmpty(ottenutoRisultato.getWikiTitle()));
        //        assertEquals(sorgente, ottenutoRisultato.getUrlRequest());
        //        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorCode()));
        //        assertTrue(textService.isEmpty(ottenutoRisultato.getErrorMessage()));
        //        assertEquals(JSON_SUCCESS, ottenutoRisultato.getValidMessage());
        //        assertTrue(textService.isValid(ottenutoRisultato.getResponse()));
        //        assertTrue(ottenutoRisultato.getResponse().startsWith(previsto));
        //        assertTrue(ottenutoRisultato.getIntValue() == 0);
        //
        //        ottenuto = service.leggeWebTxt(sorgente);
        //        assertNotNull(ottenuto);
        //        assertTrue(textService.isValid(ottenuto));
        //        assertTrue(ottenuto.equals(ottenutoRisultato.getResponse()));
        //        assertTrue(ottenuto.startsWith(previsto));
        //
        //        System.out.println(String.format("2 - Legge il testo grezzo di una pagina web"));
        //        System.out.println(String.format("La pagina web è: %s", ottenutoRisultato.getUrlRequest()));
        //        System.out.println("Risultato restituito in formato html");
        //        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
        //        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");
        //
        //        System.out.println(VUOTA);
        //        System.out.println(ottenuto.substring(0, previsto.length()));
    }

    @Test
    @Order(3)
    @DisplayName("3 - Legge una risorsa sul server")
    public void leggeCsv() {
        sorgente = URL_CONTINENTI;
        ottenuto = service.leggeWebTxt(sorgente);
        assertNotNull(ottenuto);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(ottenuto.substring(0, 200));
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