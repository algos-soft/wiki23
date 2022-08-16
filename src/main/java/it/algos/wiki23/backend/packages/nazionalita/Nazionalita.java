package it.algos.wiki23.backend.packages.nazionalita;

import com.vaadin.flow.component.icon.*;
import it.algos.vaad23.backend.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: lun, 25-apr-2022
 * Time: 18:21
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 */
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@EqualsAndHashCode(callSuper = false)
public class Nazionalita extends AEntity {

    private static final transient int WIDTHEM = 20;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = WIDTHEM, search = true)
    public String singolare;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = WIDTHEM)
    public String pluraleParagrafo;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = WIDTHEM)
    public String pluraleLista;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = WIDTHEM)
    public String linkPaginaNazione;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, header = "bio", caption = "Numero di biografie che utilizzano la stessa nazionalità plurale", widthEM = 6)
    public int numBio;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, header = "sin")
    public int numSingolari;

    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolCol.thumb, headerIcon = VaadinIcon.LINES)
    public boolean superaSoglia;

    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolCol.yesNoReverse, headerIcon = VaadinIcon.HAMMER)
    public boolean esistePaginaLista;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return singolare;
    }

}// end of crud entity class