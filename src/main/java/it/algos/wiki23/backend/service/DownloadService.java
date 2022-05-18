package it.algos.wiki23.backend.service;

import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
        List<Long> listaPageIds = null;
        List<MiniWrap> listaMiniWrap = null;
        List<Long> listaPageIdsDaLeggere = null;
        List<WrapBio> listaWrapBio = null;
        String bio = "bio";

        //--Controlla quante pagine ci sono nella categoria
        //--Si collega come anonymous; non serve essere loggati <br>
        checkCategoria(categoryTitle);

        //--Controlla il collegamento come bot
        checkBot();

        //--Parte dalla lista di tutti i (long) pageIds della categoria
        //--Deve riuscire a gestire una lista di circa 500.000 long per la category BioBot
        //--Tempo medio previsto = circa 1 minuto (come bot la categoria legge 5.000 pagine per volta)
        //--Nella listaPageIds possono esserci anche voci SENZA il tmpl BIO, che verranno scartate dopo
        listaPageIds = queryService.getListaPageIds(categoryTitle);

                //--Usa la lista di pageIds e recupera una lista (stessa lunghezza) di miniWrap
                //--Deve riuscire a gestire una lista di circa 435.000 long per la category BioBot
                //--Tempo medio previsto = circa 20 minuti  (come bot la query legge 500 pagine per volta
                listaMiniWrap = appContext.getBean(QueryTimestamp.class).urlRequest(listaPageIds).getLista();
                //--Nella listaMiniWrap possono esserci anche voci SENZA il tmpl BIO, che verranno scartate dopo

        //        //--Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere
        //        //--Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb
        //        //--Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica
        //        //--A regime deve probabilmente gestire una lista di circa 10.000 miniWrap
        //        //--si tratta delle voci nuove e di quelle modificate nelle ultime 24 ore
        //        try {
        //            listaPageIdsDaLeggere = mongo.isExistsCollection(bio) ? wikiBot.elaboraMiniWrap(listaMiniWrap) : listaPageIds;
        //        } catch (AlgosException unErrore) {
        //            logger.info(String.format("Manca la collection %s nel database MongoDB", bio));
        //        }
        //        //--Nella listaPageIdsDaLeggere possono esserci anche voci SENZA il tmpl BIO, che verranno scartate dopo

        //        //--Legge tutte le pagine
        //        //--Recupera i contenuti di tutte le voci biografiche da creare/modificare
        //        //--Controlla che esiste il tmpl BIO <br>
        //        listaWrapBio = appContext.getBean(QueryPages.class).urlRequest(listaPageIdsDaLeggere).getLista();
        //        //--Nella listaWrapBio possono ci sono solo voci CON il tmpl BIO valido

        //        //--Crea/aggiorna le voci biografiche
        //        //--Salva le entities Bio su mongoDB
        //        //--Elabora (e salva) le entities Bio
        //        creaElaboraListaBio(listaWrapBio);
        //
        //        super.fixDataDownload();
    }


    /**
     * Legge (anche come anonymous) il numero di pagine di una categoria wiki <br>
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
        logger.info(new WrapLog().message(message).usaDb().type(AETypeLog.download));

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
            message = String.format("Regolarmente collegato come %s di nick '%s'", botLogin.getUserType(), botLogin.getUsername());
        }
        else {
            message = String.format("Collegato come %s di nick '%s' e NON come bot", botLogin.getUserType(), botLogin.getUsername());
        }
        logger.info(new WrapLog().message(message).usaDb().type(AETypeLog.download));
        return status;
    }

}