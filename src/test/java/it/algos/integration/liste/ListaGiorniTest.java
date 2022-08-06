package it.algos.integration.liste;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.bio.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit.jupiter.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 25-Jul-2022
 * Time: 09:10
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("liste")
@DisplayName("Giorni lista")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaGiorniTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private ListaGiorni istanza;


    //--nome giorno
    //--typeCrono
    protected static Stream<Arguments> GIORNI() {
        return Stream.of(
                Arguments.of(VUOTA, AETypeLista.giornoNascita),
                Arguments.of(VUOTA, AETypeLista.giornoMorte),
                Arguments.of("3 luglio", AETypeLista.giornoNascita),
                Arguments.of("1º gennaio", AETypeLista.giornoMorte),
                Arguments.of("25 ottobre", AETypeLista.giornoNascita),
                Arguments.of("29 febbraio", AETypeLista.giornoMorte)
        );
    }

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();
        assertNull(istanza);
    }


    /**
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
        istanza = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - Costruttore base senza parametri")
    void costruttoreBase() {
        istanza = new ListaGiorni();
        assertNotNull(istanza);
        System.out.println(("1 - Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(2)
    @DisplayName("2 - Lista bio di vari giorni")
        //--nome giorno
        //--typeLista
    void listaBio(final String nomeGiorno, final AETypeLista type) {
        System.out.println("2 - Lista bio di vari giorni");
        sorgente = nomeGiorno;

        listBio = switch (type) {
            case giornoNascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).listaBio();
            case giornoMorte -> appContext.getBean(ListaGiorni.class).morte(sorgente).listaBio();
            default -> null;
        };

        if (listBio != null && listBio.size() > 0) {
            message = String.format("Ci sono %d biografie che implementano il giorno di %s %s", listBio.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            switch (type) {
                case giornoNascita -> printBioListaGiorniNato(listBio);
                case giornoMorte -> printBioListaGiorniMorto(listBio);
            }
        }
        else {
            message = "La listBio è nulla";
            System.out.println(message);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(3)
    @DisplayName("3 - Lista wrapLista di vari giorni")
        //--nome giorno
        //--typeLista
    void listaWrap(final String nomeGiorno, final AETypeLista type) {
        System.out.println("3 - Lista wrapLista di vari giorni");
        sorgente = nomeGiorno;
        int size;

        listWrapLista = switch (type) {
            case giornoNascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).listaWrap();
            case giornoMorte -> appContext.getBean(ListaGiorni.class).morte(sorgente).listaWrap();
            default -> null;
        };

        if (listWrapLista != null && listWrapLista.size() > 0) {
            size = listWrapLista.size();
            message = String.format("Ci sono %d wrapLista che implementano il giorno di %s %s", listWrapLista.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printWrapLista(listWrapLista);
            printWrapLista(listWrapLista.subList(size - 5, size));
        }
        else {
            message = "La lista è nulla";
            System.out.println(message);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(4)
    @DisplayName("4 - Mappa wrapLista di vari giorni")
        //--nome giorno
        //--typeLista
    void mappaWrap(final String nomeGiorno, final AETypeLista type) {
        System.out.println("4 - Mappa wrapLista di vari giorni");
        sorgente = nomeGiorno;
        int numVoci;

        mappaWrap = switch (type) {
            case giornoNascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).mappaWrap();
            case giornoMorte -> appContext.getBean(ListaGiorni.class).morte(sorgente).mappaWrap();
            default -> null;
        };

        if (mappaWrap != null && mappaWrap.size() > 0) {
            numVoci = wikiUtility.getSizeAllWrap(mappaWrap);
            message = String.format("Ci sono %d wrapLista che implementano il giorno di %s %s", numVoci, type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printMappaWrap(mappaWrap);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }


////    @ParameterizedTest
//    @MethodSource(value = "GIORNI")
//    @Order(5)
//    @DisplayName("5 - Mappa didascalie di vari giorni")
//        //--nome giorno
//        //--typeLista
//    void mappaDidascalia(final String nomeGiorno, final AETypeLista type) {
//        System.out.println("5 - Mappa didascalie di vari giorni");
//        sorgente = nomeGiorno;
//        int numVoci;
//
//        mappa = switch (type) {
//            case giornoNascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).mappaDidascalia();
//            case giornoMorte -> appContext.getBean(ListaGiorni.class).morte(sorgente).mappaDidascalia();
//            default -> null;
//        };
//
//        if (mappa != null && mappa.size() > 0) {
//            numVoci = wikiUtility.getSizeAll(mappa);
//            message = String.format("Ci sono %d didascalie che implementano il giorno di %s %s", numVoci, type, sorgente);
//            System.out.println(message);
//            System.out.println(VUOTA);
//            printMappa(mappa);
//        }
//        else {
//            message = "La mappa è nulla";
//            System.out.println(message);
//        }
//    }


//    @ParameterizedTest
//    @MethodSource(value = "GIORNI")
//    @Order(6)
//    @DisplayName("6 - Testo body di vari giorni")
//        //--nome giorno
//        //--typeLista
//    void testoBody(final String nomeGiorno, final AETypeLista type) {
//        System.out.println("6 - Testo body di vari giorni");
//        System.out.println(VUOTA);
//        sorgente = nomeGiorno;
//
//        ottenutoRisultato = switch (type) {
//            case giornoNascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).testoBody();
//            case giornoMorte -> appContext.getBean(ListaGiorni.class).morte(sorgente).testoBody();
//            default -> null;
//        };
//        System.out.println(ottenutoRisultato.getContent());
//    }


    protected void printBioListaGiorniNato(List<Bio> listaBio) {
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
            message = "Ordinate per anni, forzaOrdinamento";
            System.out.println(message);
            message = "Anno, ordinamento, wikiTitle, nome, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printBioBaseGiorniNato(listaBio.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printBioBaseGiorniNato(listaBio.subList(iniB, endB), iniB);
        }
    }


    protected void printBioListaGiorniMorto(List<Bio> listaBio) {
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
            message = "Ordinate per anni, forzaOrdinamento";
            System.out.println(message);
            message = "Anno, ordinamento, wikiTitle, nome, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printBioBaseGiorniMorto(listaBio.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printBioBaseGiorniMorto(listaBio.subList(iniB, endB), iniB);
        }
    }

    protected void printBioBaseGiorniNato(List<Bio> listaBio, final int inizio) {
        int cont = inizio;

        for (Bio bio : listaBio) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.annoNato + SPAZIO + "(" + bio.annoNatoOrd + ")"));
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

    protected void printBioBaseGiorniMorto(List<Bio> listaBio, final int inizio) {
        int cont = inizio;

        for (Bio bio : listaBio) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.annoMorto + SPAZIO + "(" + bio.annoMortoOrd + ")"));
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