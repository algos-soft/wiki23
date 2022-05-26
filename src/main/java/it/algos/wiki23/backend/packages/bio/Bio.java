package it.algos.wiki23.backend.packages.bio;

import it.algos.vaad23.backend.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: gio, 28-apr-2022
 * Time: 11:57
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 */
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@EqualsAndHashCode(callSuper = false)
public class Bio extends AEntity {

    @AIField(type = AETypeField.lungo, enabled = false, widthEM = 7)
    public long pageId;

    @NotBlank()
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, flexGrow = true)
    public String wikiTitle;

    @Lob
    @AIField(type = AETypeField.textArea, required = true, widthEM = 48)
    public String tmplBio;


    @AIField(type = AETypeField.localDateTime)
    public LocalDateTime lastServer;

    @AIField(type = AETypeField.localDateTime)
    public LocalDateTime lastMongo;

    /**
     * valido se lastLettura >= lastModifica
     */
    @AIField(type = AETypeField.booleano)
    public boolean valido;

    /**
     * elaborato se sono riempiti i campi derivati dal tmplBio
     */
    @AIField(type = AETypeField.booleano, header = "fix")
    public boolean elaborato;

    @AIField(type = AETypeField.text)
    public String nome;

    @AIField(type = AETypeField.text)
    public String cognome;

    @AIField(type = AETypeField.text, header = "XY", widthEM = 3)
    public String sesso;

    @AIField(type = AETypeField.text, header = "giorno", widthEM = 8)
    public String giornoNato;

    @AIField(type = AETypeField.text, header = "anno", widthEM = 6)
    public String annoNato;

    @AIField(type = AETypeField.text)
    public String luogoNato;

    @AIField(type = AETypeField.text)
    public String luogoNatoLink;

    @AIField(type = AETypeField.text, header = "giorno", widthEM = 8)
    public String giornoMorto;

    @AIField(type = AETypeField.text, header = "anno", widthEM = 7)
    public String annoMorto;

    @AIField(type = AETypeField.text)
    public String luogoMorto;

    @AIField(type = AETypeField.text)
    public String luogoMortoLink;

    @AIField(type = AETypeField.text, widthEM = 12)
    public String attivita;

    @AIField(type = AETypeField.text, widthEM = 8)
    public String attivita2;

    @AIField(type = AETypeField.text, widthEM = 8)
    public String attivita3;

    @AIField(type = AETypeField.text, widthEM = 12)
    public String nazionalita;


    @Override
    public String toString() {
        return wikiTitle;
    }

}// end of crud entity class