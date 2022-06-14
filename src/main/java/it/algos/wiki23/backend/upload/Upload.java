package it.algos.wiki23.backend.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.service.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.checkerframework.checker.units.qual.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 08-Jun-2022
 * Time: 06:54
 * <p>
 * Classe specializzata per caricare (upload) le liste biografiche sul server wiki. <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale). <br>
 * Liste di attività e nazionalità (in Progetto:Biografie). <br>
 * <p>
 * Necessita del login come bot <br>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 * <p>
 * La (List<Bio>) listaBio, la (List<String>) listaDidascalie, la (Map<String, List>) mappa e (String) testoConParagrafi
 * vengono tutte regolate alla creazione dell'istanza in @PostConstruct e sono disponibili da subito <br>
 * Si usa SOLO la chiamata appContext.getBean(UploadXxx.class, yyy) per caricare l'istanza ListaXxx.class <br>
 * L'effettivo upload su wiki avviene SOLO con uploadPagina() o uploadPaginaTest() <br>
 */
public abstract class Upload {

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiUtility wikiUtility;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService textService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DateService dateService;

    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * La mappa viene creata nel @PostConstruct dell'istanza <br>
     */
    protected TreeMap<String, TreeMap<String, List<String>>> mappa;

    protected LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie;

    protected String newText2;

    protected String nomeAttivitaPlurale;

    protected WResult esegue(String wikiTitle, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie) {
        StringBuffer buffer = new StringBuffer();
        int numVoci = wikiUtility.getSizeAll(mappaDidascalie);

        buffer.append(avviso());
        buffer.append(CAPO);
        buffer.append(includeIni());
        buffer.append(forceToc());
        buffer.append(tmpListaBio(numVoci));
        buffer.append(includeEnd());
        buffer.append(CAPO);
        buffer.append(incipit());
        buffer.append(CAPO);
        buffer.append(body(wikiTitle, mappaDidascalie));
        buffer.append(note());
        buffer.append(CAPO);
        buffer.append(correlate());
        buffer.append(CAPO);
        buffer.append(portale());
        buffer.append(categorie());

        return registra(wikiTitle, buffer.toString());
    }


    protected String avviso() {
        return "<!-- NON MODIFICATE DIRETTAMENTE QUESTA PAGINA - GRAZIE -->";
    }


    protected String includeIni() {
        return "<noinclude>";
    }

    protected String forceToc() {
        return "__FORCETOC__";
    }

    protected String tmpListaBio(int numVoci) {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM yyy")); ;
        String progetto = "biografie";

        return String.format("{{ListaBio|bio=%d|data=%s|progetto=%s}}", numVoci, data, progetto);
    }

    protected String includeEnd() {
        return "</noinclude>";
    }

    protected String incipit() {
        StringBuffer buffer = new StringBuffer();
        String message;
        int maxPagina = WPref.sogliaAttNazWiki.getInt();
        int maxSottoPagina = WPref.sogliaSottoPagina.getInt();
        String prog = "Attività";
        String mod = "Bio/Plurale attività";
        String pack = nomeAttivitaPlurale;

        buffer.append("Questa è una lista");
        message = "Le didascalie delle voci sono quelle previste nel [[Progetto:Biografie/Didascalie|progetto biografie]]";
        buffer.append(textService.setRef(message));

        message = "Le voci, all'interno di ogni paragrafo, sono in ordine alfabetico per '''cognome'''; se questo manca si utilizza il '''titolo''' della pagina.";
        buffer.append(textService.setRef(message));

        buffer.append(" di persone");
        message = "La lista non è esaustiva e contiene solo le persone che sono citate nell'enciclopedia e per le quali è stato " +
                "implementato correttamente il [[template:Bio|template Bio]]";
        buffer.append(textService.setRef(message));

        buffer.append(" presenti");
        message = String.format("La pagina di una singola attività viene creata se le relative voci biografiche superano le '''%d''' unità.", maxPagina);
        buffer.append(textService.setRef(message));

        if (WPref.usaTreAttivita.is()) {
            buffer.append(" nell'enciclopedia che hanno tra le attività");
        }
        else {
            buffer.append(" nell'enciclopedia che hanno come attività");
        }
        message = String.format("Le attività sono quelle [[Discussioni progetto:Biografie/%s|'''convenzionalmente''' previste]] dalla " +
                "comunità ed [[Modulo:%s|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]", prog, mod);
        buffer.append(textService.setRef(message));

        if (WPref.usaTreAttivita.is()) {
            message = "Ogni persona è presente in diverse [[Progetto:Biografie/Attività|liste]], in base a quanto riportato in " +
                    "uno dei 3 parametri ''attività, attività2 e attività3'' del [[template:Bio|template Bio]] presente nella voce " +
                    "biografica specifica della persona";
        }
        else {
            buffer.append(" principale");
            message = "Ogni persona è presente in una sola [[Progetto:Biografie/Attività|lista]], in base a quanto riportato " +
                    "nel (primo) parametro ''attività'' del [[template:Bio|template Bio]] presente nella voce biografica specifica della " +
                    "persona";
        }
        buffer.append(textService.setRef(message));

        message = String.format(" quella di '''%s'''.", pack);
        buffer.append(message);
        buffer.append(" Le persone sono suddivise");
        message = String.format("La lista è suddivisa in paragrafi per ogni nazionalità individuata. Se il numero di voci biografiche nel" +
                " paragrafo supera le '''%s''' unità, viene creata una sottopagina.", maxSottoPagina);
        buffer.append(textService.setRef(message));

        buffer.append(" per nazionalità.");
        message = "Le nazionalità sono quelle [[Discussioni progetto:Biografie/Nazionalità|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale nazionalità|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";
        buffer.append(textService.setRef(message));

        message = "Nel paragrafo Altre... (eventuale) vengono raggruppate quelle voci biografiche che '''non''' usano il parametro " +
                "''nazionalità'' oppure che usano una nazionalità di difficile elaborazione da parte del '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]'''";
        buffer.append(textService.setRef(message));

        return buffer.toString();
    }

    protected String body(String wikiTitle, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDidascalie) {
        return mappaToText(wikiTitle, mappaDidascalie);
    }

    protected String note() {
        return "==Note==" + CAPO + "<references/>" + CAPO;
    }

    protected String correlate() {
        String cat = textService.primaMaiuscola(nomeAttivitaPlurale);
        return "==Voci correlate==" + CAPO + String.format("*[[:Categoria:%s]]", cat) + CAPO + "*[[Progetto:Biografie/Attività]]" + CAPO;
    }

    protected String portale() {
        return "{{Portale|biografie}}" + CAPO;
    }


    protected String categorie() {
        String cat = textService.primaMaiuscola(nomeAttivitaPlurale);
        return String.format("[[Categoria:Bio attività|%s]]",cat);
    }


    protected WResult registra(String wikiTitle, String newText) {
        //        newText = mappaToText(WIKI_TITLE_DEBUG,mappaDidascalie);
        return appContext.getBean(QueryWrite.class).urlRequest(wikiTitle, newText);
    }

    protected String mappaToText(String wikiTitle, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaTxt) {
        StringBuffer buffer = new StringBuffer();
        LinkedHashMap<String, List<String>> mappaSub;
        int numVoci = 0;

        if (mappaTxt != null) {
            System.out.println(VUOTA);

            for (String key : mappaTxt.keySet()) {
                mappaSub = mappaTxt.get(key);
                numVoci = wikiUtility.getSize(mappaSub);

                buffer.append(fixTitoloParagrafo(wikiTitle, key, numVoci));
                buffer.append(CAPO);
                buffer.append(fixCorpoParagrafo(wikiTitle, key, numVoci, mappaSub));
                buffer.append(CAPO);
            }
        }

        return buffer.toString();
    }

    protected String fixTitoloParagrafo(String wikiTitle, String titoloParagrafo, int numVoci) {
        if (WPref.usaParagrafiDimensionati.is()) {
            return wikiUtility.fixTitoloDimensionato(wikiTitle, titoloParagrafo, numVoci);
        }
        else {
            return wikiUtility.fixTitolo(wikiTitle, titoloParagrafo);
        }
    }


    protected String fixCorpoParagrafo(String wikiTitle, String titoloParagrafo, int numVoci, LinkedHashMap<String, List<String>> mappaSub) {
        StringBuffer buffer = new StringBuffer();
        List<String> lista;
        int max = WPref.sogliaSottoPagina.getInt();

        if (numVoci > max) {
            String vedi = String.format("{{Vedi anche|%s%s%s}}", wikiTitle, SLASH, textService.primaMaiuscola(titoloParagrafo));
            buffer.append(vedi + CAPO);
            this.uploadSottoPagina(wikiTitle, titoloParagrafo, numVoci, mappaSub);
        }
        else {
            for (String key : mappaSub.keySet()) {
                lista = mappaSub.get(key);
                for (String didascalia : lista) {
                    buffer.append(didascalia + CAPO);
                }
            }
        }

        return buffer.toString();
    }


    /**
     * Esegue la scrittura della sotto-pagina <br>
     */
    public void uploadSottoPagina(String wikiTitle, String titoloParagrafo, int numVoci, LinkedHashMap<String, List<String>> mappaSub) {
        //        newText = subMappaToText(wikiTitle, mappaSub);
        //        appContext.getBean(QueryWrite.class).urlRequest(wikiTitle + SLASH + textService.primaMaiuscola(titoloParagrafo), newText);
    }

    protected String subMappaToText(String wikiTitle, LinkedHashMap<String, List<String>> mappaSub) {
        StringBuffer buffer = new StringBuffer();
        List<String> listaSub;

        if (mappaSub != null) {
            System.out.println(VUOTA);

            for (String key : mappaSub.keySet()) {
                listaSub = mappaSub.get(key);

                buffer.append(fixTitoloParagrafo(wikiTitle, key, listaSub.size()));
                buffer.append(CAPO);
                for (String didascalia : listaSub) {
                    buffer.append(didascalia + CAPO);
                }
                buffer.append(CAPO);
            }
        }

        return buffer.toString();
    }

    //    protected String mappaSubToText(LinkedHashMap<String, List<String>> mappaSub) {
    //        String text = VUOTA;
    //        StringBuffer buffer = new StringBuffer();
    //        List<String> lista;
    //        int max = 12; //@todo da preferenze
    //
    //        if (mappaSub != null) {
    //            System.out.println(VUOTA);
    //            if (mappaSub.size() > max) {
    //                String vedi = "{{Vedi anche|Progetto:Biografie/Attività/Accademici/Statunitensi}}";
    //                buffer.append(vedi + CAPO);
    //            }
    //            else {
    //                for (String key : mappaSub.keySet()) {
    //                    lista = mappaSub.get(key);
    //                    for (String didascalia : lista) {
    //                        buffer.append(didascalia + CAPO);
    //                    }
    //                }
    //            }
    //        }
    //
    //        return buffer.toString();
    //    }


    public String setParagrafo(final String titolo) {
        return setParagrafo(titolo, 0);
    }


    /**
     * Inserisce un numero in caratteri ridotti <br>
     *
     * @param titolo da inglobare nei tag wiki (paragrafo)
     * @param numero da visualizzare (maggiore di zero)
     *
     * @return testo coi tag html
     */
    public String setParagrafo(final String titolo, final int numero) {
        String paragrafo = VUOTA;

        paragrafo += PARAGRAFO;
        paragrafo += SPAZIO;
        paragrafo += titolo;
        if (numero > 0) {
            paragrafo += SPAZIO;
            paragrafo += smallNumero(numero);
        }
        paragrafo += SPAZIO;
        paragrafo += PARAGRAFO;
        paragrafo += CAPO;

        return paragrafo;
    }


    /**
     * Inserisce un numero in caratteri ridotti <br>
     *
     * @param numero da visualizzare
     *
     * @return testo coi tag html
     */
    public String smallNumero(final int numero) {
        String testo = VUOTA;

        testo += "<span style=\"font-size:70%\">(";
        testo += numero;
        testo += ")</span>";

        return testo;
    }

}
