package it.algos.unit.query;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: sab, 14-mag-2022
 * Time: 10:48
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Test Download")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DownloadTest extends WikiTest {


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
     * Qui passa prima di ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();
    }


    @Test
    @Order(1)
    @DisplayName("1- Download singolo di 2 (circa) pagine")
    void singolo() {
        System.out.println(("1- Download singolo di 2 (circa) pagine"));
        String message;

        sorgente = CATEGORIA_ESISTENTE_TRE;
        List<Long> lista = appContext.getBean(QueryCat.class).getListaPageIds(sorgente);
        assertNotNull(lista);

        inizio = System.currentTimeMillis();
        for (Long pageid : lista) {
            appContext.getBean(QueryBio.class).getWrap(pageid);
        }

        String time = dateService.deltaTextEsatto(inizio);
        message = String.format("Per leggere %d pagine ha impiegato %s", lista.size(), time);
        System.out.println(message);
    }


    @Test
    @Order(2)
    @DisplayName("2- Download multiplo di più pagine")
    void multiplo() {
        System.out.println(("2- Download multiplo di più pagine"));
        String message;
        String time;

        sorgente = CATEGORIA_ESISTENTE_TRE;
        listaPageIds = appContext.getBean(QueryCat.class).getListaPageIds(sorgente);
        assertNotNull(listaPageIds);

        sorgente2 = arrayService.toStringaPipe(listaPageIds);
        inizio = System.currentTimeMillis();
        ottenutoRisultato = appContext.getBean(QueryBio.class).urlRequest(sorgente2);

        time = dateService.deltaTextEsatto(inizio);
        message = String.format("Per leggere %d pagine ha impiegato %s", listaPageIds.size(), time);
        System.out.println(ottenutoRisultato.isValido()?message:VUOTA);
        printRisultato(ottenutoRisultato);

        sorgente = CATEGORIA_ESISTENTE_UNO;
        listaPageIds = appContext.getBean(QueryCat.class).getListaPageIds(sorgente);
        assertNotNull(listaPageIds);

        sorgente2 = arrayService.toStringaPipe(listaPageIds);
        inizio = System.currentTimeMillis();
        ottenutoRisultato = appContext.getBean(QueryBio.class).urlRequest(sorgente2);

        time = dateService.deltaTextEsatto(inizio);
        message = String.format("Per leggere %d pagine ha impiegato %s", listaPageIds.size(), time);
        System.out.println(ottenutoRisultato.isValido()?message:VUOTA);
        printRisultato(ottenutoRisultato);
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