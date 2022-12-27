package it.algos.wiki23.backend.packages.pagina;

import static it.algos.vaad24.backend.boot.VaadCost.*;
import com.vaadin.flow.component.icon.*;
import it.algos.vaad24.backend.annotation.*;
import it.algos.vaad24.backend.entity.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.wiki23.backend.enumeration.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.stereotype.*;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 21-Sep-2022
 * Time: 17:39
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
public class Pagina extends AEntity {


    @NotBlank()
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = 50)
    public String pagina;

    @AIField(type = AETypeField.booleano, typeBool = AETypeBoolCol.thumbReverse, headerIcon = VaadinIcon.LINES)
    public boolean cancella;

    @AIField(type = AETypeField.integer)
    public int voci;

    @AIField(type = AETypeField.enumeration, enumClazz = AETypePaginaCancellare.class, widthEM = 16)
    public AETypePaginaCancellare type;


    @Override
    public String toString() {
        return VUOTA;
    }

}// end of crud entity class