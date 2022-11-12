package it.algos.vaad23.backend.packages.crono.mese;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import javax.validation.constraints.*;
import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: ven, 18-mar-2022
 * Time: 06:55
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier(TAG_MESE)
public interface MeseRepository extends MongoRepository<Mese, String> {


    @Override
    List<Mese> findAll();

    @Override
    Mese insert(Mese entity);

    @Override
    Mese save(@NotNull Mese entity);

    @Override
    void delete(Mese entity);

    Mese findFirstByNome(String nome);
    Mese findFirstByOrdine(int ordine);

}// end of crud repository class