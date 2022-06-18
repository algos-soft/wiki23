package it.algos.vaad23.backend.interfaces;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 19-mar-2018
 * Time: 21:10
 * <p>
 * Interfaccia coi metodi inizia() e reset() <br>
 * Viene implementata 'una' delle sottoclassi concrete 'singleton', <br>
 * tramite @Qualifier(QUALIFIER_DATA_VAAD) selezionato in VaadBoot.setVersInstance(), oppure <br>
 * tramite @Qualifier(QUALIFIER_DATA_xxx) selezionato in xxxBoot.setVersInstance() <br>
 */
public interface AIData {

    void inizia();
    void resetData();

}
