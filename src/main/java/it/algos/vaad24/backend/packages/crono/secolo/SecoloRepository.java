package it.algos.vaad24.backend.packages.crono.secolo;

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
 * Date: dom, 01-mag-2022
 * Time: 21:24
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier(TAG_SECOLO)
public interface SecoloRepository extends MongoRepository<Secolo, String> {

    @Override
    List<Secolo> findAll();

    <Secolo extends AEntity> Secolo insert(Secolo entity);

    <Secolo extends AEntity> Secolo save(Secolo entity);

    @Override
    void delete(Secolo entity);

    Secolo findFirstByNome(String nome);

    Secolo findFirstByOrdine(int ordine);

    Secolo findFirstByInizioGreaterThanEqualAndFineLessThanEqualAndAnteCristo(int inizio, int fine, boolean anteCristo);

    Secolo findFirstByInizioLessThanEqualAndFineGreaterThanEqualAndAnteCristo(int inizio, int fine, boolean anteCristo);

}// end of crud repository class