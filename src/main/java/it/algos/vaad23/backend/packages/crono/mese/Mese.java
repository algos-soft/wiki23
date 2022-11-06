package it.algos.vaad23.backend.packages.crono.mese;

import com.vaadin.flow.component.icon.*;
import it.algos.vaad23.backend.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import lombok.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 01-mag-2022
 * Time: 08:51
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 */
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@EqualsAndHashCode(callSuper = false)
public class Mese extends AEntity {

    @AIField(type = AETypeField.integer, widthEM = 6)
    public int ordine;

    @AIField(type = AETypeField.text)
    public String breve;

    @AIField(type = AETypeField.text)
    public String nome;

    @AIField(type = AETypeField.integer, widthEM = 6)
    public int giorni;

    @AIField(type = AETypeField.integer, widthEM = 6, headerIcon = VaadinIcon.STEP_BACKWARD, caption = "Primo giorno (annuo) del mese")
    public int primo;

    @AIField(type = AETypeField.integer, widthEM = 6, headerIcon = VaadinIcon.STEP_FORWARD, caption = "Ultimo giorno (annuo) del mese")
    public int ultimo;


    @Override
    public String toString() {
        return nome;
    }

}// end of crud entity class