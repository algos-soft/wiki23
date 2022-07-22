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
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "giornoWikiBuilder")
@EqualsAndHashCode(callSuper = false)
public class GiornoWiki extends Giorno {

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, header = "bio", caption = "Numero di biografie che utilizzano questi giorni", widthEM = 6)
    public int numBio;

    @AIField(type = AETypeField.text, widthEM = 7, caption = "Giorno di nascita")
    public String nati;

    @AIField(type = AETypeField.text, widthEM = 7, caption = "Giorno di morte")
    public String morti;

    @AIField(type = AETypeField.booleano, headerIcon = VaadinIcon.PENCIL)
    public boolean superaSoglia;

    @AIField(type = AETypeField.booleano, headerIcon = VaadinIcon.SEARCH)
    public boolean esistePagina;

    @Override
    public String toString() {
        return VUOTA;
    }

}// end of crud entity class