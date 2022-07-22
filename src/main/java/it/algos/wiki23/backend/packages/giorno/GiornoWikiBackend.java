package it.algos.wiki23.backend.packages.giorno;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import it.algos.vaad23.backend.wrapper.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Thu, 14-Jul-2022
 * Time: 20:04
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class GiornoWikiBackend extends CrudBackend {

    public GiornoWikiRepository repository;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public GiornoBackend giornoBackend;

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
    //@todo registrare eventualmente come costante in VaadCost il valore del Qualifier
    public GiornoWikiBackend(@Autowired @Qualifier("GiornoWiki") final MongoRepository crudRepository) {
        super(crudRepository, GiornoWiki.class);
        this.repository = (GiornoWikiRepository) crudRepository;
    }


    public GiornoWiki creaIfNotExist(final Giorno annoBase) {
        return checkAndSave(newEntity(annoBase));
    }

    public GiornoWiki checkAndSave(final GiornoWiki annoBase) {
        return findByNome(annoBase.nome) != null ? null : repository.insert(annoBase);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public GiornoWiki newEntity() {
        return newEntity((Giorno) null);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param giornoBase proveniente da vaadin23
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public GiornoWiki newEntity(final Giorno giornoBase) {
        GiornoWiki annoWiki = GiornoWiki.giornoWikiBuilder().build();

        annoWiki.ordine = giornoBase.ordine;
        annoWiki.nome = giornoBase.nome;
        annoWiki.mese = giornoBase.mese;
        annoWiki.trascorsi = giornoBase.trascorsi;
        annoWiki.mancanti = giornoBase.mancanti;

        return annoWiki;
    }

    public GiornoWiki findByNome(final String nome) {
        return repository.findFirstByNome(nome);
    }

    /**
     * Creazione di alcuni dati iniziali <br>
     * Viene invocato alla creazione del programma o dal bottone Reset della lista <br>
     * La collezione viene svuotata <br>
     * I dati possono essere presi da una Enumeration, da un file CSV locale, da un file CSV remoto o creati hardcoded <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public boolean reset() {
        List<Giorno> giorniBase = null;

        if (mongoService.isCollectionNullOrEmpty(Anno.class)) {
            logger.error(new WrapLog().exception(new AlgosException("Manca la collezione 'Giorno'")));
            return false;
        }

        if (super.reset()) {
            giorniBase = giornoBackend.findAll();
            for (Giorno giorno : giorniBase) {
                creaIfNotExist(giorno);
            }
        }

        return true;
    }


}// end of crud backend class
