package it.algos.wiki23.backend.packages.attivita;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.genere.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.upload.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 18-apr-2022
 * Time: 21:25
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class AttivitaBackend extends WikiBackend {


    public AttivitaRepository repository;


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
    public AttivitaBackend(@Autowired @Qualifier(TAG_ATTIVITA) final MongoRepository crudRepository) {
        super(crudRepository, Attivita.class);
        this.repository = (AttivitaRepository) crudRepository;
    }

    public Attivita creaIfNotExist(final String singolare, final String categoria, final String paragrafo, final AETypeGenere typeGenere, final boolean aggiunta) {
        return checkAndSave(newEntity(singolare, categoria, paragrafo, typeGenere, aggiunta));
    }

    public Attivita checkAndSave(final Attivita attivita) {
        return isExist(attivita.singolare) ? null : repository.insert(attivita);
    }


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Attivita newEntity() {
        return newEntity(VUOTA, VUOTA, VUOTA, null, true);
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param singolare  di riferimento (obbligatorio, unico)
     * @param categoria  di riferimento (obbligatorio, non unico)
     * @param paragrafo  di riferimento (obbligatorio, non unico)
     * @param typeGenere (obbligatorio, non unico)
     * @param aggiunta   flag (facoltativo, di default false)
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Attivita newEntity(final String singolare, final String categoria, final String paragrafo, final AETypeGenere typeGenere, final boolean aggiunta) {
        return Attivita.builder()
                .singolare(textService.isValid(singolare) ? singolare : null)
                .categoria(textService.isValid(categoria) ? categoria : null)
                .paragrafo(textService.isValid(paragrafo) ? paragrafo : null)
                .type(typeGenere != null ? typeGenere : AETypeGenere.maschile)
                .aggiunta(aggiunta)
                .numBio(0)
                .superaSoglia(false)
                .esistePagina(false)
                .build();
    }


    public List<Attivita> findAll() {
        return repository.findAll();
    }


    public List<Attivita> findAttivitaDistinctByPlurali() {
        List<Attivita> lista = new ArrayList<>();
        Set<String> set = new HashSet();
        Sort sortOrder = Sort.by(Sort.Direction.DESC, "plurale");
        List<Attivita> listaAll = repository.findAll(sortOrder);

        for (Attivita attivita : listaAll) {
            if (set.add(attivita.paragrafo)) {
                lista.add(attivita);
            }
        }

        return lista;
    }

    /**
     * Pagine che esistono sul server wikipedia e che non superano la soglia prevista per le liste <br>
     * flag esistePagina=true <br>
     * flag superaSoglia=false <br>
     *
     * @return attività con liste da cancellare
     */
    public List<Attivita> findPagineDaCancellare() {
        List<Attivita> listaDaCancellare = new ArrayList<>();
        List<Attivita> listaPlurali = findAttivitaDistinctByPlurali();

        for (Attivita attivita : listaPlurali) {
            if (attivita.esistePagina && !attivita.superaSoglia) {
                listaDaCancellare.add(attivita);
            }
        }

        return listaDaCancellare;
    }


    public List<String> findAllPlurali() {
        List<String> lista = new ArrayList<>();
        List<Attivita> listaAll = findAttivitaDistinctByPlurali();

        for (Attivita attivita : listaAll) {
            lista.add(attivita.paragrafo);
        }

        return lista;
    }

    public boolean isExist(final String attivitaSingolare) {
        return findFirstBySingolare(attivitaSingolare) != null;
    }

    /**
     * Retrieves the first entity by a 'singular' property.
     * Cerca una singola entity con una query. <br>
     * Restituisce un valore valido ANCHE se esistono diverse entities <br>
     *
     * @param attivitaSingolare per costruire la query
     *
     * @return the FIRST founded entity
     */
    public Attivita findFirstBySingolare(final String attivitaSingolare) {
        return repository.findFirstBySingolare(attivitaSingolare);
    }

    /**
     * Retrieves the first entity by a 'plural' property.
     * Cerca una singola entity con una query. <br>
     * Restituisce un valore valido ANCHE se esistono diverse entities <br>
     *
     * @param attivitaPlurale per costruire la query
     *
     * @return the FIRST founded entity
     */
    public Attivita findFirstByPlurale(final String attivitaPlurale) {
        return repository.findFirstByParagrafo(attivitaPlurale);
    }


    public List<Attivita> findAllByPlurale(final String plurale) {
        return repository.findAllByParagrafoOrderBySingolareAsc(plurale);
    }

    /**
     * Crea una lista di singolari che hanno lo stesso plurale. <br>
     *
     * @param attivitaPlurale da selezionare
     *
     * @return lista di singolari filtrati
     */
    public List<String> findSingolariByPlurale(final String attivitaPlurale) {
        List<String> listaNomi = new ArrayList<>();
        List<Attivita> listaAttivita = findAllByPlurale(attivitaPlurale);

        for (Attivita attivita : listaAttivita) {
            listaNomi.add(attivita.singolare);
        }

        return listaNomi;
    }


    public LinkedHashMap<String, List<String>> findMappaSingolariByPlurale() {
        LinkedHashMap<String, List<String>> mappa = new LinkedHashMap<>();
        List<String> lista;
        List<Attivita> listaAll = repository.findAll();
        String plurale;
        String singolare;

        for (Attivita attivita : listaAll) {
            plurale = attivita.paragrafo;
            singolare = attivita.singolare;

            if (mappa.get(plurale) == null) {
                lista = new ArrayList<>();
                lista.add(singolare);
                mappa.put(plurale, lista);
            }
            else {
                lista = mappa.get(plurale);
                lista.add(singolare);
                mappa.put(plurale, lista);
            }
        }

        return mappa;
    }

    /**
     * Conta il totale delle voci bio per tutte le attività associate a quella indicata. <br>
     * Recupera l'attività plurale e quindi tutte le attività singole associate <br>
     *
     * @param attivitaSingolare selezionata
     *
     * @return totale di voci biografiche interessate
     */
    public int contBio(final Attivita attivitaSingolare) {
        int numBio = 0;
        List<Attivita> lista = this.findAllByPlurale(attivitaSingolare.paragrafo);

        for (Attivita attivita : lista) {
            numBio += attivita.numBio;
        }

        return numBio;
    }

    /**
     * Legge la mappa di valori dal modulo di wiki <br>
     * Cancella la (eventuale) precedente lista di attività <br>
     * Elabora la mappa per creare le singole attività <br>
     * Integra le attività con quelle di genere <br>
     *
     * @param wikiTitle della pagina su wikipedia
     *
     * @return true se l'azione è stata eseguita
     */
    public void download(final String wikiTitle) {
        long inizio = System.currentTimeMillis();
        int size = 0;
        String singolare;
        String categoria;
        String paragrafo;
        AETypeGenere typeGenere = null;
        Genere genere;
        Map<String, String> mappa = wikiApiService.leggeMappaModulo(wikiTitle);

        if (mappa != null && mappa.size() > 0) {
            deleteAll();
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                singolare = entry.getKey();
                if (singolare.equals("medico")) {
                    int a = 87;
                }

                categoria = entry.getValue();
                genere = genereBackend.findFirstBySingolare(singolare);
                typeGenere = genere != null ? genere.getType() : AETypeGenere.nessuno;
                paragrafo = getParagrafo(genere, categoria);

                //                if (genere != null) {
                //                    paragrafo = getParagrafo(genere,categoria);
                //                }
                //                else {
                //                    paragrafo = categoria;
                //                }
                if (creaIfNotExist(singolare, categoria, paragrafo, typeGenere, false) != null) {
                    size++;
                }
            }
        }
        else {
            message = String.format("Non sono riuscito a leggere da wiki il modulo %s", wikiTitle);
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
            return;
        }

        super.fixDownload(inizio, wikiTitle, mappa.size(), size);
        aggiunge();
    }


    private String getParagrafo(final Genere genere, final String categoria) {
        String paragrafo = VUOTA;
        AETypeGenere type;

        if (genere == null) {
            return categoria;
        }

        type = genere.getType();
        paragrafo = switch (type) {
            case maschile -> genere.pluraleMaschile;
            case femminile -> genere.pluraleFemminile;
            case entrambi -> {
                if (genere.pluraleMaschile.equals(genere.pluraleFemminile)) {
                    yield genere.pluraleMaschile;
                }
                else {
                    yield genere.pluraleMaschile ;
                }
            }
            case nessuno -> categoria;
        };

        return paragrafo;
    }


    /**
     * Aggiunge le ex-attività NON presenti nel modulo 'Modulo:Bio/Plurale attività' <br>
     * Le recupera dal modulo 'Modulo:Bio/Plurale attività genere' <br>
     * Le aggiunge se trova la corrispondenza tra il nome con e senza EX <br>
     */
    private void aggiunge() {
        List<Genere> listaEx = genereBackend.findStartingEx();
        String attivitaSingolare;
        String genereSingolare;
        Attivita entity;
        String message;
        int size = 0;
        String singolare;
        String categoria;
        String paragrafo;
        AETypeGenere typeGenere;

        if (listaEx == null || listaEx.size() == 0) {
            message = "Il modulo genere deve essere scaricato PRIMA di quello di attività";
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
            return;
        }

        if (listaEx != null) {
            for (Genere genere : listaEx) {
                entity = null;
                attivitaSingolare = VUOTA;
                genereSingolare = genere.singolare;

                if (genereSingolare.startsWith(TAG_EX)) {
                    attivitaSingolare = genereSingolare.substring(TAG_EX.length());
                }
                if (genereSingolare.startsWith(TAG_EX2)) {
                    attivitaSingolare = genereSingolare.substring(TAG_EX2.length());
                }

                if (textService.isValid(attivitaSingolare)) {
                    entity = findFirstBySingolare(attivitaSingolare);
                }

                if (entity != null) {
                    singolare = entity.singolare;
                    categoria = entity.categoria;
                    paragrafo = categoria;
                    typeGenere = AETypeGenere.maschile;
                    if (creaIfNotExist(singolare, categoria, paragrafo, typeGenere, true) != null) {
                        size++;
                    }
                    else {
                        logger.info(new WrapLog().message(genereSingolare));
                    }
                }
            }
        }
        message = String.format("Aggiunte %s ex-attività dalla collection genere", textService.format(size));
        logger.info(new WrapLog().message(message));
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void elabora() {
        long inizio = System.currentTimeMillis();
        int numBio;
        int numSingolari;
        int soglia = WPref.sogliaAttNazWiki.getInt();
        int cont = 0;
        String size;
        String time;
        int tot = count();

        for (Attivita attivita : findAll()) {
            attivita.numBio = 0;
            update(attivita);
        }

        //--Spazzola tutte le attività distinte plurali (circa 657)
        //--Per ognuna recupera le attività singolari
        //--Per ognuna attività singolare calcola quante biografie la usano (in 1 o 3 parametri)
        //--Memorizza e registra il dato nella entityBean
        for (Attivita attivita : findAttivitaDistinctByPlurali()) {
            numBio = 0;
            numSingolari = 0;

            for (String singolare : findSingolariByPlurale(attivita.paragrafo)) {
                numBio += bioBackend.countAttivitaAll(singolare);
                numSingolari++;
            }

            for (Attivita attivitaOK : findAllByPlurale(attivita.paragrafo)) {
                attivitaOK.numBio = numBio;
                attivitaOK.superaSoglia = numBio >= soglia ? true : false;
                attivitaOK.esistePagina = esistePagina(attivitaOK.paragrafo);
                attivitaOK.numSingolari = numSingolari;
                update(attivitaOK);

                if (Pref.debug.is()) {
                    cont++;
                    if (mathService.multiploEsatto(100, cont)) {
                        size = textService.format(cont);
                        time = dateService.deltaText(inizio);
                        message = String.format("Finora controllata l'esistenza di %s/%s liste di attività, in %s", size, tot, time);
                        logger.info(new WrapLog().message(message).type(AETypeLog.elabora));
                    }
                }
            }
        }

        super.fixElaboraMinuti(inizio, "attività");
    }

    /**
     * Esegue un azione di upload, specifica del programma/package in corso <br>
     */
    public void uploadAll() {
        WResult result;
        long inizio = System.currentTimeMillis();
        int sottoSoglia = 0;
        int daCancellare = 0;
        int modificate = 0;
        int nonModificate = 0;
        List<String> listaPluraliUnici = findAllPlurali();
        this.fixNext();

        for (String pluraleAttivita : listaPluraliUnici) {
            result = uploadPagina(pluraleAttivita);
            if (result.isValido()) {
                if (result.isModificata()) {
                    modificate++;
                }
                else {
                    nonModificate++;
                }
            }
            else {
                sottoSoglia++;
                if (result.getErrorCode().equals(KEY_ERROR_CANCELLANDA)) {
                    daCancellare++;
                }
            }
        }
        super.fixUploadMinuti(inizio, sottoSoglia, daCancellare, nonModificate, modificate, "attività");
    }

    /**
     * Controlla l'esistenza della pagina wiki relativa a questa attività (lista) <br>
     */
    public boolean esistePagina(String pluraleAttivita) {
        String wikiTitle = "Progetto:Biografie/Attività/" + textService.primaMaiuscola(pluraleAttivita);
        return appContext.getBean(QueryExist.class).isEsiste(wikiTitle);
    }

    /**
     * Scrive una pagina definitiva sul server wiki <br>
     */
    public WResult uploadPagina(String pluraleAttivitaMinuscola) {
        WResult result = WResult.errato();
        String message;
        int numVoci = bioBackend.countAttivitaPlurale(pluraleAttivitaMinuscola);
        String voci = textService.format(numVoci);
        String pluraleAttivitaMaiuscola = textService.primaMaiuscola(pluraleAttivitaMinuscola);
        int soglia = WPref.sogliaAttNazWiki.getInt();
        String wikiTitle = "Progetto:Biografie/Attività/" + pluraleAttivitaMaiuscola;

        if (numVoci > soglia) {
            result = appContext.getBean(UploadAttivita.class).upload(pluraleAttivitaMinuscola);
            if (result.isValido()) {
                if (result.isModificata()) {
                    message = String.format("Lista %s utilizzati in %s voci biografiche", pluraleAttivitaMinuscola, voci);
                }
                else {
                    message = String.format("Attività %s utilizzata in %s voci biografiche. %s", pluraleAttivitaMinuscola, voci, result.getValidMessage());
                }
                if (Pref.debug.is()) {
                    logger.info(new WrapLog().message(message).type(AETypeLog.upload));
                }
            }
            else {
                logger.warn(new WrapLog().message(result.getErrorMessage()).type(AETypeLog.upload));
            }
        }
        else {
            message = String.format("L'attività %s ha solo %s voci biografiche e non raggiunge il numero necessario per avere una pagina dedicata", pluraleAttivitaMinuscola, voci);
            if (Pref.debug.is()) {
                result.setErrorMessage(message).setValido(false);
                logger.info(new WrapLog().message(message).type(AETypeLog.upload));
            }
            if (esistePagina(pluraleAttivitaMinuscola)) {
                result.setErrorCode(KEY_ERROR_CANCELLANDA);
                message = String.format("Esiste la pagina %s che andrebbe cancellata", wikiTitle);
                logger.warn(new WrapLog().message(message).type(AETypeLog.upload).usaDb());
            }
        }

        return result;
    }

    public void fixNext() {
        LocalDateTime adesso = LocalDateTime.now();
        LocalDateTime prossimo = adesso.plusDays(7);
        WPref.uploadAttivitaPrevisto.setValue(prossimo);
    }

}// end of crud backend class
