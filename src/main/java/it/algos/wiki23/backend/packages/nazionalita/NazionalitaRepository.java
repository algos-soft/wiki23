package it.algos.wiki23.backend.packages.nazionalita;

import it.algos.vaad23.backend.entity.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: lun, 25-apr-2022
 * Time: 18:21
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier(TAG_NAZIONALITA)
public interface NazionalitaRepository extends MongoRepository<Nazionalita, String> {

    @Override
    List<Nazionalita> findAll();

    <Nazionalita extends AEntity> Nazionalita insert(Nazionalita entity);

    <Nazionalita extends AEntity> Nazionalita save(Nazionalita entity);

    @Override
    void delete(Nazionalita entity);

    Nazionalita findFirstBySingolare(String singolare);
    Nazionalita findFirstByPlurale(String plurale);
    List<Nazionalita> findAllByPluraleOrderBySingolareAsc(String plurale);

}// end of crud repository class