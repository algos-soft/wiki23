package it.algos.wiki23.backend.packages.professione;

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
 * Date: mar, 26-apr-2022
 * Time: 07:19
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier(TAG_PROFESSIONE)
public interface ProfessioneRepository extends MongoRepository<Professione, String> {

    @Override
    List<Professione> findAll();

    <Professione extends AEntity> Professione insert(Professione entity);

    <Professione extends AEntity> Professione save(Professione entity);

    @Override
    void delete(Professione entity);

    Professione findFirstByAttivita(String attivita);

}// end of crud repository class