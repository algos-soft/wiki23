package it.algos.wiki23.backend.packages.anno;

import static it.algos.vaad24.backend.boot.VaadCost.*;
import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad24.backend.annotation.*;
import it.algos.vaad24.backend.entity.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.packages.crono.anno.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import javax.validation.constraints.*;
import javax.validation.constraints.Size;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.stereotype.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 08-Jul-2022
 * Time: 06:34
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 */
@Component
@Document
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "annoWikiBuilder")
@EqualsAndHashCode(callSuper = false)
public class AnnoWiki extends AEntity {

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, header = "#", widthEM = 6, caption = "Ordine a partire dal 1.000 a.C.")
    public int ordine;

    @AIField(type = AETypeField.text, widthEM = 7, caption = "Nome corrente")
    public String nome;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, header = "nati", caption = "Numero di biografie che utilizzano i nati in questo anno", widthEM = 6)
    public int bioNati;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, header = "morti", caption = "Numero di biografie che utilizzano i morti in questo anno", widthEM = 6)
    public int bioMorti;

    @AIField(type = AETypeField.text, widthEM = 10, header = "nati", caption = "Anno di nascita")
    public String pageNati;

    @AIField(type = AETypeField.text, widthEM = 10, header = "morti", caption = "Anno di morte")
    public String pageMorti;

    @AIField(type = AETypeField.booleano)
    public boolean esistePaginaNati;

    @AIField(type = AETypeField.booleano)
    public boolean esistePaginaMorti;

    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolCol.thumb, header = "n")
    public boolean natiOk;

    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolCol.thumb, header = "m")
    public boolean mortiOk;

    @AIField(type = AETypeField.integer)
    public int secolo;

    @Override
    public String toString() {
        return VUOTA;
    }

}// end of crud entity class