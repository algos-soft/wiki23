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
@DisplayName("Text Service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaAttivitaTest extends WikiTest {

    /**
     * Classe principale di riferimento <br>
     */
    private ListaAttivita istanza;

    LinkedHashMap<String, List<WrapDidascalia>> mappaWrap;

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
    }


    @Test
    @Order(1)
    @DisplayName("1 - Costruttore base")
    void costruttore() {
        sorgente = "attore";
        istanza = appContext.getBean(ListaAttivita.class, sorgente, ListaAttivita.AETypeAttivita.singolare);
        assertNotNull(istanza);

        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(2)
    @DisplayName("2 - Lista bio di varie attivita")
        //--attivita singola
    void getListaBio(final String attività, final ListaAttivita.AETypeAttivita type) {
        System.out.println("2 - Lista bio di varie attivita");
        sorgente = attività;
        istanza = appContext.getBean(ListaAttivita.class, sorgente, type);
        assertNotNull(istanza);

        listBio = istanza.getListaBio();
        if (listBio != null) {
            message = String.format("Ci sono %d biografie che implementano l'attività %s", listBio.size(), sorgente);
            System.out.println(message);
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
    @DisplayName("3 - Lista wrap di varie attivita")
        //--attivita singola
    void getListaWrap(final String attività, final ListaAttivita.AETypeAttivita type) {
        System.out.println("3 - Lista wrap di varie attivita");
        sorgente = attività;
        istanza = appContext.getBean(ListaAttivita.class, sorgente, type);
        assertNotNull(istanza);

        listWrap = istanza.getListaWrap();
        mappaWrap = istanza.getMappaWrap();

        if (mappaWrap != null) {
            message = String.format("Ci sono %d biografie che implementano l'attività %s", mappaWrap.size(), sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            //                    printWrapListaAttivita(listWrap);
        }
        else {
            message = "La mappaWrap è nulla";
            System.out.println(message);
        }
        //        if (listWrap != null) {
        //            message = String.format("Ci sono %d biografie che implementano l'attività %s", listWrap.size(), sorgente);
        //            System.out.println(message);
        //            System.out.println(VUOTA);
        //            printWrapListaAttivita(listWrap);
        //        }
        //        else {
        //            message = "La listWrap è nulla";
        //            System.out.println(message);
        //        }
    }

    protected void printBioAttivita(List<Bio> listaBio) {
        String message;
        int max = 20;
        int tot = listaBio.size();
        int iniA = 0;
        int endA = Math.min(max, tot);
        int iniB = tot - max > 0 ? tot - max : 0;
        int endB = tot;

        if (listaBio != null) {
            message = String.format("Faccio vedere le prime e le ultime %d biografie", max);
            System.out.println(message);
            message = "Divise in paragrafi di nazione e (eventualmente) primo carattere";
            System.out.println(message);
            message = "Ordinate per nazione, primo carattere e cognome (se manca per wikiTitle)";
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
            System.out.print(textService.isValid(bio.nazionalita) ? nazionalitaBackend.findBySingolare(bio.nazionalita).plurale : VUOTA);
            System.out.print("]");
            System.out.print(SPAZIO);

            System.out.print("[");
            System.out.print(textService.isValid(bio.cognome) ? bio.cognome.substring(0, 1) : VUOTA);
            System.out.print("]");
            System.out.print(SPAZIO);

            System.out.print("[");
            System.out.print(bio.cognome);
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
