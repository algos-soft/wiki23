package it.algos.wiki23.backend.packages.giorno;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad23.backend.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;
import javax.validation.constraints.Size;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Thu, 14-Jul-2022
 * Time: 20:04
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 */
@Document
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "giornoWikiBuilder")
@EqualsAndHashCode(callSuper = false)
public class GiornoWiki extends AEntity {

    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    @AIField(type = AETypeField.integer, header = "#", widthEM = 3, caption = "Ordinamento da inizio anno")
    public int ordine;

    @AIField(type = AETypeField.text, caption = "Nome corrente", sortProperty = "ordine")
    public String nome;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, header = "nati", caption = "Numero di biografie che utilizzano i nati in questo giorno", widthEM = 6)
    public int bioNati;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, header = "morti", caption = "Numero di biografie che utilizzano i morti in questo giorno", widthEM = 6)
    public int bioMorti;

    @AIField(type = AETypeField.text, widthEM = 10, header = "nati", caption = "Anno di nascita")
    public String pageNati;

    @AIField(type = AETypeField.text, widthEM = 10, header = "morti", caption = "Anno di morte")
    public String pageMorti;

    @AIField(type = AETypeField.booleano, widthEM = 6)
    public boolean nati;

    @AIField(type = AETypeField.booleano, widthEM = 6)
    public boolean morti;

    @Override
    public String toString() {
        return VUOTA;
    }

}// end of crud entity class