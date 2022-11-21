package it.algos.vaad23.backend.packages.anagrafica;

import it.algos.vaad23.backend.entity.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: Thu, 02-Jun-2022
 * Time: 08:02
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
@MappedSuperclass
public class Via extends AEntity {


    @NotBlank()
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    public String nome;


    @Override
    public String toString() {
        return nome;
    }

}// end of crud entity class