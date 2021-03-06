package it.algos.wiki23.backend.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
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
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
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
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload() {
        return super.upload(PATH_ATTIVITA);
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
        buffer.append(wikiUtility.setParagrafo("Attivit?? usate"));
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
        buffer.append(String.format("'''%s''' attivit??", textService.format(attivitaUsate)));
        message = "Le attivit?? sono quelle [[Discussioni progetto:Biografie/Attivit??|'''convenzionalmente''' previste]] " +
                "dalla comunit?? ed [[Modulo:Bio/Plurale attivit??|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";
        buffer.append(textService.setRef(message));
        buffer.append(" '''effettivamente utilizzate''' nelle");
        buffer.append(String.format(" '''%s'''", textService.format(vociBio)));
        message = "La '''differenza''' tra le voci della categoria e quelle utilizzate ?? dovuta allo specifico utilizzo del [[template:Bio|template Bio]] ed in particolare all'uso del parametro Categorie=NO";
        buffer.append(textService.setRef(message));
        buffer.append(" voci biografiche che usano il [[template:Bio|template Bio]] e");
        if (WPref.usaTreAttivita.is()) {
            buffer.append(" i parametri '''''attivit??, attivit??2, attivit??3'''''.");
//            message = "Ogni persona ?? presente in '''diverse liste''', in base a quanto riportato in uno dei '''3''' parametri '''''attivit??, attivit??2 e attivit??3''''' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona";
            message = LISTA_ATTIVITA_TRE;
        }
        else {
            buffer.append(" il '''primo''' parametro '''''attivit??'''''.");
//            message = String.format("Ogni persona ?? presente in '''una sola lista''', in base a quanto riportato nel" +
//                    " '''primo''' parametro '''''attivit??''''' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona");
            message = LISTA_ATTIVITA_UNICA;
        }
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);

        return buffer.toString();
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

        message = String.format("La pagina di una singola %s viene creata se le relative voci biografiche superano le %s unit??.", "attivit??", soglia);
        buffer.append(textService.setRef(message));

        message = String.format("La lista ?? suddivisa in paragrafi per ogni '''[[Modulo:Bio/Plurale nazionalit??|nazionalit??]]''' individuata. Se il numero" +
                " di voci biografiche nel paragrafo supera le %s unit??, viene creata una sottopagina.", sub);
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);

        buffer.append(color);
        buffer.append(textService.setBold("categoria"));
        message = "Le categorie possono avere sottocategorie e suddivisioni diversamente articolate e possono avere anche voci che hanno implementato la categoria stessa al di fuori del [[template:Bio|template Bio]].";
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);

        if (WPref.usaTreAttivita.is()) {
            buffer.append(color);
            buffer.append(textService.setBold("1?? att"));
            buffer.append(CAPO);

            buffer.append(color);
            buffer.append(textService.setBold("2?? att"));
            buffer.append(CAPO);

            buffer.append(color);
            buffer.append(textService.setBold("3?? att"));
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
                riga = rigaUsate(key, mappaSingola, treAttivita, cont, soglia, linkLista);
                if (textService.isValid(riga)) {
                    buffer.append(riga);
                    k = k + 1;
                    cont = k;
                }
            }
        }

        cont--;
        if (cont != attivitaUsate) {
            message = String.format("Le attivit?? usate non corrispondono: previste=%s trovate=%s", attivitaUsate, cont);
            logger.error(new WrapLog().exception(new AlgosException(message)));
        }

        return buffer.toString();
    }


    protected String rigaUsate(String plurale, MappaStatistiche mappaSingola, boolean treAttivita, int cont, int soglia, boolean linkLista) {
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
        buffer.append(fixNomeUsate(plurale, soglia, linkLista));

        buffer.append(doppioTag);
        buffer.append(tagSin);
        buffer.append(categoria);
        buffer.append(doppioTag);
        buffer.append(mappaSingola.getNumAttivitaUno());

        if (treAttivita) {
            buffer.append(doppioTag);
            buffer.append(mappaSingola.getNumAttivitaDue());

            buffer.append(doppioTag);
            buffer.append(mappaSingola.getNumAttivitaTre());

            buffer.append(doppioTag);
            buffer.append(mappaSingola.getNumAttivitaTotali());
        }
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String fixNomeUsate(String nomeAttivitaPlurale, int soglia, boolean linkLista) {
        String nomeVisibile = nomeAttivitaPlurale;
        boolean superaSoglia;
        int numVociBio;
        String listaTag = "Progetto:Biografie/Attivit??/";

        numVociBio = bioBackend.countAttivitaPlurale(nomeAttivitaPlurale);
        superaSoglia = numVociBio >= soglia;
        if (superaSoglia || linkLista) {
            nomeVisibile = listaTag + textService.primaMaiuscola(nomeAttivitaPlurale) + PIPE + nomeAttivitaPlurale;
            nomeVisibile = textService.setDoppieQuadre(nomeVisibile);
        }

        return nomeVisibile;
    }
    protected String fixNomeParzialmenteUsate(String nomeAttivitaPlurale, int soglia, boolean linkLista) {
        String nomeVisibile = nomeAttivitaPlurale;
        boolean superaSoglia;
        int numVociBio;
        String listaTag = "Progetto:Biografie/Attivit??/";

        numVociBio = bioBackend.countAttivitaPlurale(nomeAttivitaPlurale);
        superaSoglia = numVociBio >= soglia;
        if (superaSoglia && linkLista) {
            nomeVisibile = listaTag + textService.primaMaiuscola(nomeAttivitaPlurale) + PIPE + nomeAttivitaPlurale;
            nomeVisibile = textService.setDoppieQuadre(nomeVisibile);
        }

        return nomeVisibile;
    }


    /**
     * Seconda tabella <br>
     */
    protected String parzialmenteUsate() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(wikiUtility.setParagrafo("Attivit?? parzialmente usate"));
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
        buffer.append(String.format("'''%s''' attivit?? presenti", textService.format(attivitaParzialmenteUsate)));
        buffer.append(" nell' [[Modulo:Bio/Plurale attivit??|'''elenco del progetto Biografie''']] ma '''non utilizzate''' nel primo parametro ''attivit??''");
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
        buffer.append(textService.setBold("attivit??"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("categoria"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("1?? att"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("2?? att"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("3?? att"));
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
            message = String.format("Le attivit?? parzialmente usate non corrispondono: previste=%s trovate=%s", attivitaParzialmenteUsate, cont);
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
        buffer.append(fixNomeParzialmenteUsate(plurale, soglia, linkLista));

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
     * Terza tabella <br>
     */
    protected String nonUsate() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(wikiUtility.setParagrafo("Attivit?? non usate"));
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
        buffer.append(String.format("'''%s''' attivit?? presenti", textService.format(attivitaNonUsate)));
        buffer.append(" nell' [[Modulo:Bio/Plurale attivit??|'''elenco del progetto Biografie''']] ma '''non utilizzate''' in nessuno dei " +
                "3 parametri ''attivit??, attivit??2, attivit??3''");
        message = "Si tratta di attivit?? '''originariamente''' discusse ed [[Modulo:Bio/Plurale attivit??|inserite nell'elenco]] che non sono '''mai''' state utilizzate o che sono state in un secondo tempo sostituite da altre denominazioni";
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
        buffer.append(textService.setBold("attivit??"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("categoria"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("1?? att"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("2?? att"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("3?? att"));
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
        String listaTag = "[[Progetto:Biografie/Attivit??/";
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
        buffer.append(fixNomeParzialmenteUsate(plurale, 0,false));

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
     * Recupera la lista
     */
    @Override
    protected void creaLista() {
        lista = attivitaBackend.findAttivitaDistinctByPlurali();
    }

    /**
     * Costruisce la mappa <br>
     */
    @Override
    protected void creaMappa() {
        super.creaMappa();
        MappaStatistiche mappaSingola;
        List<String> singolari = null;
        int numAttivitaUno;
        int numAttivitaDue;
        int numAttivitaTre;

        for (Attivita attivita : (List<Attivita>) lista) {
            singolari = attivitaBackend.findSingolariByPlurale(attivita.plurale);
            numAttivitaUno = 0;
            numAttivitaDue = 0;
            numAttivitaTre = 0;

            for (String singolare : singolari) {
                numAttivitaUno += bioBackend.countAttivita(singolare);
                numAttivitaDue += bioBackend.countAttivitaDue(singolare);
                numAttivitaTre += bioBackend.countAttivitaTre(singolare);
            }

            mappaSingola = new MappaStatistiche(attivita.plurale, numAttivitaUno, numAttivitaDue, numAttivitaTre);
            mappa.put(attivita.plurale, mappaSingola);
        }
    }

}
