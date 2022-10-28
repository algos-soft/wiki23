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

    public MeseRepository repository;

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

    public Mese crea(final int ordine, final String breve, final String nome, final int giorni, int primo, int ultimo) {
        Mese mese = newEntity(ordine, breve, nome, giorni, primo, ultimo);
        return repository.insert(mese);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Mese newEntity() {
        return newEntity(0, VUOTA, VUOTA, 0, 0, 0);
    }

    public Mese newEntity(Document doc) {
        return newEntity(27, doc.getString("breve"), doc.getString("nome"), 0, 0, 0);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param ordine (obbligatorio, unico)
     * @param breve  (obbligatorio, unico)
     * @param nome   (obbligatorio, unico)
     * @param giorni (obbligatorio)
     * @param primo  giorno dell'anno
     * @param ultimo giorno dell'anno
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Mese newEntity(int ordine, String breve, String nome, int giorni, int primo, int ultimo) {
        return Mese.builder()
                .ordine(ordine)
                .breve(textService.isValid(breve) ? breve : null)
                .nome(textService.isValid(nome) ? nome : null)
                .giorni(giorni)
                .primo(primo)
                .ultimo(ultimo)
                .build();
    }

    public Mese findByNome(final String nome) {
        return repository.findFirstByNome(nome);
    }

    public int getOrdine(final String nomeMaiuscoloMinuscolo) {
        Mese mese = findByNome(textService.primaMinuscola(nomeMaiuscoloMinuscolo));
        return mese != null ? mese.ordine : 0;
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
        return super.reset() ? resetServer(nomeFile) != null : false;
    }

    public List<Mese> resetConfig(String nomeFile) {
        Map<String, List<String>> mappa = resourceService.leggeMappaConfig(nomeFile);
        return reset(mappa);
    }

    public List<Mese> resetServer(String nomeFile) {
        Map<String, List<String>> mappa = resourceService.leggeMappaServer(nomeFile);
        return reset(mappa);
    }

    /**
     * Creazione di alcuni dati iniziali <br>
     * I dati possono essere presi da una Enumeration, da un file CSV locale, da un file CSV remoto o creati hardcoded <br>
     */
    public List<Mese> reset(Map<String, List<String>> mappa) {
        List<Mese> mesi = new ArrayList<>();
        Mese mese;
        List<String> riga;
        int giorni;
        String breve;
        String nome;
        String message;
        int ordine = 0;
        int primo = 0;
        int ultimo = 0;

        if (mappa != null) {
            for (String key : mappa.keySet()) {
                riga = mappa.get(key);
                if (riga.size() >= 3) {
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
                    return null;
                }
                if (riga.size() >= 4) {
                    primo = Integer.decode(riga.get(3));
                }
                if (riga.size() >= 5) {
                    ultimo = Integer.decode(riga.get(4));
                }

                if (giorni > 0 && primo > 0 && ultimo > 0) {
                    if (giorni != (ultimo - primo + 1)) {
                        message = String.format("Il numero di 'giorni' da 'primo' a 'ultimo' non coincidono per il mese di %s", nome);
                        logger.error(new WrapLog().exception(new AlgosException(message)));
                        return null;
                    }
                }

                mese = crea(ordine, breve, nome, giorni, primo, ultimo);
                if (mese != null) {
                    mesi.add(mese);
                }
                else {
                    logger.error(new WrapLog().exception(new AlgosException(String.format("La entity %s non è stata salvata", nome))).usaDb());
                }
            }
        }
        else {
            logger.error(new WrapLog().exception(new AlgosException("Non ho trovato il file sul server")).usaDb());
            return null;
        }

        return mesi;
    }

}// end of crud backend class
