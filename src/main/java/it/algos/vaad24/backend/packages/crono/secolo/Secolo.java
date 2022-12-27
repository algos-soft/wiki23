package it.algos.vaad24.backend.packages.crono.secolo;

import it.algos.vaad24.backend.annotation.*;
import it.algos.vaad24.backend.entity.*;
import it.algos.vaad24.backend.enumeration.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.stereotype.*;

import javax.persistence.*;


/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 01-mag-2022
 * Time: 21:24
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 */
@Component
@Document
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass()
public class Secolo extends AEntity {

    @AIField(type = AETypeField.integer, header = "#", widthEM = 4, caption = "Ordinamento a partire dal XX secolo a.C.")
    public int ordine;

    @AIField(type = AETypeField.text, caption = "Nome corrente")
    public String nome;

    @AIField(type = AETypeField.integer, widthEM = 6, caption = "Primo anno del secolo")
    public int inizio;

    @AIField(type = AETypeField.integer, widthEM = 6, caption = "Ultimo anno del secolo")
    public int fine;

    @AIField(type = AETypeField.booleano, header = "a.C.")
    public boolean anteCristo;

    @Override
    public String toString() {
        return nome;
    }

}// end of crud entity class