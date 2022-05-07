package it.algos.unit.service;

import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 07-mar-2022
 * Time: 19:46
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("quickly")
@DisplayName("Text service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TextServiceTest extends AlgosTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private TextService service;


    //--testoIn
    //--primaMaiuscola
    //--primaMinuscola
    protected static Stream<Arguments> NOMI() {
        return Stream.of(
                Arguments.of(null, VUOTA, VUOTA),
                Arguments.of(VUOTA, VUOTA, VUOTA),
                Arguments.of("MARIO", "MARIO", "mARIO"),
                Arguments.of("mario", "Mario", "mario"),
                Arguments.of("Mario", "Mario", "mario"),
                Arguments.of("maRio", "MaRio", "maRio"),
                Arguments.of("MaRio", "MaRio", "maRio"),
                Arguments.of(" mario", "Mario", "mario"),
                Arguments.of("mario ", "Mario", "mario"),
                Arguments.of(" mario ", "Mario", "mario"),
                Arguments.of(" Mario", "Mario", "mario"),
                Arguments.of("Mario ", "Mario", "mario"),
                Arguments.of(" Mario ", "Mario", "mario")
        );
    }


    //--sorgente
    //--previsto
    protected static Stream<Arguments> QUADRE() {
        return Stream.of(
                Arguments.of(null, VUOTA),
                Arguments.of(VUOTA, VUOTA),
                Arguments.of("antonio ", "antonio"),
                Arguments.of("[antonio", "antonio"),
                Arguments.of(" antonio]]", "antonio"),
                Arguments.of("[antonio]", "antonio"),
                Arguments.of(" [[antonio]]", "antonio"),
                Arguments.of("[[[antonio] ", "antonio"),
                Arguments.of("antonio]]] ", "antonio"),
                Arguments.of("an[[ton]]ella ", "an[[ton]]ella"),
                Arguments.of("[[[antonio]]]", "antonio")
        );
    }


    //--sorgente
    //--larghezza
    //--previsto
    protected static Stream<Arguments> PAD() {
        return Stream.of(
                Arguments.of(null, 0, VUOTA),
                Arguments.of(VUOTA, 0, VUOTA),
                Arguments.of("antonio", 10, "antonio   "),
                Arguments.of("antonio", 7, "antonio"),
                Arguments.of("antonio", 5, "antonio"),
                Arguments.of("          antonio", 15, "antonio        "),
                Arguments.of("   mario", 15, "mario          "),
                Arguments.of(" mario", 7, "mario  "),
                Arguments.of(" mario ", 5, "mario")
        );
    }

    //--sorgente
    //--larghezza
    //--previsto
    protected static Stream<Arguments> SIZE() {
        return Stream.of(
                Arguments.of(null, 0, VUOTA),
                Arguments.of(VUOTA, 0, VUOTA),
                Arguments.of("antonio", 10, "antonio   "),
                Arguments.of("antonio", 7, "antonio"),
                Arguments.of("antonio", 5, "anton"),
                Arguments.of("          antonio", 15, "antonio        "),
                Arguments.of("   mario", 15, "mario          "),
                Arguments.of(" mario", 7, "mario  "),
                Arguments.of(" mario ", 4, "mari")
        );
    }

    //--sorgente
    //--larghezza
    //--previsto
    protected static Stream<Arguments> SIZE_QUADRE() {
        return Stream.of(
                Arguments.of(null, 0, VUOTA),
                Arguments.of(VUOTA, 0, VUOTA),
                Arguments.of(null, 3, "[   ]"),
                Arguments.of(VUOTA, 3, "[   ]"),
                Arguments.of("antonio", 10, "[antonio   ]"),
                Arguments.of("antonio", 7, "[antonio]"),
                Arguments.of("antonio", 5, "[anton]"),
                Arguments.of("          antonio", 15, "[antonio        ]"),
                Arguments.of("   mario", 15, "[mario          ]"),
                Arguments.of(" mario", 7, "[mario  ]"),
                Arguments.of(" mario ", 4, "[mari]")
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
        service = textService;
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
    @DisplayName("1 - Controlla testo vuoto")
    void testIsEmpty() {
        ottenutoBooleano = service.isEmpty(null);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty(VUOTA);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty(PIENA);
        assertFalse(ottenutoBooleano);

        System.out.println("1 - Controlla testo vuoto");
        System.out.println("Fatto");
    }


    @Test
    @Order(2)
    @DisplayName("2 - Controlla validità")
    void testIsValid() {
        ottenutoBooleano = service.isValid(null);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isValid(VUOTA);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isValid(PIENA);
        assertTrue(ottenutoBooleano);

        System.out.println("2 - Controlla validità di una stringa");
        System.out.println("Fatto");
        System.out.println(VUOTA);

        ottenutoBooleano = service.isValid((List) null);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isValid((new ArrayList()));
        assertFalse(ottenutoBooleano);

        System.out.println("2 - Controlla validità di un oggetto generico");
        System.out.println("Fatto");
    }

    @ParameterizedTest
    @MethodSource(value = "NOMI")
    @Order(3)
    @DisplayName("3 - Prima maiuscola")
        //--testoIn
        //--primaMaiuscola
        //--primaMinuscola
    void primaMaiuscola(final String sorgente, final String previsto, final String nonUsata) {
        System.out.println("3 - Prima maiuscola");
        System.out.println(VUOTA);
        ottenuto = service.primaMaiuscola(sorgente);
        assertEquals(previsto, ottenuto);
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
        System.out.println(VUOTA);
    }


    @ParameterizedTest
    @MethodSource(value = "NOMI")
    @Order(4)
    @DisplayName("4 - Prima minuscola")
    void primaMinuscola(final String sorgente, final String nonUsata, final String previsto) {
        System.out.println("4 - Prima minuscola");
        System.out.println(VUOTA);
        ottenuto = service.primaMinuscola(sorgente);
        assertEquals(previsto, ottenuto);
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
    }


    @Test
    @Order(5)
    @DisplayName("5 - Restituisce un array di stringhe")
    void getArray() {
        System.out.println("5 - Restituisce un array da una stringa di valori multipli separati da virgole");
        System.out.println(VUOTA);

        ottenutoArray = service.getArray(null);
        assertNull(ottenutoArray);

        ottenutoArray = service.getArray(sorgente);
        assertNull(ottenutoArray);
        printLista(ottenutoArray);

        sorgente = "codedescrizioneordine";
        String[] stringArray2 = {"codedescrizioneordine"};
        previstoArray = new ArrayList(Arrays.asList(stringArray2));
        ottenutoArray = service.getArray(sorgente);
        assertNotNull(ottenutoArray);
        assertEquals(ottenutoArray, previstoArray);
        printLista(ottenutoArray);

        sorgente = "code,descrizione,ordine";
        String[] stringArray3 = {"code", "descrizione", "ordine"};
        previstoArray = new ArrayList(Arrays.asList(stringArray3));
        ottenutoArray = service.getArray(sorgente);
        assertNotNull(ottenutoArray);
        assertEquals(ottenutoArray, previstoArray);
        printLista(ottenutoArray);

        sorgente = " code, descrizione , ordine ";
        String[] stringArray4 = {"code", "descrizione", "ordine"};
        previstoArray = new ArrayList(Arrays.asList(stringArray4));
        ottenutoArray = service.getArray(sorgente);
        assertNotNull(ottenutoArray);
        assertEquals(ottenutoArray, previstoArray);
        printLista(ottenutoArray);
    }


    @Test
    @Order(6)
    @DisplayName("6 - Sostituisce una parte di testo")
    public void sostituisce() {
        System.out.println("6 - Sostituisce una parte di testo");

        sorgente = "{{Simbolo|Italian Province (Crown).svg|24}} {{IT-SU}}";
        sorgente2 = "Province";
        sorgente3 = "Regioni";
        previsto = "{{Simbolo|Italian Regioni (Crown).svg|24}} {{IT-SU}}";

        ottenuto = service.sostituisce(sorgente, sorgente2, sorgente3);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s%s", FORWARD, sorgente));
        System.out.println(String.format("Da levare %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Da mettere %s%s", FORWARD, sorgente3));
        System.out.println(String.format("Testo ottenuto %s%s", FORWARD, previsto));
    }

    @Test
    @Order(7)
    @DisplayName("7 - Elimina un tag da un testo")
    public void levaTesto() {
        System.out.println("7 - Elimina un tag da un testo");

        sorgente = "{{Simbolo|Italian Province (Crown).svg|24}} {{IT-SU}}";
        sorgente2 = "i";
        previsto = "{{Smbolo|Italan Provnce (Crown).svg|24}} {{IT-SU}}";

        ottenuto = service.levaTesto(sorgente, sorgente2);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s%s", FORWARD, sorgente));
        System.out.println(String.format("Da levare %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Testo ottenuto %s%s", FORWARD, previsto));
    }

    @Test
    @Order(8)
    @DisplayName("8 - Elimina le virgole dal testo")
    public void levaVirgole() {
        System.out.println("8 - Elimina le virgole dal testo");

        sorgente = "{{Sim,bolo, Italian, Prov,ince, (Crown).svg|24}}, {{IT,SU}}";
        previsto = "{{Simbolo Italian Province (Crown).svg|24}} {{ITSU}}";

        ottenuto = service.levaVirgole(sorgente);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s%s", FORWARD, sorgente));
        System.out.println(String.format("Testo ottenuto %s%s", FORWARD, previsto));

        sorgente = "125,837,655";
        previsto = "125837655";

        ottenuto = service.levaVirgole(sorgente);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s%s", FORWARD, sorgente));
        System.out.println(String.format("Testo ottenuto %s%s", FORWARD, previsto));
    }

    @Test
    @Order(9)
    @DisplayName("9 - Elimina i punti dal testo")
    public void levaPunti() {
        System.out.println("9 - Elimina i punti dal testo");

        sorgente = "/Users.gac.Documents.IdeaProjects.operativi.vaadflow14.src.main.java.it.algos.vaadflow14.wizard";
        previsto = "/UsersgacDocumentsIdeaProjectsoperativivaadflow14srcmainjavaitalgosvaadflow14wizard";

        ottenuto = service.levaPunti(sorgente);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s%s", FORWARD, sorgente));
        System.out.println(String.format("Testo ottenuto %s%s", FORWARD, previsto));

        sorgente = "125.837.655";
        previsto = "125837655";

        ottenuto = service.levaPunti(sorgente);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s%s", FORWARD, sorgente));
        System.out.println(String.format("Testo ottenuto %s%s", FORWARD, previsto));
    }


    @Test
    @Order(10)
    @DisplayName("10 - Sostituisce gli slash con punti")
    public void slashToPoint() {
        System.out.println("10 - Sostituisce gli slash con punti");

        sorgente = " Users/gac/Documents/IdeaProjects/operativi/vaadflow14/src/main/java/it/algos/vaadflow14/wizard ";
        previsto = "Users.gac.Documents.IdeaProjects.operativi.vaadflow14.src.main.java.it.algos.vaadflow14.wizard";

        ottenuto = service.slashToPoint(sorgente);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s%s", FORWARD, sorgente));
        System.out.println(String.format("Testo ottenuto %s%s", FORWARD, previsto));

        sorgente = " /Users/gac/Documents/IdeaProjects/operativi/vaadflow14/src/main/java/it/algos/vaadflow14/wizard";
        previsto = "/Users.gac.Documents.IdeaProjects.operativi.vaadflow14.src.main.java.it.algos.vaadflow14.wizard";

        ottenuto = service.slashToPoint(sorgente);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s%s", FORWARD, sorgente));
        System.out.println(String.format("Testo ottenuto %s%s", FORWARD, previsto));
    }


    @Test
    @Order(11)
    @DisplayName("11 - Sostituisce i punti con slash")
    public void pointToSlash() {
        System.out.println("11 - Sostituisce i punti con slash");

        sorgente = " Users.gac.Documents.IdeaProjects.operativi.vaadflow14.src.main.java.it.algos.vaadflow14.wizard ";
        previsto = "Users/gac/Documents/IdeaProjects/operativi/vaadflow14/src/main/java/it/algos/vaadflow14/wizard";

        ottenuto = service.pointToSlash(sorgente);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s%s", FORWARD, sorgente));
        System.out.println(String.format("Testo ottenuto %s%s", FORWARD, previsto));

        sorgente = " /Users.gac.Documents.IdeaProjects.operativi.vaadflow14.src.main.java.it.algos.vaadflow14.wizard ";
        previsto = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow14/src/main/java/it/algos/vaadflow14/wizard";

        ottenuto = service.pointToSlash(sorgente);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s%s", FORWARD, sorgente));
        System.out.println(String.format("Testo ottenuto %s%s", FORWARD, previsto));
    }

    @Test
    @Order(12)
    @DisplayName("12 - Formattazione di un numero")
    public void format() {
        System.out.println("12 - Formattazione di un numero intero");

        sorgenteIntero = 4;
        ottenuto = service.format(sorgenteIntero);
        System.out.println(String.format("%s%s%s", sorgenteIntero, FORWARD, ottenuto));

        sorgenteIntero = 857;
        ottenuto = service.format(sorgenteIntero);
        System.out.println(String.format("%s%s%s", sorgenteIntero, FORWARD, ottenuto));

        sorgenteIntero = 1534;
        ottenuto = service.format(sorgenteIntero);
        System.out.println(String.format("%s%s%s", sorgenteIntero, FORWARD, ottenuto));

        sorgenteIntero = 1974350;
        ottenuto = service.format(sorgenteIntero);
        System.out.println(String.format("%s%s%s", sorgenteIntero, FORWARD, ottenuto));

        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("12 - Formattazione di un numero partendo da una stringa di testo");

        sorgente = "4";
        ottenuto = service.format(sorgente);
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));

        sorgente = "857";
        ottenuto = service.format(sorgente);
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));

        sorgente = "1534";
        ottenuto = service.format(sorgente);
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));

        sorgente = "1974350";
        ottenuto = service.format(sorgente);
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
    }


    @Test
    @Order(13)
    @DisplayName("13 - Numero giustificato a 2/3 cifre")
    public void format2() {
        System.out.println("13 - Formattazione di un numero giustificato a 2 cifre");

        sorgenteIntero = 4;
        ottenuto = service.format2(sorgenteIntero);
        System.out.println(String.format("%s%s%s", sorgenteIntero, FORWARD, ottenuto));

        sorgenteIntero = 35;
        ottenuto = service.format2(sorgenteIntero);
        System.out.println(String.format("%s%s%s", sorgenteIntero, FORWARD, ottenuto));

        sorgenteIntero = 820;
        ottenuto = service.format2(sorgenteIntero);
        System.out.println(String.format("%s%s%s", sorgenteIntero, FORWARD, ottenuto));

        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("13 - Formattazione di un numero giustificato a 3 cifre");

        sorgenteIntero = 4;
        ottenuto = service.format3(sorgenteIntero);
        System.out.println(String.format("%s%s%s", sorgenteIntero, FORWARD, ottenuto));

        sorgenteIntero = 35;
        ottenuto = service.format3(sorgenteIntero);
        System.out.println(String.format("%s%s%s", sorgenteIntero, FORWARD, ottenuto));

        sorgenteIntero = 820;
        ottenuto = service.format3(sorgenteIntero);
        System.out.println(String.format("%s%s%s", sorgenteIntero, FORWARD, ottenuto));
    }

    @Test
    @Order(14)
    @DisplayName("14 - Elimina dal testo il tagFinale")
    public void levaCoda() {
        System.out.println("14 - Elimina dal testo il tagFinale");
        System.out.println("Rimangono eventuali spazi vuoti in testa alla stringa originaria");
        System.out.println("Vengono rimossi eventuali spazi vuoti in coda alla stringa elaborata");

        sorgente = "Non Levare questa fine ";
        sorgente2 = VUOTA;
        previsto = "Non Levare questa fine ";
        ottenuto = service.levaCoda(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s[%s]", FORWARD, sorgente));
        System.out.println(String.format("Da levare %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Testo ottenuto %s[%s]", FORWARD, ottenuto));

        sorgente = " Levare questa fine Non ";
        sorgente2 = "Non";
        previsto = " Levare questa fine";
        ottenuto = service.levaCoda(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s[%s]", FORWARD, sorgente));
        System.out.println(String.format("Da levare %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Testo ottenuto %s[%s]", FORWARD, ottenuto));

        sorgente = " Non Levare questa fine ";
        sorgente2 = " fine ";
        previsto = " Non Levare questa";
        ottenuto = service.levaCoda(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s[%s]", FORWARD, sorgente));
        System.out.println(String.format("Da levare %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Testo ottenuto %s[%s]", FORWARD, ottenuto));

        sorgente = " Levare questa fine Non ";
        sorgente2 = "NonEsisteQuestoTag";
        previsto = " Levare questa fine Non ";
        ottenuto = service.levaCoda(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s[%s]", FORWARD, sorgente));
        System.out.println(String.format("Da levare %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Testo ottenuto %s[%s]", FORWARD, ottenuto));
    }

    @Test
    @Order(15)
    @DisplayName("15 - Elimina il testo dopo tagInterrompi")
    public void levaCodaDa() {
        System.out.println("15 - Elimina il testo dopo tagInterrompi");
        System.out.println("Rimangono eventuali spazi vuoti in testa alla stringa originaria");
        System.out.println("Vengono rimossi eventuali spazi vuoti in coda alla stringa elaborata");

        sorgente = " Levare questa fine Non ";
        sorgente2 = VUOTA;
        previsto = " Levare questa fine Non ";
        ottenuto = service.levaCodaDa(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s[%s]", FORWARD, sorgente));
        System.out.println(String.format("TagInterrompi %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Testo ottenuto %s[%s]", FORWARD, ottenuto));

        sorgente = " Levare questa fine Non ancora altro testo ";
        sorgente2 = "Non";
        previsto = " Levare questa fine";
        ottenuto = service.levaCodaDa(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s[%s]", FORWARD, sorgente));
        System.out.println(String.format("TagInterrompi %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Testo ottenuto %s[%s]", FORWARD, ottenuto));

        sorgente = " Levare questa fine Non ancora altro testo ";
        sorgente2 = "non";
        previsto = " Levare questa fine Non ancora altro testo ";
        ottenuto = service.levaCodaDa(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s[%s]", FORWARD, sorgente));
        System.out.println(String.format("TagInterrompi %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Testo ottenuto %s[%s]", FORWARD, ottenuto));

        sorgente = " Levare <re>questa<ref> fine Non ancora altro testo</ref>";
        sorgente2 = "<ref";
        previsto = " Levare <re>questa";
        ottenuto = service.levaCodaDa(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s[%s]", FORWARD, sorgente));
        System.out.println(String.format("TagInterrompi %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Testo ottenuto %s[%s]", FORWARD, ottenuto));

        sorgente = " Levare questa<ref> fine Non ancora altro testo</ref>";
        sorgente2 = "<ref ";
        previsto = " Levare questa<ref> fine Non ancora altro testo</ref>";
        ottenuto = service.levaCodaDa(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(String.format("Testo originale %s[%s]", FORWARD, sorgente));
        System.out.println(String.format("TagInterrompi %s%s", FORWARD, sorgente2));
        System.out.println(String.format("Testo ottenuto %s[%s]", FORWARD, ottenuto));
    }

    @Test
    @Order(16)
    @DisplayName("16 - Elimina parentesi quadre")
    public void setNoQuadre() {
        System.out.println("16 - Elimina (eventuali) parentesi quadre singole in testa e coda della stringa");
        System.out.println("Funziona per TUTTE le quadre che sono esattamente in TESTA o in CODA alla stringa");
        System.out.println(VUOTA);

        //--sorgente
        //--previsto
        QUADRE().forEach(this::printNoQuadre);
    }


    void printNoQuadre(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previsto = (String) mat[1];
        ottenuto = service.setNoQuadre(sorgente);
        assertEquals(previsto, ottenuto);
        print(sorgente, ottenuto);
    }

    @Test
    @Order(17)
    @DisplayName("17 - Allunga un testo alla lunghezza desiderata")
    public void rightPad() {
        System.out.println("17 - Allunga un testo alla lunghezza desiderata");
        System.out.println("Se è più corto, aggiunge spazi vuoti");
        System.out.println("Se è più lungo, rimane inalterato");

        System.out.println(VUOTA);

        //--sorgente
        //--larghezza
        //--previsto
        PAD().forEach(this::printRightPad);
    }

    void printRightPad(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];
        previsto = (String) mat[2];
        ottenuto = service.rightPad(sorgente, previstoIntero);
        ottenutoIntero = ottenuto.length();
        previstoIntero = Math.max(previstoIntero, ottenutoIntero);
        assertEquals(previsto, ottenuto);
        assertEquals(previstoIntero, ottenutoIntero);
        sorgente = String.format("%s%s%d%s", sorgente, " (", sorgenteIntero, PARENTESI_TONDA_END);
        ottenuto = String.format("%s%d%s%s%s%s", PARENTESI_TONDA_INI, ottenutoIntero, ") ", QUADRA_INI, ottenuto, QUADRA_END);
        print(sorgente, ottenuto);
    }

    @Test
    @Order(18)
    @DisplayName("18 - Forza un testo alla lunghezza desiderata")
    public void fixSize() {
        System.out.println("18 - Forza un testo alla lunghezza desiderata");
        System.out.println("Se è più corto, aggiunge spazi vuoti");
        System.out.println("Se è più lungo, lo tronca");
        System.out.println(VUOTA);

        //--sorgente
        //--larghezza
        //--previsto
        SIZE().forEach(this::printFixSize);
    }

    void printFixSize(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];
        previsto = (String) mat[2];
        ottenuto = service.fixSize(sorgente, previstoIntero);
        ottenutoIntero = ottenuto.length();
        assertEquals(previsto, ottenuto);
        assertEquals(previstoIntero, ottenutoIntero);
        sorgenteIntero = sorgente != null ? sorgente.length() : 0;
        sorgente = String.format("%s%s%d%s", sorgente, " (", sorgenteIntero, PARENTESI_TONDA_END);
        ottenuto = String.format("%s%d%s%s%s%s", PARENTESI_TONDA_INI, ottenutoIntero, ") ", QUADRA_INI, ottenuto, QUADRA_END);
        print(sorgente, ottenuto);
    }

    @Test
    @Order(19)
    @DisplayName("19 - Forza la lunghezza più le quadre")
    public void fixSizeQuadre() {
        System.out.println("19 - Forza un testo alla lunghezza desiderata e aggiunge singole parentesi quadre in testa e coda");
        System.out.println("Se arriva una testo vuota, restituisce un testo vuota con singole parentesi quadre aggiunte");
        System.out.println(VUOTA);

        //--sorgente
        //--larghezza
        //--previsto
        SIZE_QUADRE().forEach(this::printSizeQuadre);
    }

    void printSizeQuadre(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];
        previsto = (String) mat[2];
        ottenuto = service.fixSizeQuadre(sorgente, previstoIntero);
        ottenutoIntero = ottenuto.length();
        previstoIntero = previstoIntero > 0 ? previstoIntero + 2 : 0;
        assertEquals(previsto, ottenuto);
        assertEquals(previstoIntero, ottenutoIntero);
        sorgenteIntero = sorgente != null ? sorgente.length() : 0;
        sorgente = String.format("%s%s%d%s", sorgente, " (", sorgenteIntero, PARENTESI_TONDA_END);
        ottenuto = String.format("%s%d%s%s", PARENTESI_TONDA_INI, ottenutoIntero, ") ", ottenuto);
        print(sorgente, ottenuto);
    }


    @Test
    @Order(20)
    @DisplayName("20 - Leva un testo iniziale")
    void levaTesta() {
        sorgente = "Non Levare questo inizio ";
        sorgente2 = "Non";
        previsto = "Levare questo inizio";
        ottenuto = service.levaTesta(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);

        sorgente = "Non Levare questo inizio ";
        sorgente2 = "";
        previsto = "Non Levare questo inizio";
        ottenuto = service.levaTesta(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);

        sorgente = "Non Levare questo inizio ";
        sorgente2 = "NonEsisteQuestoTag";
        previsto = "Non Levare questo inizio";
        ottenuto = service.levaTesta(sorgente, sorgente2);
        assertEquals(previsto, ottenuto);
    }

    @Test
    @Order(21)
    @DisplayName("21 - levaTestoPrimaDi")
    public void levaTestoPrimaDi() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadflow14";
        sorgente2 = "IdeaProjects";
        previsto = "/operativi/vaadflow14";

        ottenuto = service.levaTestoPrimaDi(sorgente, sorgente2);
        assertNotNull(ottenuto);
        assertEquals(previsto, ottenuto);

        //        ottenuto = text.levaTesto(sorgente, sorgente2);
        //        assertNotNull(ottenuto);
        //        assertEquals(previsto, ottenuto);
    }

    protected void print(final String sorgente, final String ottenuto) {
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
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