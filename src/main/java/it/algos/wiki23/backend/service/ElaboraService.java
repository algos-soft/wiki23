package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.service.*;
import it.algos.vaad23.backend.wrapper.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
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
public class ElaboraService extends WAbstractService {


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

        bio.elaborato = true;
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


    /**
     * Regola la property <br>
     * Indipendente dalla lista di Nomi <br>
     * Nei nomi composti, prende solo il primo <br>
     * Se esiste nella lista dei Prenomi (nomi doppi), lo accetta <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixNome(final String testoGrezzo) {
        String testoValido = wikiBotService.estraeValoreInizialeGrezzoPuntoAmmesso(testoGrezzo);
        List<String> listaDoppiNomi;

        if (testoValido.contains(SPAZIO)) {
            listaDoppiNomi = doppionomeBackend.fetchCode();
            if (!listaDoppiNomi.contains(testoValido)) {
                testoValido = testoValido.substring(0, testoValido.indexOf(SPAZIO)).trim();
            }
        }

        return testoValido;
    }

    /**
     * Regola la property <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixCognome(String testoGrezzo) {
        return wikiBotService.estraeValoreInizialeGrezzoPuntoAmmesso(testoGrezzo);
    }


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni specifiche della property <br>
     * Controlla che il valore esista nella collezione linkata <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixAttivitaValida(String testoGrezzo) {
        String testoValido = fixAttivita(testoGrezzo);
        Attivita attivita = null;

        try {
            attivita = attivitaBackend.findBySingolare(testoValido);
        } catch (Exception unErrore) {
            logger.info(new WrapLog().exception(unErrore));
        }

        return attivita != null ? attivita.getSingolare() : VUOTA;
    }

    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixAttivita(String testoGrezzo) {
        //--se contiene un punto interrogativo (in coda) è valido
        String testoValido = wikiBotService.estraeValoreInizialeGrezzoPuntoEscluso(testoGrezzo);

        //--minuscola
        testoValido = testoValido.toLowerCase();

        //--eventuali quadre rimaste (può succedere per le attività ex-)
        testoValido = testoValido.replaceAll(QUADRA_INI_REGEX, VUOTA);
        testoValido = testoValido.replaceAll(QUADRA_END_REGEX, VUOTA);

        return testoValido.trim();
    }


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixNazionalitaValida(String testoGrezzo) {
        String testoValido = fixNazionalita(testoGrezzo);
        Nazionalita nazionalita = null;

        try {
            nazionalita = nazionalitaBackend.findBySingolare(testoValido);
        } catch (Exception unErrore) {
            logger.info(new WrapLog().exception(unErrore));
        }

        return nazionalita != null ? nazionalita.getSingolare() : VUOTA;
    }


    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixNazionalita(String testoGrezzo) {
        //--se contiene un punto interrogativo (in coda) è valido
        String testoValido = wikiBotService.estraeValoreInizialeGrezzoPuntoEscluso(testoGrezzo);

        //--minuscola
        testoValido = testoValido.toLowerCase();

        //        //--eventuali quadre rimaste (può succedere per le attività ex-)
        //        testoValido = testoValido.replaceAll(QUADRA_INI_REGEX,VUOTA);
        //        testoValido = testoValido.replaceAll(QUADRA_END_REGEX,VUOTA);

        return testoValido.trim();
    }

}