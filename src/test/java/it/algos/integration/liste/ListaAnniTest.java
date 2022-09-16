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
 * Date: Sun, 17-Jul-2022
 * Time: 06:44
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("liste")
@DisplayName("Anni lista")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaAnniTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private ListaAnni istanza;

    //--nome anno
    //--typeCrono
    protected static Stream<Arguments> LISTA_ANNI() {
        return Stream.of(
                Arguments.of(VUOTA, AETypeLista.annoNascita),
                Arguments.of(VUOTA, AETypeLista.annoMorte),
                Arguments.of("1278", AETypeLista.annoNascita),
                Arguments.of("483", AETypeLista.annoNascita),
                Arguments.of("1392", AETypeLista.annoNascita),
                Arguments.of("1785", AETypeLista.annoNascita),
                Arguments.of("325", AETypeLista.annoMorte),
                Arguments.of("1318", AETypeLista.annoNascita),
                Arguments.of("245 a.C.", AETypeLista.annoMorte),
                Arguments.of("4 a.C.", AETypeLista.annoNascita),
                Arguments.of("12", AETypeLista.annoMorte),
                Arguments.of("2022", AETypeLista.annoMorte)
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
        istanza = new ListaAnni();
        assertNotNull(istanza);
        System.out.println(("1 - Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }


    @ParameterizedTest
    @MethodSource(value = "LISTA_ANNI")
    @Order(2)
    @DisplayName("2 - Lista bio di vari anni")
        //--nome anno
        //--typeLista
    void listaBio(final String nomeAnno, final AETypeLista type) {
        System.out.println("2 - Lista bio di vari anni");
        sorgente = nomeAnno;

        listBio = switch (type) {
            case annoNascita -> appContext.getBean(ListaAnni.class).nascita(sorgente).listaBio();
            case annoMorte -> appContext.getBean(ListaAnni.class).morte(sorgente).listaBio();
            default -> null;
        };

        if (listBio != null && listBio.size() > 0) {
            message = String.format("Ci sono %d biografie che implementano l'anno di %s %s", listBio.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            switch (type) {
                case annoNascita -> printBioListaAnniNato(listBio);
                case annoMorte -> printBioListaAnniMorto(listBio);
            }
        }
        else {
            message = "La listBio è nulla";
            System.out.println(message);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "LISTA_ANNI")
    @Order(3)
    @DisplayName("3 - Lista wrapLista di vari anni")
        //--nome anno
        //--typeLista
    void listaWrap(final String nomeAnno, final AETypeLista type) {
        System.out.println("3 - Lista wrapLista di vari anni");
        sorgente = nomeAnno;
        int size = 0;

        listWrapLista = switch (type) {
            case annoNascita -> appContext.getBean(ListaAnni.class).nascita(sorgente).listaWrap();
            case annoMorte -> appContext.getBean(ListaAnni.class).morte(sorgente).listaWrap();
            default -> null;
        };

        if (listWrapLista != null && listWrapLista.size() > 0) {
            message = String.format("Ci sono %d wrapDidascalia che implementano l'anno %s %s", listWrapLista.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printWrapLista(listWrapLista);
            size = listWrapLista.size();

        }
        else {
            message = "La lista è nulla";
            System.out.println(message);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "LISTA_ANNI")
    @Order(4)
    @DisplayName("4 - Mappa wrapLista di vari anni")
        //--nome anno
        //--typeLista
    void mappaWrap(final String nomeAnno, final AETypeLista type) {
        System.out.println("4 - Mappa wrapLista di vari anni");
        sorgente = nomeAnno;
        int numVoci;

        mappaWrap = switch (type) {
            case annoNascita -> appContext.getBean(ListaAnni.class).nascita(sorgente).mappaWrap();
            case annoMorte -> appContext.getBean(ListaAnni.class).morte(sorgente).mappaWrap();
            default -> null;
        };

        if (mappaWrap != null && mappaWrap.size() > 0) {
            numVoci = wikiUtility.getSizeAllWrap(mappaWrap);
            message = String.format("Ci sono %d wrapLista che implementano l'anno %s %s", numVoci, type, sorgente);
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
//    @MethodSource(value = "LISTA_ANNI")
//    @Order(5)
//    @DisplayName("5 - Mappa didascalie di vari anni")
//        //--nome anno
//        //--typeLista
//    void mappaDidascalia(final String nomeAnno, final AETypeLista type) {
//        System.out.println("5 - Mappa didascalie di vari anni");
//        sorgente = nomeAnno;
//        int numVoci;
//
//        mappa = switch (type) {
//            case annoNascita -> appContext.getBean(ListaAnni.class).nascita(sorgente).mappaDidascalia();
//            case annoMorte -> appContext.getBean(ListaAnni.class).morte(sorgente).mappaDidascalia();
//            default -> null;
//        };
//
//        if (mappa != null && mappa.size() > 0) {
//            numVoci = wikiUtility.getSizeAll(mappa);
//            message = String.format("Ci sono %d didascalie che implementano l'anno %s %s", numVoci, type, sorgente);
//            System.out.println(message);
//            System.out.println(VUOTA);
//            printMappa(mappa);
//        }
//        else {
//            message = "La mappa è nulla";
//            System.out.println(message);
//        }
//    }

////    @ParameterizedTest
//    @MethodSource(value = "LISTA_ANNI")
//    @Order(6)
//    @DisplayName("6 - Testo body di vari anni")
//        //--nome anno
//        //--typeLista
//    void testoBody(final String nomeAnno, final AETypeLista type) {
//        System.out.println("6 - Testo body di vari anni");
//        System.out.println(VUOTA);
//        sorgente = nomeAnno;
//
//        ottenutoRisultato = switch (type) {
//            case annoNascita -> appContext.getBean(ListaAnni.class).nascita(sorgente).testoBody();
//            case annoMorte -> appContext.getBean(ListaAnni.class).morte(sorgente).testoBody();
//            default -> null;
//        };
//        System.out.println(ottenutoRisultato.getContent());
//    }





    protected void printBioListaAnniNato(List<Bio> listaBio) {
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
            message = "Ordinate per giorni, forzaOrdinamento";
            System.out.println(message);
            message = "Giorno, ordinamento, wikiTitle, nome, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printBioBaseAnniNato(listaBio.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printBioBaseAnniNato(listaBio.subList(iniB, endB), iniB);
        }
    }


    protected void printBioListaAnniMorto(List<Bio> listaBio) {
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
            message = "Ordinate per giorni, forzaOrdinamento";
            System.out.println(message);
            message = "Giorno, ordinamento, wikiTitle, nome, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printBioBaseAnniMorto(listaBio.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printBioBaseAnniMorto(listaBio.subList(iniB, endB), iniB);
        }
    }

    protected void printBioBaseAnniNato(List<Bio> listaBio, final int inizio) {
        int cont = inizio;

        for (Bio bio : listaBio) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            if (textService.isValid(bio.giornoNato)) {
                System.out.print(textService.setQuadre(bio.giornoNato + SPAZIO + "(" + bio.giornoNatoOrd + ")"));
                System.out.print(SPAZIO);
            }
            else {
                System.out.print(SPAZIO + "(null)");
                System.out.print(SPAZIO);
            }

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

    protected void printBioBaseAnniMorto(List<Bio> listaBio, final int inizio) {
        int cont = inizio;

        for (Bio bio : listaBio) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            if (textService.isValid(bio.giornoMorto)) {
                System.out.print(textService.setQuadre(bio.giornoMorto + SPAZIO + "(" + bio.giornoMortoOrd + ")"));
                System.out.print(SPAZIO);
            }
            else {
                System.out.print(SPAZIO + "(null)");
                System.out.print(SPAZIO);
            }
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