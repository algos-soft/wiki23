package it.algos.wiki23.backend.packages.professione;

import com.vaadin.flow.component.icon.*;
import it.algos.vaad24.backend.annotation.*;
import it.algos.vaad24.backend.entity.*;
import it.algos.vaad24.backend.enumeration.*;
import lombok.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: mar, 26-apr-2022
 * Time: 07:19
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 */
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@EqualsAndHashCode(callSuper = false)
public class Professione extends AEntity {

    private static final transient int WIDTHEM = 20;

    @AIField(type = AETypeField.text, widthEM = WIDTHEM, search = true)
    public String attivita;

    @AIField(type = AETypeField.text, widthEM = WIDTHEM)
    public String pagina;

    @AIField(type = AETypeField.booleano, headerIcon = VaadinIcon.ADD_DOCK, caption = "aggiunta (ex-attività)", usaCheckBox3Vie = true)
    public boolean aggiunta;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return attivita;
    }

}// end of crud entity class