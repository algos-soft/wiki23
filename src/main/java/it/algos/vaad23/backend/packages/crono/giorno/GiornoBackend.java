package it.algos.vaad23.backend.packages.crono.giorno;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.logic.*;
import it.algos.vaad23.backend.packages.crono.mese.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
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
 * Date: lun, 02-mag-2022
 * Time: 08:26
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class GiornoBackend extends CrudBackend {

    public GiornoRepository repository;

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
    public GiornoBackend(@Autowired @Qualifier(TAG_GIORNO) final MongoRepository crudRepository) {
        super(crudRepository, Giorno.class);
        this.repository = (GiornoRepository) crudRepository;
    }

    public boolean crea(final int ordine, final String nome, final Mese mese, final int trascorsi, final int mancanti) {
        Giorno giorno = newEntity(ordine, nome, mese, trascorsi, mancanti);
        return crudRepository.insert(giorno) != null;
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Giorno newEntity() {
        return newEntity(0, VUOTA, null, 0, 0);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param ordine    di presentazione nel popup/combobox (obbligatorio, unico)
     * @param nome      corrente
     * @param mese      di appartenenza
     * @param trascorsi di inizio anno
     * @param mancanti  alla fine dell'anno
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Giorno newEntity(final int ordine, final String nome, final Mese mese, final int trascorsi, final int mancanti) {
        return Giorno.builder()
                .ordine(ordine)
                .nome(textService.isValid(nome) ? nome : null)
                .mese(mese)
                .trascorsi(trascorsi)
                .mancanti(mancanti)
                .build();
    }

    public Giorno findByNome(final String nome) {
        return repository.findFirstByNome(nome);
    }

    @Override
    public List<Giorno> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "ordine"));
    }

    public List<String> findNomi() {
        return findAll().stream()
                .map(giorno -> giorno.nome)
                .collect(Collectors.toList());
    }

    public List<Giorno> findAllByMese(Mese mese) {
        return findAll().stream()
                .filter(giorno -> giorno.mese.nome.equals(mese.nome))
                .collect(Collectors.toList());
    }


    public List<String> findNomiByMese(String nomeMese) {
        return findAll().stream()
                .filter(giorno -> giorno.mese.nome.equals(nomeMese))
                .map(giorno -> giorno.nome)
                .collect(Collectors.toList());
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
        int ordine;
        List<HashMap> lista;
        String nome;
        String meseTxt;
        Mese mese;
        int trascorsi = 0;
        int mancanti = 0;
        int tot = 365;
        String message;

        if (meseBackend.count() < 1) {
            logger.error(new WrapLog().exception(new AlgosException("Manca la collezione 'Mese'")).usaDb());
            return false;
        }

        if (super.reset()) {
            //costruisce i 366 records
            lista = dateService.getAllGiorni();
            for (HashMap mappaGiorno : lista) {
                nome = (String) mappaGiorno.get(KEY_MAPPA_GIORNI_TITOLO);
                meseTxt = (String) mappaGiorno.get(KEY_MAPPA_GIORNI_MESE_TESTO);
                mese = meseBackend.findByNome(meseTxt);
                if (mese == null) {
                    message = String.format("Manca il mese di %s", meseTxt);
                    logger.error(new WrapLog().exception(new AlgosException(message)).usaDb());
                }

                ordine = (int) mappaGiorno.get(KEY_MAPPA_GIORNI_BISESTILE);
                trascorsi = (int) mappaGiorno.get(KEY_MAPPA_GIORNI_NORMALE);
                mancanti = tot - trascorsi;

                try {
                    crea(ordine, nome, mese, trascorsi, mancanti);
                } catch (Exception unErrore) {
                    logger.error(new WrapLog().exception(unErrore).usaDb());
                }
            }
        }

        return true;
    }

}// end of crud backend class
