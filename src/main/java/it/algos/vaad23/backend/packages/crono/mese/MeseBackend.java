package it.algos.vaad23.backend.packages.crono.mese;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.backend.wrapper.*;
import org.bson.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 01-mag-2022
 * Time: 08:51
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class MeseBackend extends CrudBackend {

    private MeseRepository repository;

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
    public MeseBackend(@Autowired @Qualifier(TAG_MESE) final MongoRepository crudRepository) {
        super(crudRepository, Mese.class);
        this.repository = (MeseRepository) crudRepository;
    }

    public boolean crea(final int giorni, final String breve, final String nome) {
        Mese mese = newEntity(giorni, breve, nome);
        return crudRepository.insert(mese) != null;
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Mese newEntity() {
        return newEntity(0, VUOTA, VUOTA);
    }

    public Mese newEntity(Document doc) {
        return newEntity(27, doc.getString("breve"), doc.getString("nome"));
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param giorni (obbligatorio)
     * @param breve  (obbligatorio, unico)
     * @param nome   (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Mese newEntity(final int giorni, final String breve, final String nome) {
        return Mese.builder()
                .giorni(giorni)
                .breve(textService.isValid(breve) ? breve : null)
                .nome(textService.isValid(nome) ? nome : null)
                .build();
    }

    public Mese findByNome(final String nome) {
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
        String nomeFile = "mesi";
        Map<String, List<String>> mappa;
        List<String> riga;
        int giorni;
        String breve;
        String nome;

        if (super.reset()) {
            mappa = resourceService.leggeMappaServer(nomeFile);
            if (mappa != null) {
                for (String key : mappa.keySet()) {
                    riga = mappa.get(key);
                    if (riga.size() == 3) {
                        try {
                            giorni = Integer.decode(riga.get(0));
                        } catch (Exception unErrore) {
                            logger.error(new WrapLog().exception(unErrore).usaDb());
                            giorni = 0;
                        }
                        breve = riga.get(1);
                        nome = riga.get(2);
                    }
                    else {
                        logger.error(new WrapLog().exception(new AlgosException("I dati non sono congruenti")).usaDb());
                        return false;
                    }
                    if (!crea(giorni, breve, nome)) {
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
