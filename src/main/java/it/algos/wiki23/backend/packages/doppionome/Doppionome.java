package it.algos.wiki23.backend.packages.doppionome;

import it.algos.vaad24.backend.annotation.*;
import it.algos.vaad24.backend.entity.*;
import it.algos.vaad24.backend.enumeration.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.stereotype.*;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: mar, 26-apr-2022
 * Time: 19:34
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 */
@Component
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass()
public class Doppionome extends AEntity {

    private static final transient int WIDTHEM = 20;

    @NotBlank()
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    @AIField(type = AETypeField.text, widthEM = WIDTHEM, search = true)
    public String nome;


    @Override
    public String toString() {
        return nome;
    }

}// end of crud entity class