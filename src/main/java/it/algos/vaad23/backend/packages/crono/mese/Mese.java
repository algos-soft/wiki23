package it.algos.vaad23.backend.packages.crono.mese;

import it.algos.vaad23.backend.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import lombok.*;

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
    public int giorni;

    @AIField(type = AETypeField.text)
    public String breve;

    @AIField(type = AETypeField.text)
    public String nome;

    @Override
    public String toString() {
        return nome;
    }

}// end of crud entity class