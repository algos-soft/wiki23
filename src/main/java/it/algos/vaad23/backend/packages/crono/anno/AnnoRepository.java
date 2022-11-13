package it.algos.vaad23.backend.packages.crono.anno;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 02-mag-2022
 * Time: 16:05
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier(TAG_ANNO)
public interface AnnoRepository extends MongoRepository<Anno, String> {

    @Override
    List<Anno> findAll();

    <Anno extends AEntity> Anno insert(Anno entity);

    <Anno extends AEntity> Anno save(Anno entity);

    @Override
    void delete(Anno entity);

    Anno findFirstByNome(String nome);
    Anno findFirstByOrdine(int ordine);

}// end of crud repository class