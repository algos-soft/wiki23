package it.algos.wiki23.backend.packages.bio;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.wrapper.*;
import org.bson.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
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

    public BioRepository repository;

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


    public Bio checkAndSave(Bio bio) {
        return isExist(bio.pageId) ? null : repository.insert(elaboraService.esegue(bio));
    }

    public boolean isExist(final long pageId) {
        return repository.findFirstByPageId(pageId) != null;
    }

    public Bio fixWrap(final String keyID, final WrapBio wrap) {
        Bio bio = newEntity(wrap);
        bio.setId(keyID);
        return bio;
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

    public Bio newEntity(final Document doc) {
        return Bio.builder()
                .pageId(doc.getLong("pageId"))
                .wikiTitle(doc.getString("wikiTitle"))
                .elaborato(doc.getBoolean("elaborato"))
                .nome(doc.getString("nome"))
                .cognome(doc.getString("cognome"))
                .ordinamento(doc.getString("ordinamento"))
                .sesso(doc.getString("sesso"))
                .giornoNato(doc.getString("giornoNato"))
                .annoNato(doc.getString("annoNato"))
                .luogoNato(doc.getString("luogoNato"))
                .luogoNatoLink(doc.getString("luogoNatoLink"))
                .giornoMorto(doc.getString("giornoMorto"))
                .annoMorto(doc.getString("annoMorto"))
                .luogoMorto(doc.getString("luogoMorto"))
                .luogoMortoLink(doc.getString("luogoMortoLink"))
                .attivita(doc.getString("attivita"))
                .attivita2(doc.getString("attivita2"))
                .attivita3(doc.getString("attivita3"))
                .nazionalita(doc.getString("nazionalita"))
                .build();
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

    public Bio insert(final Bio bio) {
        return repository.insert(bio);
    }

    @Override
    public Bio save(final Object entity) {
        if (entity instanceof Bio bio) {
            if (isExist(bio.pageId)) {
                try {
                    repository.save(bio);
                } catch (Exception unErrore) {
                    logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
                }
            }
            else {
                try {
                    repository.insert(bio);
                } catch (Exception unErrore) {
                    logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
                }
            }
            return bio;
        }

        return null;
    }

    @Override
    public Bio update(Object entityBean) {
        if (entityBean instanceof Bio bio) {
            if (isExist(bio.pageId)) {
                repository.save(bio);
            }
            else {
                //                repository.insert(bio);
            }
            return null;
        }

        return null;
    }

    /**
     * Conta tutte le biografie con una serie di attività plurali. <br>
     *
     * @param attivitaPlurale
     *
     * @return conteggio di biografie che la usano
     */
    public int countAttivitaPlurale(final String attivitaPlurale) {
        int numBio = 0;
        List<String> listaNomi = attivitaBackend.findSingolariByPlurale(attivitaPlurale);

        for (String singolare : listaNomi) {
            numBio += countAttivitaAll(singolare);
        }

        return numBio;
    }


    /**
     * Conta tutte le biografie con una serie di attività singolari. <br>
     *
     * @param attivitaSingola
     *
     * @return conteggio di biografie che la usano
     */
    public int countAttivitaAll(final String attivitaSingola) {
        int numBio = 0;
        Long attivitaUno = repository.countBioByAttivita(attivitaSingola);
        Long attivitaDue;
        Long attivitaTre;

        numBio = attivitaUno.intValue();

        if (WPref.usaTreAttivita.is()) {
            attivitaDue = repository.countBioByAttivita2(attivitaSingola);
            attivitaTre = repository.countBioByAttivita3(attivitaSingola);
            numBio += attivitaDue.intValue() + attivitaTre.intValue();
        }

        return numBio;
    }

    public int countAttivita(final String attivitaSingola) {
        Long numBio = repository.countBioByAttivita(attivitaSingola);
        return numBio > 0 ? numBio.intValue() : 0;
    }

    public int countAttivitaDue(final String attivitaSingola) {
        Long numBio = repository.countBioByAttivita2(attivitaSingola);
        return numBio > 0 ? numBio.intValue() : 0;
    }

    public int countAttivitaTre(final String attivitaSingola) {
        Long numBio = repository.countBioByAttivita3(attivitaSingola);
        return numBio > 0 ? numBio.intValue() : 0;
    }


    /**
     * Conta tutte le biografie con una serie di nazionalità plurali. <br>
     *
     * @param nazionalitaPlurale
     *
     * @return conteggio di biografie che la usano
     */
    public int countNazionalitaPlurale(final String nazionalitaPlurale) {
        int numBio = 0;
        List<String> listaNomi = nazionalitaBackend.findSingolariByPlurale(nazionalitaPlurale);

        for (String singolare : listaNomi) {
            numBio += countNazionalita(singolare);
        }

        return numBio;
    }

    /**
     * Conta tutte le biografie con una serie di nazionalita. <br>
     *
     * @param nazionalitaSingola singola
     *
     * @return conteggio di biografie che la usano
     */
    public int countNazionalita(final String nazionalitaSingola) {
        Long nazLong = repository.countBioByNazionalita(nazionalitaSingola);
        return nazLong.intValue();
    }

    public Bio findByKey(final long pageId) {
        return repository.findFirstByPageId(pageId);
    }

    public Bio findByTitle(final String wikiTitle) {
        return repository.findFirstByWikiTitle(wikiTitle);
    }

    public List<Long> findOnlyPageId() {
        return mongoService.projectionLong(Bio.class, "pageId");
    }

    @Override
    public List<Bio> findAll() {
        return findSenzaTmpl();
    }

    /**
     * Controlla l'esistenza della property <br>
     * La lista funziona anche se la property del sort è errata <br>
     * Ma ovviamente il sort non viene effettuato <br>
     *
     * @param sort
     */
    @Override
    public List<Bio> findAll(Sort sort) {
        if (sort == null) {
            return findSenzaTmpl();
        }
        else {
            return findSenzaTmpl(sort);
        }

    }

    public List<Bio> findSenzaTmpl() {
        return mongoService.projectionExclude(Bio.class, this, "tmplBio");
    }

    public List<Bio> findSenzaTmpl(Sort sort) {
        Document doc = null;

        if (sort != null) {
            doc = new Document("ordinamento", 1);
        }

        return mongoService.projectionExclude(Bio.class, this, doc, "tmplBio");
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void elabora() {
        long inizio = System.currentTimeMillis();
        List<Bio> lista = findAll();
        int dim = 50000;
        int blocco = lista.size() / dim;
        int ini;
        int end;
        String size;
        String time;
        String message;
        int cont = 0;

        for (int k = 0; k < blocco; k++) {
            ini = k * dim;
            end = Math.min(ini + dim, lista.size());
            for (Bio bio : lista.subList(ini, end)) {
                bio = this.findByKey(bio.pageId);
                bio = elaboraService.esegue(bio);
                save(bio);
                cont++;
            }
            size = textService.format(cont);
            time = dateService.deltaText(inizio);
            message = String.format("Elaborate finora %s voci biografiche, in %s", size, time);
            logger.info(new WrapLog().message(message).type(AETypeLog.elabora));
        }
        super.fixElaboraMinuti(inizio, "biografie");
    }

}// end of crud backend class
