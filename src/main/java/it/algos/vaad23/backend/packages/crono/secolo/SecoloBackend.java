package it.algos.vaad23.backend.packages.crono.secolo;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 01-mag-2022
 * Time: 21:24
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class SecoloBackend extends CrudBackend {

    public SecoloRepository repository;

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
    public SecoloBackend(@Autowired @Qualifier(TAG_SECOLO) final MongoRepository crudRepository) {
        super(crudRepository, Secolo.class);
        this.repository = (SecoloRepository) crudRepository;
    }

    public boolean crea(final int ordine, final String nome, final int inizio, final int fine, final boolean anteCristo) {
        Secolo secolo = newEntity(ordine, nome, inizio, fine, anteCristo);
        return crudRepository.insert(secolo) != null;
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Secolo newEntity() {
        return newEntity(0, VUOTA, 0, 0, false);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param ordine     di presentazione nel popup/combobox (obbligatorio, unico)
     * @param nome       descrittivo e visualizzabile
     * @param inizio     primo anno del secolo
     * @param fine       ultimo anno del secolo
     * @param anteCristo secolo prima o dopo Cristo
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Secolo newEntity(final int ordine, final String nome, final int inizio, final int fine, final boolean anteCristo) {
        return Secolo.builder()
                .ordine(ordine)
                .nome(textService.isValid(nome) ? nome : null)
                .inizio(inizio)
                .fine(fine)
                .anteCristo(anteCristo)
                .build();
    }

    /**
     * Seleziona un secolo dall'anno indicato <br>
     *
     * @param nome descrittivo
     *
     * @return secolo selezionato
     */
    public Secolo findByNome(final String nome) {
        return repository.findFirstByNome(nome);
    }

    @Override
    public List<Secolo> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "ordine"));
    }

    public List<String> findNomi() {
        return findAll().stream()
                .map(secolo -> secolo.nome)
                .collect(Collectors.toList());
    }
    public List<String> findNomiAscendenti() {
        List<Secolo> secoli= repository.findAll(Sort.by(Sort.Direction.ASC, "ordine"));
        return secoli.stream()
                .map(secolo -> secolo.nome)
                .collect(Collectors.toList());
    }

    /**
     * Seleziona un secolo dall'anno indicato <br>
     * SOLO per secoli AC <br>
     *
     * @param anno indicato per la selezione del secolo
     *
     * @return secolo Ante Cristo selezionato
     */
    public Secolo getSecoloAC(final int anno) {
        return repository.findFirstByInizioGreaterThanEqualAndFineLessThanEqualAndAnteCristo(anno, anno, true);
    }


    /**
     * Seleziona un secolo dall'anno indicato <br>
     * SOLO per secoli DC <br>
     *
     * @param anno indicato per la selezione del secolo
     *
     * @return secolo Dopo Cristo selezionato
     */
    public Secolo getSecoloDC(int anno) {
        return repository.findFirstByInizioLessThanEqualAndFineGreaterThanEqualAndAnteCristo(anno, anno, false);
    }
    public Secolo getSecolo(int ordine) {
        return repository.findFirstByOrdine(ordine);
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
        String nomeFile = "secoli";
        Map<String, List<String>> mappa;
        List<String> riga;
        int ordine;
        String nome;
        int inizio;
        int fine;
        boolean anteCristo = false;
        String anteCristoText;

        if (super.reset()) {
            mappa = resourceService.leggeMappaConfig(nomeFile);
            if (mappa != null) {
                for (String key : mappa.keySet()) {
                    riga = mappa.get(key);
                    if (riga.size() == 5) {
                        try {
                            ordine = Integer.decode(riga.get(0));
                        } catch (Exception unErrore) {
                            logger.error(new WrapLog().exception(unErrore).usaDb());
                            ordine = 0;
                        }
                        nome = riga.get(1);
                        try {
                            inizio = Integer.decode(riga.get(2));
                        } catch (Exception unErrore) {
                            logger.error(new WrapLog().exception(unErrore).usaDb());
                            inizio = 0;
                        }
                        try {
                            fine = Integer.decode(riga.get(3));
                        } catch (Exception unErrore) {
                            logger.error(new WrapLog().exception(unErrore).usaDb());
                            fine = 0;
                        }
                        anteCristoText = riga.get(4);
                        anteCristo = anteCristoText.equals("true") || anteCristoText.equals("vero") || anteCristoText.equals("si");
                    }
                    else {
                        logger.error(new WrapLog().exception(new AlgosException("I dati non sono congruenti")).usaDb());
                        return false;
                    }
                    nome += anteCristo ? " secolo a.C." : " secolo";

                    if (!crea(ordine, nome, inizio, fine, anteCristo)) {
                        logger.error(new WrapLog().exception(new AlgosException(String.format("La entity %s non è stata salvata", nome))).usaDb());
                    }
                }
            }
            else {
                logger.error(new WrapLog().exception(new AlgosException("Non ho trovato il file sul server")).usaDb());
                return false;
            }
        }

        return true;
    }

}// end of crud backend class
