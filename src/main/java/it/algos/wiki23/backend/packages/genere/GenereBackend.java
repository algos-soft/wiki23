package it.algos.wiki23.backend.packages.genere;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: dom, 24-apr-2022
 * Time: 10:17
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class GenereBackend extends WikiBackend {


    public GenereRepository repository;

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
    public GenereBackend(@Autowired @Qualifier(TAG_GENERE) final MongoRepository crudRepository) {
        super(crudRepository, Genere.class);
        this.repository = (GenereRepository) crudRepository;
    }

    public Genere creaIfNotExist(final String singolare, final AETypeGenere type, final String pluraleMaschile, final String pluraleFemminile) {
        return checkAndSave(newEntity(singolare, type, pluraleMaschile, pluraleFemminile));
    }

    public Genere checkAndSave(final Genere genere) {
        return isExist(genere.singolare) ? null : repository.insert(genere);
    }

    public boolean isExist(final String singolare) {
        return findFirstBySingolare(singolare) != null;
    }

    /**
     * Retrieves the first entity by a 'singular' property.
     * Cerca una singola entity con una query. <br>
     * Restituisce un valore valido ANCHE se esistono diverse entities <br>
     *
     * @param genereSingolare per costruire la query
     *
     * @return the FIRST founded entity
     */
    public Genere findFirstBySingolare(final String genereSingolare) {
        return repository.findFirstBySingolare(genereSingolare);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Genere newEntity() {
        return newEntity(VUOTA, null, VUOTA, VUOTA);
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param singolare        di riferimento
     * @param type             genere di riferimento
     * @param pluraleMaschile  di riferimento
     * @param pluraleFemminile di riferimento
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Genere newEntity(final String singolare, final AETypeGenere type, final String pluraleMaschile, final String pluraleFemminile) {
        Genere newEntityBean = Genere.builder()
                .singolare(textService.isValid(singolare) ? singolare : null)
                .type(type != null ? type : AETypeGenere.maschile)
                .pluraleMaschile(textService.isValid(pluraleMaschile) ? pluraleMaschile : null)
                .pluraleFemminile(textService.isValid(pluraleFemminile) ? pluraleFemminile : null)
                .build();

        return newEntityBean;
    }

    protected Predicate<Genere> startEx = genere -> genere.singolare.startsWith(TAG_EX) || genere.singolare.startsWith(TAG_EX2);


    public List<Genere> findStartingEx() {
        return (List<Genere>) findAll().stream().filter(startEx).collect(Collectors.toList());
    }


    public List<String> findAllSingolari() {
        List<String> singolari = new ArrayList<>();
        List<Genere> listaAll = findAll();

        for (Genere genere : listaAll) {
            singolari.add(genere.singolare);
        }

        return singolari;
    }

    public List<String> findAllPluraliMaschili() {
        List<String> plurali = new ArrayList<>();
        List<Genere> listaAll = findAll();

        for (Genere genere : listaAll) {
            if (genere.pluraleMaschile != null) {
                if (!plurali.contains(genere.pluraleMaschile)) {
                    plurali.add(genere.pluraleMaschile);
                }
            }
        }

        return plurali;
    }

    public List<String> findAllPluraliFemminili() {
        List<String> plurali = new ArrayList<>();
        List<Genere> listaAll = findAll();

        for (Genere genere : listaAll) {
            if (genere.pluraleFemminile != null) {
                if (!plurali.contains(genere.pluraleFemminile)) {
                    plurali.add(genere.pluraleFemminile);
                }
            }
        }

        return plurali;
    }

    public List<String> findAllPluraliDistinti() {
        List<String> plurali = new ArrayList<>();

        plurali.addAll(findAllPluraliMaschili());
        plurali.addAll(findAllPluraliFemminili());

        Collections.sort(plurali);
        return plurali;
    }

    public String getPluraleMaschile(String singolare) {
        Genere genere = findFirstBySingolare(singolare);
        return genere.pluraleMaschile != null ? genere.pluraleMaschile : VUOTA;
    }

    public String getPluraleFemminile(String singolare) {
        Genere genere = findFirstBySingolare(singolare);
        return genere.pluraleFemminile != null ? genere.pluraleFemminile : VUOTA;
    }

    public String getPlurale(String singolare) {
        Genere genere = findFirstBySingolare(singolare);
        return getPlurale(singolare, genere.type);
    }

    public String getPlurale(String singolare, AETypeGenere type) {
        Genere genere = findFirstBySingolare(singolare);

        if (type == AETypeGenere.maschile && genere.pluraleMaschile != null) {
            return genere.pluraleMaschile;
        }

        if (type == AETypeGenere.femminile && genere.pluraleFemminile != null) {
            return genere.pluraleFemminile;
        }

        return VUOTA;
    }


    /**
     * Legge la mappa di valori dal modulo di wiki <br>
     * Cancella la (eventuale) precedente lista di generi <br>
     * Elabora la mappa per creare i singoli generi <br>
     *
     * @param wikiTitle della pagina su wikipedia
     *
     * @return true se l'azione è stata eseguita
     */
    public void download(final String wikiTitle) {
        long inizio = System.currentTimeMillis();
        int size = 0;
        String singolare;
        String pluraliGrezzi;
        String pluraleMaschile;
        String pluraleFemminile;
        AETypeGenere type;

        Map<String, String> mappa = wikiApiService.leggeMappaModulo(wikiTitle);

        if (mappa != null && mappa.size() > 0) {
            deleteAll();
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                singolare = entry.getKey();
                singolare = textService.primaMinuscola(singolare);
                pluraliGrezzi = entry.getValue();

                pluraleMaschile = this.estraeMaschile(pluraliGrezzi);
                pluraleFemminile = this.estraeFemminile(pluraliGrezzi);

                if (textService.isValid(pluraleMaschile) && textService.isValid(pluraleFemminile)) {
                    type = AETypeGenere.entrambi;
                }
                else {
                    if (textService.isValid(pluraleMaschile)) {
                        type = AETypeGenere.maschile;
                    }
                    else {
                        type = AETypeGenere.femminile;
                    }
                }
                if (creaIfNotExist(singolare, type, pluraleMaschile, pluraleFemminile) != null) {
                    size++;
                }
            }
        }
        else {
            message = String.format("Non sono riuscito a leggere da wiki il modulo %s", wikiTitle);
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
        }
        super.fixDownload(inizio, wikiTitle, mappa.size(), size);
    }


    /**
     * Funziona solo per il format: {"avvocati","M", "avvocate","F"} oppure: {"avvocati","M"}
     */
    private String estraeMaschile(String testoPlurale) {
        String pluraleMaschile = VUOTA;
        String tagM = "M";
        String tagApi = "\"";

        if (testoPlurale.contains(tagM)) {
            pluraleMaschile = textService.estrae(testoPlurale, tagApi, tagApi);
        }

        return pluraleMaschile;
    }


    /**
     * Funziona solo per il format: {"avvocati","M", "avvocate","F"} oppure: {"avvocate","F"}
     */
    private String estraeFemminile(String testoPlurale) {
        String pluraleFemminile = "";
        String plurale = "";
        String tagIni = "{";
        String tagEnd = "}";
        String tagVir = ",";
        String tagUgu = "=";
        String tagApi = "";
        String tagM = "M";
        String tagF = "F";
        String[] parti;
        String tag = "F";

        // Funziona solo per il format: { "avvocati","M", "avvocate","F"}
        plurale = textService.setNoGraffe(testoPlurale);
        parti = plurale.split(tagVir);
        for (int k = 0; k < parti.length; k++) {
            parti[k] = textService.setNoDoppiApici(parti[k]);
        }

        for (int k = 0; k < parti.length; k++) {
            if (parti[k].equals(tag)) {
                if (k > 0) {
                    pluraleFemminile = parti[k - 1];
                }
            }
        }

        return pluraleFemminile;
    }


}// end of crud backend class
