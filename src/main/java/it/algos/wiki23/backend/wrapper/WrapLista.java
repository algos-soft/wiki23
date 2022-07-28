package it.algos.wiki23.backend.wrapper;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 27-Jul-2022
 * Time: 10:42
 * <p>
 * Semplice wrapper per gestire i dati necessari ad una lista <br>
 */
public class WrapLista {

    public String titoloParagrafo;

    public String titoloRiga;

    public String didascaliaBreve;

    public String didascaliaLunga;

    public WrapLista(String titoloParagrafo, String titoloRiga, String didascaliaBreve) {
        this.titoloParagrafo = titoloParagrafo;
        this.titoloRiga = titoloRiga;
        this.didascaliaBreve = didascaliaBreve;
        this.didascaliaLunga = titoloRiga != null && titoloRiga.length() > 0 ? titoloRiga + SEP + didascaliaBreve : didascaliaBreve;
    }

}
