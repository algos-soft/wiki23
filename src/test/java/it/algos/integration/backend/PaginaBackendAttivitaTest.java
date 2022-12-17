package it.algos.integration.backend;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.pagina.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Thu, 10-Nov-2022
 * Time: 10:25
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Pagina PaginaBackendAttivita")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaginaBackendAttivitaTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private PaginaBackend backend;

    @Autowired
    private PaginaRepository repository;


    private Pagina entityBean;


    private List<Pagina> listaBeans;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();
        backend = paginaBackend;

        backend.repository = repository;
        backend.crudRepository = repository;
        backend.arrayService = arrayService;
        backend.reflectionService = reflectionService;
    }


    /**
     * Inizializzazione dei service <br>
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void initMocks() {
        super.initMocks();
    }


    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
        super.fixRiferimentiIncrociati();
    }

    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        this.entityBean = null;
        this.listaBeans = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - count (mongoDB)")
    void count() {
        System.out.println("1 - count (mongoDB)");
        String message;

        ottenutoIntero = backend.count();
        message = String.format("Ci sono in totale %s entities nel database mongoDB", textService.format(ottenutoIntero));
        System.out.println(message);
    }

    @Test
    @Order(2)
    @DisplayName("2 - findAll (mongoDB)")
    void findAll() {
        System.out.println("2 - findAll (mongoDB)");
        String message;

        listaBeans = backend.findAll();
        assertNotNull(listaBeans);
        message = String.format("Ci sono in totale %s entities di %s su mongoDB", textService.format(listaBeans.size()), "Pagina");
        System.out.println(message);
    }


    @Test
    @Order(3)
    @DisplayName("3 - pagine di attività sul server")
    void getList() {
        System.out.println("3 - pagine di attività sul server");
        String message;

        sorgente = "Biografie/Attività/";
        sorgenteIntero = 102;
        ottenutoArray = queryService.getList(sorgente, sorgenteIntero);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);
        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine che iniziano con %s", textService.format(ottenutoArray.size()), sorgente);
        System.out.println(message);
        System.out.println(VUOTA);
        super.print(ottenutoArray);
    }

    @Test
    @Order(4)
    @DisplayName("4 - pagine di attività di primo livello sul server")
    void getList4() {
        System.out.println("4 - pagine di attività di primo livello sul server");
        String message;

        sorgente = "Biografie/Attività/";
        sorgenteIntero = 102;
        sorgenteArray = queryService.getList(sorgente, sorgenteIntero);
        ottenutoArray = backend.getPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);
        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine di attività di primo livello con %s", textService.format(ottenutoArray.size()), sorgente);
        System.out.println(message);
        System.out.println(VUOTA);
        print(ottenutoArray);
    }


    @Test
    @Order(5)
    @DisplayName("5 - pagine di attività di secondo livello sul server")
    void getList5() {
        System.out.println("5 - pagine di attività di secondo livello sul server");
        String message;

        sorgente = "Biografie/Attività/";
        sorgenteIntero = 102;
        sorgenteArray = queryService.getList(sorgente, sorgenteIntero);
        ottenutoArray = backend.getSottoPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);
        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine di attività di secondo livello con %s", textService.format(ottenutoArray.size()), sorgente);
        System.out.println(message);
        System.out.println(VUOTA);
        print(ottenutoArray);
    }


    @Test
    @Order(6)
    @DisplayName("6 - pagine di attività di terzo livello senza nazionalità sul server")
    void getList6() {
        System.out.println("6 - pagine di attività di terzo livello senza nazionalità sul server");
        String message;

        sorgente = "Biografie/Attività/";
        sorgenteIntero = 102;
        sorgenteArray = queryService.getList(sorgente, sorgenteIntero);
        ottenutoArray = backend.getSottoSottoPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);
        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine di attività di terzo livello con %s", textService.format(ottenutoArray.size()), sorgente);
        System.out.println(message);
        System.out.println(VUOTA);
        print(ottenutoArray);
    }

    @Test
    @Order(7)
    @DisplayName("7 - count pagine di attività di primo livello su mongoDB")
    void getList7() {
        System.out.println("7 - count pagine di attività di primo livello su mongoDB");
        String tagBase = PATH_ATTIVITA + SLASH;
        String message;
        int cont = 0;
        int cancellande = 0;
        int min = 50;

        sorgente = "Biografie/Attività/";
        sorgenteIntero = 102;
        sorgenteArray = queryService.getList(sorgente, sorgenteIntero);
        ottenutoArray = backend.getPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);

        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine di attività di primo livello con %s su mongoDB", textService.format(ottenutoArray.size()), sorgente);
        System.out.println(message);
        System.out.println(VUOTA);
        for (String wikiTitle : ottenutoArray) {
            wikiTitle = textService.levaTesta(wikiTitle, tagBase);
            wikiTitle = textService.primaMinuscola(wikiTitle);
            ottenutoIntero = bioBackend.countAttivitaPlurale(wikiTitle);
            cancellande = cancellande < min ? cancellande++ : cancellande;
        }
        message = String.format("Ci sono %d pagine da cancellare", cancellande);
        System.out.println(message);

        System.out.println(VUOTA);
        for (String wikiTitle : ottenutoArray) {
            wikiTitle = textService.levaTesta(wikiTitle, tagBase);
            wikiTitle = textService.primaMinuscola(wikiTitle);
            ottenutoIntero = bioBackend.countAttivitaPlurale(wikiTitle);
            printAttivita(min, ++cont, wikiTitle, ottenutoIntero);
        }
    }


//    @Test
    @Order(8)
    @DisplayName("8 - count pagine di attività di secondo livello su mongoDB")
    void getList8() {
        System.out.println("8 - count pagine di attività di secondo livello su mongoDB");
        String tagBase = PATH_ATTIVITA + SLASH;
        String message;
        int cont = 0;
        int daCancellare = 0;
        int min = 40;
        String paginaParentePrimoLivello;
        String attivita;
        String nazionalita;

        sorgente = "Biografie/Attività/";
        sorgenteIntero = 102;
        sorgenteArray = queryService.getList(sorgente, sorgenteIntero);
        ottenutoArray = backend.getSottoPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);

        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine di attività di secondo livello con %s su mongoDB", textService.format(ottenutoArray.size()), sorgente);
        System.out.println(message);
        System.out.println(VUOTA);
        for (String wikiTitle : ottenutoArray) {
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            attivita = textService.levaTesta(paginaParentePrimoLivello, tagBase);
            attivita = textService.primaMinuscola(attivita);
            nazionalita = wikiTitle.substring(wikiTitle.lastIndexOf(SLASH) + 1);
            nazionalita = textService.primaMinuscola(nazionalita);
            ottenutoIntero = bioBackend.countAttivitaNazionalitaAll(attivita, nazionalita);
            daCancellare = ottenutoIntero < min ? daCancellare + 1 : daCancellare;
        }
        message = String.format("Ci sono %d pagine da cancellare", daCancellare);
        System.out.println(message);

        System.out.println(VUOTA);
        for (String wikiTitle : ottenutoArray) {
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            attivita = textService.levaTesta(paginaParentePrimoLivello, tagBase);
            attivita = textService.primaMinuscola(attivita);
            nazionalita = wikiTitle.substring(wikiTitle.lastIndexOf(SLASH) + 1);
            nazionalita = textService.primaMinuscola(nazionalita);
            ottenutoIntero = bioBackend.countAttivitaNazionalitaAll(attivita, nazionalita);
            printAttivita(min, ++cont, wikiTitle, ottenutoIntero);
        }
    }


    @Test
    @Order(9)
    @DisplayName("9 - count pagine di attività di terzo livello su mongoDB")
    void getList9() {
        System.out.println("9 - count pagine di attività di terzo livello su mongoDB");
        String tagBase = PATH_ATTIVITA + SLASH;
        String message;
        int cont = 0;
        int daCancellare = 0;
        int min = 40;
        String paginaParentePrimoLivello;
        String paginaParenteSecondoLivello;
        String letteraIniziale;
        String attivita;
        String nazionalita;

        sorgente = "Biografie/Attività/";
        sorgenteIntero = 102;
        sorgenteArray = queryService.getList(sorgente, sorgenteIntero);
        ottenutoArray = backend.getSottoSottoPagine(sorgenteArray);
        assertNotNull(ottenutoArray);
        assertTrue(ottenutoArray.size() > 0);

        System.out.println(VUOTA);
        message = String.format("Ci sono %s pagine di attività di terzo livello con %s su mongoDB", textService.format(ottenutoArray.size()), sorgente);
        System.out.println(message);
        System.out.println(VUOTA);
        for (String wikiTitle : ottenutoArray) {
            paginaParenteSecondoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(paginaParenteSecondoLivello, SLASH);
            letteraIniziale = wikiTitle.substring(wikiTitle.lastIndexOf(SLASH) + 1);
            attivita = textService.levaTesta(paginaParentePrimoLivello, tagBase);
            attivita = textService.primaMinuscola(attivita);
            nazionalita = paginaParenteSecondoLivello.substring(paginaParenteSecondoLivello.lastIndexOf(SLASH) + 1);
            nazionalita = nazionalita.equals(TAG_LISTA_ALTRE) ? nazionalita : textService.primaMinuscola(nazionalita);
            ottenutoIntero = bioBackend.countAttivitaNazionalitaAll(attivita, nazionalita, letteraIniziale);
            daCancellare = ottenutoIntero < min ? daCancellare + 1 : daCancellare;
        }
        message = String.format("Ci sono %d pagine da cancellare", daCancellare);
        System.out.println(message);

        System.out.println(VUOTA);
        for (String wikiTitle : ottenutoArray) {
            paginaParenteSecondoLivello = textService.levaCodaDaUltimo(wikiTitle, SLASH);
            paginaParentePrimoLivello = textService.levaCodaDaUltimo(paginaParenteSecondoLivello, SLASH);
            letteraIniziale = wikiTitle.substring(wikiTitle.lastIndexOf(SLASH) + 1);
            attivita = textService.levaTesta(paginaParentePrimoLivello, tagBase);
            attivita = textService.primaMinuscola(attivita);
            nazionalita = paginaParenteSecondoLivello.substring(paginaParenteSecondoLivello.lastIndexOf(SLASH) + 1);
            nazionalita = nazionalita.equals(TAG_LISTA_ALTRE) ? nazionalita : textService.primaMinuscola(nazionalita);
            ottenutoIntero = bioBackend.countAttivitaNazionalitaAll(attivita, nazionalita, letteraIniziale);
            printAttivita(min, ++cont, wikiTitle, ottenutoIntero);
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

    protected void printAttivita(int min, int cont, String wikiTitle, int numVoci) {
        System.out.print(cont);
        System.out.print(PARENTESI_TONDA_END);
        System.out.print(SPAZIO);
        System.out.print(wikiTitle);
        System.out.print(FORWARD);
        System.out.print(numVoci);
        if (numVoci < min) {
            System.out.print(SPAZIO);
            System.out.print(SPAZIO);
            System.out.print(SPAZIO);
            System.out.print("ATTENZIONE - Questa va cancellata");
        }
        System.out.println(VUOTA);
    }

    protected void print(List<String> lista) {
        String tag = "Progetto:Biografie/Attività/";

        lista = lista
                .stream()
                .map(title -> title.substring(tag.length()))
                .collect(Collectors.toList());

        super.print(lista);
    }

}