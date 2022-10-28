package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.packages.bio.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;

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
    @Order(21)
    @DisplayName("21 - countGiornoNato")
    void countGiornoNato() {
        System.out.println("21 - countGiornoNato");

        sorgente = null;
        ottenutoIntero = backend.countGiornoNato(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = VUOTA;
        ottenutoIntero = backend.countGiornoNato(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "5 termidoro";
        ottenutoIntero = backend.countGiornoNato(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "aprile";
        ottenutoIntero = backend.countGiornoNato(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "18 settembre";
        previstoIntero = 1060;
        ottenutoIntero = backend.countGiornoNato(sorgente);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "29 febbraio";
        previstoIntero = 294;
        ottenutoIntero = backend.countGiornoNato(sorgente);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));
    }


    @Test
    @Order(22)
    @DisplayName("22 - findGiornoNato")
    void findGiornoNato() {
        System.out.println("22 - findGiornoNato");

        sorgente = null;
        previstoIntero = 0;
        listaBeans = backend.findGiornoNato(sorgente);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = VUOTA;
        previstoIntero = 0;
        listaBeans = backend.findGiornoNato(sorgente);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "5 termidoro";
        previstoIntero = 0;
        listaBeans = backend.findGiornoNato(sorgente);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "aprile";
        previstoIntero = 0;
        listaBeans = backend.findGiornoNato(sorgente);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "18 settembre";
        previstoIntero = 1060;
        listaBeans = backend.findGiornoNato(sorgente);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "29 febbraio";
        previstoIntero = 294;
        listaBeans = backend.findGiornoNato(sorgente);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));
    }


    @Test
    @Order(23)
    @DisplayName("23 - countGiornoNatoSecolo")
    void countGiornoNatoSecolo() {
        System.out.println("23 - countGiornoNatoSecolo");
        String giusto = MASCULINE_ORDINAL_INDICATOR;
        String sbagliato = DEGREE_SIGN;

        sorgente = "1918";
        sorgente2 = "XVI secolo";
        ottenutoIntero = backend.countGiornoNatoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero == 0);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "18 settembre";
        sorgente2 = VUOTA;
        previstoIntero = 1;
        ottenutoIntero = backend.countGiornoNatoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "18 settembre";
        sorgente2 = TAG_LISTA_NO_ANNO;
        previstoIntero = 1;
        ottenutoIntero = backend.countGiornoNatoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "18 settembre";
        sorgente2 = "XVI secolo";
        previstoIntero = 9;
        ottenutoIntero = backend.countGiornoNatoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1" + giusto + SPAZIO + "gennaio"; // MASCULINE_ORDINAL_INDICATOR - giusto
        sorgente2 = "XX secolo";
        previstoIntero = 1519;
        ottenutoIntero = backend.countGiornoNatoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1" + sbagliato + SPAZIO + "gennaio"; // DEGREE_SIGN - sbagliato
        sorgente2 = "XVIII secolo";
        previstoIntero = 58;
        ottenutoIntero = backend.countGiornoNatoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));
    }

    //    @Test
    @Order(22)
    @DisplayName("22 - countGiornoMorto")
    void countGiornoMorto() {
        System.out.println("22 - countGiornoMorto");

        sorgente = null;
        ottenutoIntero = backend.countGiornoMorto(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = VUOTA;
        ottenutoIntero = backend.countGiornoMorto(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "5 termidoro";
        ottenutoIntero = backend.countGiornoMorto(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "aprile";
        ottenutoIntero = backend.countGiornoMorto(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "31 dicembre";
        previstoIntero = 515;
        ottenutoIntero = backend.countGiornoMorto(sorgente);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nel giorno %s in totale", ottenutoIntero, sorgente));

        sorgente = "29 febbraio";
        previstoIntero = 118;
        ottenutoIntero = backend.countGiornoMorto(sorgente);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nel giorno %s in totale", ottenutoIntero, sorgente));
    }

//    @Test
    @Order(31)
    @DisplayName("31 - countAnnoNato")
    void countAllAnnoNato() {
        System.out.println("31 - countAnnoNato");

        sorgente = null;
        ottenutoIntero = backend.countAnnoNato(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = VUOTA;
        ottenutoIntero = backend.countAnnoNato(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "3825";
        ottenutoIntero = backend.countAnnoNato(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "4 luglio";
        ottenutoIntero = backend.countAnnoNato(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "70 A.C.";
        ottenutoIntero = backend.countAnnoNato(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "70 a.C.";
        previstoIntero = 4;
        ottenutoIntero = backend.countAnnoNato(sorgente);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "1645";
        previstoIntero = 76;
        ottenutoIntero = backend.countAnnoNato(sorgente);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));
    }


//    @Test
    @Order(32)
    @DisplayName("32 - countAnnoMorto")
    void countAnnoMorto() {
        System.out.println("32 - countAnnoMorto");

        sorgente = null;
        ottenutoIntero = backend.countAnnoMorto(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = VUOTA;
        ottenutoIntero = backend.countAnnoMorto(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "3825";
        ottenutoIntero = backend.countAnnoMorto(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "4 luglio";
        ottenutoIntero = backend.countAnnoMorto(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "70 A.C.";
        ottenutoIntero = backend.countAnnoMorto(sorgente);
        assertTrue(ottenutoIntero == 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "1645";
        previstoIntero = 85;
        ottenutoIntero = backend.countAnnoMorto(sorgente);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "54 a.C.";
        previstoIntero = 9;
        ottenutoIntero = backend.countAnnoMorto(sorgente);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nell'anno %s in totale", ottenutoIntero, sorgente));
    }




//    @Test
    @Order(42)
    @DisplayName("42 - countGiornoMortoSecolo")
    void countGiornoMortoSecolo() {
        System.out.println("42 - countGiornoMortoSecolo");
        String giusto = MASCULINE_ORDINAL_INDICATOR;
        String sbagliato = DEGREE_SIGN;

        sorgente = "2017";
        sorgente2 = "XX secolo";
        ottenutoIntero = backend.countGiornoNatoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero == 0);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "18 settembre";
        sorgente2 = VUOTA;
        previstoIntero = 2;
        ottenutoIntero = backend.countGiornoMortoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "18 settembre";
        sorgente2 = TAG_LISTA_NO_ANNO;
        previstoIntero = 2;
        ottenutoIntero = backend.countGiornoMortoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "18 settembre";
        sorgente2 = "XIV secolo";
        previstoIntero = 4;
        ottenutoIntero = backend.countGiornoMortoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1" + giusto + SPAZIO + "gennaio"; // MASCULINE_ORDINAL_INDICATOR - giusto
        sorgente2 = "XV secolo";
        previstoIntero = 4;
        ottenutoIntero = backend.countGiornoMortoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1" + sbagliato + SPAZIO + "gennaio"; // DEGREE_SIGN - sbagliato
        sorgente2 = "XVI secolo";
        previstoIntero = 13;
        ottenutoIntero = backend.countGiornoMortoSecolo(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nel giorno %s per il secolo %s", ottenutoIntero, sorgente, sorgente2));
    }

//    @Test
    @Order(51)
    @DisplayName("51 - countAnnoNatoMese")
    void countAnnoNatoMese() {
        System.out.println("51 - countAnnoNatoMese");

        sorgente = "1645";
        sorgente2 = VUOTA;
        previstoIntero = 32;
        ottenutoIntero = backend.countAnnoNatoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1645";
        sorgente2 = TAG_LISTA_NO_GIORNO;
        previstoIntero = 32;
        ottenutoIntero = backend.countAnnoNatoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1645";
        sorgente2 = "marzo";
        previstoIntero = 3;
        ottenutoIntero = backend.countAnnoNatoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1645";
        sorgente2 = "Marzo";
        previstoIntero = 3;
        ottenutoIntero = backend.countAnnoNatoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1912";
        previstoIntero = 1832;
        ottenutoIntero = backend.countAnnoNato(sorgente);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s in totale", ottenutoIntero, sorgente));

        sorgente = "1912";
        sorgente2 = "Agosto";
        previstoIntero = 158;
        ottenutoIntero = backend.countAnnoNatoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1912";
        sorgente2 = VUOTA;
        previstoIntero = 102;
        ottenutoIntero = backend.countAnnoNatoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1912";
        sorgente2 = TAG_LISTA_NO_GIORNO;
        previstoIntero = 102;
        ottenutoIntero = backend.countAnnoNatoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "70 a.C.";
        sorgente2 = TAG_LISTA_NO_GIORNO;
        previstoIntero = 3;
        ottenutoIntero = backend.countAnnoNatoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));
    }

//    @Test
    @Order(52)
    @DisplayName("52 - countAnnoMortoMese")
    void countAnnoMortoMese() {
        System.out.println("52 - countAnnoMortoMese");

        sorgente = "1645";
        sorgente2 = VUOTA;
        previstoIntero = 32;
        ottenutoIntero = backend.countAnnoMortoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1645";
        sorgente2 = TAG_LISTA_NO_GIORNO;
        previstoIntero = 32;
        ottenutoIntero = backend.countAnnoMortoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1645";
        sorgente2 = "marzo";
        previstoIntero = 2;
        ottenutoIntero = backend.countAnnoMortoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        sorgente = "1645";
        sorgente2 = "Marzo";
        previstoIntero = 2;
        ottenutoIntero = backend.countAnnoMortoMese(sorgente, sorgente2);
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));
    }











//    @Test
    @Order(461)
    @DisplayName("461 - findAllAnnoNatoMese")
    void findAllAnnoNatoMese() {
        System.out.println("461 - findAllAnnoNatoMese");

        sorgente = "1645";
        sorgente2 = VUOTA;
        previstoIntero = 32;
        listaBeans = backend.findAnnoNatoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertTrue(ottenutoIntero > 0);
        assertEquals(previstoIntero, ottenutoIntero);
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        System.out.println(VUOTA);
        sorgente = "1645";
        sorgente2 = "marzo";
        listaBeans = backend.findAnnoNatoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));
        System.out.println(VUOTA);
        printBio(listaBeans);

        System.out.println(VUOTA);
        sorgente = "1912";
        sorgente2 = "Agosto";
        listaBeans = backend.findAnnoNatoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        System.out.println(String.format("Ci sono %d voci biografiche di nati nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));
        System.out.println(VUOTA);
        printBio(listaBeans);

    }


//    @Test
    @Order(222)
    @DisplayName("222 - findAllAnnoMortoMese")
    void findAllAnnoMortoMese() {
        System.out.println("222 - findAllAnnoMortoMese");

        sorgente = "1645";
        sorgente2 = VUOTA;
        previstoIntero = 32;
        listaBeans = backend.findAnnoMortoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        assertTrue(ottenutoIntero > 0);
        System.out.println(String.format("Ci sono %d voci biografiche di morti nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));

        System.out.println(VUOTA);
        sorgente = "1645";
        sorgente2 = "marzo";
        listaBeans = backend.findAnnoMortoMese(sorgente, sorgente2);
        assertNotNull(listaBeans);
        ottenutoIntero = listaBeans.size();
        System.out.println(String.format("Ci sono %d voci biografiche di morti nell'anno %s per il mese di %s", ottenutoIntero, sorgente, sorgente2));
        System.out.println(VUOTA);
        printBio(listaBeans);
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
