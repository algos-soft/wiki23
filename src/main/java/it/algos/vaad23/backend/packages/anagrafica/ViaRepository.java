package it.algos.vaad23.backend.packages.anagrafica;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import javax.persistence.*;
import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: Thu, 02-Jun-2022
 * Time: 08:02
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier(TAG_VIA)
public interface ViaRepository extends MongoRepository<Via, String> {

    @Override
    List<Via> findAll();

    <Via extends AEntity> Via insert(Via entity);

    <Via extends AEntity> Via save(Via entity);

    @Override
    void delete(Via entity);

}// end of crud repository class