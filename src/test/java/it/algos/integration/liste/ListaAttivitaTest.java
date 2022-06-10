package it.algos.integration.liste;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.wrapper.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

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
@Tag("liste")
@DisplayName("Attivita lista")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaAttivitaTest extends WikiTest {

    /**
     * Classe principale di riferimento <br>
     */
    private ListaAttivita istanza;

    private LinkedHashMap<String, List<WrapDidascalia>> mappaWrap;

    LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaLista;

    private TreeMap<String, TreeMap<String, List<String>>> treeMap;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();
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
        mappaLista = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - Costruttore base")
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
        //--flag singolare versus plurale
    void listaBio(final String attività, final boolean flagSingola) {
        System.out.println("2 - Lista bio di varie attivita");
        sorgente = attività;
        String flag;

        if (flagSingola) {
            listBio = appContext.getBean(ListaAttivita.class).singolare(sorgente).listaBio();
            flag = "(singolare)";
        }
        else {
            listBio = appContext.getBean(ListaAttivita.class).plurale(sorgente).listaBio();
            flag = "(plurale)";
        }

        if (listBio != null) {
            message = String.format("Ci sono %d biografie che implementano l'attività %s %s", listBio.size(), sorgente, flag);
            System.out.println(message);
            if (!flagSingola) {
                System.out.println(attivitaBackend.findByPlurale(sorgente));
            }
            System.out.println(VUOTA);
            printBioAttivita(listBio);
        }
        else {
            message = "La listBio è nulla";
            System.out.println(message);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(3)
    @DisplayName("3 - Lista wrapDidascalia di varie attivita")
        //--nome attivita
        //--flag singolare versus plurale
    void listaWrap(final String attività, final boolean flagSingola) {
        System.out.println("3 - Lista wrapDidascalia di varie attivita");
        sorgente = attività;
        String flag;

        if (flagSingola) {
            listWrapDidascalia = appContext.getBean(ListaAttivita.class).singolare(sorgente).listaWrapDidascalie();
            flag = "(singolare)";
        }
        else {
            listWrapDidascalia = appContext.getBean(ListaAttivita.class).plurale(sorgente).listaWrapDidascalie();
            flag = "(plurale)";
        }

        if (listWrapDidascalia != null) {
            message = String.format("Ci sono %d wrapDidascalia che implementano l'attività %s %s", listWrapDidascalia.size(), sorgente, flag);
            System.out.println(message);
            if (!flagSingola) {
                System.out.println(attivitaBackend.findByPlurale(sorgente));
            }
            System.out.println(VUOTA);
            printWrapListaAttivita(listWrapDidascalia);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(4)
    @DisplayName("4 - Mappa wrapDidascalia di varie attivita")
        //--nome attivita
        //--flag singolare versus plurale
    void getListaDidascalie(final String attività, final boolean flagSingola) {
        System.out.println("4 - Mappa wrapDidascalia di varie attivita");
        sorgente = attività;
        String flag;
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie;

        if (flagSingola) {
            mappaWrapDidascalie = appContext.getBean(ListaAttivita.class).singolare(sorgente).mappaWrapDidascalie();
            flag = "(singolare)";
        }
        else {
            mappaWrapDidascalie = appContext.getBean(ListaAttivita.class).plurale(sorgente).mappaWrapDidascalie();
            flag = "(plurale)";
        }

        if (mappaWrapDidascalie != null) {
            message = String.format("Ci sono %d wrapDidascalia che implementano l'attività %s %s", mappaWrapDidascalie.size(), sorgente, flag);
            System.out.println(message);
            if (!flagSingola) {
                System.out.println(attivitaBackend.findByPlurale(sorgente));
            }
            System.out.println(VUOTA);
            printMappaWrapDidascalia(sorgente, mappaWrapDidascalie);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }


    protected void printMappaWrapDidascalia(String attivita, LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaLista) {
        int cont = 0;
        LinkedHashMap<String, List<WrapDidascalia>> mappaSub;

        if (mappaLista != null) {
            message = String.format("WrapDidascalie per l'attività %s", attivita);
            System.out.println(message);
            System.out.println(VUOTA);

            for (String key : mappaLista.keySet()) {
                mappaSub = mappaLista.get(key);
                cont++;
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);

                System.out.print("[");
                System.out.print(key);
                System.out.print("]");
                System.out.print(SPAZIO);

                System.out.println(VUOTA);

                printMappaSub(mappaSub);
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

                System.out.print("[");
                System.out.print(key);
                System.out.print("]");
                System.out.print(SPAZIO);

                System.out.println(VUOTA);

                printWrapDidascalia(lista);
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

                System.out.print("[");
                System.out.print(key);
                System.out.print("]");
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

                System.out.print("[");
                System.out.print(key);
                System.out.print("]");
                System.out.print(SPAZIO);

                System.out.println(VUOTA);

                printWrapDidascalia(lista);
            }
        }
    }


    protected void printWrapDidascalia(List<WrapDidascalia> lista) {
        int cont = 0;
        String value;

        if (lista != null) {
            for (WrapDidascalia wrap : lista) {
                cont++;
                value = wrap.getWikiTitle();

                System.out.print(TAB);
                System.out.print(TAB);
                System.out.print(cont);
                System.out.print(PARENTESI_TONDA_END);
                System.out.print(SPAZIO);

                System.out.print("[");
                System.out.print(value);
                System.out.print("]");
                System.out.print(SPAZIO);

                System.out.println(VUOTA);
            }
        }
    }

    protected void printBioAttivita(List<Bio> listaBio) {
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
            message = "Ordinate per forzaOrdinamento";
            System.out.println(message);
            System.out.println(VUOTA);

            printBioBase(listaBio.subList(iniA, endA));
            System.out.println(TRE_PUNTI);
            printBioBase(listaBio.subList(iniB, endB));
        }
    }

    protected void printBioBase(List<Bio> listaBio) {
        int cont = 0;

        for (Bio bio : listaBio) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print("[");
            System.out.print(bio.cognome);
            System.out.print("]");
            System.out.print(SPAZIO);

            System.out.print("[");
            System.out.print(textService.isValid(bio.cognome) ? bio.cognome.substring(0, 1) : VUOTA);
            System.out.print("]");
            System.out.print(SPAZIO);

            System.out.print("[");
            System.out.print(textService.isValid(bio.nazionalita) ? nazionalitaBackend.findBySingolare(bio.nazionalita).plurale : VUOTA);
            System.out.print("]");
            System.out.print(SPAZIO);

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

}
