package it.algos.wiki23.backend.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import static it.algos.wiki23.backend.upload.Upload.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sat, 20-Aug-2022
 * Time: 20:41
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatisticheBio extends Statistiche {


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     */
    public StatisticheBio() {
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

//        buffer.append(wikiUtility.setParagrafo("Anni"));
//        buffer.append("Statistiche dei nati e morti per ogni anno");
//        message = String.format("Potenzialmente dal [[1000 a.C.]] al [[{{CURRENTYEAR}}]], anche se non tutti gli anni hanno una propria pagina di nati o morti");
//        buffer.append(textService.setRef(message));
//        buffer.append(PUNTO + SPAZIO);
//        buffer.append("Vengono prese in considerazione '''solo''' le voci biografiche che hanno valori '''validi e certi''' degli anni di nascita e morte della persona.");

        return buffer.toString();
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
        buffer.append("Statistiche");
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("27 marzo");
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("13 luglio");
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String corpo() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Pippoz");
        return buffer.toString();
    }

    protected String riga(MappaStatistiche mappa) {
        return VUOTA;
    }

    protected String correlate() {
        return VUOTA;
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult upload() {
        super.esegue();
        return super.upload(PATH_STATISTICHE);
    }

    /**
     * Esegue la scrittura della pagina <br>
     */
    public WResult uploadTest() {
        super.esegue();
        return super.upload(UPLOAD_TITLE_DEBUG + STATISTICHE);
    }

}
