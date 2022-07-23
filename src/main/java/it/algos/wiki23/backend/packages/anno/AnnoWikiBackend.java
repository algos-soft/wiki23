package it.algos.wiki23.backend.packages.anno;

import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.service.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Fri, 08-Jul-2022
 * Time: 06:34
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class AnnoWikiBackend extends WikiBackend {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiUtility wikiUtility;

    public AnnoWikiRepository repository;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AnnoBackend annoBackend;

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
    public AnnoWikiBackend(@Autowired @Qualifier("AnnoWiki") final MongoRepository crudRepository) {
        super(crudRepository, AnnoWiki.class);
        this.repository = (AnnoWikiRepository) crudRepository;
    }

    public AnnoWiki creaIfNotExist(final Anno annoBase) {
        return checkAndSave(newEntity(annoBase));
    }

    public AnnoWiki checkAndSave(final AnnoWiki annoBase) {
        return findByNome(annoBase.nome) != null ? null : repository.insert(annoBase);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public AnnoWiki newEntity() {
        return newEntity((Anno) null);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param annoBase proveniente da vaadin23
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public AnnoWiki newEntity(final Anno annoBase) {
        AnnoWiki annoWiki = AnnoWiki.annoWikiBuilder()
                .ordine(annoBase.ordine)
                .nome(annoBase.nome)
                .build();

        return fixProperties(annoWiki);
    }

    public AnnoWiki fixProperties(AnnoWiki annoWiki) {
        annoWiki.pageNati = wikiUtility.wikiTitleNatiAnno(annoWiki.nome);
        annoWiki.pageMorti = wikiUtility.wikiTitleMortiAnno(annoWiki.nome);
        return annoWiki;
    }

    @Override
    public List<AnnoWiki> findAll() {
        return repository.findAll();
    }

    public AnnoWiki findByNome(final String nome) {
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
        List<Anno> anniBase = null;

        if (mongoService.isCollectionNullOrEmpty(Anno.class)) {
            logger.error(new WrapLog().exception(new AlgosException("Manca la collezione 'Anno'")));
            return false;
        }

        if (super.reset()) {
            Sort sort = Sort.by(Sort.Direction.ASC, "ordine");
            anniBase = annoBackend.findAll(sort);
            for (Anno anno : anniBase) {
                creaIfNotExist(anno);
            }
        }

        return true;
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void elabora() {
        long inizio = System.currentTimeMillis();
        int cont = 0;
        String size;
        String time;
        int tot = count();

        //--Per ogni anno calcola quante biografie lo usano (nei 2 parametri)
        //--Memorizza e registra il dato nella entityBean
        for (AnnoWiki anno : findAll()) {
            anno.bioNati = bioBackend.countAnnoNato(anno.nome);
            anno.bioMorti = bioBackend.countAnnoMorto(anno.nome);

            update(anno);

            if (Pref.debug.is()) {
                cont++;
                if (mathService.multiploEsatto(500, cont)) {
                    size = textService.format(cont);
                    time = dateService.deltaText(inizio);
                    message = String.format("Finora controllata l'esistenza di %s/%s anni, in %s", size, tot, time);
                    logger.info(new WrapLog().message(message).type(AETypeLog.elabora));
                }
            }
        }

        super.fixElaboraMinuti(inizio, "anni");
    }

}// end of crud backend class
