package it.algos.wiki23.backend.packages.bio;

import com.querydsl.core.annotations.*;
import it.algos.vaad23.backend.annotation.*;
import it.algos.vaad23.backend.entity.*;
import it.algos.vaad23.backend.enumeration.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.*;
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
//@QueryEntity
@Document
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@EqualsAndHashCode(callSuper = false)
public class Bio extends AEntity {

    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    @AIField(type = AETypeField.lungo, enabled = false, widthEM = 7)
    public long pageId;


    @NotBlank()
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = 16)
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

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text)
    public String nome;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text)
    public String cognome;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, search = true, widthEM = 16)
    public String ordinamento;

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

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = 12)
    public String attivita;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = 8)
    public String attivita2;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = 8)
    public String attivita3;

    @Indexed(unique = false, direction = IndexDirection.DESCENDING)
    @AIField(type = AETypeField.text, widthEM = 12)
    public String nazionalita;


    @Override
    public String toString() {
        return wikiTitle;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}// end of crud entity class