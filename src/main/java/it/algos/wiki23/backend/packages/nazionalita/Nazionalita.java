package it.algos.wiki23.backend.packages.nazionalita;

import com.vaadin.flow.component.icon.*;
import it.algos.vaad23.backend.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import lombok.*;

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

    @AIField(type = AETypeField.text, widthEM = WIDTHEM, search = true)
    public String singolare;

    @AIField(type = AETypeField.text, widthEM = WIDTHEM)
    public String plurale;

    @AIField(type = AETypeField.integer, header = "bio", caption = "Numero di biografie che utilizzano queste nazionalità singolari")
    public int numBio;

    @AIField(type = AETypeField.integer, header = "sin")
    public int numSingolari;

    @AIField(type = AETypeField.booleano, headerIcon = VaadinIcon.LIST)
    public boolean pagina;

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return singolare;
    }

}// end of crud entity class