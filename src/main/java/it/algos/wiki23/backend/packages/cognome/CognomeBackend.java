package it.algos.wiki23.backend.packages.cognome;

import com.mongodb.client.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.logic.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
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
 * Date: Wed, 10-Aug-2022
 * Time: 08:43
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class CognomeBackend extends CrudBackend {

    public CognomeRepository repository;

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
    public CognomeBackend(@Autowired @Qualifier(TAG_COGNOME) final MongoRepository crudRepository) {
        super(crudRepository, Cognome.class);
        this.repository = (CognomeRepository) crudRepository;
    }

    public Cognome creaIfNotExist(final String nome) {
        return checkAndSave(newEntity(nome));
    }

    public Cognome checkAndSave(final Cognome cognome) {
        return isExist(cognome.cognome) ? null : repository.insert(cognome);
    }

    public boolean isExist(final String cognome) {
        return repository.findFirstByCognome(cognome) != null;
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Cognome newEntity() {
        return newEntity(VUOTA);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param cognome (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Cognome newEntity(final String cognome) {
        return Cognome.builder()
                .cognome(textService.isValid(cognome) ? cognome : null)
                .build();
    }

    /**
     * Fetches all code of Prenome <br>
     *
     * @return all selected property
     */
    public List<String> fetchCognome() {
        List<String> lista = new ArrayList<>();
        List<Cognome> listaEntities = repository.findAll();

        for (Cognome cognome : listaEntities) {
            lista.add(cognome.cognome);
        }

        return lista;
    }




    /**
     * Cancella i cognomi esistenti <br>
     * Crea tutti i cognomi <br>
     * Controlla che ci siano almeno n voci biografiche per il singolo cognome <br>
     * Registra la entity <br>
     * Non registra la entity col cognomi mancante <br>
     */
    public void elabora() {
        long inizio = System.currentTimeMillis();
        int tot = 0;
        int cont = 0;
//        logger.info("Creazione completa cognomi delle biografie. Circa 4 minuti.");

        //--Cancella tutte le entities della collezione
        deleteAll();

        //@Field("cogn")
//        DistinctIterable<String> listaCognomiDistinti = mongo.mongoOp.getCollection("bio").distinct("cogn", String.class);
//        for (String cognomeTxt : listaCognomiDistinti) {
//            tot++;
//
//            if (saveCognome(cognomeTxt) != null) {
//                cont++;
//            }// end of if cycle
//        }// end of for cycle
//
//        super.setLastElabora(EATempo.minuti, inizio);
//        logger.info("Creazione di " + text.format(cont) + " cognomi su un totale di " + text.format(tot) + " cognomi distinti. Tempo impiegato: " + date.deltaText(inizio));
    }// end of method

}// end of crud backend class
