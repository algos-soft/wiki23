package it.algos.unit.wiki;

import it.algos.*;
import it.algos.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.interfaces.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.wiki.query.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.boot.test.context.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 21-gen-2019
 * Time: 08:39
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@SpringBootTest(classes = {Wiki23Application.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
@Tag("wiki")
@DisplayName("DidascaliaService - Elaborazione delle didascalie.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DidascaliaServiceTest extends WikiTest {

    private static final String DATA_BASE_NAME = "vaadwiki";

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    @InjectMocks
    public DidascaliaService service;

    private String wikiTitleDue = "Sonia Todd";


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    protected void setUpAll() {
        super.setUpAll();

        //--reindirizzo l'istanza della superclasse
        service = didascaliaService;
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

    //    private Giorno creaGiorno(String giornoText) {
    //        Giorno giorno = null;
    //        giorno = new Giorno();
    //        //        giorno.titolo = giornoText;
    //        return giorno;
    //    }// end of method

    //    private Anno creaAnno(int annoInt) {
    //        Anno anno = null;
    //        anno = new Anno();
    //        anno.titolo = "" + annoInt;
    //        return anno;
    //    }// end of method

    //    private Attivita creaAttivita(String attivitaText) {
    //        Attivita attivita = null;
    //        attivita = new Attivita();
    //        attivita.singolare = attivitaText;
    //        return attivita;
    //    }// end of method

    //    private Nazionalita creaNazionalita(String nazionalitaText) {
    //        Nazionalita nazionalita = null;
    //        nazionalita = new Nazionalita();
    //        nazionalita.singolare = nazionalitaText;
    //        return nazionalita;
    //    }// end of method


    private Bio creaBio() {
        Bio entity = null;
        //        Giorno giorno = null;
        //        Anno anno = creaAnno(1963);
        //        Attivita attivita = creaAttivita("attrice");
        //        Nazionalita nazionalita = creaNazionalita("australiana");
        //
        //        entity = new Bio();
        //        entity.pageid = 29999;
        //        entity.wikiTitle = wikiTitle;
        //        entity.nome = "Sonia";
        //        entity.cognome = "Todd";
        //        entity.sesso = "F";
        //        entity.luogoNato = "Adelaide";
        //        entity.luogoNatoLink = "Adelaide (Australia)";
        //        entity.giornoNascita = giorno;
        //        entity.annoNascita = anno;
        //        entity.attivita = attivita;
        //        entity.nazionalita = nazionalita;

        return entity;
    }

    @Test
    @Order(1)
    @DisplayName("1 - Nome e cognome semplice")
    void getNomeCognome() {
        System.out.println("1 - Nome e cognome semplice");

        sorgente = "Sigurd Ribbung";
        sorgente2 = "Sigurd";
        sorgente3 = "Ribbung";
        previsto = "[[Sigurd Ribbung]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        printDidascalia(sorgente, sorgente2, sorgente3, ottenuto);

        //        wrap = queryBio.urlRequest(sorgente).getWrap();
        //        assertNotNull(wrap);
        //        assertTrue(wrap.isValido());
        //        bio = bioService.newEntity(wrap);
        //        bio = elaboraService.esegue(bio);
        //        ottenuto = service.getNomeCognome(bio);
        //        assertTrue(textService.isValid(ottenuto));
        //        assertEquals(previsto, ottenuto);
        //        System.out.println(VUOTA);
        //        System.out.println(VUOTA);
        //        System.out.println("Lo stesso passando da WrapBio e Bio");
        //        print(bio, ottenuto);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Nome doppio e cognome semplice")
    void getNomeCognome2() {
        System.out.println("2 - Nome doppio e cognome semplice");

        sorgente = "Bernart Arnaut d'Armagnac";
        sorgente2 = "Bernart";
        sorgente3 = "d'Armagnac";
        previsto = "[[Bernart Arnaut d'Armagnac]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        printDidascalia(sorgente, sorgente2, sorgente3, ottenuto);
    }


    @Test
    @Order(3)
    @DisplayName("3 - Nome doppio e cognome semplice")
    void getNomeCognome3() {
        System.out.println("3 - Nome doppio e cognome semplice");

        sorgente = "Francesco Maria Pignatelli";
        sorgente2 = "Francesco Maria";
        sorgente3 = "Pignatelli";
        previsto = "[[Francesco Maria Pignatelli]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        printDidascalia(sorgente, sorgente2, sorgente3, ottenuto);
    }


    @Test
    @Order(4)
    @DisplayName("4 - Titolo disambiguato")
    void getNomeCognome4() {
        System.out.println("4 - Titolo disambiguato");

        sorgente = "Colin Campbell (generale)";
        sorgente2 = "Colin";
        sorgente3 = "Campbell";
        previsto = "[[Colin Campbell (generale)|Colin Campbell]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        printDidascalia(sorgente, sorgente2, sorgente3, ottenuto);
    }


    //    @ParameterizedTest
    @MethodSource(value = "PAGINE_BIO")
    @Order(5)
    @DisplayName("5 - Didascalie varie con simboli specifici nato e morto")
    //--titolo
    //--pagina valida
    void didascalia(final String wikiTitle, final boolean paginaValida) {
        System.out.println(VUOTA);
        String nomeCognome;
        String attivitaNazionalita;
        String lista;
        String natoMorto;
        long pageId;
        String wikiBioTmpl;

        ottenutoRisultato = appContext.getBean(QueryBio.class).urlRequest(wikiTitle);
        assertEquals(paginaValida, ottenutoRisultato.isValido());

        System.out.println(String.format("Biografia: %s", wikiTitle));

        if (ottenutoRisultato.isValido()) {
            pageId = ottenutoRisultato.getLongValue();
            wikiBioTmpl = textService.isValid(ottenutoRisultato.getWrap().getTemplBio()) ? ottenutoRisultato.getWrap().getTemplBio() : VUOTA;
            bio = bioBackend.newEntity(pageId, wikiTitle, wikiBioTmpl);
            bio = elaboraService.esegue(bio);
            assertNotNull(bio.pageId);

            nomeCognome = service.getNomeCognome(bio);
            attivitaNazionalita = service.getAttivitaNazionalita(bio);
            lista = service.lista(bio);
            natoMorto = service.luogoNatoMorto(bio);

            System.out.println(VUOTA);
            System.out.println(String.format("PageId: %s", pageId));
            System.out.println(String.format("WikiBio: %s", getMax(wikiBioTmpl)));
            System.out.println(VUOTA);
            System.out.println(String.format("Nome = %s", bio.nome));
            System.out.println(String.format("Cognome = %s", bio.cognome));
            System.out.println(String.format("GiornoNato = %s", bio.giornoNato));
            System.out.println(String.format("AnnoNato = %s", bio.annoNato));
            System.out.println(String.format("LuogoNato = %s", bio.luogoNato));
            System.out.println(String.format("LuogoNatoLink = %s", bio.luogoNatoLink));
            System.out.println(String.format("GiornoMorto = %s", bio.giornoMorto));
            System.out.println(String.format("AnnoMorto = %s", bio.annoMorto));
            System.out.println(String.format("LuogoMorto = %s", bio.luogoMorto));
            System.out.println(String.format("LuogoMortoLink = %s", bio.luogoMortoLink));
            System.out.println(String.format("Attività = %s", bio.attivita));
            System.out.println(String.format("Attività2 = %s", bio.attivita2));
            System.out.println(String.format("Attività3 = %s", bio.attivita3));
            System.out.println(String.format("Nazionalità = %s", bio.nazionalita));
            System.out.println(VUOTA);
            System.out.println(String.format("NomeCognome (recuperato dal titolo della voce): %s%s%s", CAPO, nomeCognome, CAPO));
            System.out.println(String.format("AttivitaNazionalita (attività, attività2, attività3, nazionalità): %s%s%s", CAPO, attivitaNazionalita, CAPO));
            System.out.println(String.format("NatoMorto (usando anche luogoNatoLink e luogoMortoLink): %s%s%s", CAPO, natoMorto, CAPO));
            System.out.println(String.format("Lista (NomeCognome + AttivitaNazionalita + NatoMorto): %s%s%s", CAPO, lista, CAPO));
        }
        else {
            System.out.println(String.format("Errore: %s", ottenutoRisultato.getErrorCode()));
            System.out.println(VUOTA);
            printRisultato(ottenutoRisultato);
        }
    }

    @Test
    @Order(6)
    @DisplayName("6 - Didascalie lista con varianti di linkCrono")
    void linkCrono() {
        String wikiBioTmpl;
        String lista;
        AITypePref oldValue = WPref.linkCrono.getEnumCurrentObj();
        AITypePref newValue;
        sorgente = "Gaio Claudio Marcello (console 50 a.C.)";
        ottenutoRisultato = appContext.getBean(QueryBio.class).urlRequest(sorgente);
        assertTrue(ottenutoRisultato.isValido());

        System.out.println(String.format("Biografia: %s", sorgente));

        pageId = ottenutoRisultato.getLongValue();
        wikiBioTmpl = textService.isValid(ottenutoRisultato.getWrap().getTemplBio()) ? ottenutoRisultato.getWrap().getTemplBio() : VUOTA;
        bio = bioBackend.newEntity(pageId, sorgente, wikiBioTmpl);
        bio = elaboraService.esegue(bio);
        System.out.println("Lista (NomeCognome + AttivitaNazionalita + NatoMorto)");

        WPref.linkCrono.setEnumCurrentObj(AETypeLinkCrono.nessuno);
        lista = service.lista(bio);
        System.out.println(VUOTA);
        System.out.println("AETypeLinkCrono: " + AETypeLinkCrono.nessuno);
        System.out.println(lista);

        WPref.linkCrono.setEnumCurrentObj(AETypeLinkCrono.voce);
        lista = service.lista(bio);
        System.out.println(VUOTA);
        System.out.println("AETypeLinkCrono: " + AETypeLinkCrono.voce);
        System.out.println(lista);

        WPref.linkCrono.setEnumCurrentObj(AETypeLinkCrono.lista);
        lista = service.lista(bio);
        System.out.println(VUOTA);
        System.out.println("AETypeLinkCrono: " + AETypeLinkCrono.lista);
        System.out.println(lista);

        WPref.linkCrono.setEnumCurrentObj(oldValue);
    }


    //    @ParameterizedTest
    @MethodSource(value = "PAGINE_BIO")
    @Order(7)
    @DisplayName("7 - Didascalie pagine 'Nati nel xxx(anno)'")
    //--titolo
    //--pagina valida
    void didascaliaAnnoNato(final String wikiTitle, final boolean paginaValida) {
        System.out.println("7 - Didascalie pagine 'Nati nel xxx(anno)'");
        System.out.println(VUOTA);

        if (!paginaValida) {
            return;
        }
        sorgente = wikiTitle;
        bio = appContext.getBean(QueryBio.class).getBio(wikiTitle);
        assertNotNull(bio);

        ottenuto = service.didascaliaAnnoNato(bio);
        System.out.println(String.format("Biografia: %s", wikiTitle));
        System.out.println(String.format("Didascalia anno nato: %s", ottenuto));
    }


    //    @ParameterizedTest
    @MethodSource(value = "PAGINE_BIO")
    @Order(8)
    @DisplayName("8 - Didascalie pagine 'Morti nel xxx(anno)'")
    //--titolo
    //--pagina valida
    void didascaliaAnnoMorto(final String wikiTitle, final boolean paginaValida) {
        System.out.println("8 - Didascalie pagine 'Morti nel xxx(anno)'");
        System.out.println(VUOTA);

        if (!paginaValida) {
            return;
        }
        sorgente = wikiTitle;
        bio = appContext.getBean(QueryBio.class).getBio(wikiTitle);
        assertNotNull(bio);

        ottenuto = service.didascaliaAnnoMorto(bio);
        System.out.println(String.format("Biografia: %s", wikiTitle));
        System.out.println(String.format("Didascalia anno morto: %s", ottenuto));
    }


    //    @ParameterizedTest
    @MethodSource(value = "PAGINE_BIO")
    @Order(9)
    @DisplayName("9 - Didascalie pagine 'Nazionalità'")
    //--titolo
    //--pagina valida
    void didascaliaNazionalita(final String wikiTitle, final boolean paginaValida) {
        System.out.println("9 - Didascalie pagine 'Nazionalità'");
        System.out.println(VUOTA);
        if (!paginaValida) {
            return;
        }
        sorgente = wikiTitle;
        bio = appContext.getBean(QueryBio.class).getBio(wikiTitle);
        assertNotNull(bio);

        ottenuto = service.didascaliaGiornoNato(bio);
        System.out.println(String.format("Biografia: %s", wikiTitle));
        System.out.println(String.format("Didascalia lista: %s", ottenuto));
    }


    @ParameterizedTest
    @MethodSource(value = "PAGINE_BIO")
    @Order(10)
    @DisplayName("10 - Didascalie varie senza assertEquals")
        //--titolo
        //--pagina valida
    void didascalieVarie(final String wikiTitle, final boolean paginaValida) {
        System.out.println("10 - Didascalie varie senza assertEquals");
        String tmpl;
        if (!paginaValida) {
            return;
        }

        sorgente = wikiTitle;
        bio = appContext.getBean(QueryBio.class).getBio(wikiTitle);
        assertNotNull(bio);
        tmpl = bio.getTmplBio();
        tmpl = tmpl.replaceAll(CAPO, SPAZIO);
        System.out.println(VUOTA);
        System.out.println(String.format("Biografia: %s", sorgente));
        System.out.println(String.format("TmplBio: %s", tmpl.substring(0, 140)));
        System.out.println(String.format("%s", tmpl.substring(140, 280)));
        System.out.println(String.format("%s", tmpl.substring(280, Math.min(420, tmpl.length()))));

        System.out.println(VUOTA);
        ottenuto = service.giornoNato(bio);
        System.out.println(String.format("giornoNato: %s", ottenuto));
        ottenuto = service.giornoNatoSimbolo(bio);
        System.out.println(String.format("giornoNatoSimbolo: %s", ottenuto));
        ottenuto = service.giornoNatoSimboloParentesi(bio);
        System.out.println(String.format("giornoNatoSimboloParentesi: %s", ottenuto));

        System.out.println(VUOTA);
        ottenuto = service.giornoMorto(bio);
        System.out.println(String.format("giornoMorto: %s", ottenuto));
        ottenuto = service.giornoMortoSimbolo(bio);
        System.out.println(String.format("giornoMortoSimbolo: %s", ottenuto));
        ottenuto = service.giornoMortoSimboloParentesi(bio);
        System.out.println(String.format("giornoMortoSimboloParentesi: %s", ottenuto));

        System.out.println(VUOTA);
        ottenuto = service.annoNato(bio);
        System.out.println(String.format("annoNato: %s", ottenuto));
        ottenuto = service.annoNatoSimbolo(bio);
        System.out.println(String.format("annoNatoSimbolo: %s", ottenuto));
        ottenuto = service.annoNatoSimboloParentesi(bio);
        System.out.println(String.format("annoNatoSimboloParentesi: %s", ottenuto));

        System.out.println(VUOTA);
        ottenuto = service.annoMorto(bio);
        System.out.println(String.format("annoMorto: %s", ottenuto));
        ottenuto = service.annoMortoSimbolo(bio);
        System.out.println(String.format("annoMortoSimbolo: %s", ottenuto));
        ottenuto = service.annoMortoSimboloParentesi(bio);
        System.out.println(String.format("annoMortoSimboloParentesi: %s", ottenuto));

        System.out.println(VUOTA);
        ottenuto = service.luogoNato(bio);
        System.out.println(String.format("luogoNato: %s", ottenuto));
        ottenuto = service.luogoNatoAnno(bio);
        System.out.println(String.format("luogoNatoAnno: %s", ottenuto));

        System.out.println(VUOTA);
        ottenuto = service.luogoMorto(bio);
        System.out.println(String.format("luogoMorto: %s", ottenuto));
        ottenuto = service.luogoMortoAnno(bio);
        System.out.println(String.format("luogoMortoAnno: %s", ottenuto));

        System.out.println(VUOTA);
        ottenuto = service.luogoNatoMorto(bio);
        System.out.println(String.format("crono: %s", ottenuto));
        System.out.println(VUOTA);
        ottenuto = service.lista(bio);
        System.out.println(String.format("lista: %s", ottenuto));
    }

    @ParameterizedTest
    @MethodSource(value = "PAGINE_BIO")
    @Order(11)
    @DisplayName("11 - Didascalie varie senza assertEquals")
        //--titolo
        //--pagina valida
    void didascalieVarie2(final String wikiTitle, final boolean paginaValida) {
        System.out.println("11 - Didascalie varie senza assertEquals");
        String tmpl;
        if (!paginaValida) {
            return;
        }

        sorgente = wikiTitle;
        bio = appContext.getBean(QueryBio.class).getBio(wikiTitle);
        assertNotNull(bio);
        tmpl = bio.getTmplBio();
        tmpl = tmpl.replaceAll(CAPO, SPAZIO);
        System.out.println(VUOTA);
        System.out.println(String.format("Biografia: %s", sorgente));
        System.out.println(String.format("TmplBio: %s", tmpl.substring(0, 140)));
        System.out.println(String.format("%s", tmpl.substring(140, 280)));
        System.out.println(String.format("%s", tmpl.substring(280, Math.min(420, tmpl.length()))));

        System.out.println(VUOTA);
        ottenuto = service.didascaliaGiornoNato(bio);
        System.out.println(String.format("giornoNato (%s): %s", bio.giornoNato, ottenuto));
        System.out.println(VUOTA);
        ottenuto = service.didascaliaGiornoMorto(bio);
        System.out.println(String.format("giornoMorto (%s): %s", bio.giornoMorto, ottenuto));
        System.out.println(VUOTA);
        ottenuto = service.didascaliaAnnoNato(bio);
        System.out.println(String.format("annoNato (%s): %s", bio.annoNato, ottenuto));
        System.out.println(VUOTA);
        ottenuto = service.didascaliaAnnoMorto(bio);
        System.out.println(String.format("annoMorto (%s): %s", bio.annoMorto, ottenuto));
        System.out.println(VUOTA);
        ottenuto = service.lista(bio);
        System.out.println(String.format("lista: %s", ottenuto));

        //        ottenuto = service.getDidascaliaGiornoNato(bio);
        //        ottenuto2 = service.getWrapGiornoNato(bio).didascaliaBreve;
        //        System.out.println(VUOTA);
        //        System.out.println(String.format("Giorno nato: %s", ottenuto));
        //
        //        ottenuto = service.getDidascaliaGiornoMorto(bio);
        //        ottenuto2 = service.getWrapGiornoMorto(bio).didascaliaBreve;
        //        System.out.println(VUOTA);
        //        System.out.println(String.format("Giorno morto: %s", ottenuto));
        //
        //        ottenuto = service.getDidascaliaAnnoNato(bio);
        //        System.out.println(VUOTA);
        //        System.out.println(String.format("Anno nato: %s", ottenuto));
        //
        //        ottenuto = service.getDidascaliaAnnoMorto(bio);
        //        System.out.println(VUOTA);
        //        System.out.println(String.format("Anno morto: %s", ottenuto));
        //
        //        ottenuto = service.getDidascaliaLista(bio);
        //        System.out.println(VUOTA);
        //        System.out.println(String.format("Lista: %s", ottenuto));
    }


    @Test
    @Order(12)
    @DisplayName("12 - Didascalie varie con assertEquals")
    void didascalieVarie2() {
        previsto = "[[Roman Protasevič]], attivista e giornalista bielorusso";
    }

    //    @Test
    public void download() {
        System.out.println("*************");
        System.out.println("Tipi possibili di didascalie per " + "Matteo Renzi");
        System.out.println("Senza chiave");
        System.out.println("*************");
        //        for (EADidascalia dida : EADidascalia.values()) {
        //            ottenuto = didascalia.esegue(bio, dida, false);
        //            System.out.println(dida.name() + ": " + ottenuto);
        //        }// end of for cycle
        System.out.println("*************");
        System.out.println("Con chiave");
        System.out.println("*************");
        //        for (EADidascalia dida : EADidascalia.values()) {
        //            //            ottenuto = didascalia.esegue(bio, dida);
        //            System.out.println(dida.name() + ": " + ottenuto);
        //        }// end of for cycle
        System.out.println("*************");

    }// end of single test


    /**
     * Test con uscita sul terminale di Idea
     */
    //    @Test
    public void esegueTestDidascalie() {
        System.out.println("");
        System.out.println("Algos");
        System.out.println("");
        System.out.println("Tipi possibili di discalie");
        System.out.println("Esempio '" + "Matteo Salvini" + "'");
        System.out.println("");
        Bio bio = creaBio();
        //        for (EADidascalia type : EADidascalia.values()) {
        //            ottenuto = didascaliaService.getBaseSenza(bio, type);
        //            if (text.isValid(ottenuto)) {
        //                System.out.println(type.name() + ": " + ottenuto);
        //            }
        //            else {
        //                System.out.println(type.name() + ": Manca");
        //            }// end of if/else cycle
        //        }// end of for cycle
        System.out.println("");
    }// end of single test

    //    /**
    //     * Pagina completa con uscita su pagina utente
    //     */
    //    @Test
    //    public void esegueTestUplod() {
    //        didascaliaService.esegue();
    //    }// end of single test

    void elabora(String wikiTitle) {
        //        sorgente = wikiTitle;
        //        wrap = queryBio.urlRequest(sorgente).getWrap();
        //        assertNotNull(wrap);
        //        assertTrue(wrap.isValido());
        //        bio = bioService.newEntity(wrap);
        //        elaboraService.esegue(bio);
        //        try {
        //            bioService.save(bio, AEOperation.edit);
        //        } catch (Exception unErrore) {
        //            System.out.println("Errore");
        //        }
    }

    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterAll
    void tearDownAll() {
        //        FlowVar.typeSerializing = AETypeSerializing.spring;
    }

}// end of class
