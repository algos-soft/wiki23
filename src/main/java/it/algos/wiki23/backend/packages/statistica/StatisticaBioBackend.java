package it.algos.wiki23.backend.packages.statistica;

import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.logic.*;
import it.algos.vaad24.backend.packages.crono.secolo.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

import java.time.*;
import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Sun, 21-Aug-2022
 * Time: 14:07
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class StatisticaBioBackend extends WikiBackend {

    public StatisticaBioRepository repository;

    /**
     * Costruttore @Autowired (facoltativo) @Qualifier (obbligatorio) <br>
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation <br>
     * Si usa un @Qualifier(), per specificare la classe che incrementa l'interfaccia repository <br>
     * Si usa una costante statica, per essere sicuri di scriverla uguale a quella di xxxRepository <br>
     * Regola la classe di persistenza dei dati specifica e la passa al costruttore della superclasse <br>
     * Regola la entityClazz (final nella superclasse) associata a questo service <br>
     *
     * @param crudRepository per la persistenza dei dati
     */
    public StatisticaBioBackend(@Autowired @Qualifier("statistica") final MongoRepository crudRepository) {
        super(crudRepository, StatisticaBio.class);
        this.repository = (StatisticaBioRepository) crudRepository;
    }

    public StatisticaBio creaIfNotExist(LocalDate evento,
                                        int bio,
                                        int giorni,
                                        int anni,
                                        int attivita,
                                        int nazionalita,
                                        int attesa) {
        return checkAndSave(newEntity(evento, bio, giorni, anni, attivita, nazionalita, attesa));
    }

    public StatisticaBio checkAndSave(final StatisticaBio statistica) {
        return isExist(statistica.ordine, statistica.evento) ? null : repository.insert(statistica);
    }

    public boolean isExist(final int ordine, LocalDate evento) {
        boolean esisteOrdine = repository.findFirstByOrdine(ordine) != null;
        boolean esisteEvento = repository.findFirstByEvento(evento) != null;
        return esisteOrdine || esisteEvento;
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public StatisticaBio newEntity() {
        return newEntity(null, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param evento      (obbligatorio)
     * @param bio         (obbligatorio)
     * @param giorni      (obbligatorio)
     * @param anni        (obbligatorio)
     * @param attivita    (obbligatorio)
     * @param nazionalita (obbligatorio)
     * @param attesa      (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public StatisticaBio newEntity(
            LocalDate evento,
            int bio,
            int giorni,
            int anni,
            int attivita,
            int nazionalita,
            int attesa) {
        return StatisticaBio.builder()
                .ordine(getNewOrdine())
                .evento(evento)
                .bio(bio)
                .giorni(giorni)
                .anni(anni)
                .attivita(attivita)
                .nazionalita(nazionalita)
                .attesa(attesa)
                .build();
    }

    @Override
    public List findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "evento"));
    }

    public int getNewOrdine() {
        int newOrdine = 1;
        int existingOrdine;
        List<StatisticaBio> lista;

        lista = repository.findAll(Sort.by(Sort.Direction.DESC, "ordine"));
        if (lista != null && lista.size() > 0) {
            existingOrdine = lista.get(0).ordine;
            newOrdine = existingOrdine + 1;
        }

        return newOrdine;
    }

    public StatisticaBio findLast() {
        StatisticaBio lastStatistica=null;
        List<StatisticaBio> lista;

        lista = repository.findAll(Sort.by(Sort.Direction.DESC, "ordine"));
        if (lista != null && lista.size() > 0) {
            lastStatistica = lista.get(0);
        }

        return lastStatistica;
    }

}// end of crud backend class
