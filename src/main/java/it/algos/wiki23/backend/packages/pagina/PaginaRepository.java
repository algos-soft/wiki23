package it.algos.wiki23.backend.packages.pagina;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaad23.backend.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
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
 * Date: Wed, 21-Sep-2022
 * Time: 17:39
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier("Pagina")
public interface PaginaRepository extends MongoRepository<Pagina, String> {

    @Override
    List<Pagina> findAll();

    <Pagina extends AEntity> Pagina insert(Pagina entity);

    <Pagina extends AEntity> Pagina save(Pagina entity);

    @Override
    void delete(Pagina entity);

    Pagina findFirstByPagina(String pagina);

}// end of crud repository class