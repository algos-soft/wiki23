package it.algos.integration.liste;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.liste.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.wrapper.*;
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
 * Date: Mon, 25-Jul-2022
 * Time: 09:10
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
@DisplayName("Giorni lista")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaGiorniTest extends WikiTest {


    /**
     * Classe principale di riferimento <br>
     */
    private ListaGiorni istanza;

    private LinkedHashMap<String, List<WrapLista>> mappaWrap;

    //--nome giorno
    //--typeCrono
    protected static Stream<Arguments> GIORNI() {
        return Stream.of(
                Arguments.of(VUOTA, AETypeCrono.nascita),
                Arguments.of(VUOTA, AETypeCrono.morte),
                Arguments.of("3 luglio", AETypeCrono.nascita),
                Arguments.of("3 luglio", AETypeCrono.morte),
                Arguments.of("1º gennaio", AETypeCrono.nascita),
                Arguments.of("1º gennaio", AETypeCrono.morte),
                Arguments.of("25 ottobre", AETypeCrono.nascita),
                Arguments.of("25 ottobre", AETypeCrono.morte),
                Arguments.of("29 febbraio", AETypeCrono.nascita),
                Arguments.of("29 febbraio", AETypeCrono.morte)
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
        istanza = new ListaGiorni();
        assertNotNull(istanza);
        System.out.println(("1 - Costruttore base senza parametri"));
        System.out.println(VUOTA);
        System.out.println(String.format("Costruttore base senza parametri per un'istanza di %s", istanza.getClass().getSimpleName()));
    }

    //    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(2)
    @DisplayName("2 - Lista bio di vari giorni")
    //--nome giorno
    //--typeCrono
    void listaBio(final String nomeGiorno, final AETypeCrono type) {
        System.out.println("2 - Lista bio di vari giorni");
        sorgente = nomeGiorno;

        listBio = switch (type) {
            case nascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).listaBio();
            case morte -> appContext.getBean(ListaGiorni.class).morte(sorgente).listaBio();
        };

        if (listBio != null && listBio.size() > 0) {
            message = String.format("Ci sono %d biografie che implementano il giorno di %s %s", listBio.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            switch (type) {
                case nascita -> printBioListaGiorniNato(listBio);
                case morte -> printBioListaGiorniMorto(listBio);
            }
        }
        else {
            message = "La listBio è nulla";
            System.out.println(message);
        }
    }


    //    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(3)
    @DisplayName("3 - Lista wrapDidascalia di vari giorni")
    //--nome giorno
    //--typeCrono
    void listaWrapDidascalie(final String nomeGiorno, final AETypeCrono type) {
        System.out.println("3 - Lista wrapDidascalia di vari giorni");
        sorgente = nomeGiorno;

        listWrapDidascalia = switch (type) {
            case nascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).listaWrapDidascalie();
            case morte -> appContext.getBean(ListaGiorni.class).morte(sorgente).listaWrapDidascalie();
            default -> null;
        };

        if (listWrapDidascalia != null) {
            message = String.format("Ci sono %d wrapDidascalia che implementano il giorno di %s %s", listWrapDidascalia.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            switch (type) {
                case nascita -> printWrapListaGiorniNato(listWrapDidascalia);
                case morte -> printWrapListaGiorniMorto(listWrapDidascalia);
            }
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }

    //    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(4)
    @DisplayName("4 - Mappa wrapDidascalia di vari giorni")
    //--nome giorno
    //--typeCrono
    void mappaWrapDidascalie(final String nomeGiorno, final AETypeCrono type) {
        System.out.println("4 - Mappa wrapDidascalia di vari giorni");
        sorgente = nomeGiorno;
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDidascalie;

        mappaWrapDidascalie = switch (type) {
            case nascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).mappaWrapDidascalie();
            case morte -> appContext.getBean(ListaGiorni.class).morte(sorgente).mappaWrapDidascalie();
            default -> null;
        };

        if (mappaWrapDidascalie != null) {
            message = String.format("WrapDidascalie che implementano il giorno di %s %s", type, sorgente);
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
    @MethodSource(value = "GIORNI")
    @Order(5)
    @DisplayName("5 - Mappa didascalie di vari giorni")
    //--nome giorno
    //--typeCrono
    void mappaDidascalie(final String nomeGiorno, final AETypeCrono type) {
        System.out.println("5 - Mappa didascalie di vari giorni");
        sorgente = nomeGiorno;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie;

        mappaDidascalie = switch (type) {
            case nascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).mappaDidascalie();
            case morte -> appContext.getBean(ListaGiorni.class).morte(sorgente).mappaDidascalie();
            default -> null;
        };

        if (mappaDidascalie != null) {
            message = String.format("Didascalie che implementano il giorno di %s %s", type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printMappaDidascaliaGiorni("il giorno", sorgente, mappaDidascalie);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }


    //    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(6)
    @DisplayName("6 - Mappa paragrafi di varie attivita")
    //--nome giorno
    //--typeCrono
    void mappaParagrafi(final String nomeGiorno, final AETypeCrono type) {
        System.out.println("6 - Mappa paragrafi di varie attivita");
        sorgente = nomeGiorno;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafi;

        mappaParagrafi = switch (type) {
            case nascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).mappaParagrafi();
            case morte -> appContext.getBean(ListaGiorni.class).morte(sorgente).mappaParagrafi();
            default -> null;
        };

        if (mappaParagrafi != null) {
            message = String.format("Didascalie che implementano il giorno di %s %s", type, sorgente);
            System.out.println(message);
            System.out.println(attivitaBackend.findFirstByPlurale(sorgente));
            System.out.println(VUOTA);
            printMappaDidascaliaGiorni("il giorno", sorgente, mappaParagrafi);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }

    //    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(80)
    @DisplayName("80 - Lista wrap di vari giorni")
    //--nome giorno
    //--typeCrono
    void listaWrap(final String nomeGiorno, final AETypeCrono type) {
        System.out.println("80 - Lista wrap di vari giorni");
        sorgente = nomeGiorno;
        int size = 0;

        listWrapLista = switch (type) {
            case nascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).listaWrap();
            case morte -> appContext.getBean(ListaGiorni.class).morte(sorgente).listaWrap();
            default -> null;
        };

        if (listWrapLista != null && listWrapLista.size() > 0) {
            message = String.format("Ci sono %d wrapDidascalia che implementano il giorno di %s %s", listWrapLista.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            //            printWrapLista(listWrapLista);
            size = listWrapLista.size();
            printWrapLista(listWrapLista.subList(size - 5, size));
        }
        else {
            message = "La lista è nulla";
            System.out.println(message);
        }
    }

    //        @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(81)
    @DisplayName("81 - Mappa wrapLista di vari giorni")
    //--nome giorno
    //--typeCrono
    void mappaWrap(final String nomeGiorno, final AETypeCrono type) {
        System.out.println("81 - Mappa wrapLista di vari giorni");
        sorgente = nomeGiorno;

        mappaWrap = switch (type) {
            case nascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).mappaWrap();
            case morte -> appContext.getBean(ListaGiorni.class).morte(sorgente).mappaWrap();
            default -> null;
        };

        if (mappaWrap != null && mappaWrap.size() > 0) {
            message = String.format("Ci sono %d wrapDidascalia che implementano il giorno di %s %s", mappaWrap.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printMappaWrap(mappaWrap);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }


    //    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(82)
    @DisplayName("82 - Mappa didascalie di vari giorni")
    //--nome giorno
    //--typeCrono
    void mappaDidascalia(final String nomeGiorno, final AETypeCrono type) {
        System.out.println("82 - Mappa didascalie di vari giorni");
        sorgente = nomeGiorno;

        mappa = switch (type) {
            case nascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).mappaDidascalia();
            case morte -> appContext.getBean(ListaGiorni.class).morte(sorgente).mappaDidascalia();
            default -> null;
        };

        if (mappa != null && mappa.size() > 0) {
            message = String.format("Ci sono %d wrapDidascalia che implementano il giorno di %s %s", mappa.size(), type, sorgente);
            System.out.println(message);
            System.out.println(VUOTA);
            printMappa(mappa);
        }
        else {
            message = "La mappa è nulla";
            System.out.println(message);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(83)
    @DisplayName("83 - Testo body di vari giorni")
        //--nome giorno
        //--typeCrono
    void testoBody(final String nomeGiorno, final AETypeCrono type) {
        System.out.println("83 - Testo body di vari giorni");
        System.out.println(VUOTA);
        sorgente = nomeGiorno;

        ottenutoRisultato = switch (type) {
            case nascita -> appContext.getBean(ListaGiorni.class).nascita(sorgente).testoBody();
            case morte -> appContext.getBean(ListaGiorni.class).morte(sorgente).testoBody();
            default -> null;
        };
        System.out.println(ottenutoRisultato.getContent());
    }

    protected void printWrapLista(List<WrapLista> listWrapLista) {
        int max = 15;
        if (listWrapLista != null) {
            message = String.format("Faccio vedere una lista delle prime %d didascalie", max);
            System.out.println(message);
            message = "Paragrafo, riga, didascaliaBreve";
            System.out.println(message);
            System.out.println(VUOTA);
            printSub(listWrapLista.subList(0, Math.min(max, listWrapLista.size())));
        }
    }

    protected void printMappaWrap(LinkedHashMap<String, List<WrapLista>> mappaWrap) {
        List<WrapLista> lista;

        if (mappaWrap != null) {
            message = String.format("Faccio vedere una mappa delle didascalie");
            System.out.println(VUOTA);
            for (String paragrafo : mappaWrap.keySet()) {
                System.out.print("==");
                System.out.print(paragrafo);
                System.out.print("==");
                System.out.print(CAPO);
                lista = mappaWrap.get(paragrafo);
                printSub(lista);
            }
        }
    }

    protected void printSub(List<WrapLista> listWrapLista) {
        for (WrapLista wrap : listWrapLista) {
            System.out.print(wrap.titoloParagrafo);
            System.out.print(SEP);
            if (textService.isValid(wrap.titoloRiga)) {
                System.out.print(wrap.titoloRiga);
                System.out.print(SEP);
            }
            System.out.println(wrap.didascaliaBreve);
        }
        System.out.println(VUOTA);
    }

    protected void printMappa(Map<String, List<String>> mappa) {
        List<String> lista;

        if (mappa != null) {
            message = String.format("Faccio vedere una mappa delle didascalie");
            System.out.println(message);
            System.out.println(VUOTA);
            for (String paragrafo : mappa.keySet()) {
                System.out.print("==");
                System.out.print(paragrafo);
                System.out.print("==");
                System.out.print(CAPO);
                lista = mappa.get(paragrafo);
                for (String didascalia : lista) {
                    System.out.println(didascalia);
                }
                System.out.println(VUOTA);
            }
        }
    }

    protected void printBioListaGiorniNato(List<Bio> listaBio) {
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
            message = "Ordinate per anni, forzaOrdinamento";
            System.out.println(message);
            message = "Anno, ordinamento, wikiTitle, nome, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printBioBaseGiorniNato(listaBio.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printBioBaseGiorniNato(listaBio.subList(iniB, endB), iniB);
        }
    }


    protected void printBioListaGiorniMorto(List<Bio> listaBio) {
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
            message = "Ordinate per anni, forzaOrdinamento";
            System.out.println(message);
            message = "Anno, ordinamento, wikiTitle, nome, cognome";
            System.out.println(message);
            System.out.println(VUOTA);

            printBioBaseGiorniMorto(listaBio.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printBioBaseGiorniMorto(listaBio.subList(iniB, endB), iniB);
        }
    }

    protected void printBioBaseGiorniNato(List<Bio> listaBio, final int inizio) {
        int cont = inizio;

        for (Bio bio : listaBio) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.annoNato + SPAZIO + "(" + bio.annoNatoOrd + ")"));
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

    protected void printBioBaseGiorniMorto(List<Bio> listaBio, final int inizio) {
        int cont = inizio;

        for (Bio bio : listaBio) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(bio.annoMorto + SPAZIO + "(" + bio.annoMortoOrd + ")"));
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


    protected void printWrapListaGiorniNato(List<WrapDidascalia> wrapLista) {
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
            message = "Secolo, anno, wikiTitle";
            System.out.println(message);
            System.out.println(VUOTA);

            printWrapListaBaseGiorniNato(wrapLista.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printWrapListaBaseGiorniNato(wrapLista.subList(iniB, endB), iniB);
        }
    }

    protected void printWrapListaGiorniMorto(List<WrapDidascalia> wrapLista) {
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
            message = "Secolo, anno, wikiTitle";
            System.out.println(message);
            System.out.println(VUOTA);

            printWrapListaBaseGiorniMorto(wrapLista.subList(iniA, endA), iniA);
            System.out.println(TRE_PUNTI);
            printWrapListaBaseGiorniMorto(wrapLista.subList(iniB, endB), iniB);
        }
    }

    protected void printWrapListaBaseGiorniNato(List<WrapDidascalia> wrapLista, final int inizio) {
        int cont = inizio;

        for (WrapDidascalia wrap : wrapLista) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getSecoloParagrafoNato()) ? wrap.getSecoloParagrafoNato() : NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getAnnoNato()) ? wrap.getAnnoNato() : NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getWikiTitle()) ? wrap.getWikiTitle() : NULL));
            System.out.print(SPAZIO);

            System.out.println(SPAZIO);
        }
    }

    protected void printWrapListaBaseGiorniMorto(List<WrapDidascalia> wrapLista, final int inizio) {
        int cont = inizio;

        for (WrapDidascalia wrap : wrapLista) {
            cont++;
            System.out.print(cont);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getSecoloParagrafoMorto()) ? wrap.getSecoloParagrafoMorto() : NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getAnnoMorto()) ? wrap.getAnnoMorto() : NULL));
            System.out.print(SPAZIO);

            System.out.print(textService.setQuadre(textService.isValid(wrap.getWikiTitle()) ? wrap.getWikiTitle() : NULL));
            System.out.print(SPAZIO);

            System.out.println(SPAZIO);
            Object alfa = wrap;
        }
    }


    protected void printMappaWrapDidascalia(String giorno, LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrap) {
        LinkedHashMap<String, List<WrapDidascalia>> mappaSub;

        if (mappaWrap != null) {
            message = String.format("WrapDidascalie per il giorno %s", giorno);
            System.out.println(message);
            message = "Secolo, anno, cognome";
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


    private void printMappaDidascaliaGiorni(String tag, String giorno, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie) {
        LinkedHashMap<String, List<String>> mappaSub;

        if (mappaDidascalie != null) {
            message = String.format("Didascalie per %s %s", tag, giorno);
            System.out.println(message);
            message = "Secolo, anno, didascalia";
            System.out.println(message);
            System.out.println(VUOTA);

            for (String key : mappaDidascalie.keySet()) {
                mappaSub = mappaDidascalie.get(key);
                System.out.print(wikiUtility.setParagrafo(key));

                printMappaSubDidascalieGiorni(mappaSub);
                System.out.println(VUOTA);
            }
        }
    }


    private void printMappaSubDidascalieGiorni(LinkedHashMap<String, List<String>> mappaSub) {
        List<String> lista;

        if (mappaSub != null) {
            for (String key : mappaSub.keySet()) {
                lista = mappaSub.get(key);
                if (lista.size() == 1) {
                    if (textService.isValid(key)) {
                        System.out.print(ASTERISCO);
                        System.out.print(key);
                        System.out.print(SEP);
                        System.out.print(lista.get(0));
                        System.out.println(VUOTA);
                    }
                    else {
                        System.out.print(ASTERISCO);
                        System.out.print(lista.get(0));
                        System.out.println(VUOTA);
                    }
                }
                else {
                    if (textService.isValid(key)) {
                        System.out.print(ASTERISCO);
                        System.out.print(key);
                        System.out.print(SPAZIO);
                        System.out.println(VUOTA);
                        printDidascaliaGiorni(lista, true);
                    }
                    else {
                        printDidascaliaGiorni(lista, false);
                    }
                }
            }
        }
    }

    private void printDidascaliaGiorni(List<String> lista, boolean doppioAsterisco) {
        if (lista != null) {
            for (String didascalia : lista) {
                System.out.print(ASTERISCO);
                if (doppioAsterisco) {
                    System.out.print(ASTERISCO);
                }
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