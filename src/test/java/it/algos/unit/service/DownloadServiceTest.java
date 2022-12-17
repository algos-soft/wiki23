package it.algos.unit.service;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.service.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mer, 18-mag-2022
 * Time: 19:52
 * <p>
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("service")
@DisplayName("Download service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DownloadServiceTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private DownloadService service;

    /**
     * Execute only once before running all tests <br>
     * Esegue una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpAll() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = downloadService;
    }


    /**
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUpEach() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    @Test
    @Order(11)
    @DisplayName("1 - Conta la categoria principale")
    void ciclo() {
        System.out.println(("1 - Conta la categoria principale"));
        System.out.println((VUOTA));
        service.checkCategoria(CATEGORIA_PRINCIPALE_BIOBOT);
        service.checkBot();
    }

    @Test
    @Order(2)
    @DisplayName("2 - Diversi collegamenti")
    void ciclo2() {
        System.out.println(("2 - Conta una categoria media in diversi collegamenti"));
        sorgente = CATEGORIA_ESISTENTE_MEDIA;

        //--collegato come anonymous
        System.out.println((VUOTA));
        service.checkCategoria(sorgente);
        service.checkBot();

        //--si collega come user
        System.out.println((VUOTA));
        queryService.logAsUser();
        service.checkCategoria(sorgente);
        service.checkBot();

        //--si collega come admin
        System.out.println((VUOTA));
        queryService.logAsAdmin();
        service.checkCategoria(sorgente);
        service.checkBot();

        //--si collega come bot
        System.out.println((VUOTA));
        queryService.logAsBot();
        service.checkCategoria(sorgente);
        service.checkBot();
    }

//    @Test
    @Order(3)
    @DisplayName("3 - Ciclo cat+pageIds")
    void ciclo3() {
        System.out.println(("3 - Ciclo (parziale) cat+pageIds"));

        sorgente = CATEGORIA_ESISTENTE_MEDIA;
        System.out.println((VUOTA));
        service.checkCategoria(sorgente);
        service.checkBot();
        listaPageIds = service.getListaPageIds(sorgente);

        sorgente = CATEGORIA_ESISTENTE_LUNGA;
        System.out.println((VUOTA));
        service.checkCategoria(sorgente);
        service.checkBot();
        listaPageIds = service.getListaPageIds(sorgente);

        sorgente = CATEGORIA_ESISTENTE_LUNGA;
        System.out.println((VUOTA));
        service.checkCategoria(sorgente);
        queryService.logAsBot();
        service.checkBot();
        listaPageIds = service.getListaPageIds(sorgente);
        printLong(listaPageIds,10);
    }

//    @Test
    @Order(4)
    @DisplayName("4 - Ciclo cat+pageIds+listMiniWrap")
    void ciclo4() {
        System.out.println(("4 - Ciclo (parziale) cat+pageIds+listMiniWrap"));
        queryService.logAsBot();

        sorgente = CATEGORIA_ESISTENTE_LUNGA;
        System.out.println((VUOTA));
        service.checkCategoria(sorgente);
        queryService.logAsBot();
        service.checkBot();
        listaPageIds = service.getListaPageIds(sorgente);
        printLong(listaPageIds,10);

        System.out.println((VUOTA));
        listMiniWrap = service.getListaWrapTime(listaPageIds);
        printMiniWrap(listMiniWrap.subList(0, Math.min(10, listMiniWrap.size())));
    }


//    @Test
    @Order(5)
    @DisplayName("5 - Ciclo cat+pageIds+listMiniWrap+listaPageIdsDaLeggere")
    void ciclo5() {
        System.out.println(("5 - Ciclo (parziale) cat+pageIds+listMiniWrap+listaPageIdsDaLeggere"));
        queryService.logAsBot();

        sorgente = CATEGORIA_ESISTENTE_LUNGA;
        System.out.println((VUOTA));
        service.checkCategoria(sorgente);
        queryService.logAsBot();
        service.checkBot();
        listaPageIds = service.getListaPageIds(sorgente);
        printLong(listaPageIds,10);

        System.out.println((VUOTA));
        listMiniWrap = service.getListaWrapTime(listaPageIds);
        printMiniWrap(listMiniWrap.subList(0, Math.min(10, listMiniWrap.size())));

        System.out.println((VUOTA));
        listaPageIds = service.elaboraListaWrapTime(listMiniWrap);
        printLong(listaPageIds,10);
    }

//    @Test
    @Order(6)
    @DisplayName("6 - Ciclo cat+pageIds+listMiniWrap+listaPageIdsDaLeggere+listaWrapBio")
    void ciclo6() {
        System.out.println(("6 - Ciclo (parziale) cat+pageIds+listMiniWrap+listaPageIdsDaLeggere+listaWrapBio"));
        queryService.logAsBot();

        sorgente = CATEGORIA_ESISTENTE_MEDIA;
        System.out.println((VUOTA));
        service.checkCategoria(sorgente);
        queryService.logAsBot();
        service.checkBot();
        listaPageIds = service.getListaPageIds(sorgente);
        printLong(listaPageIds,10);

        System.out.println((VUOTA));
        listMiniWrap = service.getListaWrapTime(listaPageIds);
        printMiniWrap(listMiniWrap.subList(0, Math.min(10, listMiniWrap.size())));

        System.out.println((VUOTA));
        listaPageIds = service.elaboraListaWrapTime(listMiniWrap);
        printLong(listaPageIds,10);

        System.out.println((VUOTA));
        listWrapBio = service.getListaWrapBio(listaPageIds);
        printWrapBio(listWrapBio.subList(0,Math.min(10,listWrapBio.size())));
    }


//    @Test
    @Order(7)
    @DisplayName("7 - Ciclo cat+pageIds+listMiniWrap+listaPageIdsDaLeggere+listaWrapBio+creaElaboraBio")
    void ciclo7() {
        System.out.println(("7 - Ciclo cat+pageIds+listMiniWrap+listaPageIdsDaLeggere+listaWrapBio+creaElaboraBio"));
        queryService.logAsBot();

        sorgente = CATEGORIA_ESISTENTE_MEDIA;
        System.out.println((VUOTA));
        service.checkCategoria(sorgente);
        queryService.logAsBot();
        service.checkBot();
        listaPageIds = service.getListaPageIds(sorgente);
        printLong(listaPageIds,10);

        System.out.println((VUOTA));
        listMiniWrap = service.getListaWrapTime(listaPageIds);
        printMiniWrap(listMiniWrap.subList(0, Math.min(10, listMiniWrap.size())));

        System.out.println((VUOTA));
        listaPageIds = service.elaboraListaWrapTime(listMiniWrap);
        printLong(listaPageIds,10);

        System.out.println((VUOTA));
        listWrapBio = service.getListaWrapBio(listaPageIds);
        printWrapBio(listWrapBio.subList(0,Math.min(10,listWrapBio.size())));

        System.out.println((VUOTA));
        service.creaElaboraListaBio(listWrapBio);
    }


//    @Test
    @Order(1)
    @DisplayName("8 - Ciclo completo")
    void ciclo8() {
        System.out.println(("8 - Ciclo completo"));
        queryService.logAsBot();

        sorgente = CATEGORIA_ESISTENTE_MEDIA;
        service.cicloCorrente(sorgente);
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