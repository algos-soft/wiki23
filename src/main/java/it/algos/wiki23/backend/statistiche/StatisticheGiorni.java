package it.algos.wiki23.backend.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.giorno.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.time.*;
import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 31-Jul-2022
 * Time: 11:23
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatisticheGiorni extends Statistiche {


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(StatisticheGiorni.class) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public StatisticheGiorni() {
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
        super.lastStatistica = WPref.statisticaGiorni;
    }

    @Override
    protected String incipit() {
        StringBuffer buffer = new StringBuffer();
        String message;

        buffer.append(wikiUtility.setParagrafo("Giorni"));
        buffer.append("Statistiche dei nati e morti per ogni giorno dell'anno");
        message = String.format("Previsto il [[29 febbraio]] per gli [[Anno bisestile|anni bisestili]]");
        buffer.append(textService.setRef(message));
        buffer.append(PUNTO + SPAZIO);
        buffer.append("Vengono prese in considerazione '''solo''' le voci biografiche che hanno valori '''validi e certi''' dei giorni di nascita e morte della persona.");

        return buffer.toString();
    }

    /**
     * Elabora i dati
     */
    protected void elabora() {
        giornoWikiBackend.elabora();
    }

    /**
     * Recupera la lista
     */
    @Override
    protected void creaLista() {
        lista = giornoWikiBackend.findAll();
    }

    /**
     * Costruisce la mappa <br>
     */
    @Override
    protected void creaMappa() {
        super.creaMappa();

        MappaStatistiche mappaSingola;
        int pos = 0;
        int nati;
        int morti;
        totNati = 0;
        totMorti = 0;
        String percNati;
        String percMorti;

        for (GiornoWiki giorno : (List<GiornoWiki>) lista) {
            nati = giorno.bioNati;
            morti = giorno.bioMorti;
            mappaSingola = new MappaStatistiche(++pos, giorno.nome, nati, morti);
            mappa.put(giorno.nome, mappaSingola);
            totNati += nati;
            totMorti += morti;
        }

        for (String key : mappa.keySet()) {
            mappaSingola = mappa.get(key);
            nati = mappaSingola.getNati();
            morti = mappaSingola.getMorti();
            percNati = mathService.percentualeDueDecimali(nati, totNati);
            percMorti = mathService.percentualeDueDecimali(morti, totMorti);
            mappaSingola.setPercNati(percNati);
            mappaSingola.setPercMorti(percMorti);
            mappa.put(key, mappaSingola);
        }
    }

    @Override
    protected String colonne() {
        StringBuffer buffer = new StringBuffer();
        String message;
        String color = "! style=\"background-color:#CCC;\" |";
        String natiTxt = textService.format(totNati);
        String mortiTxt = textService.format(totMorti);

        buffer.append(color);
        buffer.append("#");
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("Giorno");
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("Nati");
        message = "Il [[template:Bio|template Bio]] della voce biografica deve avere un valore valido al parametro '''giornoMeseNascita'''";
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("Morti");
        message = "Il [[template:Bio|template Bio]] della voce biografica deve avere un valore valido al parametro '''giornoMeseMorte'''";
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("% nati ");
        message = String.format("Percentuale dei nati in questo giorno, rispetto al totale (%s) dei nati nell'intero anno", natiTxt);
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("% morti ");
        message = String.format("Percentuale dei nati in questo giorno, rispetto al totale (%s) dei morti nell'intero anno", mortiTxt);
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);

        return buffer.toString();
    }


    protected String riga(MappaStatistiche mappa) {
        StringBuffer buffer = new StringBuffer();
        String iniTag = "|-";
        String doppioTag = " || ";
        String pipe = "|";
        String nato;
        String morto;

        buffer.append(iniTag);
        buffer.append(CAPO);
        buffer.append(pipe);
        buffer.append(mappa.getPos());

        buffer.append(doppioTag);
        buffer.append(textService.setDoppieQuadre(mappa.getNome()));

        buffer.append(doppioTag);
        nato = wikiUtility.wikiTitleNatiGiorno(mappa.getNome()) + PIPE + mappa.getNati();
        buffer.append(textService.setDoppieQuadre(nato));

        buffer.append(doppioTag);
        morto = wikiUtility.wikiTitleMortiGiorno(mappa.getNome()) + PIPE + mappa.getMorti();
        buffer.append(textService.setDoppieQuadre(morto));

        buffer.append(doppioTag);
        buffer.append(mappa.getPercNati());

        buffer.append(doppioTag);
        buffer.append(mappa.getPercMorti());

        buffer.append(CAPO);

        return buffer.toString();
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload() {
        super.esegue();
        return super.upload(PATH_GIORNI);
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult uploadTest() {
        super.esegue();
        return super.upload(UPLOAD_TITLE_DEBUG + GIORNI);
    }

}
