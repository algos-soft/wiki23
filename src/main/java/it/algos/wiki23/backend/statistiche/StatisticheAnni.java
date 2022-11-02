package it.algos.wiki23.backend.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.anno.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.data.domain.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 01-Aug-2022
 * Time: 13:32
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatisticheAnni extends Statistiche {

    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public StatisticheAnni() {
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


    @Override
    protected String incipit() {
        StringBuffer buffer = new StringBuffer();
        String message;

        buffer.append(wikiUtility.setParagrafo("Anni"));
        buffer.append("Statistiche dei nati e morti per ogni anno");
        message = String.format("Potenzialmente dal [[1000 a.C.]] al [[{{CURRENTYEAR}}]], anche se non tutti gli anni hanno una propria pagina di nati o morti");
        buffer.append(textService.setRef(message));
        buffer.append(PUNTO + SPAZIO);
        buffer.append("Vengono prese in considerazione '''solo''' le voci biografiche che hanno valori '''validi e certi''' degli anni di nascita e morte della persona.");

        return buffer.toString();
    }

    /**
     * Elabora i dati
     */
    protected void elabora() {
        annoWikiBackend.elabora();
    }

    /**
     * Recupera la lista
     */
    @Override
    protected void creaLista() {
        lista = annoWikiBackend.findAll(Sort.by(Sort.Direction.DESC, "ordine"));
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

        for (AnnoWiki anno : (List<AnnoWiki>) lista) {
            nati = anno.bioNati;
            morti = anno.bioMorti;
            mappaSingola = new MappaStatistiche(++pos, anno.nome, nati, morti);
            mappa.put(anno.nome, mappaSingola);
            totNati += nati;
            totMorti += morti;
        }
    }


    @Override
    protected String colonne() {
        StringBuffer buffer = new StringBuffer();
        String color = "! style=\"background-color:#CCC;\" |";
        String message;

        buffer.append(color);
        buffer.append("#");
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("Anno");
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("Nati");
        message = "Il [[template:Bio|template Bio]] della voce biografica deve avere un valore valido al parametro '''annoNascita'''";
        buffer.append(textService.setRef(message));
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("Morti");
        message = "Il [[template:Bio|template Bio]] della voce biografica deve avere un valore valido al parametro '''annoMorte'''";
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
        nato = wikiUtility.wikiTitleNatiAnno(mappa.getNome()) + PIPE + mappa.getNati();
        buffer.append(textService.setDoppieQuadre(nato));

        buffer.append(doppioTag);
        morto = wikiUtility.wikiTitleMortiAnno(mappa.getNome()) + PIPE + mappa.getMorti();
        buffer.append(textService.setDoppieQuadre(morto));

        buffer.append(CAPO);

        return buffer.toString();
    }


    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload() {
        super.esegue();
        return super.upload(PATH_ANNI);
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult uploadTest() {
        super.esegue();
        return super.upload(UPLOAD_TITLE_DEBUG + ANNI);
    }

}
