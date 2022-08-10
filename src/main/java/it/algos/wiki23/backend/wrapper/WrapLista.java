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

    public String titoloParagrafoLink;

    public String titoloSottoParagrafo;

    public String didascaliaBreve;

    public String didascaliaLunga;

    public WrapLista(String titoloParagrafo, String titoloParagrafoLink, String titoloSottoParagrafo, String didascaliaBreve) {
        this(titoloParagrafo, titoloParagrafoLink, titoloSottoParagrafo, didascaliaBreve, titoloSottoParagrafo + SEP + didascaliaBreve);
    }

    public WrapLista(String titoloParagrafo, String titoloParagrafoLink, String titoloSottoParagrafo, String didascaliaBreve, String didascaliaLunga) {
        this.titoloParagrafo = titoloParagrafo;
        this.titoloParagrafoLink = titoloParagrafoLink;
        this.titoloSottoParagrafo = titoloSottoParagrafo;
        this.didascaliaBreve = didascaliaBreve;
        this.didascaliaLunga = didascaliaLunga;
    }

}