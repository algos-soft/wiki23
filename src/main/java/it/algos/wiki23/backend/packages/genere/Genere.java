package it.algos.wiki23.backend.packages.genere;

import it.algos.vaad23.backend.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.wiki23.backend.enumeration.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: dom, 24-apr-2022
 * Time: 10:17
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
public class Genere extends AEntity {

    private static final transient int WIDTHEM = 20;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = WIDTHEM, search = true)
    public String singolare;


    @AIField(type = AETypeField.enumeration, enumClazz = AETypeGenere.class)
    public AETypeGenere type;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = WIDTHEM)
    public String pluraleMaschile;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = WIDTHEM)
    public String pluraleFemminile;


    @Override
    public String toString() {
        return singolare;
    }

}// end of crud entity class