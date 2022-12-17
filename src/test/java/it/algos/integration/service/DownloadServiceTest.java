package it.algos.integration.service;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.service.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.wrapper.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 19-Aug-2022
 * Time: 14:09
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Text Service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DownloadServiceTest extends WikiTest {

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private DownloadService service;

    private final static String ANNO_UNO = "1337";

//    private final static String CATEGORIA_UNO = "Nati nel" + SPAZIO + ANNO_UNO;
    private final static String CATEGORIA_UNO="Premi Oscar nel 1981";
    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = downloadService;
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


    @Test
    @Order(1)
    @DisplayName("1 - ciclo checkCategoria")
    void checkCategoria() {
        System.out.println("1 - ciclo checkCategoria");

        //--Controlla quante pagine ci sono nella categoria
        service.checkCategoria(CATEGORIA_UNO);
    }

    @Test
    @Order(2)
    @DisplayName("2 - ciclo getListaPageIds")
    void getListaPageIds() {
        System.out.println("2 - ciclo getListaPageIds");

        //--Controlla quante pagine ci sono nella categoria
        service.checkCategoria(CATEGORIA_UNO);

        //--Controlla il collegamento come bot
        //--Saltato perché la categoria è piccola

        //--Crea la lista di tutti i (long) pageIds della category
        listaPageIds = service.getListaPageIds(CATEGORIA_UNO);
        System.out.println(String.format("Lista dei %d long della categoria", listaPageIds.size()));
        System.out.println(VUOTA);
        for (long lungo : listaPageIds) {
            System.out.println(lungo);
        }
    }


    @Test
    @Order(3)
    @DisplayName("3 - ciclo getListaMongoIds")
    void getListaMongoIds() {
        List<Long> listaMongoIds;
        System.out.println("3 - ciclo getListaMongoIds");

        //--Controlla quante pagine ci sono nella categoria
        service.checkCategoria(CATEGORIA_UNO);

        //--Controlla il collegamento come bot
        //--Saltato perché la categoria è piccola

        //--Crea la lista di tutti i (long) pageIds della category
        listaPageIds = service.getListaPageIds(CATEGORIA_UNO);

        //--Crea la lista di tutti i (long) pageIds esistenti nel database (mongo) locale
        listaMongoIds = getLista(ANNO_UNO);
        System.out.println(VUOTA);
        System.out.println(String.format("Lista dei %d long di mongo, ordinati diversamente", listaMongoIds.size()));
        for (long lungo : listaMongoIds) {
            System.out.println(lungo);
        }
    }


    @Test
    @Order(4)
    @DisplayName("4 - ciclo listaMongoIdsDaCancellare")
    void listaMongoIdsDaCancellare() {
        List<Long> listaMongoIds;
        List<Long> listaMongoIdsDaCancellare;
        System.out.println("4 - ciclo listaMongoIdsDaCancellare");

        //--Controlla quante pagine ci sono nella categoria
        service.checkCategoria(CATEGORIA_UNO);

        //--Controlla il collegamento come bot
        //--Saltato perché la categoria è piccola

        //--Crea la lista di tutti i (long) pageIds della category
        listaPageIds = service.getListaPageIds(CATEGORIA_UNO);

        //--Crea la lista di tutti i (long) pageIds esistenti nel database (mongo) locale
        listaMongoIds = getLista(ANNO_UNO);

        //--Recupera i (long) pageIds non più presenti nella category e da cancellare dal database (mongo) locale
        listaMongoIdsDaCancellare = service.deltaPageIds(listaMongoIds, listaPageIds, "listaMongoIdsDaCancellare");
        System.out.println(VUOTA);
        System.out.println(String.format("Lista dei %d long esistenti da cancellare", listaMongoIdsDaCancellare.size()));
        for (long lungo : listaMongoIdsDaCancellare) {
            System.out.println(lungo);
        }
    }


    @Test
    @Order(5)
    @DisplayName("5 - ciclo listaPageIdsDaCreare")
    void listaPageIdsDaCreare() {
        List<Long> listaMongoIds;
        List<Long> listaMongoIdsDaCancellare;
        List<Long> listaPageIdsDaCreare;
        System.out.println("5 - ciclo listaPageIdsDaCreare");

        //--Controlla quante pagine ci sono nella categoria
        service.checkCategoria(CATEGORIA_UNO);

        //--Controlla il collegamento come bot
        //--Saltato perché la categoria è piccola

        //--Crea la lista di tutti i (long) pageIds della category
        listaPageIds = service.getListaPageIds(CATEGORIA_UNO);

        //--Crea la lista di tutti i (long) pageIds esistenti nel database (mongo) locale
        listaMongoIds = getLista(ANNO_UNO);

        //--Recupera i (long) pageIds non più presenti nella category e da cancellare dal database (mongo) locale
        listaMongoIdsDaCancellare = service.deltaPageIds(listaMongoIds, listaPageIds, "listaMongoIdsDaCancellare");

        //--Cancella dal database (mongo) locale le entities non più presenti nella category <br>
        //--Saltato

        //--Recupera i (long) pageIds presenti nella category e non esistenti ancora nel database (mongo) locale e da creare
        listaPageIdsDaCreare = service.deltaPageIds(listaPageIds, listaMongoIds, "listaPageIdsDaCreare");
        System.out.println(VUOTA);
        System.out.println(String.format("Lista dei %d long mancanti da creare", listaPageIdsDaCreare.size()));
        for (long lungo : listaPageIdsDaCreare) {
            System.out.println(lungo);
        }
    }


    @Test
    @Order(6)
    @DisplayName("6 - ciclo getListaWrapTime")
    void getListaWrapTime() {
        List<Long> listaMongoIds;
        List<Long> listaMongoIdsDaCancellare;
        List<Long> listaPageIdsDaCreare;
        List<WrapTime> listaWrapTime;
        System.out.println("6 - ciclo getListaWrapTime");

        //--Controlla quante pagine ci sono nella categoria
        service.checkCategoria(CATEGORIA_UNO);

        //--Controlla il collegamento come bot
        //--Saltato perché la categoria è piccola

        //--Crea la lista di tutti i (long) pageIds della category
        listaPageIds = service.getListaPageIds(CATEGORIA_UNO);

        //--Crea la lista di tutti i (long) pageIds esistenti nel database (mongo) locale
        listaMongoIds = getLista(ANNO_UNO);

        //--Recupera i (long) pageIds non più presenti nella category e da cancellare dal database (mongo) locale
        listaMongoIdsDaCancellare = service.deltaPageIds(listaMongoIds, listaPageIds, "listaMongoIdsDaCancellare");

        //--Cancella dal database (mongo) locale le entities non più presenti nella category <br>
        //--Saltato

        //--Recupera i (long) pageIds presenti nella category e non esistenti ancora nel database (mongo) locale e da creare
        listaPageIdsDaCreare = service.deltaPageIds(listaPageIds, listaMongoIds, "listaPageIdsDaCreare");

        //--Crea le nuove voci presenti nella category e non ancora esistenti nel database (mongo) locale
        //--Saltato

        //--Usa la lista di pageIds della categoria e recupera una lista (stessa lunghezza) di wrapTimes con l'ultima modifica sul server
        listaWrapTime = service.getListaWrapTime(listaPageIds);
        System.out.println(VUOTA);
        System.out.println(String.format("Lista dei %d wrapTimes col timestamp del server da controllare", listaWrapTime.size()));
        for (WrapTime wrap : listaWrapTime) {
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.print(wrap.getWikiTitle());
            System.out.print(SEP);
            System.out.println(wrap.getLastModifica());
        }
    }


    @Test
    @Order(7)
    @DisplayName("7 - ciclo elaboraListaWrapTime")
    void elaboraListaWrapTime() {
        List<Long> listaMongoIds;
        List<Long> listaMongoIdsDaCancellare;
        List<Long> listaPageIdsDaCreare;
        List<WrapTime> listaWrapTime;
        List<Long> listaPageIdsDaLeggere;
        System.out.println("7 - ciclo elaboraListaWrapTime");

        //--Controlla quante pagine ci sono nella categoria
        service.checkCategoria(CATEGORIA_UNO);

        //--Controlla il collegamento come bot
        //--Saltato perché la categoria è piccola

        //--Crea la lista di tutti i (long) pageIds della category
        listaPageIds = service.getListaPageIds(CATEGORIA_UNO);

        //--Crea la lista di tutti i (long) pageIds esistenti nel database (mongo) locale
        listaMongoIds = getLista(ANNO_UNO);

        //--Recupera i (long) pageIds non più presenti nella category e da cancellare dal database (mongo) locale
        listaMongoIdsDaCancellare = service.deltaPageIds(listaMongoIds, listaPageIds, "listaMongoIdsDaCancellare");

        //--Cancella dal database (mongo) locale le entities non più presenti nella category <br>
        //--Saltato

        //--Recupera i (long) pageIds presenti nella category e non esistenti ancora nel database (mongo) locale e da creare
        listaPageIdsDaCreare = service.deltaPageIds(listaPageIds, listaMongoIds, "listaPageIdsDaCreare");

        //--Crea le nuove voci presenti nella category e non ancora esistenti nel database (mongo) locale
        //--Saltato

        //--Usa la lista di pageIds della categoria e recupera una lista (stessa lunghezza) di wrapTimes con l'ultima modifica sul server
        listaWrapTime = service.getListaWrapTime(listaPageIds);

        //--Elabora la lista di wrapTimes e costruisce una lista di pageIds da leggere
        listaPageIdsDaLeggere = wikiBotService.elaboraWrapTime(listaWrapTime);
        System.out.println(VUOTA);
        System.out.println(String.format("Lista dei %d long modificati da leggere", listaPageIdsDaLeggere.size()));
        for (long lungo : listaPageIdsDaLeggere) {
            System.out.println(lungo);
        }
    }


    @Test
    @Order(8)
    @DisplayName("8 - ciclo getListaWrapBio")
    void getListaWrapBio() {
        List<Long> listaMongoIds;
        List<Long> listaMongoIdsDaCancellare;
        List<Long> listaPageIdsDaCreare;
        List<WrapTime> listaWrapTime;
        List<Long> listaPageIdsDaLeggere;
        List<WrapBio> listaWrapBio;
        System.out.println("8 - ciclo getListaWrapBio");

        //--Controlla quante pagine ci sono nella categoria
        service.checkCategoria(CATEGORIA_UNO);

        //--Controlla il collegamento come bot
        //--Saltato perché la categoria è piccola

        //--Crea la lista di tutti i (long) pageIds della category
        listaPageIds = service.getListaPageIds(CATEGORIA_UNO);

        //--Crea la lista di tutti i (long) pageIds esistenti nel database (mongo) locale
        listaMongoIds = getLista(ANNO_UNO);

        //--Recupera i (long) pageIds non più presenti nella category e da cancellare dal database (mongo) locale
        listaMongoIdsDaCancellare = service.deltaPageIds(listaMongoIds, listaPageIds, "listaMongoIdsDaCancellare");

        //--Cancella dal database (mongo) locale le entities non più presenti nella category <br>
        //--Saltato

        //--Recupera i (long) pageIds presenti nella category e non esistenti ancora nel database (mongo) locale e da creare
        listaPageIdsDaCreare = service.deltaPageIds(listaPageIds, listaMongoIds, "listaPageIdsDaCreare");

        //--Crea le nuove voci presenti nella category e non ancora esistenti nel database (mongo) locale
        //--Saltato

        //--Usa la lista di pageIds della categoria e recupera una lista (stessa lunghezza) di wrapTimes con l'ultima modifica sul server
        listaWrapTime = service.getListaWrapTime(listaPageIds);

        //--Elabora la lista di wrapTimes e costruisce una lista di pageIds da leggere
        listaPageIdsDaLeggere = wikiBotService.elaboraWrapTime(listaWrapTime);

        //--Legge tutte le pagine
        listaWrapBio = service.getListaWrapBio(listaPageIdsDaLeggere);
        System.out.println(VUOTA);
        System.out.println(String.format("Lista dei %d long modificati da leggere", listaWrapBio.size()));
        System.out.println("pageId, wikiTitle, server, mongo");
        for (WrapBio wrapBio : listaWrapBio) {
            System.out.print(wrapBio.getPageid());
            System.out.print(SEP);
            System.out.println(wrapBio.getTitle());
        }
    }

    List<Long> getLista(String anno) {
        List<Long> listaMongoIds = new ArrayList<>();
        List<Bio> listaBio = bioService.fetchAnnoNato(anno);
        for (Bio bio : listaBio) {
            listaMongoIds.add(bio.pageId);
        }

        return listaMongoIds;
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
