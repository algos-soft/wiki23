package it.algos.integration.liste;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.wrapper.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit.jupiter.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Tue, 14-Jun-2022
 * Time: 07:27
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("production")
@Tag("liste")
@DisplayName("Nazionalità lista")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaNazionalitaTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private ListaNazionalita istanza;


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
    @DisplayName("1- Costruttore base senza parametri")
    void costruttoreBase() {
        istanza = new ListaNazionalita();
        assertNotNull(istanza);
        System.out.println(("1- Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }


    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA")
    @Order(2)
    @DisplayName("2 - Lista bio di varie nazionalità")
        //--nome nazionalità
        //--typeLista
    void listaBio(final String nazionalità, final AETypeLista type) {
        System.out.println("2 - Lista bio di varie nazionalità");
        sorgente = nazionalità;

        listBio = switch (type) {
            case nazionalitaSingolare -> appContext.getBean(ListaNazionalita.class).singolare(sorgente).listaBio();
            case nazionalitaPlurale -> appContext.getBean(ListaNazionalita.class).plurale(sorgente).listaBio();
            default -> null;
        };

        if (listBio != null && listBio.size() > 0) {
            message = String.format("Ci sono %d biografie che implementano la nazionalità %s%s", listBio.size(), sorgente, type.getTagLower());
            System.out.println(message);
            if (type == AETypeLista.nazionalitaPlurale) {
                List<String> listaNomiSingoli = nazionalitaBackend.findSingolariByPlurale(sorgente);
                System.out.println(listaNomiSingoli);
            }
            System.out.println(VUOTA);
            printBioLista(listBio);
        }
        else {
            message = "La listBio è nulla";
            System.out.println(message);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA")
    @Order(3)
    @DisplayName("3 - Lista wrapLista di varie nazionalità")
        //--nome nazionalità
        //--typeLista
    void listaWrapDidascalie(final String nazionalità, final AETypeLista type) {
        System.out.println("3 - Lista wrapLista di varie nazionalità");
        sorgente = nazionalità;
        int size;

        listWrapLista = switch (type) {
            case nazionalitaSingolare -> appContext.getBean(ListaNazionalita.class).singolare(sorgente).listaWrap();
            case nazionalitaPlurale -> appContext.getBean(ListaNazionalita.class).plurale(sorgente).listaWrap();
            default -> null;
        };

        if (listWrapLista != null && listWrapLista.size() > 0) {
            size = listWrapLista.size();
            message = String.format("Ci sono %d wrapLista che implementano la nazionalità %s%s", listWrapLista.size(), sorgente, type.getTagLower());
            System.out.println(message);
            if (type == AETypeLista.nazionalitaPlurale) {
                List<String> listaNomiSingoli = nazionalitaBackend.findSingolariByPlurale(sorgente);
                System.out.println(listaNomiSingoli);
            }
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
    @MethodSource(value = "NAZIONALITA")
    @Order(4)
    @DisplayName("4 - Mappa wrapLista di varie nazionalità")
        //--nome nazionalità
        //--typeLista
    void mappaWrapDidascalie(final String nazionalità, final AETypeLista type) {
        System.out.println("4 - Mappa wrapLista di varie nazionalità");
        sorgente = nazionalità;
        int numVoci;

        mappaWrap = switch (type) {
            case nazionalitaSingolare -> appContext.getBean(ListaNazionalita.class).singolare(sorgente).mappaWrap();
            case nazionalitaPlurale -> appContext.getBean(ListaNazionalita.class).plurale(sorgente).mappaWrap();
            default -> null;
        };

        if (mappaWrap != null && mappaWrap.size() > 0) {
            numVoci = wikiUtility.getSizeAllWrap(mappaWrap);
            message = String.format("Ci sono %d wrapLista che implementano la nazionalità %s%s", numVoci, sorgente, type.getTagLower());
            System.out.println(message);
            if (type == AETypeLista.nazionalitaPlurale) {
                List<String> listaNomiSingoli = nazionalitaBackend.findSingolariByPlurale(sorgente);
                System.out.println(listaNomiSingoli);
            }
            System.out.println(VUOTA);
            printMappaWrap(mappaWrap);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }


    //    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA")
    @Order(5)
    @DisplayName("5 - Mappa didascalie di varie nazionalità")
    //--nome nazionalità
    //--typeLista
    void mappaDidascalie(final String nazionalità, final AETypeLista type) {
        System.out.println("5 - Mappa didascalie di varie nazionalità");
        sorgente = nazionalità;

        mappa = switch (type) {
            case nazionalitaSingolare -> appContext.getBean(ListaNazionalita.class).singolare(sorgente).mappaDidascalia();
            case nazionalitaPlurale -> appContext.getBean(ListaNazionalita.class).plurale(sorgente).mappaDidascalia();
            default -> null;
        };

        if (mappa != null && mappa.size() > 0) {
            message = String.format("Didascalie che implementano la nazionalità %s %s", sorgente, type.getTagLower());
            System.out.println(message);
            if (type == AETypeLista.nazionalitaPlurale) {
                List<String> listaNomiSingoli = nazionalitaBackend.findSingolariByPlurale(sorgente);
                System.out.println(listaNomiSingoli);
            }
            System.out.println(VUOTA);
            printMappaList(mappa);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }


    //    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA")
    @Order(6)
    @DisplayName("6 - Testo body di varie nazionalità")
    //--nome nazionalità
    //--typeLista
    void testoBody(final String nazionalità, final AETypeLista type) {
        System.out.println("6 - Testo body di vari giorni");
        System.out.println(VUOTA);
        sorgente = nazionalità;

        ottenutoRisultato = switch (type) {
            case nazionalitaSingolare -> appContext.getBean(ListaNazionalita.class).singolare(sorgente).testoBody();
            case nazionalitaPlurale -> appContext.getBean(ListaNazionalita.class).plurale(sorgente).testoBody();
            default -> WResult.errato("AETypeLista errato");
        };
        if (ottenutoRisultato.isValido()) {
            System.out.println(ottenutoRisultato.getContent());
        }
        else {
            printRisultato(ottenutoRisultato);
        }
    }


    //    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA")
    @Order(7)
    @DisplayName("7 - Mappa paragrafi dimensionati di varie nazionalità")
    //--nome nazionalità
    //--typeLista
    void mappaParagrafiDimensionati(final String nazionalità, final AETypeLista type) {
        System.out.println("7 - Mappa paragrafi dimensionati di varie nazionalità");
        sorgente = nazionalità;
        String flag;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafiDimensionati;

        //        if (flagSingola) {
        //            mappaParagrafiDimensionati = appContext.getBean(ListaNazionalita.class).singolare(sorgente).mappaParagrafiDimensionati();
        //            flag = "(singolare)";
        //        }
        //        else {
        //            mappaParagrafiDimensionati = appContext.getBean(ListaNazionalita.class).plurale(sorgente).mappaParagrafiDimensionati();
        //            flag = "(plurale)";
        //        }

        //        if (mappaParagrafiDimensionati != null) {
        //            message = String.format("Didascalie che implementano la nazionalità %s %s", sorgente, flag);
        //            System.out.println(message);
        //            if (!flagSingola) {
        //                System.out.println(nazionalitaBackend.findFirstByPlurale(sorgente));
        //            }
        //            System.out.println(VUOTA);
        //            printMappaDidascalia("la nazionalità", sorgente, mappaParagrafiDimensionati);
        //        }
        //        else {
        //            message = "La mappa è nulla";
        //            System.out.println(message);
        //        }
    }


    //    @ParameterizedTest
    @MethodSource(value = "NAZIONALITA")
    @Order(8)
    @DisplayName("8 - Mappa sottoPagine")
    //--nome attivita
    //--typeLista
    void mappaSottoPagine(final String attività, final AETypeLista type) {
        System.out.println("8 - Mappa sottoPagine");
        int soglia = 50;
        sorgente = attività;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie = null;
        int size;

        //        if (flagSingola) {
        //            mappaDidascalie = appContext.getBean(ListaNazionalita.class).singolare(sorgente).mappaDidascalie();
        //        }
        //        else {
        //            mappaDidascalie = appContext.getBean(ListaNazionalita.class).plurale(sorgente).mappaDidascalie();
        //        }

        System.out.println(VUOTA);
        System.out.println(String.format("Solo le attività di '%s' che superano la soglia di %s", sorgente, soglia));
        for (String key : mappaDidascalie.keySet()) {
            size = wikiUtility.getSize(mappaDidascalie.get(key));
            if (size > soglia) {
                message = String.format("%s%s%d", key, FORWARD, size);
                System.out.println(message);
            }
        }
    }

    protected void printWrapListaNazionalita(List<WrapDidascalia> wrapLista) {
        String message;
        int max = 10;
        int tot = wrapLista.size();
        int iniA = 0;
        int endA = Math.min(max, tot);
        int iniB = tot - max > 0 ? tot - max : 0;
        int endB = tot;

        if (wrapLista != null) {
            message = String.format("Faccio vedere le prime e le ultime %d wrapDidascalie", max);
            System.out.println(message);
            message = "Parametri di ordinamento (nell'ordine):";
            System.out.println(message);
            message = "Attività, primo carattere, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printWrapListaBaseNazionalita(wrapLista.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printWrapListaBaseNazionalita(wrapLista.subList(iniB, endB), iniB);
        }
    }

    protected void printWrapListaBaseNazionalita(List<WrapDidascalia> wrapLista, final int inizio) {
        int cont = inizio;

        for (WrapDidascalia wrap : wrapLista) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getAttivitaParagrafo()) ? wrap.getAttivitaParagrafo() : NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(wrap.getPrimoCarattere()));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getCognome()) ? wrap.getCognome() : NULL));
            System.out.print(SPAZIO);

            System.out.println(SPAZIO);
        }
    }


    protected void printMappaWrapDidascalia(String nazionalità, LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrap) {
        int cont = 0;
        LinkedHashMap<String, List<WrapDidascalia>> mappaSub;

        if (mappaWrap != null) {
            message = String.format("WrapDidascalie per la nazionalità %s", nazionalità);
            System.out.println(message);
            message = "Attività, primoCarattere, wikiTitle";
            System.out.println(message);
            System.out.println(VUOTA);

            for (String key : mappaWrap.keySet()) {
                mappaSub = mappaWrap.get(key);
                cont++;
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);

                System.out.print(textService.setQuadre(key));
                System.out.print(SPAZIO);

                System.out.println(VUOTA);

                printMappaSub(mappaSub);
                System.out.println(VUOTA);
            }
        }
    }

    protected void printMappaSub(LinkedHashMap<String, List<WrapDidascalia>> mappaSub) {
        int cont = 0;
        List lista;

        if (mappaSub != null) {
            for (String key : mappaSub.keySet()) {
                lista = mappaSub.get(key);
                cont++;
                System.out.print(TAB);
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);

                System.out.print(textService.setQuadre(key));
                System.out.print(SPAZIO);

                System.out.println(VUOTA);

                printWrapDidascalia(lista);
            }
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