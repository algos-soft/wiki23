package it.algos.wiki23.backend.service;

import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.service.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.bio.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: sab, 30-apr-2022
 * Time: 18:56
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ElaboraService.class); <br>
 * 3) @Autowired public ElaboraService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ElaboraService extends AbstractService {


    /**
     * Elabora la singola voce biografica <br>
     * Estrae dal tmplBioServer i singoli parametri previsti nella enumeration ParBio <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile e utilizzabile per le liste <br>
     */
    public Bio esegue(Bio bio) {

        //--Recupera i valori base di tutti i parametri dal tmplBioServer
        LinkedHashMap<String, String> mappa = bioService.estraeMappa(bio);

        //--Elabora valori validi dei parametri significativi
        //--Inserisce i valori nella entity Bio
        if (mappa != null) {
            setValue(bio, mappa);
        }

        return bio;
    }


    //--Inserisce i valori nella entity Bio
    public void setValue(Bio bio, HashMap<String, String> mappa) {
        String value;
        String message;

        if (bio != null) {
            for (ParBio par : ParBio.values()) {
                value = mappa.get(par.getTag());
                if (value != null) {

                    try {
                        par.setValue(bio, value);
                    } catch (AlgosException unErrore) {
                        message = String.format("Exception %s nel ParBio %s della bio %s", unErrore.getMessage(), par.getTag(), bio.wikiTitle);
//                        logger.info(message, this.getClass(), "setValue");
                    } catch (Exception unErrore) {
//                        logger.info(String.format("%s nel ParBio %s", unErrore.toString(), par.getTag()), this.getClass(), "setValue");
                    }
                }
            }
        }
    }

}