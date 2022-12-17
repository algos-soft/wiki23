package it.algos.integration.service;

import com.mongodb.client.*;
import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.interfaces.*;
import it.algos.vaad24.backend.packages.crono.giorno.*;
import it.algos.vaad24.backend.packages.crono.mese.*;
import it.algos.vaad24.backend.packages.geografia.continente.*;
import it.algos.vaad24.backend.packages.utility.test.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.ui.views.*;
import org.bson.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit.jupiter.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: Wed, 01-Jun-2022
 * Time: 18:30
 */
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("service")
@DisplayName("Mongo Service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongoServiceTest extends AlgosIntegrationTest {

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private MongoService service;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MeseBackend meseBackend;

    private MongoDatabase dataBase;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    private MongoCollection<Document> collection;

    //--clazz
    //--esiste collezione
    //--previstoIntero
    //--risultatoEsatto
    protected static Stream<Arguments> CLAZZ() {
        return Stream.of(
                Arguments.of(null, false, 0, false),
                Arguments.of(CrudView.class, false, 0, false),
                Arguments.of(AIType.class, false, 0, true),
                Arguments.of(Mese.class, true, 12, true),
                Arguments.of(Continente.class, true, 7, true),
                Arguments.of(Giorno.class, true, 366, true)
                //                Arguments.of(Via.class, false, 25, false)
                //                Arguments.of(Stato.class, false, 249, true),
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

        //--reindirizzo l'istanza della superclasse
        service = mongoService;
    }

    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito tutte le referenze 'mockate' <br>
     * Nelle sottoclassi devono essere regolati i riferimenti dei service specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixRiferimentiIncrociati() {
    }

    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    protected void setUpEach() {
        super.setUpEach();

        collection = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - Stato del database")
    void status() {
        System.out.println("1 - Stato del database");

        ottenuto = service.getDatabaseName();
        assertTrue(textService.isValid(ottenuto));
        System.out.println(VUOTA);
        System.out.println(String.format("Nome del dataBase corrente: [%s]", ottenuto));

        dataBase = service.getDataBase();
        assertNotNull(dataBase);
        System.out.println(VUOTA);
        System.out.println(String.format("DataBase corrente: [%s]", dataBase));

        listaStr = service.getCollezioni();
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(String.format("Collezioni esistenti: %s", listaStr));
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ")
    @Order(2)
    @DisplayName("2 - Collezioni del database")
    /*
      Controlla l'esistenza della collezione (dall'elenco di tutte le condizioni esistenti nel mongoDB)
     */
        //--clazz
        //--esiste collezione
    void collectionClazz(final Class clazz, final boolean esisteCollezione) {
        System.out.println("2 - Controlla l'esistenza della collezione");
        String message = String.format("Clazz%s%s", FORWARD, getSimpleName(clazz));
        System.out.println(message);
        System.out.println(VUOTA);

        ottenutoBooleano = service.isExistsCollection(clazz);
        assertEquals(esisteCollezione, ottenutoBooleano);

        if (ottenutoBooleano) {
            message = String.format("La clazz %s ha una corrispondente collection", getSimpleName(clazz));
        }
        else {
            message = String.format("La clazz %s non ha collection", getSimpleName(clazz));
        }
        System.out.println(message);
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ")
    @Order(3)
    @DisplayName("3 - Recupera la collezione")
    /*
      Recupera la collezione
     */
        //--clazz
        //--esiste collezione
    void collectionClazz3(final Class clazz, final boolean esisteCollezione) {
        System.out.println("3 - Recupera la collezione");
        String message = String.format("Clazz%s%s", FORWARD, getSimpleName(clazz));
        System.out.println(message);
        System.out.println(VUOTA);

        sorgente = clazz != null ? clazz.getSimpleName() : VUOTA;
        collection = service.getCollection(sorgente);
        assertEquals(esisteCollezione, collection != null);
        if (esisteCollezione) {
            message = String.format("La clazz %s ha una corrispondente collection%s%s", getSimpleName(clazz), FORWARD,
                    collection.toString()
            );
        }
        else {
            message = String.format("La clazz %s non ha collection", getSimpleName(clazz));
        }
        System.out.println(message);
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ")
    @Order(4)
    @DisplayName("4 - Count della collezione")
    /*
      Numero di entities totali presenti nella collezione
     */
        //--clazz
        //--esiste collezione
        //--previstoIntero
        //--risultatoEsatto
    void collectionClazz4(final Class clazz, final boolean nonUsato, final int previstoIntero, final boolean esatto) {
        System.out.println("4 - Count della collezione");
        String message = String.format("Clazz%s%s", FORWARD, getSimpleName(clazz));
        System.out.println(message);
        System.out.println(VUOTA);
        String clazzName = clazz != null ? clazz.getSimpleName() : "()";

        ottenutoIntero = service.count(clazz);

        if (ottenutoIntero == previstoIntero) {
            message = String.format("La collezione '%s' contiene %s records (entities) che sono i %s previsti", clazzName, ottenutoIntero, previstoIntero);
        }
        else {
            if (esatto) {
                message = String.format("La collezione '%s' contiene %s records (entities) che non sono i %s previsti", clazzName,
                        ottenutoIntero, previstoIntero
                );
            }
            else {
                if (ottenutoIntero == 0) {
                    message = String.format("La collezione '%s' non contiene records (entities) che mentre ne sono previsti %s", clazzName, previstoIntero);
                }
                else {
                    message = String.format("La collezione '%s' contiene %s records (entities) che sono diversi dai %s previsti", clazzName, ottenutoIntero, previstoIntero);
                }
            }
        }
        System.out.println(message);
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ")
    @Order(5)
    @DisplayName("5 - Collezioni del database")
    /*
      Controlla se la collezione è vuota (dal numero di entities presenti)
     */
        //--clazz
        //--esiste collezione
    void collectionClazz5(final Class clazz, final boolean esisteCollezione) {
        System.out.println("5 - Controlla se la collezione è vuota (dal numero di entities presenti)");
        String message = String.format("Clazz%s%s", FORWARD, getSimpleName(clazz));
        System.out.println(message);
        System.out.println(VUOTA);

        ottenutoBooleano = service.isExistsCollection(clazz);
        assertEquals(esisteCollezione, ottenutoBooleano);

        if (ottenutoBooleano) {
            message = String.format("La clazz %s ha una corrispondente collection", getSimpleName(clazz));
        }
        else {
            message = String.format("La clazz %s non ha collection", getSimpleName(clazz));
        }
        System.out.println(message);
        System.out.println(VUOTA);
    }


    @Test
    @Order(6)
    @DisplayName("6 - Query semplice all entities")
    void query() {
        System.out.println("6 - Query semplice all entities");
        System.out.println(VUOTA);

        clazz = Mese.class;
        List<Mese> lista = service.query(clazz);
        assertEquals(12, lista.size());
        message = String.format("La query per la clazz %s fornisce una lista di %d entities", getSimpleName(clazz), lista.size());
        System.out.println(message);
    }


    @Test
    @Order(7)
    @DisplayName("7 - Query semplice all entities")
    void queryProperty() {
        System.out.println("7 - Query semplice all entities");
        System.out.println(VUOTA);

        clazz = Mese.class;
        sorgente = "nome";
        List<String> listaNomi = service.projectionString(clazz, sorgente);
        assertEquals(12, listaNomi.size());
        message = String.format(
                "La query per la clazz %s fornisce una lista di %d entities sotto forma di una singola property",
                getSimpleName(clazz),
                listaNomi.size()
        );
        System.out.println(message);
        System.out.println(VUOTA);
        for (String nome : listaNomi) {
            System.out.println(nome);
        }
    }


    @Test
    @Order(8)
    @DisplayName("8 - Query exclude")
    void projectionExclude() {
        System.out.println("8 - Query exclude");
        System.out.println(VUOTA);

        clazz = Mese.class;
        sorgente = "giorni";
        List<Mese> listaMesi = service.projectionExclude(clazz, meseBackend, sorgente);
        assertEquals(12, listaMesi.size());
        message = String.format(
                "La query per la clazz %s fornisce una lista di %d entities sotto forma di una entityBean senza una property",
                getSimpleName(clazz),
                listaMesi.size()
        );
        System.out.println(message);
        System.out.println(VUOTA);
        for (Mese mese : listaMesi) {
            System.out.println(mese.breve);
        }
    }

    @Test
    @Order(9)
    @DisplayName("9 - Delete")
    void delete() {
        System.out.println("9 - Delete");
        System.out.println(VUOTA);
        clazz = Prova.class;
        service.deleteAll(clazz);
    }

    @Test
    @Order(10)
    @DisplayName("10 - Prova")
    void prova() {
        System.out.println("10 - Prova");
        Prova istanzaProva;
        clazz = Prova.class;
        sorgente = "prova";
        sorgente2 = "test";
        sorgente3 = "alfa";

        ottenutoBooleano = service.isExistsCollection(clazz);
        assertFalse(ottenutoBooleano);
        System.out.println(VUOTA);
        System.out.println(String.format("La collection %s non esiste", sorgente));

        ottenutoBooleano = service.isCollectionNullOrEmpty(clazz);
        assertTrue(ottenutoBooleano);
        System.out.println(VUOTA);
        System.out.println(String.format("La collection %s non esiste ed è vuota", sorgente));

        istanzaProva = new Prova();
        istanzaProva.id = sorgente3;
        istanzaProva.nome = sorgente2;
        istanzaProva = service.mongoOp.insert(istanzaProva);
        System.out.println(VUOTA);
        System.out.println(String.format("Aggiunta una entity di %s", sorgente));

        ottenutoIntero = service.count(sorgente);
        assertEquals(1, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella collection %s c'è 1 entity", sorgente));

        ottenutoBooleano = service.isCollectionNullOrEmpty(clazz);
        assertFalse(ottenutoBooleano);
        System.out.println(VUOTA);
        System.out.println(String.format("La collection %s non è vuota", sorgente));

        service.delete(istanzaProva);
        System.out.println(VUOTA);
        System.out.println(String.format("Cancellata la entity %s", sorgente2));

        ottenutoIntero = service.count(sorgente);
        assertEquals(0, ottenutoIntero);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella collection %s ci sono zero entities", sorgente));

        ottenutoBooleano = service.isExistsCollection(clazz);
        assertTrue(ottenutoBooleano);
        System.out.println(VUOTA);
        System.out.println(String.format("Esiste la collection %s%s%s", sorgente, FORWARD, ottenutoBooleano));

        ottenutoBooleano = service.isCollectionNullOrEmpty(clazz);
        assertTrue(ottenutoBooleano);
        System.out.println(VUOTA);
        System.out.println(String.format("La collection %s esiste ed è vuota%s%s", sorgente, FORWARD, ottenutoBooleano));

        service.deleteAll(clazz);
        System.out.println(VUOTA);
        System.out.println(String.format("Cancellata la collection %s", sorgente));

        ottenutoBooleano = service.isExistsCollection(clazz);
        assertFalse(ottenutoBooleano);
        System.out.println(VUOTA);
        System.out.println(String.format("La collection %s non esiste", sorgente));
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
