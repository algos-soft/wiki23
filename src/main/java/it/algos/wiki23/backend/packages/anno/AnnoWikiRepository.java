package it.algos.wiki23.backend.packages.anno;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaad23.backend.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 08-Jul-2022
 * Time: 06:34
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier("AnnoWiki")
public interface AnnoWikiRepository extends MongoRepository<AnnoWiki, String> {

    @Override
    List<AnnoWiki> findAll();
    List<AnnoWiki> findAllByOrderByOrdineDesc();
    List<AnnoWiki> findAllByOrderByOrdineAsc();

    <AnnoWiki extends AEntity> AnnoWiki insert(AnnoWiki entity);

    <AnnoWiki extends AEntity> AnnoWiki save(AnnoWiki entity);

    @Override
    void delete(AnnoWiki entity);

    AnnoWiki findFirstByNome(String nome);
    long countAnnoWikiByNatiOkFalse();
    long countAnnoWikiByMortiOkFalse();
    List<AnnoWiki> findAllByNatiOkFalseOrMortiOkFalse();

}// end of crud repository class