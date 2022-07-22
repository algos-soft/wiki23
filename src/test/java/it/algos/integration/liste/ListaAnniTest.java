package it.algos.integration.liste;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.test.context.junit.jupiter.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 17-Jul-2022
 * Time: 06:44
 * Unit test di una classe service o backend o query <br>
 * Estende la classe astratta AlgosTest che contiene le regolazioni essenziali <br>
 * Nella superclasse AlgosTest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse AlgosTest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("liste")
@DisplayName("Anni lista")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaAnniTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private ListaAnni istanza;

    //--nome anno
    //--typeCrono
    protected static Stream<Arguments> ANNI() {
        return Stream.of(
                Arguments.of(VUOTA, AETypeCrono.nascita),
                Arguments.of(VUOTA, AETypeCrono.morte),
                Arguments.of("1782", AETypeCrono.nascita),
                Arguments.of("1782", AETypeCrono.morte),
                Arguments.of("325", AETypeCrono.nascita),
                Arguments.of("325", AETypeCrono.morte),
                Arguments.of("1318", AETypeCrono.nascita),
                Arguments.of("1318", AETypeCrono.morte),
                Arguments.of("245 a.C.", AETypeCrono.nascita),
                Arguments.of("245 a.C.", AETypeCrono.morte),
                Arguments.of("4 a.C.", AETypeCrono.nascita),
                Arguments.of("4 a.C.", AETypeCrono.morte),
                Arguments.of("12", AETypeCrono.nascita),
                Arguments.of("12", AETypeCrono.morte)

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
    @DisplayName("1 - Costruttore base senza parametri")
    void costruttoreBase() {
        istanza = new ListaAnni();
        assertNotNull(istanza);
        System.out.println(("1 - Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }


    //    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(2)
    @DisplayName("2 - Lista bio di vari anni")
    //--nome anno
    //--typeCrono
    void listaBio(final String nomeAnno, final AETypeCrono type) {
        System.out.println("2 - Lista bio di vari anni");
        sorgente = nomeAnno;

        listBio = switch (type) {
            case nascita -> appContext.getBean(ListaAnni.class).nascita(sorgente).listaBio();
            case morte -> appContext.getBean(ListaAnni.class).morte(sorgente).listaBio();
        };

        if (listBio != null && listBio.size() > 0) {
            message = String.format("Ci sono %d biografie che implementano l'anno di %s %s", listBio.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            switch (type) {
                case nascita -> printBioListaAnniNato(listBio);
                case morte -> printBioListaAnniMorto(listBio);
            }
        }
        else {
            message = "La listBio è nulla";
            System.out.println(message);
        }
    }


    //    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(3)
    @DisplayName("3 - Lista wrapDidascalia di vari anni")
    //--nome anno
    //--typeCrono
    void listaWrapDidascalie(final String nomeAnno, final AETypeCrono type) {
        System.out.println("3 - Lista wrapDidascalia di vari anni");
        sorgente = nomeAnno;

        listWrapDidascalia = switch (type) {
            case nascita -> appContext.getBean(ListaAnni.class).nascita(sorgente).listaWrapDidascalie();
            case morte -> appContext.getBean(ListaAnni.class).morte(sorgente).listaWrapDidascalie();
            default -> null;
        };

        if (listWrapDidascalia != null) {
            message = String.format("Ci sono %d wrapDidascalia che implementano l'anno di %s %s", listWrapDidascalia.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            switch (type) {
                case nascita -> printWrapListaAnniNato(listWrapDidascalia);
                case morte -> printWrapListaAnniMorto(listWrapDidascalia);
            }
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }

    //    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(4)
    @DisplayName("4 - Mappa wrapDidascalia di vari anni")
    //--nome anno
    //--typeCrono
    void mappaWrapDidascalie(final String nomeAnno, final AETypeCrono type) {
        System.out.println("4 - Mappa wrapDidascalia di vari anni");
        sorgente = nomeAnno;
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie;

        mappaWrapDidascalie = switch (type) {
            case nascita -> appContext.getBean(ListaAnni.class).nascita(sorgente).mappaWrapDidascalie();
            case morte -> appContext.getBean(ListaAnni.class).morte(sorgente).mappaWrapDidascalie();
            default -> null;
        };

        if (mappaWrapDidascalie != null) {
            message = String.format("WrapDidascalie che implementano l'anno di %s %s", type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printMappaWrapDidascalia(sorgente, mappaWrapDidascalie);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }

    //    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(5)
    @DisplayName("5 - Mappa didascalie di vari anni")
    //--nome anno
    //--typeCrono
    void mappaDidascalie(final String nomeAnno, final AETypeCrono type) {
        System.out.println("5 - Mappa didascalie di vari anni");
        sorgente = nomeAnno;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie;

        mappaDidascalie = switch (type) {
            case nascita -> appContext.getBean(ListaAnni.class).nascita(sorgente).mappaDidascalie();
            case morte -> appContext.getBean(ListaAnni.class).morte(sorgente).mappaDidascalie();
            default -> null;
        };

        if (mappaDidascalie != null) {
            message = String.format("Didascalie che implementano l'anno di %s %s", type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printMappaDidascaliaAnni("l'anno", sorgente, mappaDidascalie);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ANNI")
    @Order(6)
    @DisplayName("6 - Mappa paragrafi di varie attivita")
        //--nome anno
        //--typeCrono
    void mappaParagrafi(final String nomeAnno, final AETypeCrono type) {
        System.out.println("6 - Mappa paragrafi di varie attivita");
        sorgente = nomeAnno;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafi;

        mappaParagrafi = switch (type) {
            case nascita -> appContext.getBean(ListaAnni.class).nascita(sorgente).mappaParagrafi();
            case morte -> appContext.getBean(ListaAnni.class).morte(sorgente).mappaParagrafi();
            default -> null;
        };

        if (mappaParagrafi != null) {
            message = String.format("Didascalie che implementano l'anno di %s %s", type, sorgente);
            System.out.println(message);
            System.out.println(attivitaBackend.findFirstByPlurale(sorgente));
            System.out.println(VUOTA);
            printMappaDidascaliaAnni("l'anno", sorgente, mappaParagrafi);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }

    //        @Test
    @Order(12)
    @DisplayName("12 - elaborazione anno 1782")
    void urlRequestListaCatBio() {
        System.out.println(("12 - elaborazione anno 1782"));
        System.out.println(VUOTA);
        List<Bio> lista2 = new ArrayList<>();

        sorgente = "1782";
        listBio = appContext.getBean(ListaAnni.class).morte(sorgente).listaBio();
        assertNotNull(listBio);
        assertTrue(listBio.size() > 0);
        printBioListaAnniMorto(listBio);

        System.out.println(VUOTA);
        for (Bio bio : listBio) {
            lista2.add(elaboraService.esegueSave(bio));
        }
        printBioListaAnniMorto(lista2);
    }


    protected void printBioListaAnniNato(List<Bio> listaBio) {
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
            message = "Ordinate per giorni, forzaOrdinamento";
            System.out.println(message);
            message = "Giorno, ordinamento, wikiTitle, nome, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printBioBaseAnniNato(listaBio.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printBioBaseAnniNato(listaBio.subList(iniB, endB), iniB);
        }
    }


    protected void printBioListaAnniMorto(List<Bio> listaBio) {
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
            message = "Ordinate per giorni, forzaOrdinamento";
            System.out.println(message);
            message = "Giorno, ordinamento, wikiTitle, nome, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printBioBaseAnniMorto(listaBio.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printBioBaseAnniMorto(listaBio.subList(iniB, endB), iniB);
        }
    }

    protected void printBioBaseAnniNato(List<Bio> listaBio, final int inizio) {
        int cont = inizio;

        for (Bio bio : listaBio) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.giornoNato + SPAZIO + "(" + bio.giornoNatoOrd + ")"));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.ordinamento));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.wikiTitle));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(bio.nome) ? bio.nome : KEY_NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(bio.cognome) ? bio.cognome : KEY_NULL));
            System.out.print(SPAZIO);

            System.out.println(SPAZIO);
        }
    }

    protected void printBioBaseAnniMorto(List<Bio> listaBio, final int inizio) {
        int cont = inizio;

        for (Bio bio : listaBio) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.giornoMorto + SPAZIO + "(" + bio.giornoMortoOrd + ")"));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.ordinamento));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.wikiTitle));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(bio.nome) ? bio.nome : KEY_NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(bio.cognome) ? bio.cognome : KEY_NULL));
            System.out.print(SPAZIO);

            System.out.println(SPAZIO);
        }
    }


    protected void printWrapListaAnniNato(List<WrapDidascalia> wrapLista) {
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
            message = "Mese, giorno, wikiTitle";
            System.out.println(message);
            System.out.println(VUOTA);

            printWrapListaBaseAnniNato(wrapLista.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printWrapListaBaseAnniNato(wrapLista.subList(iniB, endB), iniB);
        }
    }

    protected void printWrapListaAnniMorto(List<WrapDidascalia> wrapLista) {
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
            message = "Mese, giorno, wikiTitle";
            System.out.println(message);
            System.out.println(VUOTA);

            printWrapListaBaseAnniMorto(wrapLista.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printWrapListaBaseAnniMorto(wrapLista.subList(iniB, endB), iniB);
        }
    }

    protected void printWrapListaBaseAnniNato(List<WrapDidascalia> wrapLista, final int inizio) {
        int cont = inizio;

        for (WrapDidascalia wrap : wrapLista) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getMeseParagrafoNato()) ? wrap.getMeseParagrafoNato() : NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getGiornoNato()) ? wrap.getGiornoNato() : NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getWikiTitle()) ? wrap.getWikiTitle() : NULL));
            System.out.print(SPAZIO);

            System.out.println(SPAZIO);
        }
    }

    protected void printWrapListaBaseAnniMorto(List<WrapDidascalia> wrapLista, final int inizio) {
        int cont = inizio;

        for (WrapDidascalia wrap : wrapLista) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getMeseParagrafoMorto()) ? wrap.getMeseParagrafoMorto() : NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getGiornoMorto()) ? wrap.getGiornoMorto() : NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getWikiTitle()) ? wrap.getWikiTitle() : NULL));
            System.out.print(SPAZIO);

            System.out.println(SPAZIO);
        }
    }

    protected void printMappaWrapDidascalia(String anno, LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrap) {
        LinkedHashMap<String, List<WrapDidascalia>> mappaSub;

        if (mappaWrap != null) {
            message = String.format("WrapDidascalie per l'anno %s", anno);
            System.out.println(message);
            message = "Mese, giorno, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            for (String key : mappaWrap.keySet()) {
                mappaSub = mappaWrap.get(key);

                System.out.print(textService.setQuadre(key));
                System.out.print(SPAZIO);

                System.out.println(VUOTA);

                printMappaSub(mappaSub);
                System.out.println(VUOTA);
            }
        }
    }


    private void printMappaDidascaliaAnni(String tag, String anno, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie) {
        LinkedHashMap<String, List<String>> mappaSub;

        if (mappaDidascalie != null) {
            message = String.format("Didascalie per %s %s", tag, anno);
            System.out.println(message);
            message = "Mese, giorno, didascalia";
            System.out.println(message);
            System.out.println(VUOTA);

            for (String key : mappaDidascalie.keySet()) {
                mappaSub = mappaDidascalie.get(key);
                System.out.print(wikiUtility.setParagrafo(key));

                printMappaSubDidascalieAnni(mappaSub);
                System.out.println(VUOTA);
            }
        }
    }


    private void printMappaSubDidascalieAnni(LinkedHashMap<String, List<String>> mappaSub) {
        List<String> lista;

        if (mappaSub != null) {
            for (String key : mappaSub.keySet()) {
                lista = mappaSub.get(key);
                if (lista.size() == 1) {
                    System.out.print(ASTERISCO);
                    System.out.print(key);
                    System.out.print(SEP);
                    System.out.print(lista.get(0));
                    System.out.println(VUOTA);
                }
                else {
                    System.out.print(ASTERISCO);
                    System.out.print(key);
                    System.out.print(SPAZIO);

                    System.out.println(VUOTA);

                    printDidascaliaAnni(lista);
                }
            }
        }
    }

    private void printDidascaliaAnni(List<String> lista) {
        if (lista != null) {
            for (String didascalia : lista) {
                System.out.print(ASTERISCO);
                System.out.print(ASTERISCO);
                System.out.print(didascalia);
                System.out.print(SPAZIO);

                System.out.println(VUOTA);
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