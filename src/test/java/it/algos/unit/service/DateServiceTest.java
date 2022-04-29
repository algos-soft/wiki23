package it.algos.unit.service;

import it.algos.test.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.*;

import java.util.concurrent.*;
import java.util.stream.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: mer, 09-mar-2022
 * Time: 18:39
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("slowly")
@DisplayName("Date service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DateServiceTest extends ATest {

    public static final long MILLISECONDI_AL_MINUTO = 60000;

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private DateService service;


    //--millisecondi (long)
    protected static Stream<Arguments> MILLISECONDI() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of((long) 0),
                Arguments.of((long) 87),
                Arguments.of((long) 750),
                Arguments.of((long) 2340),
                Arguments.of((long) 45000),
                Arguments.of((long) 58 * MILLISECONDI_AL_MINUTO),
                Arguments.of((long) 118 * MILLISECONDI_AL_MINUTO),
                Arguments.of((long) 122 * MILLISECONDI_AL_MINUTO),
                Arguments.of((long) 7500000),
                Arguments.of((long) 86500000)
        );
    }

    //--secondi (long)
    protected static Stream<Arguments> SECONDI() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of((long) 0),
                Arguments.of((long) 45),
                Arguments.of((long) 71),
                Arguments.of((long) 87),
                Arguments.of((long) 750),
                Arguments.of((long) 2340),
                Arguments.of((long) 45000),
                Arguments.of((long) 58 * MILLISECONDI_AL_MINUTO),
                Arguments.of((long) 118 * MILLISECONDI_AL_MINUTO)
        );
    }

    //--minuti (long)
    protected static Stream<Arguments> MINUTI() {
        return Stream.of(
                Arguments.of((Long) null),
                Arguments.of((long) 0),
                Arguments.of((long) 3),
                Arguments.of((long) 45),
                Arguments.of((long) 71),
                Arguments.of((long) 87),
                Arguments.of((long) 750),
                Arguments.of((long) 2340),
                Arguments.of((long) 45000),
                Arguments.of((long) 58 * MILLISECONDI_AL_MINUTO)
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
        service = dateService;
    }


    /**
     * Qui passa prima di ogni test <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    @Test
    @Order(1)
    @DisplayName("1 - Formatta una durata (millisecondi)")
        //--millisecondi (long)
    void toText() {
        System.out.println("1 - Formatta una durata espressa in millisecondi (long)");
        System.out.println(VUOTA);
        MILLISECONDI().forEach(this::printMilliSecondi);
    }

    void printMilliSecondi(Arguments arg) {
        Object[] mat = arg.get();
        sorgenteLong = (mat[0] != null) ? (long) mat[0] : 0;

        ottenuto = service.toText(sorgenteLong);
        print(sorgenteLong, ottenuto);
    }


    @Test
    @Order(2)
    @DisplayName("2 - Formatta una durata (secondi)")
        //--secondi (long)
    void toTextSecondi() {
        System.out.println("2 - Formatta una durata espressa in secondi (long)");
        System.out.println(VUOTA);
        SECONDI().forEach(this::printSecondi);
    }

    void printSecondi(Arguments arg) {
        Object[] mat = arg.get();
        sorgenteLong = (mat[0] != null) ? (long) mat[0] : 0;

        ottenuto = service.toTextSecondi(sorgenteLong);
        print(sorgenteLong, ottenuto);
    }


    @Test
    @Order(3)
    @DisplayName("3 - Formatta una durata (minuti)")
        //--minuti (long)
    void toTextMinuti() {
        System.out.println("3 - Formatta una durata espressa in minuti (long)");
        System.out.println(VUOTA);
        MINUTI().forEach(this::printMinuti);
    }

    void printMinuti(Arguments arg) {
        Object[] mat = arg.get();
        sorgenteLong = (mat[0] != null) ? (long) mat[0] : 0;

        ottenuto = service.toTextMinuti(sorgenteLong);
        print(sorgenteLong, ottenuto);
    }


    @Test
    @Order(4)
    @DisplayName("4 - Tempo trascorso in millisecondi")
    void deltaTextEsatto() throws InterruptedException {
        System.out.println("4 - Tempo trascorso espresso in millisecondi (long)");
        System.out.println(VUOTA);

        sorgenteLong = System.currentTimeMillis();

        TimeUnit.MILLISECONDS.sleep(357);
        ottenuto = service.deltaTextEsatto(sorgenteLong);
        System.out.println(String.format("Sono trascorsi esattamente %s", ottenuto));

        TimeUnit.SECONDS.sleep(1);
        ottenuto = service.deltaTextEsatto(sorgenteLong);
        System.out.println(String.format("Sono trascorsi esattamente %s", ottenuto));
    }

    @Test
    @Order(5)
    @DisplayName("5 - Tempo trascorso in secondi")
    void deltaText() throws InterruptedException {
        System.out.println("5 - Tempo trascorso espresso in secondi (long)");
        System.out.println(VUOTA);

        sorgenteLong = System.currentTimeMillis();

        TimeUnit.MILLISECONDS.sleep(894);
        ottenuto = service.deltaText(sorgenteLong);
        System.out.println(String.format("Sono trascorsi %s", ottenuto));

        TimeUnit.SECONDS.sleep(1);
        ottenuto = service.deltaText(sorgenteLong);
        System.out.println(String.format("Sono trascorsi %s", ottenuto));

        TimeUnit.SECONDS.sleep(7);
        ottenuto = service.deltaText(sorgenteLong);
        System.out.println(String.format("Sono trascorsi %s", ottenuto));
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