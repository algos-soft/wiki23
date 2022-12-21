package it.algos.wiki23.backend.packages.giorno;

import it.algos.vaad24.backend.exception.*;
import it.algos.vaad24.backend.packages.crono.giorno.*;
import it.algos.vaad24.backend.packages.crono.mese.*;
import it.algos.vaad24.backend.wrapper.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

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
public class GiornoWikiBackend extends WikiBackend {

    public GiornoWikiRepository repository;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public GiornoBackend giornoBackend;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MeseBackend meseBackend;

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


    public GiornoWiki creaIfNotExist(final Giorno giornoBase) {
        return checkAndSave(newEntity(giornoBase));
    }

    public GiornoWiki checkAndSave(final GiornoWiki giornoWiki) {
        return findByNome(giornoWiki.nomeWiki) != null ? null : repository.insert(giornoWiki);
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
        GiornoWiki giornoWiki = GiornoWiki.giornoWikiBuilder()
                .ordine(giornoBase.ordine)
                .nomeWiki(giornoBase.nome)
                .build();

        return fixProperties(giornoWiki);
    }

    public GiornoWiki fixProperties(GiornoWiki giornoWiki) {
        giornoWiki.pageNati = wikiUtility.wikiTitleNatiGiorno(giornoWiki.nomeWiki);
        giornoWiki.pageMorti = wikiUtility.wikiTitleMortiGiorno(giornoWiki.nomeWiki);
        return giornoWiki;
    }

    @Override
    public List<GiornoWiki> findAll() {
        return repository.findAll();
    }

    public List<String> findNomi() {
        return giornoBackend.findNomi();
    }

    public GiornoWiki findByNome(final String nome) {
        return repository.findFirstByNomeWiki(nome);
    }


    public boolean isEsiste(final String nome) {
        return repository.findFirstByNomeWiki(nome) != null;
    }

    public boolean isNotEsiste(final String nome) {
        return !isEsiste(nome);
    }


    public List<String> findAllPagine() {
        List<String> listaNomi = new ArrayList<>();
        List<Giorno> listaGiorni = giornoBackend.findAll();

        for (Giorno giorno : listaGiorni) {
            listaNomi.add(wikiUtility.wikiTitleNatiGiorno(giorno.nome));
            listaNomi.add(wikiUtility.wikiTitleMortiGiorno(giorno.nome));
        }

        return listaNomi;
    }




    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void elabora() {
        long inizio = System.currentTimeMillis();

        //--Per ogni anno calcola quante biografie lo usano (nei 2 parametri)
        //--Memorizza e registra il dato nella entityBean
        for (GiornoWiki giornoWiki : findAll()) {
            giornoWiki.bioNati = bioBackend.countGiornoNato(giornoWiki.nomeWiki);
            giornoWiki.bioMorti = bioBackend.countGiornoMorto(giornoWiki.nomeWiki);

            update(giornoWiki);
        }

        super.fixElaboraMinuti(inizio, "pagine di giorni");

    }

    /**
     * Creazione di alcuni dati <br>
     * Esegue SOLO se la collection NON esiste oppure esiste ma è VUOTA <br>
     * Viene invocato alla creazione del programma <br>
     * I dati possono essere presi da una Enumeration, da un file CSV locale, da un file CSV remoto o creati hardcoded <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public AResult resetOnlyEmpty() {
        AResult result = super.resetOnlyEmpty();
        List<Giorno> giorniBase;

        if (meseBackend.count() < 1) {
            logger.error(new WrapLog().exception(new AlgosException("Manca la collezione 'Mese'")).usaDb());
            return result;
        }

        if (result.isValido()) {
            Sort sort = Sort.by(Sort.Direction.ASC, "ordine");
            giorniBase = giornoBackend.findAll(sort);
            for (Giorno giorno : giorniBase) {
                creaIfNotExist(giorno);
            }
        }
        else {
            return result;
        }

        return fixResult(result);
    }

}// end of crud backend class
