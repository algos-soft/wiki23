package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.packages.crono.giorno.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.packages.bio.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 27-May-2022
 * Time: 18:44
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("production")
@Tag("backend")
@DisplayName("Bio Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BioBackendGiorniAnniTest extends WikiTest {

    /**
     * Classe principale di riferimento <br>
     */
    private BioBackend backend;

    @Autowired
    private BioRepository repository;


    private List<Bio> listaBeans;

    private Giorno giorno;

    //--giorno progressivo nato
    //--count previsto
    protected static Stream<Arguments> GIORNO_NATO_ORD() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(-17, 0),
                Arguments.of(2843, 0),
                Arguments.of(283, 980),
                Arguments.of(60, 294)
        );
    }

    //--nome giorno nato
    //--count previsto
    protected static Stream<Arguments> GIORNO_NATO() {
        return Stream.of(
                Arguments.of(null, 0),
                Arguments.of(VUOTA, 0),
                Arguments.of("5 termidoro", 0),
                Arguments.of("aprile", 0),
                Arguments.of("9 ottobre", 980),
                Arguments.of("29 febbraio", 294)
        );
    }

    //--nome giorno
    //--nome secolo
    //--count previsto nato
    //--count previsto morto
    protected static Stream<Arguments> GIORNO_SECOLO() {
        return Stream.of(
                Arguments.of(null, null, 0, 0),
                Arguments.of(VUOTA, VUOTA, 0, 0),
                Arguments.of(null, VUOTA, 0, 0),
                Arguments.of(VUOTA, null, 0, 0),
                Arguments.of(null, "XX secolo", 0, 0),
                Arguments.of(VUOTA, "XIX secolo", 0, 0),
                Arguments.of("16 ottobre", null, 0, 0),
                Arguments.of("18 settembre", VUOTA, 0, 0),
                Arguments.of("1918", "XVI secolo", 0, 0),
                Arguments.of("18 settembre", TAG_NO_SECOLO, 0, 0),
                Arguments.of("7 maggio", TAG_LISTA_NO_ANNO, 1, 2),
                Arguments.of("18 settembre", "XVI secolo", 9, 12),
                Arguments.of("10 gennaio", "XVI secolo", 5, 10),
                Arguments.of("1" + MASCULINE_ORDINAL_INDICATOR + SPAZIO + "gennaio", "XX secolo", 1599, 282),
                Arguments.of("1" + DEGREE_SIGN + SPAZIO + "gennaio", "XVIII secolo", 59, 32)
        );
    }


    //--giorno morto
    //--count previsto
    protected static Stream<Arguments> GIORNO_MORTO_ORD() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(-17, 0),
                Arguments.of(2843, 0),
                Arguments.of(283, 444),
                Arguments.of(60, 119)
        );
    }

    //--nome giorno morto
    //--count previsto
    protected static Stream<Arguments> GIORNO_MORTO() {
        return Stream.of(
                Arguments.of(null, 0),
                Arguments.of(VUOTA, 0),
                Arguments.of("5 termidoro", 0),
                Arguments.of("aprile", 0),
                Arguments.of("9 ottobre", 444),
                Arguments.of("29 febbraio", 119)
        );
    }

    //--nome anno nato
    //--count previsto
    protected static Stream<Arguments> ANNO_NATO() {
        return Stream.of(
                Arguments.of(null, 0),
                Arguments.of(VUOTA, 0),
                Arguments.of("3825", 0),
                Arguments.of("4 luglio", 0),
                Arguments.of("70 A.C.", 0),
                Arguments.of("70 a.C.", 4),
                Arguments.of("1645", 76)
        );
    }

    //--nome anno morto
    //--count previsto
    protected static Stream<Arguments> ANNO_MORTO() {
        return Stream.of(
                Arguments.of(null, 0),
                Arguments.of(VUOTA, 0),
                Arguments.of("3825", 0),
                Arguments.of("4 luglio", 0),
                Arguments.of("70 A.C.", 0),
                Arguments.of("70 a.C.", 1),
                Arguments.of("1645", 85),
                Arguments.of("54 a.C.", 9)
        );
    }

    //--nome anno
    //--nome mese
    //--count previsto nato
    //--count previsto morto
    protected static Stream<Arguments> ANNO_MESE() {
        return Stream.of(
                Arguments.of(null, null, 0, 0),
                Arguments.of(VUOTA, VUOTA, 0, 0),
                Arguments.of(null, VUOTA, 0, 0),
                Arguments.of(VUOTA, null, 0, 0),
                Arguments.of(null, "aprile", 0, 0),
                Arguments.of(VUOTA, "settembre", 0, 0),
                Arguments.of("1918", null, 0, 0),
                Arguments.of("377", VUOTA, 0, 0),
                Arguments.of("3 gennaio", "aprile", 0, 0),
                Arguments.of("1481", TAG_NO_MESE, 0, 0),
                Arguments.of("1968", TAG_LISTA_NO_GIORNO, 212, 87),
                Arguments.of("1735", "giugno", 10, 3),
                Arguments.of("54 a.C.", "febbraio", 0, 0)
        );
    }

    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.reflectionService = reflectionService;
        backend.textService = textService;
    }


    /**
     * Inizializzazione dei service <br>
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void initMocks() {
        super.initMocks();
    }


    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
        super.fixRiferimentiIncrociati();
        backend = bioBackend;
        backend.mongoService = mongoService;
    }

    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        this.listaBeans = null;
    }

    @Test
    @Order(20)
    @DisplayName("----------")
    void test20() {
    }

    @Test
    @Order(21)
    @DisplayName("21 - countGiornoNatoOrd")
    void countGiornoNatoOrd() {
        System.out.println("21 - countGiornoNatoOrd");
        System.out.println(VUOTA);
        GIORNO_NATO_ORD().forEach(this::countGiornoNatoOrdBase);
    }

    //--giorno progressivo nato
    //--count previsto
    void countGiornoNatoOrdBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgenteIntero = (int) mat[0];
        previstoIntero = (int) mat[1];

        ottenutoIntero = backend.countGiornoNato(sorgenteIntero);
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di nati nel giorno (intero) '%s' in totale", ottenutoIntero, sorgenteIntero);
        giorno = giornoBackend.findByOrdine(sorgenteIntero);
        if (giorno != null) {
            System.out.print(message);
            System.out.print(FORWARD);
            message = String.format("Corrisponde al giorno (text) %s", giorno.nome);
            System.out.println(message);
        }
        else {
            System.out.println(message);
        }
    }

    @Test
    @Order(22)
    @DisplayName("22 - countGiornoNato")
    void countGiornoNato() {
        System.out.println("22 - countGiornoNato");
        System.out.println(VUOTA);
        GIORNO_NATO().forEach(this::countGiornoNatoBase);
    }

    //--nome giorno nato
    //--count previsto
    void countGiornoNatoBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];

        ottenutoIntero = backend.countGiornoNato(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di nati nel giorno '%s' in totale", ottenutoIntero, sorgente);
        System.out.println(message);
    }


    @Test
    @Order(23)
    @DisplayName("23 - findGiornoNato")
    void findGiornoNato() {
        System.out.println("23 - findGiornoNato");
        System.out.println(VUOTA);
        GIORNO_NATO().forEach(this::findGiornoNatoBase);
    }

    //--nome giorno nato
    //--count previsto
    void findGiornoNatoBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];

        listaBeans = backend.findGiornoNato(sorgente);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di nati nel giorno '%s' in totale", ottenutoIntero, sorgente);
        System.out.println(message);
    }


    @Test
    @Order(24)
    @DisplayName("24 - countGiornoNatoSecolo")
    void countGiornoNatoSecolo() {
        System.out.println("24 - countGiornoNatoSecolo");
        System.out.println(VUOTA);
        GIORNO_SECOLO().forEach(this::countGiornoNatoSecoloBase);
    }

    //--nome giorno
    //--nome secolo
    //--count previsto nato
    //--count previsto morto
    void countGiornoNatoSecoloBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        sorgente2 = (String) mat[1];
        previstoIntero = (int) mat[2];

        ottenutoIntero = backend.countGiornoNatoSecolo(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = textService.isValid(sorgente) ? sorgente : "(null)";
        sorgente2 = textService.isValid(sorgente2) ? sorgente2 : "(null)";
        message = String.format("Ci sono %d voci biografiche di nati nel giorno '%s' per il secolo '%s'", ottenutoIntero, sorgente, sorgente2);
        System.out.println(message);
    }


    @Test
    @Order(25)
    @DisplayName("25 - findGiornoNatoSecolo")
    void findGiornoNatoSecolo() {
        System.out.println("25 - findGiornoNatoSecolo");
        System.out.println(VUOTA);
        GIORNO_SECOLO().forEach(this::findGiornoNatoSecoloBase);
    }

    //--nome giorno
    //--nome secolo
    //--count previsto nato
    //--count previsto morto
    void findGiornoNatoSecoloBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        sorgente2 = (String) mat[1];
        previstoIntero = (int) mat[2];

        listaBeans = backend.findGiornoNatoSecolo(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di nati nel giorno '%s' per il secolo '%s'", ottenutoIntero, sorgente, sorgente2);
        System.out.println(message);
    }

    @Test
    @Order(30)
    @DisplayName("----------")
    void test30() {
    }

    @Test
    @Order(31)
    @DisplayName("31 - countGiornoMortoOrd")
    void countGiornoMortoOrd() {
        System.out.println("31 - countGiornoMortoOrd");
        System.out.println(VUOTA);
        GIORNO_MORTO_ORD().forEach(this::countGiornoMortoOrdBase);
    }

    //--giorno progressivo morto
    //--count previsto
    void countGiornoMortoOrdBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgenteIntero = (int) mat[0];
        previstoIntero = (int) mat[1];

        ottenutoIntero = backend.countGiornoMorto(sorgenteIntero);
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di morti nel giorno (intero) '%s' in totale", ottenutoIntero, sorgenteIntero);
        giorno = giornoBackend.findByOrdine(sorgenteIntero);
        if (giorno != null) {
            System.out.print(message);
            System.out.print(FORWARD);
            message = String.format("Corrisponde al giorno (text) %s", giorno.nome);
            System.out.println(message);
        }
        else {
            System.out.println(message);
        }
    }


    @Test
    @Order(32)
    @DisplayName("32 - countGiornoMorto")
    void countGiornoMorto() {
        System.out.println("32 - countGiornoMorto");
        System.out.println(VUOTA);
        GIORNO_MORTO().forEach(this::countGiornoMortoBase);
    }

    //--nome giorno morto
    //--count previsto
    void countGiornoMortoBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];

        ottenutoIntero = backend.countGiornoMorto(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di morti nel giorno '%s' in totale", ottenutoIntero, sorgente);
        System.out.println(message);
    }


    @Test
    @Order(33)
    @DisplayName("33 - findGiornoMorto")
    void findGiornoMorto() {
        System.out.println("33 - findGiornoMorto");
        System.out.println(VUOTA);
        GIORNO_MORTO().forEach(this::findGiornoMortoBase);
    }

    //--nome giorno nato
    //--count previsto
    void findGiornoMortoBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];

        listaBeans = backend.findGiornoMorto(sorgente);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di morti nel giorno '%s' in totale", ottenutoIntero, sorgente);
        System.out.println(message);
    }


    @Test
    @Order(34)
    @DisplayName("34 - countGiornoMortoSecolo")
    void countGiornoMortoSecolo() {
        System.out.println("34 - countGiornoMortoSecolo");
        System.out.println(VUOTA);
        GIORNO_SECOLO().forEach(this::countGiornoMortoSecoloBase);
    }

    //--nome giorno
    //--nome secolo
    //--count previsto nato
    //--count previsto morto
    void countGiornoMortoSecoloBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        sorgente2 = (String) mat[1];
        previstoIntero = (int) mat[3];

        ottenutoIntero = backend.countGiornoMortoSecolo(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = textService.isValid(sorgente) ? sorgente : "(null)";
        sorgente2 = textService.isValid(sorgente2) ? sorgente2 : "(null)";
        message = String.format("Ci sono %d voci biografiche di morti nel giorno '%s' per il secolo '%s'", ottenutoIntero, sorgente, sorgente2);
        System.out.println(message);
    }


    @Test
    @Order(35)
    @DisplayName("35 - findGiornoMortoSecolo")
    void findGiornoMortoSecolo() {
        System.out.println("35 - findGiornoMortoSecolo");
        System.out.println(VUOTA);
        GIORNO_SECOLO().forEach(this::findGiornoMortoSecoloBase);
    }

    //--nome giorno
    //--nome secolo
    //--count previsto nato
    //--count previsto morto
    void findGiornoMortoSecoloBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        sorgente2 = (String) mat[1];
        previstoIntero = (int) mat[3];

        listaBeans = backend.findGiornoMortoSecolo(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di morti nel giorno '%s' per il secolo '%s'", ottenutoIntero, sorgente, sorgente2);
        System.out.println(message);
    }


    @Test
    @Order(40)
    @DisplayName("----------")
    void test40() {
    }


    @Test
    @Order(42)
    @DisplayName("42 - countAnnoNato")
    void countAnnoNato() {
        System.out.println("42 - countAnnoNato");
        System.out.println(VUOTA);
        ANNO_NATO().forEach(this::countAnnoNatoBase);
    }

    //--nome anno nato
    //--count previsto
    void countAnnoNatoBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];

        ottenutoIntero = backend.countAnnoNato(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di nati nell'anno '%s' in totale", ottenutoIntero, sorgente);
        System.out.println(message);
    }


    @Test
    @Order(43)
    @DisplayName("43 - findAnnoNato")
    void findAnnoNato() {
        System.out.println("43 - findAnnoNato");
        System.out.println(VUOTA);
        ANNO_NATO().forEach(this::findAnnoNatoBase);
    }

    //--nome anno nato
    //--count previsto
    void findAnnoNatoBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];

        listaBeans = backend.findAnnoNato(sorgente);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di nati nell'anno '%s' in totale", ottenutoIntero, sorgente);
        System.out.println(message);
    }


    @Test
    @Order(44)
    @DisplayName("44 - countAnnoNatoMese")
    void countAnnoNatoMese() {
        System.out.println("44 - countAnnoNatoMese");
        System.out.println(VUOTA);
        ANNO_MESE().forEach(this::countAnnoNatoMeseBase);
    }

    //--nome anno
    //--nome mese
    //--count previsto nato
    //--count previsto morto
    void countAnnoNatoMeseBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        sorgente2 = (String) mat[1];
        previstoIntero = (int) mat[2];

        ottenutoIntero = backend.countAnnoNatoMese(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = textService.isValid(sorgente) ? sorgente : "(null)";
        sorgente2 = textService.isValid(sorgente2) ? sorgente2 : "(null)";
        message = String.format("Ci sono %d voci biografiche di nati nell'anno '%s' per il mese '%s'", ottenutoIntero, sorgente, sorgente2);
        System.out.println(message);
    }

    @Test
    @Order(45)
    @DisplayName("45 - findAnnoNatoMese")
    void findAnnoNatoMese() {
        System.out.println("45 - findAnnoNatoMese");
        System.out.println(VUOTA);
        ANNO_MESE().forEach(this::findAnnoNatoMeseBase);
    }

    //--nome anno
    //--nome mese
    //--count previsto nato
    //--count previsto morto
    void findAnnoNatoMeseBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        sorgente2 = (String) mat[1];
        previstoIntero = (int) mat[2];

        listaBeans = backend.findAnnoNatoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di nati nell'anno '%s' per il mese '%s'", ottenutoIntero, sorgente, sorgente2);
        System.out.println(message);
    }

    @Test
    @Order(50)
    @DisplayName("----------")
    void test50() {
    }


    @Test
    @Order(52)
    @DisplayName("52 - countAnnoMorto")
    void countAnnoMorto() {
        System.out.println("52 - countAnnoMorto");
        System.out.println(VUOTA);
        ANNO_MORTO().forEach(this::countAnnoMortoBase);
    }

    //--nome giorno nato
    //--count previsto
    void countAnnoMortoBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];

        ottenutoIntero = backend.countAnnoMorto(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di morti nell'anno '%s' in totale", ottenutoIntero, sorgente);
        System.out.println(message);
    }

    @Test
    @Order(53)
    @DisplayName("53 - findAnnoMorto")
    void findAnnoMorto() {
        System.out.println("53 - findAnnoMorto");
        System.out.println(VUOTA);
        ANNO_MORTO().forEach(this::findAnnoMortoBase);
    }

    //--nome anno morto
    //--count previsto
    void findAnnoMortoBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        previstoIntero = (int) mat[1];

        listaBeans = backend.findAnnoMorto(sorgente);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di morti nell'anno '%s' in totale", ottenutoIntero, sorgente);
        System.out.println(message);
    }


    @Test
    @Order(54)
    @DisplayName("54 - countAnnoMortoMese")
    void countAnnoMortoMese() {
        System.out.println("54 - countAnnoMortoMese");
        System.out.println(VUOTA);
        ANNO_MESE().forEach(this::countAnnoMortoMeseBase);
    }

    //--nome anno
    //--nome mese
    //--count previsto nato
    //--count previsto morto
    void countAnnoMortoMeseBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        sorgente2 = (String) mat[1];
        previstoIntero = (int) mat[3];

        ottenutoIntero = backend.countAnnoMortoMese(sorgente, sorgente2);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = textService.isValid(sorgente) ? sorgente : "(null)";
        sorgente2 = textService.isValid(sorgente2) ? sorgente2 : "(null)";
        message = String.format("Ci sono %d voci biografiche di morti nell'anno '%s' per il mese '%s'", ottenutoIntero, sorgente, sorgente2);
        System.out.println(message);
    }

    @Test
    @Order(55)
    @DisplayName("55 - findAnnoMortoMese")
    void findAnnoMortoMese() {
        System.out.println("55 - findAnnoMortoMese");
        System.out.println(VUOTA);
        ANNO_MESE().forEach(this::findAnnoMortoMeseBase);
    }

    //--nome anno
    //--nome mese
    //--count previsto nato
    //--count previsto morto
    void findAnnoMortoMeseBase(Arguments arg) {
        Object[] mat = arg.get();
        sorgente = (String) mat[0];
        sorgente2 = (String) mat[1];
        previstoIntero = (int) mat[3];

        listaBeans = backend.findAnnoMortoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);

        message = String.format("Ci sono %d voci biografiche di morti nell'anno '%s' per il mese '%s'", ottenutoIntero, sorgente, sorgente2);
        System.out.println(message);
    }


    @Test
    @Order(60)
    @DisplayName("----------")
    void test60() {
    }


    void printDieci(List<Bio> lista) {
        for (Bio bio : lista.subList(0, 10)) {
            System.out.println(bio.wikiTitle);
        }
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

    void printBeans(List<Bio> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (Bio bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(bean);
        }
    }

}
