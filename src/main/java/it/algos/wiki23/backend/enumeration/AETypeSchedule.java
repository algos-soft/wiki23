package it.algos.wiki23.backend.enumeration;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sat, 31-Dec-2022
 * Time: 11:56
 */
public enum AETypeSchedule {

    schema1(
            AESchedule.zeroCinqueLunedi,
            AESchedule.zeroCinqueNoLunedi,
            AESchedule.dueNoLunedi,
            AESchedule.quattroNoLunedi,
            AESchedule.dieciMartedi,
            AESchedule.dieciMercoledi,
            AESchedule.dieciGiovedi,
            AESchedule.dieciVenerdi,
            AESchedule.dieciSabato,
            AESchedule.dieciDomenica
    ),
    schema2(
            AESchedule.zeroCinqueLunedi,
            AESchedule.zeroCinqueNoLunedi,
            AESchedule.dueNoLunedi,
            AESchedule.quattroNoLunedi,
            AESchedule.dieciMartedi,
            AESchedule.dieciMercoledi,
            AESchedule.dieciGiovedi,
            AESchedule.dieciVenerdi,
            AESchedule.dieciSabato,
            AESchedule.dieciDomenica
    );

    private AESchedule resetBio;

    private AESchedule updateBio;

    private AESchedule giorni;

    private AESchedule anni;

    private AESchedule attivita;

    private AESchedule nazionalita;

    private AESchedule cognomi;
    private AESchedule nomi;

    private AESchedule elabora;

    private AESchedule statistiche;


    AETypeSchedule(
            AESchedule resetBio,
            AESchedule updateBio,
            AESchedule giorni,
            AESchedule anni,
            AESchedule attivita,
            AESchedule nazionalita,
            AESchedule cognomi,
            AESchedule nomi,
            AESchedule elabora,
            AESchedule statistiche) {
        this.resetBio = resetBio;
        this.updateBio = updateBio;
        this.giorni = giorni;
        this.anni = anni;
        this.attivita = attivita;
        this.nazionalita = nazionalita;
        this.cognomi = cognomi;
        this.nomi = nomi;
        this.elabora = elabora;
        this.statistiche = statistiche;
    }

    public AESchedule getResetBio() {
        return resetBio;
    }

    public AESchedule getUpdateBio() {
        return updateBio;
    }

    public AESchedule getGiorni() {
        return giorni;
    }

    public AESchedule getAnni() {
        return anni;
    }

    public AESchedule getAttivita() {
        return attivita;
    }

    public AESchedule getNazionalita() {
        return nazionalita;
    }

    public AESchedule getCognomi() {
        return cognomi;
    }

    public AESchedule getNomi() {
        return nomi;
    }

    public AESchedule getElabora() {
        return elabora;
    }

    public AESchedule getStatistiche() {
        return statistiche;
    }
}
