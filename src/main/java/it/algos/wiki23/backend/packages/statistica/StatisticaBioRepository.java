package it.algos.wiki23.backend.packages.statistica;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaad24.backend.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 21-Aug-2022
 * Time: 14:07
 * <p>
 * Estende l'interfaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <p>
 * Annotated with @Repository (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Eventualmente usare una costante di VaadCost come @Qualifier sia qui che nella corrispondente classe xxxBackend <br>
 */
@Repository
@Qualifier("statistica")
public interface StatisticaBioRepository extends MongoRepository<StatisticaBio, String> {

    @Override
    List<StatisticaBio> findAll();

    <StatisticaBio extends AEntity> StatisticaBio insert(StatisticaBio entity);

    <StatisticaBio extends AEntity> StatisticaBio save(StatisticaBio entity);

    @Override
    void delete(StatisticaBio entity);

    StatisticaBio findFirstByOrdine(int ordine);

    StatisticaBio findFirstByEvento(LocalDate evento);

}// end of crud repository class