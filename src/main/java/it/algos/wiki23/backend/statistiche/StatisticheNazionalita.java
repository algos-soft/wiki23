package it.algos.wiki23.backend.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 06-Jul-2022
 * Time: 20:26
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatisticheNazionalita extends Statistiche {

    private int nazionalitaUsate;

    private int nazionalitaNonUsate;

    private int nazionalitaTotali;


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(StatisticheNazionalita.class) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public StatisticheNazionalita() {
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


    protected String body() {
        StringBuffer buffer = new StringBuffer();
        this.fixConteggi();

        buffer.append(usate());
        buffer.append(nonUsate());

        return buffer.toString();
    }

    protected void fixConteggi() {
        int usate = 0;
        int nonUsate = 0;
        MappaStatistiche singolaMappa;

        if (mappa != null) {
            for (String key : mappa.keySet()) {
                singolaMappa = mappa.get(key);
                if (singolaMappa.getNumNazionalita() > 0) {
                    usate++;
                }
                else {
                    nonUsate++;
                }
            }
            nazionalitaUsate = usate;
            nazionalitaNonUsate = nonUsate;
            nazionalitaTotali = usate + nonUsate;
        }
    }

    /**
     * Prima tabella <br>
     */
    protected String usate() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(wikiUtility.setParagrafo("Nazionalità usate"));
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

        buffer.append(String.format("'''%s''' nazionalità", textService.format(nazionalitaUsate)));
        buffer.append(textService.setRef(NAZIONALITA_CONVENZIONALI));
        buffer.append(" '''effettivamente utilizzate''' nelle");
        buffer.append(String.format(" '''%s'''", textService.format(bioBackend.count())));
        buffer.append(textService.setRef(LISTA_DIFFERENZA));
        buffer.append(" voci biografiche che usano il [[template:Bio|template Bio]] ed il parametro '''''nazionalità'''''.");
        buffer.append(textService.setRef(LISTA_NAZIONALITA));
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

        message = String.format("La pagina di una singola %s viene creata se le relative voci biografiche superano le %s unità.", "nazionalità", soglia);
        buffer.append(textService.setRef(message));

        message = String.format("La lista è suddivisa in paragrafi per ogni '''[[Modulo:Bio/Plurale nazionalità|attività]]''' individuata. Se il numero" +
                " di voci biografiche nel paragrafo supera le %s unità, viene creata una sottopagina.", sub);
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);

        buffer.append(color);
        buffer.append(textService.setBold("categoria"));
        message = "Le categorie possono avere sottocategorie e suddivisioni diversamente articolate e possono avere anche voci che hanno implementato la categoria stessa al di fuori del [[template:Bio|template Bio]].";
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);

        buffer.append(color);
        buffer.append(textService.setBold("voci"));
        buffer.append(CAPO);

        return buffer.toString();
    }


    protected String corpoUsate() {
        StringBuffer buffer = new StringBuffer();
        int cont = 1;
        int k = 1;
        String riga;
        MappaStatistiche mappaSingola;
        String message;
        int soglia = WPref.sogliaAttNazWiki.getInt();
        boolean linkLista = WPref.usaLinkStatistiche.is();

        for (String key : mappa.keySet()) {
            mappaSingola = mappa.get(key);
            if (mappaSingola.isUsata()) {
                riga = rigaUsate(key, mappaSingola, cont, soglia, linkLista);
                if (textService.isValid(riga)) {
                    buffer.append(riga);
                    k = k + 1;
                    cont = k;
                }
            }
        }

        cont--;
        if (cont != nazionalitaUsate) {
            message = String.format("Le nazionalità usate non corrispondono: previste=%s trovate=%s", nazionalitaUsate, cont);
            logger.error(new WrapLog().exception(new AlgosException(message)));
        }

        return buffer.toString();
    }

    protected String rigaUsate(String plurale, MappaStatistiche mappaSingola, int cont, int soglia, boolean linkLista) {
        StringBuffer buffer = new StringBuffer();
        String tagDex = "style=\"text-align: right;\" |";
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
        buffer.append(fixNomeUsate(plurale, soglia, linkLista));

        buffer.append(doppioTag);
        buffer.append(categoria);
        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(mappaSingola.getNumNazionalita());
        buffer.append(CAPO);

        return buffer.toString();
    }


    protected String fixNomeUsate(String nomeNazionalitaPlurale, int soglia, boolean linkLista) {
        String nomeVisibile = nomeNazionalitaPlurale;
        boolean superaSoglia;
        int numVociBio;
        String listaTag = "Progetto:Biografie/Nazionalità/";

        numVociBio = bioBackend.countNazionalitaPlurale(nomeNazionalitaPlurale);
        superaSoglia = numVociBio >= soglia;
        if (superaSoglia && linkLista) {
            nomeVisibile = listaTag + textService.primaMaiuscola(nomeNazionalitaPlurale) + PIPE + nomeNazionalitaPlurale;
            nomeVisibile = textService.setDoppieQuadre(nomeVisibile);
        }

        return nomeVisibile;
    }


    /**
     * Terza tabella <br>
     */
    protected String nonUsate() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(wikiUtility.setParagrafo("Nazionalità non usate"));
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
        buffer.append(String.format("'''%s''' nazionalità", textService.format(nazionalitaNonUsate)));
        buffer.append(textService.setRef(NAZIONALITA_CONVENZIONALI));
        buffer.append(" presenti nell' [[Modulo:Bio/Plurale nazionalità|'''elenco del progetto Biografie''']] ma '''non utilizzate''' nel parametro ''nazionalità''");
        message = "Si tratta di nazionalità '''originariamente''' discusse ed [[Modulo:Bio/Plurale nazionalità|inserite nell'elenco]] che non sono '''mai''' state utilizzate o che sono state in un secondo tempo sostituite da altre denominazioni";
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
        buffer.append(textService.setBold("nazionalità"));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(textService.setBold("categoria"));
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
            if (singolaMappa.getNumNazionalita() < 1) {
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
        buffer.append(plurale);

        buffer.append(doppioTag);
        buffer.append(tagSin);
        buffer.append(categoria);
        buffer.append(CAPO);

        return buffer.toString();
    }


    /**
     * Elabora i dati
     */
    protected void elabora() {
        nazionalitaBackend.elabora();
    }

    /**
     * Recupera la lista
     */
    @Override
    protected void creaLista() {
        lista = nazionalitaBackend.findNazionalitaDistinctByPlurali();
    }

    /**
     * Costruisce la mappa <br>
     */
    @Override
    protected void creaMappa() {
        super.creaMappa();
        MappaStatistiche mappaSingola;
        List<String> singolari;
        int numNazionalita;

        for (Nazionalita nazionalita : (List<Nazionalita>) lista) {
            singolari = nazionalitaBackend.findSingolariByPlurale(nazionalita.pluraleLista);
            numNazionalita = 0;

            for (String singolare : singolari) {
                numNazionalita += bioBackend.countNazionalita(singolare);
            }

            mappaSingola = new MappaStatistiche(nazionalita.pluraleLista, numNazionalita);
            mappa.put(nazionalita.pluraleLista, mappaSingola);
        }
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload() {
        super.esegue();
        return super.upload(PATH_NAZIONALITA);
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult uploadTest() {
        super.esegue();
        return super.upload(UPLOAD_TITLE_DEBUG + NAZ);
    }

}
