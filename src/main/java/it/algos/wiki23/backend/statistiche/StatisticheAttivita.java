package it.algos.wiki23.backend.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
import static it.algos.wiki23.backend.upload.Upload.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 01-Jul-2022
 * Time: 10:35
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatisticheAttivita extends Statistiche {

    private int attivitaUsate;

    private int attivitaParzialmenteUsate;

    private int attivitaNonUsate;

    private int attivitaTotali;

    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadStatisticheAttivita.class, upload()) <br>
     * Non rimanda al costruttore della superclasse. <br>
     */
    public StatisticheAttivita() {
    }// end of constructor

    public StatisticheAttivita test() {
        this.uploadTest = true;
        return this;
    }

    /**
     * Preferenze usate da questa 'view' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.typeToc = AETypeToc.forceToc;
    }


    /**
     * Elabora i dati
     */
    protected void elabora() {
        attivitaBackend.elabora();
    }

    /**
     * Recupera la lista
     */
    @Override
    protected void creaLista() {
        lista = attivitaBackend.findAttivitaDistinctByPluraliSortPagina();
    }

    /**
     * Costruisce la mappa <br>
     * Aggiorna la entity di Attivita <br>
     */
    @Override
    protected void creaMappa() {
        super.creaMappa();
        MappaStatistiche mappaSingola;
        List<String> singolari;
        int numAttivitaUno;
        int numAttivitaDue;
        int numAttivitaTre;
        int numAttivitaTotali;
        boolean superaSoglia;
        boolean esistePagina;
        int soglia = WPref.sogliaAttNazWiki.getInt();
        int numVoci;
        int cancellande = 0;

        for (Attivita attivita : (List<Attivita>) lista) {
            singolari = attivitaBackend.findAllSingolariByPlurale(attivita.pluraleLista);
            esistePagina = attivita.esistePaginaLista;
            numAttivitaUno = 0;
            numAttivitaDue = 0;
            numAttivitaTre = 0;

            for (String singolare : singolari) {
                numAttivitaUno += bioBackend.countAttivitaSingola(singolare);
                numAttivitaDue += bioBackend.countAttivitaDue(singolare);
                numAttivitaTre += bioBackend.countAttivitaTre(singolare);
            }

            numAttivitaTotali = numAttivitaUno + numAttivitaDue + numAttivitaTre;
            numVoci = WPref.usaTreAttivita.is() ? numAttivitaTotali : numAttivitaUno;
            superaSoglia = numVoci >= soglia;
            if (superaSoglia != attivita.superaSoglia) {
                attivita.superaSoglia = superaSoglia;
                attivitaBackend.save(attivita);
                logger.info(new WrapLog().message(String.format("Aggiornato il flag '%s' di %s. Adesso è %s", "superaSoglia", attivita.pluraleLista, superaSoglia)));
            }

            if (numVoci < 50 && attivita.esistePaginaLista) {
                esistePagina = attivitaBackend.esistePagina(attivita.pluraleLista);
                cancellande++;
                logger.info(new WrapLog().message(String.format("Check del flag %s (che è true) anche se l'attività %s ha solo %d voci", "esistePagina", attivita.pluraleLista, numVoci)));
            }
            if (numVoci >= 50 && !attivita.esistePaginaLista) {
                esistePagina = attivitaBackend.esistePagina(attivita.pluraleLista);
                logger.info(new WrapLog().message(String.format("Check del flag %s (che è false) anche se l'attività %s ha addirittura %d voci", "esistePagina", attivita.pluraleLista, numVoci)));
            }
            if (esistePagina != attivita.esistePaginaLista) {
                attivita.esistePaginaLista = esistePagina;
                attivitaBackend.save(attivita);
                logger.info(new WrapLog().message(String.format("Aggiornato il flag '%s' di %s. Adesso è %s", "esistePagina", attivita.pluraleLista, esistePagina)));
            }

            mappaSingola = new MappaStatistiche(attivita.pluraleLista, numAttivitaUno, numAttivitaDue, numAttivitaTre, superaSoglia, esistePagina);
            mappa.put(attivita.pluraleLista, mappaSingola);
        }
        logger.info(new WrapLog().message(String.format("Ci sono %d pagine di Attività che andrebbero cancellate", cancellande)).usaDb());
    }

    @Override
    protected String incipit() {
        StringBuffer buffer = new StringBuffer();
        String message;

        message = "Le attività sono quelle [[Discussioni progetto:Biografie/Attività|'''convenzionalmente''' previste]] " +
                "dalla comunità ed [[Modulo:Bio/Plurale attività|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";
        buffer.append(message);

        return buffer.toString();
    }


    protected String body() {
        StringBuffer buffer = new StringBuffer();
        this.fixConteggi();

        buffer.append(usate());
        buffer.append(parzialmenteUsate());
        buffer.append(nonUsate());

        return buffer.toString();
    }

    protected void fixConteggi() {
        int usate = 0;
        int parzialmenteUsate = 0;
        int nonUsate = 0;
        MappaStatistiche singolaMappa;

        if (mappa != null) {
            for (String key : mappa.keySet()) {
                singolaMappa = mappa.get(key);
                if (singolaMappa.getNumAttivitaUno() > 0) {
                    usate++;
                }
                else {
                    if (singolaMappa.getNumAttivitaTotali() > 0) {
                        parzialmenteUsate++;
                    }
                    else {
                        nonUsate++;
                    }
                }
            }
            attivitaUsate = usate;
            attivitaParzialmenteUsate = parzialmenteUsate;
            attivitaNonUsate = nonUsate;
            attivitaTotali = usate + parzialmenteUsate + nonUsate;
        }
    }

    /**
     * Prima tabella <br>
     */
    protected String usate() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(wikiUtility.setParagrafo("Attività usate"));
        buffer.append(incipitUsate());
        buffer.append(inizioTabella());
        buffer.append(colonneUsate());
        buffer.append(corpoUsate());
        buffer.append(fineTabella());
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String incipitUsate() {
        StringBuffer buffer = new StringBuffer();
        String message;
        int vociBio = bioBackend.count();
        buffer.append(String.format("'''%s''' attività", textService.format(attivitaUsate)));
        if (WPref.usaTreAttivita.is()) {
            message = LISTA_ATTIVITA_TRE;
        }
        else {
            message = LISTA_ATTIVITA_UNICA;
        }
        buffer.append(textService.setRef(message));
        buffer.append(" '''effettivamente utilizzate''' nelle");
        buffer.append(String.format(" '''%s'''", textService.format(vociBio)));
        message = "La '''differenza''' tra le voci della categoria e quelle utilizzate è dovuta allo specifico utilizzo del [[template:Bio|template Bio]] ed in particolare all'uso del parametro Categorie=NO";
        buffer.append(textService.setRef(message));
        buffer.append(" voci biografiche che usano il [[template:Bio|template Bio]] e");
        if (WPref.usaTreAttivita.is()) {
            buffer.append(" i parametri '''''attività, attività2, attività3'''''.");
            //            message = "Ogni persona è presente in '''diverse liste''', in base a quanto riportato in uno dei '''3''' parametri '''''attività, attività2 e attività3''''' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona";
        }
        else {
            buffer.append(" il '''primo''' parametro '''''attività'''''.");
            //            message = String.format("Ogni persona è presente in '''una sola lista''', in base a quanto riportato nel" +
            //                    " '''primo''' parametro '''''attività''''' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona");
        }
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String inizioTabella() {
        String testo = VUOTA;

        testo += CAPO;
        testo += "{|class=\"wikitable sortable\" style=\"background-color:#EFEFEF; text-align: left;\"";
        testo += CAPO;

        return testo;
    }

    protected String colonneUsate() {
        StringBuffer buffer = new StringBuffer();
        String color = "! style=\"background-color:#CCC;\" |";
        String message;
        String soglia = textService.setBold(textService.format(WPref.sogliaAttNazWiki.getInt()));
        String sub = textService.setBold(textService.format(WPref.sogliaSottoPagina.getInt()));
        buffer.append(VUOTA);
        buffer.append(color);
        buffer.append(textService.setBold("#"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("lista"));

        message = String.format("La pagina di una singola %s viene creata se le relative voci biografiche superano le %s unità.", "attività", soglia);
        buffer.append(textService.setRef(message));

        message = String.format("La lista è suddivisa in paragrafi per ogni '''[[Modulo:Bio/Plurale nazionalità|nazionalità]]''' individuata. Se il numero" +
                " di voci biografiche nel paragrafo supera le %s unità, viene creata una sottopagina.", sub);
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);

        buffer.append(color);
        buffer.append(textService.setBold("categoria"));
        message = "Le categorie possono avere sottocategorie e suddivisioni diversamente articolate e possono avere anche voci che hanno implementato la categoria stessa al di fuori del [[template:Bio|template Bio]].";
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);

        if (WPref.usaTreAttivita.is()) {
            buffer.append(color);
            buffer.append(textService.setBold("1° att"));
            buffer.append(CAPO);

            buffer.append(color);
            buffer.append(textService.setBold("2° att"));
            buffer.append(CAPO);

            buffer.append(color);
            buffer.append(textService.setBold("3° att"));
            buffer.append(CAPO);

            buffer.append(color);
            buffer.append(textService.setBold("totale"));
            buffer.append(CAPO);
        }
        else {
            buffer.append(color);
            buffer.append(textService.setBold("voci"));
            buffer.append(CAPO);
        }

        return buffer.toString();
    }

    protected String corpoUsate() {
        StringBuffer buffer = new StringBuffer();
        int cont = 1;
        int k = 1;
        String riga;
        MappaStatistiche mappaSingola;
        String message;
        boolean treAttivita = WPref.usaTreAttivita.is();
        int soglia = WPref.sogliaAttNazWiki.getInt();
        boolean linkLista = WPref.usaLinkStatistiche.is();

        for (String key : mappa.keySet()) {
            mappaSingola = mappa.get(key);
            if (mappaSingola.isUsata(treAttivita)) {
                riga = rigaUsate(mappaSingola, treAttivita, cont, soglia, linkLista);
                if (textService.isValid(riga)) {
                    buffer.append(riga);
                    k = k + 1;
                    cont = k;
                }
            }
        }

        cont--;
        if (cont != attivitaUsate) {
            message = String.format("Le attività usate non corrispondono: previste=%s trovate=%s", attivitaUsate, cont);
            logger.error(new WrapLog().exception(new AlgosException(message)));
        }

        return buffer.toString();
    }


    protected String rigaUsate(MappaStatistiche mappaSingola, boolean treAttivita, int cont, int soglia, boolean linkLista) {
        StringBuffer buffer = new StringBuffer();
        String tagDex = "style=\"text-align: right;\" |";
        String nome = textService.primaMinuscola(mappaSingola.getChiave());
        String categoriaTag = "[[:Categoria:";
        String iniTag = "|-";
        String doppioTag = " || ";
        String pipe = "|";
        String endTag = "]]";
        String categoria = categoriaTag + nome + pipe + nome + endTag;

        buffer.append(iniTag);
        buffer.append(CAPO);
        buffer.append(pipe);

        buffer.append(cont);

        buffer.append(doppioTag);
        buffer.append(fixNomeUsate(mappaSingola, soglia, linkLista));

        buffer.append(doppioTag);
        buffer.append(categoria);
        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(mappaSingola.getNumAttivitaUno());

        if (treAttivita) {
            buffer.append(doppioTag);
            buffer.append(tagDex);
            buffer.append(mappaSingola.getNumAttivitaDue());

            buffer.append(doppioTag);
            buffer.append(tagDex);
            buffer.append(mappaSingola.getNumAttivitaTre());

            buffer.append(doppioTag);
            buffer.append(tagDex);
            buffer.append(mappaSingola.getNumAttivitaTotali());
        }
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String fixNomeUsate(MappaStatistiche mappa, int soglia, boolean linkLista) {
        String plurale = mappa.getChiave();
        String nomeVisibile = plurale;
        boolean superaSoglia;
        int numVociBio;
        int previste = WPref.usaTreAttivita.is() ? mappa.getNumAttivitaTotali() : mappa.getNumAttivitaUno();
        String listaTag = "Progetto:Biografie/Attività/";

        numVociBio = bioBackend.countAttivitaPlurale(plurale);
        if (numVociBio != previste) {
            logger.info(new WrapLog().message(String.format("L'attività %s ha attualmente %d voci invece delle %d previste", plurale, numVociBio, previste)));
        }

        superaSoglia = numVociBio >= soglia;
        if (superaSoglia && linkLista) {
            nomeVisibile = listaTag + textService.primaMaiuscola(plurale) + PIPE + plurale;
            nomeVisibile = textService.setDoppieQuadre(nomeVisibile);
        }

        return nomeVisibile;
    }


    /**
     * Seconda tabella <br>
     */
    protected String parzialmenteUsate() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(wikiUtility.setParagrafo("Attività parzialmente usate"));
        buffer.append(incipitParzialmenteUsate());
        buffer.append(inizioTabella());
        buffer.append(colonneParzialmenteUsate());
        buffer.append(corpoParzialmenteUsate());
        buffer.append(fineTabella());
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String incipitParzialmenteUsate() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("'''%s''' attività presenti", textService.format(attivitaParzialmenteUsate)));
        buffer.append(" nell' [[Modulo:Bio/Plurale attività|'''elenco del progetto Biografie''']] ma '''non utilizzate''' nel primo parametro ''attività''");
        buffer.append(" di qualsiasi '''voce biografica''' che usa il [[template:Bio|template Bio]]");
        buffer.append(CAPO);

        return buffer.toString();
    }


    protected String colonneParzialmenteUsate() {
        StringBuffer buffer = new StringBuffer();
        String color = "! style=\"background-color:#CCC;\" |";
        buffer.append(VUOTA);
        buffer.append(color);
        buffer.append(textService.setBold("#"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("attività"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("categoria"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("1° att"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("2° att"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("3° att"));
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String corpoParzialmenteUsate() {
        StringBuffer buffer = new StringBuffer();
        MappaStatistiche mappaSingola;
        int cont = 1;
        int k = 1;
        String riga;
        String message;
        boolean treAttivita = WPref.usaTreAttivita.is();
        boolean linkLista = WPref.usaLinkStatistiche.is();

        for (String key : mappa.keySet()) {
            mappaSingola = mappa.get(key);
            if (mappaSingola.isUsataParzialmente()) {
                riga = rigaParzialmenteUsate(key, mappaSingola, treAttivita, cont, 0, linkLista);
                if (textService.isValid(riga)) {
                    buffer.append(riga);
                    k = k + 1;
                    cont = k;
                }
            }
        }

        cont--;
        if (cont != attivitaParzialmenteUsate) {
            message = String.format("Le attività parzialmente usate non corrispondono: previste=%s trovate=%s", attivitaParzialmenteUsate, cont);
            logger.error(new WrapLog().exception(new AlgosException(message)));
        }

        return buffer.toString();
    }


    protected String rigaParzialmenteUsate(String plurale, MappaStatistiche mappaSingola, boolean treAttivita, int cont, int soglia, boolean linkLista) {
        StringBuffer buffer = new StringBuffer();
        String tagSin = "style=\"text-align: left;\" |";
        String nome = plurale.toLowerCase();
        String categoriaTag = "[[:Categoria:";
        String iniTag = "|-";
        String doppioTag = " || ";
        String pipe = "|";
        String endTag = "]]";
        String categoria;

        categoria = categoriaTag + nome + pipe + nome + endTag;
        buffer.append(iniTag);
        buffer.append(CAPO);
        buffer.append(pipe);

        buffer.append(cont);

        buffer.append(doppioTag);
        buffer.append(tagSin);
        buffer.append(plurale);

        buffer.append(doppioTag);
        buffer.append(tagSin);
        buffer.append(categoria);
        buffer.append(doppioTag);
        buffer.append(mappaSingola.getNumAttivitaUno());

        buffer.append(doppioTag);
        buffer.append(mappaSingola.getNumAttivitaDue());

        buffer.append(doppioTag);
        buffer.append(mappaSingola.getNumAttivitaTre());
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String fixNomeParzialmenteUsate(String nomeAttivitaPlurale, int soglia, boolean linkLista) {
        String nomeVisibile = nomeAttivitaPlurale;
        boolean superaSoglia;
        int numVociBio;
        String listaTag = "Progetto:Biografie/Attività/";

        numVociBio = bioBackend.countAttivitaPlurale(nomeAttivitaPlurale);
        superaSoglia = numVociBio >= soglia;
        if (superaSoglia && linkLista) {
            nomeVisibile = listaTag + textService.primaMaiuscola(nomeAttivitaPlurale) + PIPE + nomeAttivitaPlurale;
            nomeVisibile = textService.setDoppieQuadre(nomeVisibile);
        }

        return nomeVisibile;
    }

    /**
     * Terza tabella <br>
     */
    protected String nonUsate() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(wikiUtility.setParagrafo("Attività non usate"));
        buffer.append(incipitNonUsate());
        buffer.append(inizioTabella());
        buffer.append(colonneNonUsate());
        buffer.append(corpoNonUsate());
        buffer.append(fineTabella());
        buffer.append(CAPO);

        return buffer.toString();
    }


    protected String incipitNonUsate() {
        StringBuffer buffer = new StringBuffer();
        String message;
        buffer.append(String.format("'''%s''' attività presenti", textService.format(attivitaNonUsate)));
        buffer.append(" nell' [[Modulo:Bio/Plurale attività|'''elenco del progetto Biografie''']] ma '''non utilizzate''' in nessuno dei " +
                "3 parametri ''attività, attività2, attività3''");
        message = "Si tratta di attività '''originariamente''' discusse ed [[Modulo:Bio/Plurale attività|inserite nell'elenco]] che non sono '''mai''' state utilizzate o che sono state in un secondo tempo sostituite da altre denominazioni";
        buffer.append(textService.setRef(message));
        buffer.append(" di qualsiasi '''voce biografica''' che usa il [[template:Bio|template Bio]]");
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String colonneNonUsate() {
        StringBuffer buffer = new StringBuffer();
        String color = "! style=\"background-color:#CCC;\" |";
        buffer.append(VUOTA);
        buffer.append(color);
        buffer.append(textService.setBold("#"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("attività"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("categoria"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("1° att"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("2° att"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("3° att"));
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String corpoNonUsate() {
        StringBuffer buffer = new StringBuffer();
        MappaStatistiche singolaMappa;
        int cont = 1;
        int k = 1;
        String riga;

        for (String key : mappa.keySet()) {
            singolaMappa = mappa.get(key);
            if (singolaMappa.getNumAttivitaTotali() < 1) {
                riga = rigaNonUsate(key, cont);
                if (textService.isValid(riga)) {
                    buffer.append(riga);
                    k = k + 1;
                    cont = k;
                }
            }
        }

        return buffer.toString();
    }

    protected String rigaNonUsate(String plurale, int cont) {
        StringBuffer buffer = new StringBuffer();
        String tagSin = "style=\"text-align: left;\" |";
        String nome = plurale.toLowerCase();
        String listaTag = "[[Progetto:Biografie/Attività/";
        String categoriaTag = "[[:Categoria:";
        String iniTag = "|-";
        String doppioTag = " || ";
        String pipe = "|";
        String endTag = "]]";
        String categoria;
        MappaStatistiche mappaSingola;

        categoria = categoriaTag + nome + pipe + nome + endTag;
        mappaSingola = mappa.get(plurale);
        if (mappaSingola == null) {
            return VUOTA;
        }

        buffer.append(iniTag);
        buffer.append(CAPO);
        buffer.append(pipe);

        buffer.append(cont);

        buffer.append(doppioTag);
        buffer.append(tagSin);
        buffer.append(fixNomeParzialmenteUsate(plurale, 0, false));

        buffer.append(doppioTag);
        buffer.append(tagSin);
        buffer.append(categoria);
        buffer.append(doppioTag);
        buffer.append(mappaSingola.getNumAttivitaUno());

        buffer.append(doppioTag);
        buffer.append(mappaSingola.getNumAttivitaDue());

        buffer.append(doppioTag);
        buffer.append(mappaSingola.getNumAttivitaTre());
        buffer.append(CAPO);

        return buffer.toString();
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload() {
        super.esegue();
        return super.upload(uploadTest ? UPLOAD_TITLE_DEBUG + PATH_ATTIVITA : PATH_ATTIVITA);
    }

}
