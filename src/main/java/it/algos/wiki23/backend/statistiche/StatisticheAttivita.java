package it.algos.wiki23.backend.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
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
        super.typeToc = AETypeToc.noToc;
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload() {
        return super.upload(PATH_ATTIVITA);
    }

    protected String body() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(usate());
        //        buffer.append(nonUsate());

        return buffer.toString();
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

        message = String.format("Ogni persona è presente in una sola [[Progetto:Biografie/Attività|lista]], in base a quanto riportato nel" +
                " (primo) parametro ''attività'' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona");
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

        return buffer.toString();
    }

    protected String corpoUsate() {
        StringBuffer buffer = new StringBuffer();
        int cont = 1;
        int k = 1;
        String riga;

        for (String key : mappa.keySet()) {
            riga = rigaUsate(key, cont);
            if (textService.isValid(riga)) {
                buffer.append(riga);
                k = k + 1;
                cont = k;
            }
        }

        return buffer.toString();
    }


    protected String rigaUsate(String plurale, int cont) {
        StringBuffer buffer = new StringBuffer();
        String tagSin = "style=\"text-align: left;\" |";
        String nome = plurale.toLowerCase();
        String listaTag = "[[Progetto:Biografie/Attività/";
        String categoriaTag = "[[:Categoria:";
        String iniTag = "|-";
        String doppioTag = " || ";
        String pipe = "|";
        String endTag = "]]";
        String lista;
        String categoria;
        MappaStatistiche mappaSingola;
        int soglia = WPref.sogliaAttNazWiki.getInt();

        lista = listaTag + textService.primaMaiuscola(nome) + pipe + nome + endTag;
        categoria = categoriaTag + nome + pipe + nome + endTag;

        mappaSingola = mappa.get(plurale);
        if (mappaSingola == null) {
            return VUOTA;
        }

        if (mappaSingola.getNumAttivitaTotali() > 0) {
            buffer.append(iniTag);
            buffer.append(CAPO);
            buffer.append(pipe);

            buffer.append(cont);

            buffer.append(doppioTag);
            buffer.append(tagSin);
            buffer.append(fixNome(plurale, soglia));

            buffer.append(doppioTag);
            buffer.append(tagSin);
            buffer.append(categoria);

            buffer.append(doppioTag);
            buffer.append(mappaSingola.getNumAttivitaUno());

            buffer.append(doppioTag);
            buffer.append(mappaSingola.getNumAttivitaDue());

            buffer.append(doppioTag);
            buffer.append(mappaSingola.getNumAttivitaTre());

            buffer.append(doppioTag);
            buffer.append(mappaSingola.getNumAttivitaTotali());

            buffer.append(CAPO);
        }

        return buffer.toString();
    }

    protected String fixNome(String nomeAttivitaPlurale, int soglia) {
        String nomeVisibile = nomeAttivitaPlurale;
        boolean superaSoglia;
        int numVociBio;
        String listaTag = "Progetto:Biografie/Attività/";

        numVociBio = bioBackend.countAttivitaPlurale(nomeAttivitaPlurale);
        superaSoglia = numVociBio > soglia;
        if (superaSoglia) {
            nomeVisibile = listaTag + textService.primaMaiuscola(nomeAttivitaPlurale) + PIPE + nomeAttivitaPlurale;
            nomeVisibile = textService.setDoppieQuadre(nomeVisibile);
        }

        return nomeVisibile;
    }

    protected String incipitUsate() {
        StringBuffer buffer = new StringBuffer();
        String message;
        int vociBio = bioBackend.count();
        int attivita = lista.size();
        buffer.append(String.format("'''%s''' attività", textService.format(attivita)));
        message = "Le attività sono quelle [[Discussioni progetto:Biografie/Attività|'''convenzionalmente''' previste]] " +
                "dalla comunità ed [[Modulo:Bio/Plurale attività|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";
        buffer.append(textService.setRef(message));
        buffer.append(" '''effettivamente utilizzate''' nelle");
        buffer.append(String.format(" '''%s'''", textService.format(vociBio)));
        message = "La '''differenza''' tra le voci della categoria e quelle utilizzate è dovuta allo specifico utilizzo del [[template:Bio|template Bio]] ed in particolare all'uso del parametro Categorie=NO";
        buffer.append(textService.setRef(message));
        buffer.append(" voci biografiche che usano il [[template:Bio|template Bio]] ed i parametri '''''attività, attività2, " +
                "attività3'''''.");
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
        int numAttivitaTotali;

        for (Attivita attivita : (List<Attivita>) lista) {
            singolari = attivitaBackend.findSingolariByPlurale(attivita.plurale);
            numAttivitaUno = 0;
            numAttivitaDue = 0;
            numAttivitaTre = 0;
            numAttivitaTotali = 0;

            for (String singolare : singolari) {
                numAttivitaUno += bioBackend.countAttivita(singolare);
                numAttivitaDue += bioBackend.countAttivitaDue(singolare);
                numAttivitaTre += bioBackend.countAttivitaTre(singolare);
                numAttivitaTotali += bioBackend.countAttivitaAll(singolare);
            }

            mappaSingola = new MappaStatistiche(attivita.plurale, numAttivitaUno, numAttivitaDue, numAttivitaTre, numAttivitaTotali);
            mappa.put(attivita.plurale, mappaSingola);
        }
    }

}
