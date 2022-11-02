package it.algos.wiki23.backend.packages.cognome;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.upload.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

import java.util.*;
import java.util.stream.*;

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
public class CognomeBackend extends WikiBackend {

    public CognomeRepository repository;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MongoService mongoService;

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

    public Cognome creaIfNotExist(final String cognomeTxt, int numBio, boolean esistePagina) {
        return checkAndSave(newEntity(cognomeTxt, numBio, esistePagina));
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
        return newEntity(VUOTA, 0, false);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param cognomeTxt   (obbligatorio, unico)
     * @param numBio       (obbligatorio)
     * @param esistePagina (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Cognome newEntity(final String cognomeTxt, int numBio, boolean esistePagina) {
        return Cognome.builder()
                .cognome(textService.isValid(cognomeTxt) ? cognomeTxt : null)
                .numBio(numBio)
                .esistePagina(esistePagina)
                .build();
    }


    public Cognome findByCognome(final String cognome) {
        return repository.findFirstByCognome(cognome);
    }

    @Override
    public List<Cognome> findAll() {
        List<Cognome> lista = super.findAll();

        return lista.stream()
                .sorted(Comparator.comparing(c -> c.cognome))
                .collect(Collectors.toList());
    }

    public List<Cognome> findAllSortNumBio() {
        List<Cognome> lista = super.findAll();

        lista = lista.stream()
                .sorted(Comparator.comparingInt(c -> c.numBio))
                .collect(Collectors.toList());
        Collections.reverse(lista);

        return lista;
    }

    public List<Cognome> findAllStampabili() {
        List<Cognome> lista = super.findAll();
        int max = WPref.sogliaCognomiWiki.getInt();

        lista = lista.stream()
                .sorted(Comparator.comparing(c -> c.cognome))
                .filter(c -> c.numBio >= max)
                .collect(Collectors.toList());

        return lista;
    }

    public List<Cognome> findAllStampabileSortNumBio() {
        return findAllStampabileSortNumBio(WPref.sogliaCognomiWiki.getInt());
    }

    public List<Cognome> findAllStampabileSortNumBio(int numBio) {
        List<Cognome> lista = findAllSortNumBio();

        lista = lista.stream()
                .filter(c -> c.numBio >= numBio)
                .collect(Collectors.toList());
        Collections.reverse(lista);

        return lista;
    }

    public List<Cognome> findAllEccessiviMongo() {
        return findAllEccessivi(WPref.sogliaCognomiMongo.getInt());
    }

    public List<Cognome> findAllEccessiviServer() {
        return findAllEccessivi(WPref.sogliaCognomiWiki.getInt());
    }

    public List<Cognome> findAllEccessivi(int max) {
        List<Cognome> lista = findAllSortNumBio();

        lista = lista.stream()
                .filter(c -> c.numBio < max)
                .collect(Collectors.toList());
        Collections.reverse(lista);

        return lista;
    }

    /**
     * Fetches all code of cognome <br>
     *
     * @return all selected property
     */
    public List<String> findCognomi() {
        return findAll()
                .stream()
                .map(cognome -> cognome.cognome)
                .collect(Collectors.toList());
    }


    public List<String> findCognomiSortNumBio() {
        return findAllSortNumBio()
                .stream()
                .map(cognome -> cognome.cognome)
                .collect(Collectors.toList());
    }

    public List<String> findCognomiStampabiliSortNumBio() {
        return findAllStampabileSortNumBio()
                .stream()
                .map(cognome -> cognome.cognome)
                .collect(Collectors.toList());
    }

    public List<String> findCognomiStampabili() {
        return findAllStampabili()
                .stream()
                .map(cognome -> cognome.cognome)
                .collect(Collectors.toList());
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
        List<String> cognomi = bioBackend.findAllCognomiDistinti();
        //--Soglia minima per creare una entity nella collezione Cognomi sul mongoDB
        int sogliaMongo = WPref.sogliaCognomiMongo.getInt();
        //--Soglia minima per creare una pagina sul server wiki
        int sogliaWiki = WPref.sogliaCognomiWiki.getInt();

        //--Cancella tutte le entities della collezione
        deleteAll();

        for (String cognomeTxt : cognomi) {
            if (saveCognome(cognomeTxt, sogliaMongo)) {
                cont++;
            }
        }

        logger.info(new WrapLog().message(String.format("Ci sono %d cognomi distinti", tot)));
        super.fixElaboraMinuti(inizio, "cognomi");
        //        logger.info("Creazione di " + text.format(cont) + " cognomi su un totale di " + text.format(tot) + " cognomi distinti. Tempo impiegato: " + date.deltaText(inizio));
    }


    /**
     * Registra il numero di voci biografiche che hanno il cognome indicato <br>
     */
    public boolean saveCognome(String cognomeTxt, int sogliaMongo) {
        Cognome cognome = null;
        long numBio = bioBackend.countCognome(cognomeTxt);

        if (numBio >= sogliaMongo) {
            cognome = creaIfNotExist(cognomeTxt, (int) numBio, esistePagina(cognomeTxt));
        }

        return cognome != null;
    }


    /**
     * Controlla l'esistenza della pagina wiki relativa a questo cognome <br>
     */
    public boolean esistePagina(String cognome) {
        String wikiTitle = PATH_COGNOMI + textService.primaMaiuscola(cognome);
        return appContext.getBean(QueryExist.class).isEsiste(wikiTitle);
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     */
    public WResult uploadPagina(String cognomeTxt) {
        return appContext.getBean(UploadCognomi.class).upload(cognomeTxt);
    }

}// end of crud backend class
