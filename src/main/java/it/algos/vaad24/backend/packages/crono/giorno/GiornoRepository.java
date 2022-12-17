package it.algos.vaad24.backend.packages.crono.giorno;

import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 02-mag-2022
 * Time: 08:26
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier(TAG_GIORNO)
public interface GiornoRepository extends MongoRepository<Giorno, String> {

    @Override
    List<Giorno> findAll();

    <Giorno extends AEntity> Giorno insert(Giorno entity);

    <Giorno extends AEntity> Giorno save(Giorno entity);

    @Override
    void delete(Giorno entity);

    Giorno findFirstByNome(String nome);

    Giorno findFirstByOrdine(int ordine);

}// end of crud repository class