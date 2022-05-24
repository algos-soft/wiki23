package it.algos.wiki23.backend.packages.bio;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: gio, 28-apr-2022
 * Time: 11:57
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class BioBackend extends WikiBackend {

    private BioRepository repository;

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
    public BioBackend(@Autowired @Qualifier(TAG_BIO) final MongoRepository crudRepository) {
        super(crudRepository, Bio.class);
        this.repository = (BioRepository) crudRepository;
    }


    public Bio creaIfNotExist(final WrapBio wrap) {
        return checkAndSave(newEntity(wrap));
    }


    public Bio checkAndSave(final Bio bio) {
        return isExist(bio.pageId) ? null : repository.insert(bio);
    }

    public boolean isExist(final long pageId) {
        return repository.findFirstByPageId(pageId) != null;
    }


    public Bio newEntity() {
        return newEntity(0, VUOTA, VUOTA, null);
    }

    public Bio newEntity(final WrapBio wrap) {
        String message;
        if (wrap.isValida()) {
            return newEntity(wrap.getPageid(), wrap.getTitle(), wrap.getTemplBio());
        }
        else {
            message = "wrap non valido";
            if (wrap.getPageid() < 1) {
                message = "Manca il pageid";
            }
            if (textService.isEmpty(wrap.getTitle())) {
                message = "Manca il wikiTitle";
            }
            logger.info(new WrapLog().exception(new AlgosException(message)).usaDb());
            return null;
        }
    }

    public Bio newEntity(final long pageId, final String wikiTitle, final String tmplBio) {
        return newEntity(pageId, wikiTitle, tmplBio, null);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param pageId     di riferimento (obbligatorio, unico)
     * @param wikiTitle  di riferimento (obbligatorio, unico)
     * @param tmplBio    (obbligatorio, unico)
     * @param lastServer sul server wiki (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Bio newEntity(final long pageId, final String wikiTitle, final String tmplBio, final LocalDateTime lastServer) {
        LocalDateTime now = LocalDateTime.now();
        return Bio.builder()
                .pageId(pageId)
                .wikiTitle(textService.isValid(wikiTitle) ? wikiTitle : null)
                .tmplBio(textService.isValid(tmplBio) ? tmplBio : null)
                .elaborato(false)
                .lastServer(lastServer != null ? lastServer : now)
                .lastMongo(now)
                .valido(true)
                .build();
    }

    /**
     * Conta tutte le biografie con una serie di attività. <br>
     *
     * @param attivita singola
     *
     * @return conteggio di biografie che la usano
     */
    public int countAttivita(final String attivita) {
        int numBio = 0;
        Long attivitaUno = repository.countBioByAttivita(attivita);
        Long attivitaDue;
        Long attivitaTre;

        numBio = attivitaUno.intValue();

        if (WPref.usaTreAttivita.is()) {
            attivitaDue = repository.countBioByAttivita2(attivita);
            attivitaTre = repository.countBioByAttivita3(attivita);
            numBio += attivitaDue.intValue() + attivitaTre.intValue();
        }

        return numBio;
    }

    /**
     * Conta tutte le biografie con una serie di nazionalita. <br>
     *
     * @param nazionalita singola
     *
     * @return conteggio di biografie che la usano
     */
    public int countNazionalita(final String nazionalita) {
        Long nazLong = repository.countBioByNazionalita(nazionalita);
        return nazLong.intValue();
    }

    public Bio findByKey(final String pageId) {
        return repository.findFirstByPageId(pageId);
    }

}// end of crud backend class
