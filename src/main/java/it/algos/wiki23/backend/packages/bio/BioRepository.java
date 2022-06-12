package it.algos.wiki23.backend.packages.bio;

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
 * Date: gio, 28-apr-2022
 * Time: 11:57
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier(TAG_BIO)
public interface BioRepository extends MongoRepository<Bio, String> {

    @Override
    List<Bio> findAll();

    <Bio extends AEntity> Bio insert(Bio entity);

    <Bio extends AEntity> Bio save(Bio entity);

    @Override
    void delete(Bio entity);
    Bio findFirstByPageId(long pageID);
    Bio findFirstByWikiTitle(String wikiTitle);

    long countBioByAttivita(String attivita);
    long countBioByAttivita2(String attivita);
    long countBioByAttivita3(String attivita);
    long countBioByNazionalita(String nazionalita);

    @Query(value = "{ 'nome' : ?0 }", fields = "{ 'nome' : 1, 'cognome' : 1 }")
    List<Bio> findByNomeIncludeNomeAndCognomeFields(String nome);

    List<Bio> findAllByAttivitaOrderByCognome(String attivita);
    List<Bio> findAllByAttivita2OrderByCognome(String attivita2);
    List<Bio> findAllByAttivita3OrderByCognome(String attivita2);

//    @Query( fields = "{ 'pageId' : 1 }")
//    List<Bio> findIncludePageIdFields();

//    @Query(value = "{ 'status' : ?0 }", fields = "{ 'item' : 1, 'status' : 1 }")
//    List<Inventory> findByStatusIncludeItemAndStatusFields(String status);

}// end of crud repository class