package it.algos.wiki23.backend.service;

import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.wrapper.*;
import it.algos.wiki23.wiki.query.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;


/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mer, 18-mag-2022
 * Time: 19:35
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(DownloadService.class); <br>
 * 3) @Autowired public DownloadService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DownloadService extends WAbstractService {

    public void ciclo() {
        ciclo(WPref.categoriaBio.getStr());
    }

    /**
     * Ciclo di download <br>
     * Parte dalla lista di tutti i (long) pageIds della categoria <br>
     * Usa la lista di pageIds e si recupera una lista (stessa lunghezza) di miniWrap <br>
     * Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere <br>
     */
    public void ciclo(String categoryTitle) {
        List<Long> listaPageIds;
        List<MiniWrap> listaMiniWrap;
        List<Long> listaPageIdsDaLeggere;
        List<WrapBio> listaWrapBio;

        //--Controlla quante pagine ci sono nella categoria
        checkCategoria(categoryTitle);

        //--Controlla il collegamento come bot
        checkBot();

        //--Crea la lista di tutti i (long) pageIds della categoria
        listaPageIds = getListaPageIds(categoryTitle);

        //--Usa la lista di pageIds e recupera una lista (stessa lunghezza) di miniWrap
        listaMiniWrap = getListaMiniWrap(listaPageIds);

        //--Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere
        listaPageIdsDaLeggere = elaboraMiniWrap(listaMiniWrap);

        //--Legge tutte le pagine
        listaWrapBio = getListaWrapBio(listaPageIdsDaLeggere);

        //--Crea/aggiorna le voci biografiche <br>
        creaElaboraListaBio(listaWrapBio);

        WPref.downloadBio.setValue(LocalDateTime.now());
    }


    /**
     * Legge (anche come anonymous) il numero di pagine di una categoria wiki <br>
     * Si collega come anonymous; non serve essere loggati <br>
     * Serve però per le operazioni successive <br>
     *
     * @param categoryTitle da controllare
     *
     * @return numero di pagine (subcategorie escluse)
     */
    public int checkCategoria(final String categoryTitle) {
        int numPages = queryService.getSizeCat(categoryTitle);
        String message;

        if (numPages > 0) {
            message = String.format("La categoria [%s] esiste e ci sono %s voci", categoryTitle, textService.format(numPages));
        }
        else {
            message = String.format("La categoria [%s] non esiste oppure è vuota", categoryTitle);
        }
        logger.info(new WrapLog().message(message).usaDb().type(AETypeLog.bio));

        return numPages;
    }


    /**
     * Controlla il collegamento come bot <br>
     *
     * @return true se collegato come bot
     */
    public boolean checkBot() {
        boolean status = false;
        String message;

        if (botLogin != null) {
            status = switch (botLogin.getUserType()) {
                case anonymous, user, admin -> false;
                case bot -> true;
                default -> false;
            };
        }

        if (status) {
        }
        else {
            message = String.format("Collegato come %s di nick '%s' e NON come bot", botLogin.getUserType(), botLogin.getUsername());
            logger.info(new WrapLog().message(message).usaDb().type(AETypeLog.bio));
        }
        return status;
    }


    /**
     * Crea la lista di tutti i (long) pageIds della categoria <br>
     * Deve riuscire a gestire una lista di circa 500.000 long per la category BioBot <br>
     * Tempo medio previsto = circa 1 minuto (come bot la categoria legge 5.000 pagine per volta) <br>
     * Nella listaPageIds possono esserci anche voci SENZA il tmpl BIO, che verranno scartate dopo <br>
     *
     * @param categoryTitle da controllare
     *
     * @return lista di tutti i (long) pageIds
     */
    public List<Long> getListaPageIds(final String categoryTitle) {
        long inizio = System.currentTimeMillis();
        String size;
        String time;

        List<Long> listaPageIds = queryService.getListaPageIds(categoryTitle);

        size = textService.format(listaPageIds.size());
        time = dateService.deltaTextEsatto(inizio);
        String message = String.format("Recuperati %s pageIds dalla categoria '%s' in %s", size, categoryTitle, time);
        logger.info(new WrapLog().message(message).usaDb().type(AETypeLog.bio));

        return listaPageIds;
    }


    /**
     * Usa la lista di pageIds e recupera una lista (stessa lunghezza) di miniWrap <br>
     * Deve riuscire a gestire una lista di circa 500.000 long per la category BioBot <br>
     * Tempo medio previsto = circa 20 minuti (come bot la query legge 500 pagine per volta) <br>
     * Nella listaMiniWrap possono esserci anche voci SENZA il tmpl BIO, che verranno scartate dopo <br>
     *
     * @param listaPageIds di tutti i (long) pageIds
     *
     * @return lista di tutti i miniWraps
     */
    public List<MiniWrap> getListaMiniWrap(final List<Long> listaPageIds) {
        long inizio = System.currentTimeMillis();
        String size;
        String time;

        List<MiniWrap> listaMiniWrap = queryService.getMiniWrap(listaPageIds);

        size = textService.format(listaPageIds.size());
        time = dateService.deltaTextEsatto(inizio);
        String message = String.format("Creati %s miniWrap dai corrispondenti pageIds in %s", size, time);
        logger.info(new WrapLog().message(message).usaDb().type(AETypeLog.bio));

        return listaMiniWrap;
    }


    /**
     * Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere <br>
     * Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb <br>
     * Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica <br>
     * A regime deve probabilmente gestire una lista di circa 10.000 miniWrap
     * si tratta delle voci nuove e di quelle modificate nelle ultime 24 ore <br>
     * Nella listaPageIdsDaLeggere possono esserci anche voci SENZA il tmpl BIO, che verranno scartate dopo <br>
     *
     * @param listaMiniWrap con il pageIds e lastModifica
     *
     * @return listaPageIdsDaLeggere
     */
    public List<Long> elaboraMiniWrap(final List<MiniWrap> listaMiniWrap) {
        long inizio = System.currentTimeMillis();
        String size;
        String time;

        List<Long> listaPageIdsDaLeggere = wikiBotService.elaboraMiniWrap(listaMiniWrap);

        size = textService.format(listaMiniWrap.size());
        time = dateService.deltaTextEsatto(inizio);
        String message = String.format("Elaborati %s miniWrap per controllare lastModifica in %s", size, time);
        logger.info(new WrapLog().message(message).usaDb().type(AETypeLog.bio));

        return listaPageIdsDaLeggere;
    }


    /**
     * Legge tutte le pagine <br>
     * Recupera i contenuti di tutte le voci biografiche da creare/modificare <br>
     * Controlla che esiste il tmpl BIO <br>
     * Nella listaWrapBio possono ci sono solo voci CON il tmpl BIO valido <br>
     *
     * @param listaPageIdsDaLeggere dal server wiki
     *
     * @return listaWrapBio
     */
    public List<WrapBio> getListaWrapBio(final List<Long> listaPageIdsDaLeggere) {
        long inizio = System.currentTimeMillis();
        String size;
        String time;

        List<WrapBio> listaWrapBio = appContext.getBean(QueryWrapBio.class).getWrap(listaPageIdsDaLeggere);

        size = textService.format(listaPageIdsDaLeggere.size());
        time = dateService.deltaTextEsatto(inizio);
        String message = String.format("Scaricati %s wrapBio dal server in %s", size, time);
        logger.info(new WrapLog().message(message).usaDb().type(AETypeLog.bio));

        return listaWrapBio;
    }


    /**
     * Crea/aggiorna le voci biografiche <br>
     * Salva le entities Bio su mongoDB <br>
     * Elabora (e salva) le entities Bio <br>
     * Nella listaWrapBio possono ci sono solo voci CON il tmpl BIO valido <br>
     *
     * @param listaWrapBio da elaborare e salvare
     */
    public void creaElaboraListaBio(final List<WrapBio> listaWrapBio) {
        long inizio = System.currentTimeMillis();
        String message;
        String size;
        String time;
        int modificate = 0;

        if (listaWrapBio != null && listaWrapBio.size() > 0) {
            for (WrapBio wrap : listaWrapBio) {
                modificate = creaElaboraBio(wrap) ? modificate + 1 : modificate;
            }

            size = textService.format(modificate);
            time = dateService.deltaTextEsatto(inizio);
            message = String.format("Create o aggiornate %s biografie in %s", size, time);
            logger.info(new WrapLog().message(message).usaDb().type(AETypeLog.bio));
        }
        else {
            message = "Nessuna voce da aggiungere/modificare";
            logger.info(new WrapLog().message(message).usaDb().type(AETypeLog.bio));
        }
    }


    /**
     * Crea/aggiorna una singola entity <br>
     */
    public boolean creaElaboraBio(WrapBio wrap) {
        Bio bio;

        if (wrap != null && wrap.isValida()) {
            bio = bioBackend.newEntity(wrap);
            bio = elaboraService.esegue(bio);
            bioBackend.save(bio);
            return true;
        }

        return false;
    }

}