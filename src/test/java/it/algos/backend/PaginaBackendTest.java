package it.algos.backend;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import it.algos.*;
import it.algos.base.*;
import it.algos.wiki23.backend.packages.pagina.*;
import org.bson.*;
import org.bson.conversions.*;
import org.junit.jupiter.api.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 13-Nov-2022
 * Time: 19:26
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("backend")
@DisplayName("Pagina Backend")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaginaBackendTest extends WikiTest {

    /**
     * The Service.
     */
    @InjectMocks
    private PaginaBackend backend;

    @Autowired
    private PaginaRepository repository;


    private Pagina entityBean;


    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        Assertions.assertNotNull(backend);

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
    }


    @Test
    @Order(1)
    @DisplayName("1 - count")
    void count() {
        System.out.println("1 - count");
        String message;

        ottenutoIntero = bioBackend.count();
        assertTrue(ottenutoIntero > 0);
        message = String.format("Ci sono in totale %s entities nel database Bio", textService.format(ottenutoIntero));
        System.out.println(message);
    }


    @Test
    @Order(2)
    @DisplayName("2 - findAllWikiTitlePageId")
    void findAllWikiTitlePageId() {
        System.out.println("2 - findAllWikiTitlePageId");
        String message;

        listBio = bioBackend.findAllWikiTitlePageId();
        assertNotNull(listBio);
        message = String.format("Ci sono in totale %s entities di %s (wikiTitle, pageId)", textService.format(listBio.size()), "Bio");
        System.out.println(message);
        System.out.println("E sono state caricate in circa " + dateService.deltaText(inizio));

    }

    @Test
    @Order(3)
    @DisplayName("3 - findAllWrapTime")
    void findAllWrapTime() {
        System.out.println("3 - findAllWrapTime");
        String message;

        listMiniWrap = bioBackend.findAllWrapTime();
        assertNotNull(listMiniWrap);
        message = String.format("Ci sono in totale %s entities di %s (pageIdField, wikiTitleField, lastServerField)", textService.format(listMiniWrap.size()), "Bio");
        System.out.println(message);
        System.out.println("E sono state caricate in circa " + dateService.deltaText(inizio));

    }


    @Test
    @Order(4)
    @DisplayName("4 - annoNatoOrd")
    void annoNatoOrd() {
        System.out.println("4 - Valori di annoNatoOrd");
        String message;
        MongoCollection collection = mongoService.getCollection("bio");
        List<Integer> listaMaggiore = new ArrayList<>();
        List<Integer> listaValido = new ArrayList<>();
        List<Integer> listaErrato = new ArrayList<>();
        List<String> nonCodificati = new ArrayList<>();
        int soglia = 100;
        String wikiTitle=VUOTA;
        String natoTxt;
        int nato = 0;
        int natoOrd;
        int previsto;

        Bson projection = Projections.fields(Projections.include("wikiTitle","annoNato", "annoNatoOrd"), Projections.excludeId());
        var documents = collection.find().projection(projection);

        for (var doc : documents) {
            wikiTitle = ((Document) doc).get("wikiTitle", String.class);
            natoTxt = ((Document) doc).get("annoNato", String.class);
            natoOrd = ((Document) doc).get("annoNatoOrd", Integer.class);
            if (textService.isValid(natoTxt)) {
                nato = 0;
                try {
                    nato = Integer.decode(natoTxt);
                } catch (Exception unErrore) {
                    nonCodificati.add(natoTxt);
                    message = String.format("L'anno %s della voce %s non è gestibile", natoTxt, wikiTitle);
                    System.out.println(message);
                }
            }
            else {
                nato = 0;
            }
            if (natoOrd > soglia) {
                listaMaggiore.add(nato);
                previsto = check(nato);
                if (previsto == natoOrd) {
                    listaValido.add(nato);
                }
                else {
                    listaErrato.add(nato);
                }
            }
        }

        Object alfa = listaMaggiore;
        Object beta = listaValido;
        Object gamma = listaErrato;
        listMiniWrap = bioBackend.findAllWrapTime();
        assertNotNull(listMiniWrap);
        message = String.format("Ci sono in totale %s entities di %s (pageIdField, wikiTitleField, lastServerField)", textService.format(listMiniWrap.size()), "Bio");
        System.out.println(message);
        System.out.println("E sono state caricate in circa " + dateService.deltaText(inizio));

    }

    private int check(int valueIn) {
        int valueOut = valueIn;
        int deltaInizio = 1000;
        int deltaMoltiplicatore = 100;

        valueOut = valueOut + deltaInizio;
        valueOut = valueOut * deltaMoltiplicatore;
        return valueOut;
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

    void printBeans(List<Pagina> listaBeans) {
        System.out.println(VUOTA);
        int k = 0;

        for (Pagina bean : listaBeans) {
            System.out.print(++k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(bean);
        }
    }

}