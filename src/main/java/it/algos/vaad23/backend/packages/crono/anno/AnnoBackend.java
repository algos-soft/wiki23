package it.algos.vaad23.backend.packages.crono.anno;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.backend.packages.crono.secolo.*;
import it.algos.vaad23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: lun, 02-mag-2022
 * Time: 16:05
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class AnnoBackend extends CrudBackend {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public SecoloBackend secoloBackend;

    public AnnoRepository repository;

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
    public AnnoBackend(@Autowired @Qualifier(TAG_ANNO) final MongoRepository crudRepository) {
        super(crudRepository, Anno.class);
        this.repository = (AnnoRepository) crudRepository;
    }

    public boolean crea(final int ordine, final String nome, final Secolo secolo, final boolean anteCristo, final boolean bisestile) {
        Anno anno = newEntity(ordine, nome, secolo, anteCristo, bisestile);
        return crudRepository.insert(anno) != null;
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Anno newEntity() {
        return newEntity(0, VUOTA, null, false, false);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param ordine    di presentazione nel popup/combobox (obbligatorio, unico)
     * @param nome      corrente
     * @param secolo    di appartenenza
     * @param bisestile flag per gli anni bisestili
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Anno newEntity(final int ordine, final String nome, final Secolo secolo, final boolean anteCristo, final boolean bisestile) {
        return Anno.builder()
                .ordine(ordine)
                .nome(textService.isValid(nome) ? nome : null)
                .secolo(secolo)
                .anteCristo(anteCristo)
                .bisestile(bisestile)
                .build();
    }

    public Anno findByNome(final String nome) {
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
        if (secoloBackend.count() < 1) {
            logger.error(new WrapLog().exception(new AlgosException("Manca la collezione 'Secolo'")).usaDb());
            return false;
        }

        if (super.reset()) {
            //--costruisce gli anni prima di cristo dal 1000
            for (int k = ANTE_CRISTO; k > 0; k--) {
                crea(k, true);
            }

            //--costruisce gli anni dopo cristo fino al 2030
            for (int k = 1; k <= DOPO_CRISTO; k++) {
                crea(k, false);
            }
        }

        return true;
    }

    public void crea(int anno, boolean ante) {
        int ordine;
        String prima = " a.C";
        String dopo = VUOTA;
        String nome;
        Secolo secolo;
        boolean bisestile = ante ? false : dateService.isBisestile(anno);

        ordine = ANNO_INIZIALE - anno;
        nome = String.format("%d %s", anno, ante ? prima : dopo);
        secolo = ante ? secoloBackend.getSecoloAC(anno) : secoloBackend.getSecoloDC(anno);
        crea(ordine, nome, secolo, ante, bisestile);
    }

}// end of crud backend class
