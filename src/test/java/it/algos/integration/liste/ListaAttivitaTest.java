package it.algos.integration.liste;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.wrapper.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 03-Jun-2022
 * Time: 20:06
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("production")
@Tag("liste")
@DisplayName("Attività lista")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaAttivitaTest extends WikiTest {

    /**
     * Classe principale di riferimento <br>
     */
    private ListaAttivita istanza;


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
     * Qui passa a ogni test delle sottoclassi <br>
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
    void costruttore() {
        istanza = appContext.getBean(ListaAttivita.class);
        assertNotNull(istanza);

        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(2)
    @DisplayName("2 - Lista bio di varie attivita")
        //--nome attivita
        //--typeLista
    void listaBio(final String attività, final AETypeLista type) {
        System.out.println("2 - Lista bio di varie attivita");
        sorgente = attività;

        listBio = switch (type) {
            case attivitaSingolare -> appContext.getBean(ListaAttivita.class).singolare(sorgente).listaBio();
            case attivitaPlurale -> appContext.getBean(ListaAttivita.class).plurale(sorgente).listaBio();
            default -> null;
        };

        if (listBio != null && listBio.size() > 0) {
            message = String.format("Ci sono %d biografie che implementano l'attività %s %s", listBio.size(), sorgente, type.getTagLower());
            System.out.println(message);
            if (type == AETypeLista.attivitaPlurale) {
                List<String> listaNomiSingoli = attivitaBackend.findAllSingolariByPlurale(sorgente);
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
    @MethodSource(value = "ATTIVITA")
    @Order(3)
    @DisplayName("3 - Lista wrapLista di varie attivita")
        //--nome attivita
        //--typeLista
    void listaWrapDidascalie(final String attività, final AETypeLista type) {
        System.out.println("3 - Lista wrapLista di varie attivita");
        sorgente = attività;
        int size;

        listWrapLista = switch (type) {
            case attivitaSingolare -> appContext.getBean(ListaAttivita.class).singolare(sorgente).listaWrap();
            case attivitaPlurale -> appContext.getBean(ListaAttivita.class).plurale(sorgente).listaWrap();
            default -> null;
        };

        if (listWrapLista != null && listWrapLista.size() > 0) {
            size = listWrapLista.size();
            message = String.format("Ci sono %d wrapLista che implementano l'attività %s%s", listWrapLista.size(), sorgente, type.getTagLower());
            System.out.println(message);
            if (type == AETypeLista.attivitaPlurale) {
                List<String> listaNomiSingoli = attivitaBackend.findAllSingolariByPlurale(sorgente);
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
    @MethodSource(value = "ATTIVITA")
    @Order(4)
    @DisplayName("4 - Mappa wrapLista di varie attivita")
        //--nome attivita
        //--typeLista
    void mappaWrapDidascalie(final String attività, final AETypeLista type) {
        System.out.println("4 - Mappa wrapLista di varie attivita");
        sorgente = attività;
        int numVoci;

        mappaWrap = switch (type) {
            case attivitaSingolare -> appContext.getBean(ListaAttivita.class).singolare(sorgente).mappaWrap();
            case attivitaPlurale -> appContext.getBean(ListaAttivita.class).plurale(sorgente).mappaWrap();
            default -> null;
        };

        if (mappaWrap != null && mappaWrap.size() > 0) {
            numVoci = wikiUtility.getSizeAllWrap(mappaWrap);
            message = String.format("Ci sono %d wrapLista che implementano l'attività %s %s", numVoci, sorgente, type.getTagLower());
            System.out.println(message);
            if (type == AETypeLista.attivitaPlurale) {
                List<String> listaNomiSingoli = attivitaBackend.findAllSingolariByPlurale(sorgente);
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


    @Test
    @Order(5)
    @DisplayName("5 - nobiliTedeschi")
    void nobiliTedeschi() {
        System.out.println("5 - nobiliTedeschi");
        sorgente = "nobili";
        sorgente2 = "Tedeschi";
        int numVoci;
        List<WrapLista> listaSotto;

        mappaWrap = appContext.getBean(ListaAttivita.class).plurale(sorgente).mappaWrap();

        if (mappaWrap != null && mappaWrap.size() > 0) {
            listWrapLista = mappaWrap.get(sorgente2);
            numVoci = listWrapLista.size();
            message = String.format("Ci sono %d wrapLista che implementano l'attività %s e sono %s", numVoci, sorgente, sorgente2);
            System.out.println(message);

            //            listaSotto = new ArrayList<>();
            //
            //            for (WrapLista wrap : listWrapLista) {
            //                stringa;
            //            }

            sorgente3 = "C";
            listaSotto = listWrapLista.stream().filter(wrap -> wrap.titoloSottoParagrafo.equals(sorgente3)).collect(Collectors.toList());
            numVoci = listaSotto.size();
            message = String.format("Ci sono %d wrapLista che implementano l'attività %s e sono %s ed iniziano con %s", numVoci, sorgente, sorgente2, sorgente3);
            System.out.println(VUOTA);
            System.out.println(message);
            printSub(listaSotto);

            sorgente3 = "E";
            listaSotto = listWrapLista.stream().filter(wrap -> wrap.titoloSottoParagrafo.equals(sorgente3)).collect(Collectors.toList());
            numVoci = listaSotto.size();
            message = String.format("Ci sono %d wrapLista che implementano l'attività %s e sono %s ed iniziano con %s", numVoci, sorgente, sorgente2, sorgente3);
            System.out.println(VUOTA);
            System.out.println(message);
            printSub(listaSotto);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }

    //    //    @ParameterizedTest
    //    @MethodSource(value = "ATTIVITA")
    //    @Order(4)
    //    @DisplayName("4 - Mappa wrapDidascalia di varie attivita")
    //        //--nome attivita
    //        //--flag singolare versus plurale
    //    void mappaWrapDidascalie(final String attività, final boolean flagSingola) {
    //        System.out.println("4 - Mappa wrapDidascalia di varie attivita");
    //        sorgente = attività;
    //        String flag;
    //        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie;
    //
    //        if (flagSingola) {
    //            mappaWrapDidascalie = appContext.getBean(ListaAttivita.class).singolare(sorgente).mappaWrapDidascalie();
    //            flag = "(singolare)";
    //        }
    //        else {
    //            mappaWrapDidascalie = appContext.getBean(ListaAttivita.class).plurale(sorgente).mappaWrapDidascalie();
    //            flag = "(plurale)";
    //        }
    //
    //        if (mappaWrapDidascalie != null) {
    //            message = String.format("WrapDidascalie che implementano l'attività %s %s", sorgente, flag);
    //            System.out.println(message);
    //            if (!flagSingola) {
    //                System.out.println(attivitaBackend.findFirstByPlurale(sorgente));
    //            }
    //            System.out.println(VUOTA);
    //            printMappaWrapDidascalia(sorgente, mappaWrapDidascalie);
    //        }
    //        else {
    //            message = "La mappa è nulla";
    //            System.out.println(message);
    //        }
    //    }

    //    @ParameterizedTest
    //    @MethodSource(value = "ATTIVITA")
    //    @Order(5)
    //    @DisplayName("5 - Mappa didascalie di varie attivita")
    //        //--nome attivita
    //        //--flag singolare versus plurale
    //    void mappaDidascalie(final String attività, final boolean flagSingola) {
    //        System.out.println("5 - Mappa didascalie di varie attivita");
    //        sorgente = attività;
    //        String flag;
    //        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie;
    //
    //        if (flagSingola) {
    //            mappaDidascalie = appContext.getBean(ListaAttivita.class).singolare(sorgente).mappaDidascalie();
    //            flag = "(singolare)";
    //        }
    //        else {
    //            mappaDidascalie = appContext.getBean(ListaAttivita.class).plurale(sorgente).mappaDidascalie();
    //            flag = "(plurale)";
    //        }
    //
    //        if (mappaDidascalie != null) {
    //            message = String.format("Didascalie che implementano l'attività %s %s", sorgente, flag);
    //            System.out.println(message);
    //            if (!flagSingola) {
    //                System.out.println(attivitaBackend.findFirstByPlurale(sorgente));
    //            }
    //            System.out.println(VUOTA);
    //            printMappaDidascalia("l'attività", sorgente, mappaDidascalie);
    //        }
    //        else {
    //            message = "La mappa è nulla";
    //            System.out.println(message);
    //        }
    //    }

    //    @ParameterizedTest
    //    @MethodSource(value = "ATTIVITA")
    //    @Order(6)
    //    @DisplayName("6 - Mappa paragrafi di varie attivita")
    //        //--nome attivita
    //        //--flag singolare versus plurale
    //    void mappaParagrafi(final String attività, final boolean flagSingola) {
    //        System.out.println("6 - Mappa paragrafi di varie attivita");
    //        sorgente = attività;
    //        String flag;
    //        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafi;
    //
    //        if (flagSingola) {
    //            mappaParagrafi = appContext.getBean(ListaAttivita.class).singolare(sorgente).mappaParagrafi();
    //            flag = "(singolare)";
    //        }
    //        else {
    //            mappaParagrafi = appContext.getBean(ListaAttivita.class).plurale(sorgente).mappaParagrafi();
    //            flag = "(plurale)";
    //        }
    //
    //        if (mappaParagrafi != null) {
    //            message = String.format("Didascalie che implementano l'attività %s %s", sorgente, flag);
    //            System.out.println(message);
    //            if (!flagSingola) {
    //                System.out.println(attivitaBackend.findFirstByPlurale(sorgente));
    //            }
    //            System.out.println(VUOTA);
    //            printMappaDidascalia("l'attività", sorgente, mappaParagrafi);
    //        }
    //        else {
    //            message = "La mappa è nulla";
    //            System.out.println(message);
    //        }
    //    }

    //    @ParameterizedTest
    //    @MethodSource(value = "ATTIVITA")
    //    @Order(7)
    //    @DisplayName("7 - Mappa paragrafi dimensionati di varie attivita")
    //        //--nome attivita
    //        //--flag singolare versus plurale
    //    void mappaParagrafiDimensionati(final String attività, final boolean flagSingola) {
    //        System.out.println("7 - Mappa paragrafi dimensionati di varie attivita");
    //        sorgente = attività;
    //        String flag;
    //        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafiDimensionati;
    //
    //        if (flagSingola) {
    //            mappaParagrafiDimensionati = appContext.getBean(ListaAttivita.class).singolare(sorgente).mappaParagrafiDimensionati();
    //            flag = "(singolare)";
    //        }
    //        else {
    //            mappaParagrafiDimensionati = appContext.getBean(ListaAttivita.class).plurale(sorgente).mappaParagrafiDimensionati();
    //            flag = "(plurale)";
    //        }
    //
    //        if (mappaParagrafiDimensionati != null) {
    //            message = String.format("Didascalie che implementano l'attività %s %s", sorgente, flag);
    //            System.out.println(message);
    //            if (!flagSingola) {
    //                System.out.println(attivitaBackend.findFirstByPlurale(sorgente));
    //            }
    //            System.out.println(VUOTA);
    //            printMappaDidascalia("l'attività", sorgente, mappaParagrafiDimensionati);
    //        }
    //        else {
    //            message = "La mappa è nulla";
    //            System.out.println(message);
    //        }
    //    }

    //    @ParameterizedTest
    //    @MethodSource(value = "ATTIVITA")
    //    @Order(8)
    //    @DisplayName("8 - Mappa sottoPagine")
    //        //--nome attivita
    //        //--flag singolare versus plurale
    //    void mappaSottoPagine(final String attività, final boolean flagSingola) {
    //        System.out.println("8 - Mappa sottoPagine");
    //        int soglia = 50;
    //        sorgente = attività;
    //        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie;
    //        int size;
    //
    //        if (flagSingola) {
    //            mappaDidascalie = appContext.getBean(ListaAttivita.class).singolare(sorgente).mappaDidascalie();
    //        }
    //        else {
    //            mappaDidascalie = appContext.getBean(ListaAttivita.class).plurale(sorgente).mappaDidascalie();
    //        }
    //
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Solo le nazionalità di '%s' che superano la soglia di %s", sorgente, soglia));
    //        for (String key : mappaDidascalie.keySet()) {
    //            size = wikiUtility.getSize(mappaDidascalie.get(key));
    //            if (size > soglia) {
    //                message = String.format("%s%s%d", key, FORWARD, size);
    //                System.out.println(message);
    //            }
    //        }
    //    }

    protected void printWrapListaAttivita(List<WrapDidascalia> wrapLista) {
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
            message = "Nazione, primo carattere, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printWrapListaBaseAttivita(wrapLista.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printWrapListaBaseAttivita(wrapLista.subList(iniB, endB), iniB);
        }
    }

    protected void printWrapListaBaseAttivita(List<WrapDidascalia> wrapLista, final int inizio) {
        int cont = inizio;

        for (WrapDidascalia wrap : wrapLista) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getNazionalitaParagrafo()) ? wrap.getNazionalitaParagrafo() : NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(wrap.getPrimoCarattere()));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getCognome()) ? wrap.getCognome() : NULL));
            System.out.print(SPAZIO);

            System.out.println(SPAZIO);
        }
    }


    protected void printMappaWrapDidascalia(String attivita, LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrap) {
        int cont = 0;
        LinkedHashMap<String, List<WrapDidascalia>> mappaSub;

        if (mappaWrap != null) {
            message = String.format("WrapDidascalie per l'attività %s", attivita);
            System.out.println(message);
            message = "Nazionalità, primoCarattere, wikiTitle";
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


    protected void printTreeMap(String attivita, TreeMap<String, TreeMap<String, List<String>>> treeMap) {
        int cont = 0;
        TreeMap<String, List<String>> mappaSub;

        if (treeMap != null) {
            message = String.format("WrapDidascalie per l'attività %s", attivita);
            System.out.println(message);
            System.out.println(VUOTA);

            for (String key : treeMap.keySet()) {
                mappaSub = treeMap.get(key);
                cont++;
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);

                System.out.print(textService.setQuadre(key));
                System.out.print(SPAZIO);

                System.out.println(VUOTA);

                printTreeMapSub(mappaSub);
            }
        }
    }

    protected void printTreeMapSub(TreeMap<String, List<String>> mappaSub) {
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
