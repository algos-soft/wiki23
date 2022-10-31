package it.algos.unit.wiki;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 28-Jun-2022
 * Time: 14:54
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Text Service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikiUtilityTest extends WikiTest {

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    @InjectMocks
    public WikiUtility service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = wikiUtility;
    }

    /**
     * Inizializzazione dei service <br>
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void initMocks() {
        assertNotNull(service);
    }

    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
        super.fixRiferimentiIncrociati();
        service.textService = textService;
    }

    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    //    @Test
    @Order(1)
    @DisplayName("1 - WikiTitle di tutte le 366 pagine giorni di nascita e morte")
    void wikiTitleGiorno() {
        System.out.println("1 - WikiTitle di tutte le 366 pagine giorni di nascita e morte");
        System.out.println(VUOTA);
        listaStr = giornoBackend.findAllNomi();
        int contNati = 0;
        int contMorti = 0;
        StringBuffer buffer = new StringBuffer();

        for (String giorno : listaStr) {
            ottenuto = service.wikiTitleNatiGiorno(giorno);
            ottenuto2 = service.wikiTitleMortiGiorno(giorno);
            System.out.println(String.format("%s%s'%s' e '%s'", giorno, FORWARD, ottenuto, ottenuto2));
            ottenutoBooleano = queryService.isEsiste(ottenuto);
            if (!ottenutoBooleano) {
                buffer.append(String.format("La pagina %s non esiste", ottenuto));
                buffer.append(CAPO);
                contNati++;
            }
            ottenutoBooleano = queryService.isEsiste(ottenuto2);
            if (!ottenutoBooleano) {
                buffer.append(String.format("La pagina %s non esiste", ottenuto2));
                buffer.append(CAPO);
                contMorti++;
            }
        }
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d pagine di nati che non esistono", contNati));
        System.out.println(String.format("Ci sono %d pagine di morti che non esistono", contMorti));
        System.out.println(VUOTA);
        System.out.println(buffer.toString());
    }


    //    @Test
    @Order(2)
    @DisplayName("2 - WikiTitle di tutte le 3030 (circa) pagine anni di nascita e morte")
    void wikiTitleAnno() {
        System.out.println("2 - WikiTitle di tutte le 3030 (circa) pagine anni di nascita e morte");
        System.out.println(VUOTA);
        listaStr = annoBackend.findAllNomi();
        int contNati = 0;
        int contMorti = 0;
        StringBuffer buffer = new StringBuffer();

        for (String anno : listaStr) {
            ottenuto = service.wikiTitleNatiAnno(anno);
            ottenuto2 = service.wikiTitleMortiAnno(anno);
            System.out.println(String.format("%s%s'%s' e '%s'", anno, FORWARD, ottenuto, ottenuto2));
            ottenutoBooleano = queryService.isEsiste(ottenuto);
            if (!ottenutoBooleano) {
                buffer.append(String.format("La pagina %s non esiste", ottenuto));
                buffer.append(CAPO);
                contNati++;
            }
            ottenutoBooleano = queryService.isEsiste(ottenuto2);
            if (!ottenutoBooleano) {
                buffer.append(String.format("La pagina %s non esiste", ottenuto2));
                buffer.append(CAPO);
                contMorti++;
            }
        }
        System.out.println(VUOTA);
        System.out.println(String.format("Ci sono %d pagine di nati che non esistono", contNati));
        System.out.println(String.format("Ci sono %d pagine di morti che non esistono", contMorti));
        System.out.println(VUOTA);
        System.out.println(buffer.toString());
    }

    //    @Test
    @Order(3)
    @DisplayName("3 - Nati nel")
    void nati() {
        for (int k = 1; k <= 20; k++) {
            ottenuto = service.wikiTitleNatiAnno(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

        System.out.println(VUOTA);
        for (int k = 75; k <= 95; k++) {
            ottenuto = service.wikiTitleNatiAnno(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

        System.out.println(VUOTA);
        for (int k = 795; k <= 820; k++) {
            ottenuto = service.wikiTitleNatiAnno(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

        System.out.println(VUOTA);
        for (int k = 895; k <= 905; k++) {
            ottenuto = service.wikiTitleNatiAnno(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }

    }

    //    @Test
    @Order(4)
    @DisplayName("4 - Morti nel")
    void morti() {
        for (int k = 1; k <= 100; k++) {
            ottenuto = service.wikiTitleMortiAnno(String.valueOf(k));
            assertTrue(textService.isValid(ottenuto));
            System.out.println(ottenuto);
        }
    }

    //    @Test
    @Order(5)
    @DisplayName("5 - Link giorno nato")
    void linkGiornoNato() {
        System.out.println("5 - Link giorno nato usato nelle didascalie (prima e dopo)");
        System.out.println(VUOTA);
        sorgente = PAGINA_OTTO;
        bio = queryService.getBio(sorgente);

        previsto = "[[Nati il 20 ottobre|20 ottobre]]";
        ottenuto = service.linkGiornoNatoTesta(bio);
        assertEquals(previsto, ottenuto);
        System.out.println("Senza icona e senza parentesi (usato prima della didascalia in anni)");
        System.out.println(String.format("%s (nato il %s)%s%s", bio.getWikiTitle(), bio.giornoNato, FORWARD, ottenuto));

        previsto = "(n.&nbsp;[[Nati il 20 ottobre|20 ottobre]])";
        ottenuto = service.linkGiornoNatoCoda(bio, true);
        assertEquals(previsto, ottenuto);
        System.out.println("Con icona e parentesi (usato in coda alle didascalie)");
        System.out.println(String.format("%s (nato il %s)%s%s", bio.getWikiTitle(), bio.giornoNato, FORWARD, ottenuto));
        System.out.println(VUOTA);
    }


    //    @Test
    @Order(6)
    @DisplayName("6 - Link giorno morto")
    void linkGiornoMorto() {
        System.out.println("6 - Link giorno morto usato nelle didascalie (prima e dopo)");
        System.out.println(VUOTA);
        sorgente = PAGINA_DIECI;
        bio = queryService.getBio(sorgente);

        previsto = "[[Morti l'8 novembre|8 novembre]]";
        ottenuto = service.linkGiornoMortoTesta(bio);
        assertEquals(previsto, ottenuto);
        System.out.println("Senza icona e senza parentesi (usato prima della didascalia in anni)");
        System.out.println(String.format("%s (morto il %s)%s%s", bio.getWikiTitle(), bio.giornoNato, FORWARD, ottenuto));

        previsto = "(†&nbsp;[[Morti l'8 novembre|8 novembre]])";
        ottenuto = service.linkGiornoMortoCoda(bio, true);
        assertEquals(previsto, ottenuto);
        System.out.println("Con icona e parentesi (usato in coda alle didascalie)");
        System.out.println(String.format("%s (morto il %s)%s%s", bio.getWikiTitle(), bio.giornoNato, FORWARD, ottenuto));
        System.out.println(VUOTA);

    }

    //    @Test
    @Order(7)
    @DisplayName("7 - Link anno nato")
    void linkAnnoNato() {
        System.out.println("7 - Link anno nato usato nelle didascalie (prima e dopo)");
        System.out.println(VUOTA);
        sorgente = PAGINA_OTTO;
        bio = queryService.getBio(sorgente);

        previsto = "[[Nati nel 1792|1792]]";
        ottenuto = service.linkAnnoNatoTesta(bio);
        assertEquals(previsto, ottenuto);
        System.out.println("Senza icona e senza parentesi (usato prima della didascalia in giorni)");
        System.out.println(String.format("%s (nato nel %s)%s%s", bio.getWikiTitle(), bio.annoNato, FORWARD, ottenuto));

        previsto = "(n.&nbsp;[[Nati nel 1792|1792]])";
        ottenuto = service.linkAnnoNatoCoda(bio, true);
        assertEquals(previsto, ottenuto);
        System.out.println("Con icona e parentesi (usato in coda alle didascalie)");
        System.out.println(String.format("%s (nato nel %s)%s%s", bio.getWikiTitle(), bio.annoNato, FORWARD, ottenuto));
        System.out.println(VUOTA);

    }


    //    @Test
    @Order(8)
    @DisplayName("8 - Link anno morto")
    void linkAnnoMorto() {
        System.out.println("8 - Link anno morto usato nelle didascalie (prima e dopo)");
        System.out.println(VUOTA);
        sorgente = PAGINA_UNDICI;
        bio = queryService.getBio(sorgente);

        previsto = "[[Morti nell'835|835]]";
        ottenuto = service.linkAnnoMortoTesta(bio);
        assertEquals(previsto, ottenuto);
        System.out.println("Senza icona e senza parentesi (usato prima della didascalia in giorni)");
        System.out.println(String.format("%s (morto nel %s)%s%s", bio.getWikiTitle(), bio.annoMorto, FORWARD, ottenuto));

        previsto = "(†&nbsp;[[Morti nell'835|835]])";
        ottenuto = service.linkAnnoMortoCoda(bio, true);
        assertEquals(previsto, ottenuto);
        System.out.println("Con icona e parentesi (usato in coda alle didascalie)");
        System.out.println(String.format("%s (morto nel %s)%s%s", bio.getWikiTitle(), bio.annoMorto, FORWARD, ottenuto));
        System.out.println(VUOTA);
    }

    @ParameterizedTest
    @MethodSource(value = "TITOLO_ANNI")
    @Order(10)
    @DisplayName("10 - Titolo anni")
    void titoloAnni(String nomeAnno, final AETypeLista type, String titolo) {
        sorgente = nomeAnno;
        previsto = titolo;

        ottenuto = switch (type) {
            case annoNascita -> service.wikiTitleNatiAnno(sorgente);
            case annoMorte -> service.wikiTitleMortiAnno(sorgente);
            default -> {yield VUOTA;}
        };
        assertEquals(previsto, ottenuto);
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
    }


    @ParameterizedTest
    @MethodSource(value = "TITOLO_GIORNI")
    @Order(11)
    @DisplayName("11 - Titolo giorni")
    void titoloGiorni(String nomeGiorno, final AETypeLista type, String titolo) {
        sorgente = nomeGiorno;
        previsto = titolo;

        ottenuto = switch (type) {
            case giornoNascita -> service.wikiTitleNatiGiorno(sorgente);
            case giornoMorte -> service.wikiTitleMortiGiorno(sorgente);
            default -> {yield VUOTA;}
        };
        assertEquals(previsto, ottenuto);
        System.out.println(String.format("%s%s%s", sorgente, FORWARD, ottenuto));
    }

    @Test
    @Order(12)
    @DisplayName("12 - primo del mese")
    void fixPrimoMese() {
        System.out.println("12 - primo del mese");
        System.out.println(VUOTA);
        char charSorgente;
        char charOttenuto;
        char charPrevisto = (char) 186;
        previsto = "1" + charPrevisto + SPAZIO + "gennaio";

        sorgente = "1 gennaio";
        ottenuto = service.fixPrimoMese(sorgente);
        charOttenuto = ottenuto.charAt(1);
        assertEquals(previsto, ottenuto);
        assertEquals(charPrevisto, charOttenuto);

        charSorgente = (char) 186;
        sorgente = "1" + charSorgente + SPAZIO + "gennaio";
        ottenuto = service.fixPrimoMese(sorgente);
        charOttenuto = ottenuto.charAt(1);
        assertEquals(previsto, ottenuto);
        assertEquals(charPrevisto, charOttenuto);

        charSorgente = (char) 176;
        sorgente = "1" + charSorgente + SPAZIO + "gennaio";
        ottenuto = service.fixPrimoMese(sorgente);
        charOttenuto = ottenuto.charAt(1);
        assertEquals(previsto, ottenuto);
        assertEquals(charPrevisto, charOttenuto);

        charSorgente = (char) 176;
        sorgente = "1" + charSorgente + SPAZIO + "dicembre";
        previsto = "1" + charPrevisto + SPAZIO + "dicembre";
        ottenuto = service.fixPrimoMese(sorgente);
        charOttenuto = ottenuto.charAt(1);
        assertEquals(previsto, ottenuto);
        assertEquals(charPrevisto, charOttenuto);

        sorgente = "11 agosto";
        previsto = "11 agosto";
        ottenuto = service.fixPrimoMese(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "18 marzo";
        previsto = "18 marzo";
        ottenuto = service.fixPrimoMese(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "24 giugno";
        previsto = "24 giugno";
        ottenuto = service.fixPrimoMese(sorgente);
        assertEquals(previsto, ottenuto);
    }

    @ParameterizedTest
    @MethodSource(value = "DIACRITICI")
    @Order(13)
    @DisplayName("13 - Diacritici")
        //--cognome
        //--flag diacritico
    void diacritici(String cognome, final boolean diacritico) {
        sorgente = cognome;
        previstoBooleano = diacritico;

        ottenutoBooleano = service.isDiacritica(sorgente);
        assertEquals(previstoBooleano, ottenutoBooleano);
        if (ottenutoBooleano) {
            ottenuto = service.fixDiacritica(sorgente);
            System.out.println(String.format("La parola '%s' contiene diacritici e diventa %s%s", sorgente, FORWARD,ottenuto));
        }
        else {
            System.out.println(String.format("La parola '%s' NON contiene diacritici", sorgente));
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

}
