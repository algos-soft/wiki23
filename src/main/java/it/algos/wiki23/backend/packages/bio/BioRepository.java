package it.algos.wiki23.backend.packages.bio;

import it.algos.vaad23.backend.entity.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
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

    long countBioByCognome(String cognome);

    long countBioByAttivita(String attivita);

    long countBioByAttivita2(String attivita);

    long countBioByAttivita3(String attivita);

    long countBioByNazionalita(String nazionalita);

    long countBioByAttivitaAndNazionalita(String attivita, String nazionalita);

    long countBioByNazionalitaAndAttivita(String nazionalita, String attivita);

    long countBioByGiornoNato(String giornoNato);

    long countBioByGiornoNatoOrd(int giornoNatoOrd);

    long countBioByGiornoMorto(String giornoMorto);

    long countBioByGiornoMortoOrd(int giornoNatoOrd);

    @Query(value = "{ 'nome' : ?0 }", fields = "{ 'nome' : 1, 'cognome' : 1 }")
    List<Bio> findByNomeIncludeNomeAndCognomeFields(String nome);

    List<Bio> findAllByAttivitaOrderByOrdinamento(String attivita);

    List<Bio> findAllByAttivita2OrderByOrdinamento(String attivita2);

    List<Bio> findAllByAttivita3OrderByOrdinamento(String attivita2);

    List<Bio> findAllByNazionalitaOrderByCognome(String nazionalita);

    List<Bio> findAllByNazionalitaOrderByOrdinamento(String nazionalita);

    List<Bio> findAllByAttivitaAndNazionalitaOrderByOrdinamento(String attivita, String nazionalita);

    List<Bio> findAllByNazionalitaAndAttivitaOrderByOrdinamento(String nazionalita, String attivita);

    List<Bio> findAllByAnnoNatoOrderByGiornoNatoOrdAscOrdinamentoAsc(String annoNato);

    List<Bio> findAllByAnnoMortoOrderByGiornoMortoOrdAscOrdinamentoAsc(String annoMorto);


    //Anni nati
    //Anni nati nel mese
    long countBioByAnnoNato(String annoNato);

    long countBioByAnnoNatoAndGiornoNatoOrdBetween(String annoNato, int primo, int ultimo);

    long countBioByAnnoNatoAndGiornoNatoOrd(String annoNato, int zero);

    List<Bio> findAllByAnnoNatoOrderByOrdinamento(String annoNato);

    List<Bio> findAllByAnnoNatoAndGiornoNatoOrdBetweenOrderByGiornoNatoOrd(String annoNato, int primo, int ultimo);

    List<Bio> findAllByAnnoNatoAndGiornoNatoOrdOrderByOrdinamento(String annoNato, int zero);
    //Anni nati


    //Anni morti
    //Anni morti nel mese
    long countBioByAnnoMorto(String annoMorto);

    long countBioByAnnoMortoAndGiornoMortoOrdBetween(String annoMorto, int primo, int ultimo);

    long countBioByAnnoMortoAndGiornoMortoOrd(String annoMorto, int zero);

    List<Bio> findAllByAnnoMortoOrderByOrdinamento(String annoMorto);

    List<Bio> findAllByAnnoMortoAndGiornoMortoOrdBetweenOrderByOrdinamento(String annoMorto, int primo, int ultimo);

    List<Bio> findAllByAnnoMortoAndGiornoMortoOrdOrderByOrdinamento(String annoMorto, int zero);
    //Anni morti


    List<Bio> findAllByGiornoNatoOrderByAnnoNatoOrdAscOrdinamentoAsc(String giornoNato);

    List<Bio> findAllByGiornoMortoOrderByAnnoMortoOrdAscOrdinamentoAsc(String giornoMorto);

    List<Bio> findAllByCognomeOrderByOrdinamento(String cognome);

    List<Bio> findAllByErrato(boolean errato);


    long countBioByErratoIsTrue();

    long countBioByOrdinamentoIsNull();

    long countBioBySessoIsNull();

    long countBioBySessoEquals(String sesso);

    long countBioByErroreIs(AETypeBioError error);

    List<Bio> findBySessoIsNull();

    List<Bio> findByOrdinamentoIsNull();

    List<Bio> findBySessoIsLike(String regex);

}// end of crud repository class