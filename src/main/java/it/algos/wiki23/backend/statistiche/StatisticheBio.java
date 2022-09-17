package it.algos.wiki23.backend.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.statistica.*;
import static it.algos.wiki23.backend.upload.Upload.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.text.*;
import java.time.*;
import java.time.format.*;

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

    private StatisticaBio oldStatistica;

    private StatisticaBio newStatistica;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public StatisticaBioBackend statisticaBioBackend;

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

    private String tagSin = "style=\"text-align: left;\" |";

    private String tagDex = "style=\"text-align: right;\" |";

    private String iniTag = "|-";

    private String doppioTag = " || ";

    private String pipe = "|";

    /**
     * Elabora i dati
     */
    protected void elabora() {
        oldStatistica = getOldStatistica();
        newStatistica = creaNewStatistica();
        if (newStatistica == null) {
            newStatistica = oldStatistica;
        }
    }

    protected StatisticaBio getOldStatistica() {
        if (isEsistonoStatistiche()) {
            return getLastStatistica();
        }
        else {
            return creaNewStatistica();
        }
    }

    protected boolean isEsistonoStatistiche() {
        return mongoService.isExistsCollection(StatisticaBio.class);
    }

    protected StatisticaBio getLastStatistica() {
        return statisticaBioBackend.findLast();
    }

    protected StatisticaBio creaNewStatistica() {
        LocalDate evento = LocalDate.now();
        int bio = mongoService.count(Bio.class);

        int giorni = giornoWikiBackend.findAll().size();
        int anni = annoWikiBackend.findAll().size();
        int attivita = attivitaBackend.findAttivitaDistinctByPluraliOld().size();
        int nazionalita = nazionalitaBackend.findNazionalitaDistinctByPlurali().size();
        int attesa = 2;

        return statisticaBioBackend.creaIfNotExist(evento, bio, giorni, anni, attivita, nazionalita, attesa);
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

    protected String inizioTabella() {
        String testo = VUOTA;

        testo += CAPO;
        testo += "{|class=\"wikitable\" style=\"background-color:#EFEFEF; text-align: right;\"";
        testo += CAPO;

        return testo;
    }

    @Override
    protected String colonne() {
        StringBuffer buffer = new StringBuffer();
        String color = "! style=\"background-color:#CCC;\" |";
        String message;
        String eventoOld = oldStatistica.evento.format(DateTimeFormatter.ofPattern("d MMM yyy"));
        String eventoNew = newStatistica.evento.format(DateTimeFormatter.ofPattern("d MMM yyy"));

        buffer.append(color);
        buffer.append("Statistiche");
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(eventoOld);
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append(eventoNew);
        buffer.append(CAPO);
        buffer.append(color);
        buffer.append("Delta");
        buffer.append(CAPO);

        return buffer.toString();
    }

    protected String corpo() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(rigaCategoria());
        buffer.append(rigaGiorni());
        buffer.append(rigaAnni());
        buffer.append(rigaAttivita());
        buffer.append(rigaNazionalita());
        buffer.append(rigaAttesa());

        return buffer.toString();
    }

    protected String rigaCategoria() {
        StringBuffer buffer = new StringBuffer();
        String message;

        buffer.append(iniTag);
        buffer.append(CAPO);
        buffer.append(pipe);
        buffer.append(tagSin);
        buffer.append("'''[[:Categoria:BioBot|Template bio]]''' ");
        message = String.format("Una differenza di alcune decine di pagine tra la categoria e le voci gestite dal bot è '''fisiologica''' e dovuta a categorizzazioni diverse tra il software mediawiki ed il [[template:bio|template bio]]");
        buffer.append(textService.setRef(message));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(oldStatistica.bio));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(newStatistica.bio));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(newStatistica.bio - oldStatistica.bio);

        buffer.append(CAPO);

        return buffer.toString();
    }
    protected String rigaGiorni() {
        StringBuffer buffer = new StringBuffer();
        String message;

        buffer.append(iniTag);
        buffer.append(CAPO);
        buffer.append(pipe);
        buffer.append(tagSin);
        buffer.append("'''[[Progetto:Biografie/Giorni|Giorni interessati]]''' ");
        message = String.format("Previsto il [[29 febbraio]] per gli [[Anno bisestile|anni bisestili]]");
        buffer.append(textService.setRef(message));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(oldStatistica.giorni));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(newStatistica.giorni));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(newStatistica.giorni - oldStatistica.giorni);

        buffer.append(CAPO);

        return buffer.toString();
    }


    protected String rigaAnni() {
        StringBuffer buffer = new StringBuffer();
        String message;

        buffer.append(iniTag);
        buffer.append(CAPO);
        buffer.append(pipe);
        buffer.append(tagSin);
        buffer.append("'''[[Progetto:Biografie/Anni|Anni interessati]]''' ");
        message = String.format("Potenzialmente dal [[1000 a.C.]] al [[{{CURRENTYEAR}}]]");
        buffer.append(textService.setRef(message));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(oldStatistica.anni));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(newStatistica.anni));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(newStatistica.anni - oldStatistica.anni);

        buffer.append(CAPO);

        return buffer.toString();
    }



    protected String rigaAttivita() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(iniTag);
        buffer.append(CAPO);
        buffer.append(pipe);
        buffer.append(tagSin);
        buffer.append("'''[[Progetto:Biografie/Attività|Attività utilizzate]]''' ");
        buffer.append(textService.setRef(INFO_ATTIVITA_PREVISTE));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(oldStatistica.attivita));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(newStatistica.attivita));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(newStatistica.attivita - oldStatistica.attivita);

        buffer.append(CAPO);

        return buffer.toString();
    }


    protected String rigaNazionalita() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(iniTag);
        buffer.append(CAPO);
        buffer.append(pipe);
        buffer.append(tagSin);
        buffer.append("'''[[Progetto:Biografie/Nazionalità|Nazionalità utilizzate]]''' ");
        buffer.append(textService.setRef(INFO_NAZIONALITA_PREVISTE));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(oldStatistica.nazionalita));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(newStatistica.nazionalita));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(newStatistica.nazionalita - oldStatistica.nazionalita);

        buffer.append(CAPO);

        return buffer.toString();
    }



    protected String rigaAttesa() {
        StringBuffer buffer = new StringBuffer();
        String message;

        buffer.append(iniTag);
        buffer.append(CAPO);
        buffer.append(pipe);
        buffer.append(tagSin);
        buffer.append("'''[[:Categoria:BioBot|Template bio]]''' ");
        message = String.format("Giorni di attesa '''indicativi''' prima che ogni singola voce venga ricontrollata per registrare eventuali modifiche intervenute nei parametri significativi");
        buffer.append(textService.setRef(message));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(oldStatistica.attesa));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(textService.format(newStatistica.attesa));

        buffer.append(doppioTag);
        buffer.append(tagDex);
        buffer.append(newStatistica.attesa - oldStatistica.attesa);

        buffer.append(CAPO);

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
