package it.algos.wiki23.backend.packages.statistica;

import static it.algos.vaad24.backend.boot.VaadCost.*;
import com.querydsl.core.annotations.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaad24.backend.annotation.*;
import it.algos.vaad24.backend.entity.*;
import it.algos.vaad24.backend.enumeration.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.validation.constraints.Size;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.stereotype.*;

import java.time.*;
import java.time.format.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 21-Aug-2022
 * Time: 14:07
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
public class StatisticaBio extends AEntity {

    private static final transient int WIDTHEM = 6;

    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    @AIField(type = AETypeField.integer, header = "#", widthEM = 4)
    public int ordine;

    @AIField(type = AETypeField.localDate)
    public LocalDate evento;

    @AIField(type = AETypeField.integer, header = "Bio", widthEM = WIDTHEM)
    public int bio;

    @AIField(type = AETypeField.integer, header = "Gio", widthEM = WIDTHEM)
    public int giorni;

    @AIField(type = AETypeField.integer, header = "Anni", widthEM = WIDTHEM)
    public int anni;

    @AIField(type = AETypeField.integer, header = "Att", widthEM = WIDTHEM)
    public int attivita;

    @AIField(type = AETypeField.integer, header = "Nat", widthEM = WIDTHEM)
    public int nazionalita;

    @AIField(type = AETypeField.integer, header = "Delta", widthEM = WIDTHEM)
    public int attesa;

    @Override
    public String toString() {
        return evento.format(DateTimeFormatter.ofPattern("d-MMM-yy"));
    }

}// end of crud entity class