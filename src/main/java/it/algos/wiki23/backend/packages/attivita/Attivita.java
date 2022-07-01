package it.algos.wiki23.backend.packages.attivita;

import com.vaadin.flow.component.icon.*;
import it.algos.vaad23.backend.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 18-apr-2022
 * Time: 21:25
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 */
@Document
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@EqualsAndHashCode(callSuper = false)
public class Attivita extends AEntity {

    private static final transient int WIDTHEM = 20;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = WIDTHEM, search = true)
    public String singolare;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = WIDTHEM, caption = "Attività plurale")
    public String plurale;

    @AIField(type = AETypeField.booleano, headerIcon = VaadinIcon.ADD_DOCK, caption = "aggiunta (ex-attività)", usaCheckBox3Vie = true)
    public boolean aggiunta;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, header = "bio", caption = "Numero di biografie che utilizzano queste attività singolari",
            widthEM = 6)
    public int numBio;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.integer, header = "sin")
    public int numSingolari;

    @AIField(type = AETypeField.booleano, headerIcon = VaadinIcon.PENCIL)
    public boolean superaSoglia;

    @AIField(type = AETypeField.booleano, headerIcon = VaadinIcon.SEARCH)
    public boolean esistePagina;

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return singolare;
    }


}// end of crud entity class