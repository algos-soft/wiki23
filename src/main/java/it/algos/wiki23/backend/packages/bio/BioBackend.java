package it.algos.wiki23.backend.packages.bio;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.wiki.*;
import it.algos.wiki23.backend.wrapper.*;
import org.bson.*;
import org.checkerframework.checker.units.qual.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * Project wiki
 * Created by Algos
 * User: gac
 * Date: gio, 28-apr-2022
 * Time: 11:57
 * <p>
 * Service di una entityClazz specifica e di un package <br>
 * Garantisce i metodi di collegamento per accedere al database <br>
 * Non mantiene lo stato di una istanza entityBean <br>
 * Mantiene lo stato della entityClazz <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * NOT annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (inutile, esiste già @Service) <br>
 */
@Service
public class BioBackend extends WikiBackend {

    public BioRepository repository;

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
    public BioBackend(@Autowired @Qualifier(TAG_BIO) final MongoRepository crudRepository) {
        super(crudRepository, Bio.class);
        this.repository = (BioRepository) crudRepository;
    }


    public Bio creaIfNotExist(final WrapBio wrap) {
        return checkAndSave(newEntity(wrap));
    }


    public Bio checkAndSave(Bio bio) {
        return isExist(bio.pageId) ? null : repository.insert(elaboraService.esegue(bio));
    }

    public boolean isExist(final long pageId) {
        return repository.findFirstByPageId(pageId) != null;
    }

    public Bio fixWrap(final String keyID, final WrapBio wrap) {
        Bio bio = newEntity(wrap);
        bio.setId(keyID);
        return bio;
    }

    public Bio newEntity() {
        return newEntity(0, VUOTA, VUOTA, null);
    }

    public Bio newEntity(final WrapBio wrap) {
        String message;
        if (wrap.isValida()) {
            return newEntity(wrap.getPageid(), wrap.getTitle(), wrap.getTemplBio());
        }
        else {
            message = "wrap non valido";
            if (wrap.getPageid() < 1) {
                message = "Manca il pageid";
            }
            if (textService.isEmpty(wrap.getTitle())) {
                message = "Manca il wikiTitle";
            }
            logger.info(new WrapLog().exception(new AlgosException(message)).usaDb());
            return null;
        }
    }

    public Bio newEntity(final long pageId, final String wikiTitle, final String tmplBio) {
        return newEntity(pageId, wikiTitle, tmplBio, null);
    }

    public Bio newEntity(final Document doc) {
        return Bio.builder()
                .pageId(doc.getLong("pageId"))
                .wikiTitle(doc.getString("wikiTitle"))
                .elaborato(doc.getBoolean("elaborato"))
                .nome(doc.getString("nome"))
                .cognome(doc.getString("cognome"))
                .ordinamento(doc.getString("ordinamento"))
                .sesso(doc.getString("sesso"))
                .giornoNato(doc.getString("giornoNato"))
                .giornoNatoOrd(doc.getInteger("giornoNatoOrd"))
                .annoNato(doc.getString("annoNato"))
                .annoNatoOrd(doc.getInteger("annoNatoOrd"))
                .luogoNato(doc.getString("luogoNato"))
                .luogoNatoLink(doc.getString("luogoNatoLink"))
                .giornoMorto(doc.getString("giornoMorto"))
                .giornoMortoOrd(doc.getInteger("giornoMortoOrd"))
                .annoMorto(doc.getString("annoMorto"))
                .annoMortoOrd(doc.getInteger("annoMortoOrd"))
                .luogoMorto(doc.getString("luogoMorto"))
                .luogoMortoLink(doc.getString("luogoMortoLink"))
                .attivita(doc.getString("attivita"))
                .attivita2(doc.getString("attivita2"))
                .attivita3(doc.getString("attivita3"))
                .nazionalita(doc.getString("nazionalita"))
                .build();
    }

    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param pageId     di riferimento (obbligatorio, unico)
     * @param wikiTitle  di riferimento (obbligatorio, unico)
     * @param tmplBio    (obbligatorio, unico)
     * @param lastServer sul server wiki (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata e senza keyID)
     */
    public Bio newEntity(final long pageId, final String wikiTitle, final String tmplBio, final LocalDateTime lastServer) {
        LocalDateTime now = LocalDateTime.now();
        return Bio.builder()
                .pageId(pageId)
                .wikiTitle(textService.isValid(wikiTitle) ? wikiTitle : null)
                .tmplBio(textService.isValid(tmplBio) ? tmplBio : null)
                .elaborato(false)
                .lastServer(lastServer != null ? lastServer : now)
                .lastMongo(now)
                .valido(true)
                .build();
    }

    public Bio insert(final Bio bio) {
        return repository.insert(bio);
    }

    @Override
    public Bio save(final Object entity) {
        Bio bioTemp;

        if (entity instanceof Bio bio) {

            if (textService.isEmpty(bio.id) && bio.pageId > 0) {
                bioTemp = findByKey(bio.pageId);
                if (bioTemp != null) {
                    bio.id = bioTemp.id;
                }
            }

            if (!bio.errato) {
                bio.errore = null;
            }
            if (bio.errore == null) {
                bio.errato = false;
            }

            if (isExist(bio.pageId)) {
                try {
                    repository.save(bio);
                } catch (Exception unErrore) {
                    logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
                    return null;
                }
            }
            else {
                try {
                    repository.insert(bio);
                } catch (Exception unErrore) {
                    logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
                    return null;
                }
            }
            return bio;
        }

        return null;
    }

    @Override
    public Bio update(Object entityBean) {
        if (entityBean instanceof Bio bio) {
            if (isExist(bio.pageId)) {
                repository.save(bio);
            }
            else {
                //                repository.insert(bio);
            }
            return null;
        }

        return null;
    }

    /**
     * Conta tutte le biografie con una serie di attività plurali. <br>
     *
     * @param attivitaPlurale
     *
     * @return conteggio di biografie che la usano
     */
    public int countAttivitaPlurale(final String attivitaPlurale) {
        int numBio = 0;
        List<String> listaNomi = attivitaBackend.findSingolariByPlurale(attivitaPlurale);

        for (String singolare : listaNomi) {
            numBio += countAttivitaAll(singolare);
        }

        return numBio;
    }


    /**
     * Conta tutte le biografie con una serie di attività singolari. <br>
     *
     * @param attivitaSingola
     *
     * @return conteggio di biografie che la usano
     */
    public int countAttivitaAll(final String attivitaSingola) {
        int numBio = 0;
        Long attivitaUno = repository.countBioByAttivita(attivitaSingola);
        Long attivitaDue;
        Long attivitaTre;

        numBio = attivitaUno.intValue();

        if (WPref.usaTreAttivita.is()) {
            attivitaDue = repository.countBioByAttivita2(attivitaSingola);
            attivitaTre = repository.countBioByAttivita3(attivitaSingola);
            numBio += attivitaDue.intValue() + attivitaTre.intValue();
        }

        return numBio;
    }

    public int countAttivita(final String attivitaSingola) {
        Long numBio = repository.countBioByAttivita(attivitaSingola);
        return numBio > 0 ? numBio.intValue() : 0;
    }

    public int countAttivitaDue(final String attivitaSingola) {
        Long numBio = repository.countBioByAttivita2(attivitaSingola);
        return numBio > 0 ? numBio.intValue() : 0;
    }

    public int countAttivitaTre(final String attivitaSingola) {
        Long numBio = repository.countBioByAttivita3(attivitaSingola);
        return numBio > 0 ? numBio.intValue() : 0;
    }

    public int countAttivitaNazionalita(final String attivitaSingolare, final String nazionalitaSingolare) {
        Long numBio = repository.countBioByAttivitaAndNazionalita(attivitaSingolare, nazionalitaSingolare);
        return numBio > 0 ? numBio.intValue() : 0;
    }

    /**
     * Conta tutte le biografie incrociate di un'attività plurale con una nazionalità plurale. <br>
     * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     *
     * @param attivitaSingolarePlurale    da controllare/convertire in plurale
     * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
     *
     * @return conteggio di biografie che usano l'attività e la nazionalità
     */
    public int countAttivitaNazionalitaAll(final String attivitaSingolarePlurale, final String nazionalitaSingolarePlurale) {
        return countAttivitaNazionalitaAll(attivitaSingolarePlurale, nazionalitaSingolarePlurale, VUOTA);
    }

    /**
     * Conta tutte le biografie incrociate di un'attività plurale con una nazionalità plurale. <br>
     * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     *
     * @param attivitaSingolarePlurale    da controllare/convertire in plurale
     * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
     * @param letteraIniziale             della (eventuale) sottoSottoPagina
     *
     * @return conteggio di biografie che usano l'attività e la nazionalità
     */
    public int countAttivitaNazionalitaAll(String attivitaSingolarePlurale, String nazionalitaSingolarePlurale, String letteraIniziale) {
        int numBio = 0;
        List<String> listaAttivita;
        List<String> listaNazionalita;
        String attivitaPlurale = attivitaBackend.pluraleBySingolarePlurale(attivitaSingolarePlurale);
        String nazionalitaPlurale = nazionalitaBackend.pluraleBySingolarePlurale(nazionalitaSingolarePlurale);

        listaAttivita = attivitaBackend.findSingolariByPlurale(attivitaPlurale);
        listaNazionalita = nazionalitaBackend.findSingolariByPlurale(nazionalitaPlurale);

        for (String attivitaSingola : listaAttivita) {
            for (String nazionalitaSingola : listaNazionalita) {
                numBio += countAttivitaNazionalita(attivitaSingola, nazionalitaSingola);
            }
        }

        if (textService.isValid(letteraIniziale)) {
            numBio = findAllAttivitaNazionalita(attivitaSingolarePlurale, nazionalitaSingolarePlurale, letteraIniziale).size();
        }

        return numBio;
    }


    public int countNazionalitaAttivita(final String nazionalita, final String attivitaSingolare) {
        Long numBio = repository.countBioByNazionalitaAndAttivita(nazionalita, attivitaSingolare);
        return numBio > 0 ? numBio.intValue() : 0;
    }


    /**
     * Conta tutte le biografie con una serie di nazionalità plurali. <br>
     *
     * @param nazionalitaPlurale
     *
     * @return conteggio di biografie che la usano
     */
    public int countNazionalitaPlurale(final String nazionalitaPlurale) {
        int numBio = 0;
        List<String> listaNomi = nazionalitaBackend.findSingolariByPlurale(nazionalitaPlurale);

        for (String singolare : listaNomi) {
            numBio += countNazionalita(singolare);
        }

        return numBio;
    }

    /**
     * Conta tutte le biografie con una serie di nazionalita. <br>
     *
     * @param nazionalitaSingola singola
     *
     * @return conteggio di biografie che la usano
     */
    public int countNazionalita(final String nazionalitaSingola) {
        Long nazLong = repository.countBioByNazionalita(nazionalitaSingola);
        return nazLong.intValue();
    }

    public int countGiornoNato(final String giornoNato) {
        Long giornoLong = repository.countBioByGiornoNato(giornoNato);
        return giornoLong.intValue();
    }


    public int countGiornoMorto(final String giornoNato) {
        Long giornoLong = repository.countBioByGiornoMorto(giornoNato);
        return giornoLong.intValue();
    }


    public int countAnnoNato(final String annoNato) {
        Long annoLong = repository.countBioByAnnoNato(annoNato);
        return annoLong.intValue();
    }


    public int countAnnoMorto(final String annoMorto) {
        Long annoLong = repository.countBioByAnnoMorto(annoMorto);
        return annoLong.intValue();
    }

    public Bio findByKey(final long pageId) {
        return repository.findFirstByPageId(pageId);
    }

    public Bio findByTitle(final String wikiTitle) {
        return repository.findFirstByWikiTitle(wikiTitle);
    }

    public List<Long> findOnlyPageId() {
        return mongoService.projectionLong(Bio.class, "pageId");
    }

    public List<Bio> findAllAll() {
        return super.findAll();
    }

    public List<Bio> findAll() {
        return findSenzaTmpl();
    }

    public List<Bio> fetchErrori() {
        return repository.findAllByErrato(true);
    }

    public List<Bio> findAttivitaNazionalita(final String attivitaSingolare, final String nazionalitaSingolare) {
        return repository.findAllByAttivitaAndNazionalitaOrderByOrdinamento(attivitaSingolare, nazionalitaSingolare);
    }


    /**
     * Recupera tutte le biografie incrociate di un'attività plurale con una nazionalità plurale. <br>
     * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
     *
     * @param attivitaSingolarePlurale    da controllare/convertire in plurale
     * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
     *
     * @return Lista di biografie che usano l'attività e la nazionalità
     */
    public List<Bio> findAllAttivitaNazionalita(String attivitaSingolarePlurale, String nazionalitaSingolarePlurale) {
        return findAllAttivitaNazionalita(attivitaSingolarePlurale, nazionalitaSingolarePlurale,VUOTA);
    }

        /**
         * Recupera tutte le biografie incrociate di un'attività plurale con una nazionalità plurale. <br>
         * L'attività può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
         * La nazionalità può essere espressa direttamente come plurale oppure come singolare e ne viene ricavato il plurale <br>
         *
         * @param attivitaSingolarePlurale    da controllare/convertire in plurale
         * @param nazionalitaSingolarePlurale da controllare/convertire in plurale
         * @param letteraIniziale             della (eventuale) sottoSottoPagina
         *
         * @return Lista di biografie che usano l'attività e la nazionalità
         */
    public List<Bio> findAllAttivitaNazionalita(String attivitaSingolarePlurale, String nazionalitaSingolarePlurale, String letteraIniziale) {
        List<Bio> lista = new ArrayList<>();
        List<String> listaAttivita;
        List<String> listaNazionalita;
        String attivitaPlurale = attivitaBackend.pluraleBySingolarePlurale(attivitaSingolarePlurale);
        String nazionalitaPlurale = nazionalitaBackend.pluraleBySingolarePlurale(nazionalitaSingolarePlurale);

        listaAttivita = attivitaBackend.findSingolariByPlurale(attivitaPlurale);
        listaNazionalita = nazionalitaBackend.findSingolariByPlurale(nazionalitaPlurale);

        for (String attivitaSingola : listaAttivita) {
            for (String nazionalitaSingola : listaNazionalita) {
                lista.addAll(findAttivitaNazionalita(attivitaSingola, nazionalitaSingola));
            }
        }

        if (textService.isValid(letteraIniziale)) {
            lista = lista
                    .stream()
                    .filter(bio -> (textService.isValid(bio.cognome)&&bio.cognome.startsWith(letteraIniziale)))
                    .collect(Collectors.toList());
        }

        return bioService.sortByForzaOrdinamento(lista);
    }

    /**
     * Controlla l'esistenza della property <br>
     * La lista funziona anche se la property del sort è errata <br>
     * Ma ovviamente il sort non viene effettuato <br>
     *
     * @param sort
     */
    @Override
    public List<Bio> findAll(Sort sort) {
        if (sort == null) {
            return findSenzaTmpl();
        }
        else {
            return findSenzaTmpl(sort);
        }

    }

    public List<Bio> findSenzaTmpl() {
        return mongoService.projectionExclude(Bio.class, this, "tmplBio");
    }

    public List<Bio> findSenzaTmpl(Sort sort) {
        Document doc = null;

        //        if (sort == null) {
        doc = new Document("ordinamento", 1);
        //        }

        return mongoService.projectionExclude(Bio.class, this, doc, "tmplBio");
    }

    public List<Bio> findSenzaTmpl(Document doc) {
        return mongoService.projectionExclude(Bio.class, this, doc, "tmplBio");
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void elabora() {
        long inizio = System.currentTimeMillis();
        List<Bio> lista = findAll();
        int dim = 50000;
        int blocco = lista.size() / dim;
        int ini;
        int end;
        String size;
        String time;
        String message;
        int cont = 0;
        Bio bioTemp;
        Bio bioSaved;

        for (int k = 0; k <= blocco; k++) {
            ini = k * dim;
            end = Math.min(ini + dim, lista.size());
            if (Pref.debug.is()) {
                logger.info(new WrapLog().message(String.format("Ini %s - End %s", ini, end)).type(AETypeLog.elabora));
            }

            for (Bio bio : lista.subList(ini, end)) {
                bioTemp = this.findByTitle(bio.wikiTitle);

                bioSaved = elaboraService.esegueSave(bioTemp);
                cont++;
            }

            if (Pref.debug.is()) {
                size = textService.format(cont);
                time = dateService.deltaText(inizio);
                message = String.format("Elaborate finora %s voci biografiche, in %s", size, time);
                logger.info(new WrapLog().message(message).type(AETypeLog.elabora));
            }
        }
        super.fixElaboraMinuti(inizio, "biografie");
    }

    /**
     * Esegue un azione di elaborazione, specifica del programma/package in corso <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void errori() {
        resetErrori();
        fixErroriSesso();
        fixMancaOrdinamento();
    }

    public void resetErrori() {
        mongoService.mongoOp.updateMulti(new Query(), Update.update("errato", false), Bio.class);
    }

    public int countErrori() {
        return ((Long) repository.countBioByErratoIsTrue()).intValue();
    }

    public int countSessoMancante() {
        return ((Long) repository.countBioBySessoIsNull()).intValue();
    }

    public int countSessoLungo() {
        Long lungo = null;
        Query query = new Query();

        query.addCriteria(Criteria.where("sesso").regex(".{2,}"));
        lungo = mongoService.mongoOp.count(query, Bio.class);

        return lungo != null ? lungo.intValue() : 0;
    }

    public int countSessoErrato() {
        Long lungo = null;
        Query query = new Query();

        query.addCriteria(Criteria.where("sesso").regex("[^M^F]{1}"));
        lungo = mongoService.mongoOp.count(query, Bio.class);

        return lungo != null ? lungo.intValue() : 0;
    }

    public int countOrdinamento() {
        return ((Long) repository.countBioByOrdinamentoIsNull()).intValue();
    }

    public List<Bio> getSessoLungo() {
        List<Bio> listaLunghe = null;
        Query query = new Query();

        query.addCriteria(Criteria.where("sesso").regex(".{2,}"));
        listaLunghe = mongoService.mongoOp.find(query, Bio.class);

        return listaLunghe;
    }

    public List<Bio> getSessoErrato() {
        List<Bio> listaLunghe = null;
        Query query = new Query();

        query.addCriteria(Criteria.where("sesso").regex("[^M^F]{1}"));
        listaLunghe = mongoService.mongoOp.find(query, Bio.class);

        return listaLunghe;
    }

    public int countNazionalitaGenere() {
        return ((Long) repository.countBioByErroreIs(AETypeBioError.nazionalitaGenere)).intValue();
    }

    public void fixErroriSesso() {
        int totali;
        int nulli;
        int maschi;
        int femmine;
        List<Bio> lista;

        totali = ((Long) repository.count()).intValue();
        nulli = ((Long) repository.countBioBySessoIsNull()).intValue();
        maschi = ((Long) repository.countBioBySessoEquals("M")).intValue();
        femmine = ((Long) repository.countBioBySessoEquals("F")).intValue();
        totali = nulli + maschi + femmine;

        lista = repository.findBySessoIsNull();
        for (Bio bio : lista) {
            bio.errato = true;
            bio.errore = AETypeBioError.sessoMancante;
            save(bio);
        }

        lista = getSessoLungo();
        for (Bio bio : lista) {
            bio.errato = true;
            bio.errore = AETypeBioError.sessoLungo;
            save(bio);
        }

        lista = getSessoErrato();
        for (Bio bio : lista) {
            bio.errato = true;
            bio.errore = AETypeBioError.sessoErrato;
            save(bio);
        }
    }

    public void fixMancaOrdinamento() {
        List<Bio> lista;

        lista = repository.findByOrdinamentoIsNull();
        for (Bio bio : lista) {
            bio.errato = true;
            bio.errore = AETypeBioError.mancaOrdinamento;
            save(bio);
        }
    }

}// end of crud backend class
