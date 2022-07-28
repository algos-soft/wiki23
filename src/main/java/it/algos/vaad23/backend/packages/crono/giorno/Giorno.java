package it.algos.vaad23.backend.packages.crono.giorno;

import com.vaadin.flow.component.icon.*;
import it.algos.vaad23.backend.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.packages.crono.mese.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 02-mag-2022
 * Time: 08:26
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
public class Giorno extends AEntity {

    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    @AIField(type = AETypeField.integer, header = "#", widthEM = 3, caption = "Ordinamento da inizio anno")
    public int ordine;

    @AIField(type = AETypeField.text, caption = "Nome corrente", sortProperty = "ordine")
    public String nome;

    @DBRef
    @AIField(type = AETypeField.link, linkClazz = MeseBackend.class)
    public Mese mese;

    @AIField(type = AETypeField.integer, widthEM = 6, headerIcon = VaadinIcon.STEP_BACKWARD, caption = "Progressivo da inizio anno")
    public int trascorsi;

    @AIField(type = AETypeField.integer, widthEM = 6, headerIcon = VaadinIcon.STEP_FORWARD, caption = "Mancanti alla fine dell'anno")
    public int mancanti;

    @Override
    public String toString() {
        return nome;
    }

}// end of crud entity class