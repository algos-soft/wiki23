package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.vaad23.backend.boot.VaadCost.NOTE;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.packages.crono.anno.*;
import it.algos.vaad23.backend.packages.crono.giorno.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.packages.attivita.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.packages.nazionalita.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.regex.*;

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

    /**
     * Elabora la singola voce biografica <br>
     * Estrae dal tmplBioServer i singoli parametri previsti nella enumeration ParBio <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile e utilizzabile per le liste <br>
     */
    public Bio esegueSave(Bio bio) {
        return bioBackend.save(esegue(bio));
    }

    //--Inserisce i valori nella entity Bio
    public void setValue(Bio bio, HashMap<String, String> mappa) {
        String value;
        String message;

        if (bio != null) {
            for (ParBio par : ParBio.values()) {
                value = mappa.get(par.getTag());
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
     * Regola la property <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixOrdinamento(String testoGrezzo) {

        if (testoGrezzo.startsWith(DEFAULT_SORT)) {
            testoGrezzo = textService.levaTesta(testoGrezzo, DEFAULT_SORT);
        }
        if (testoGrezzo.endsWith(DOPPIE_GRAFFE_END)) {
            testoGrezzo = textService.levaCodaDa(testoGrezzo, DOPPIE_GRAFFE_END);
        }

        return wikiBotService.estraeValoreInizialeGrezzoPuntoAmmesso(testoGrezzo);
    }

    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * A seconda del flag:
     * CONTROLLA che il valore sia valido - solo M o F <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixSesso(String testoGrezzo) {
        String testoValido = fixValoreGrezzo(testoGrezzo);
        testoValido = testoValido.toLowerCase();

        if (MASCHI.contains(testoValido)) {
            testoValido = "M";
        }

        if (FEMMINE.contains(testoValido)) {
            testoValido = "F";
        }

        return testoValido;
    }


    /**
     * Regola questa property <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixLuogoValido(String testoGrezzo) {
        String testoValido;

        if (textService.isEmpty(testoGrezzo)) {
            return VUOTA;
        }

        testoValido = testoGrezzo.trim();
        testoValido = textService.levaDopo(testoValido, REF);
        testoValido = textService.levaDopo(testoValido, NOTE);
        testoValido = textService.levaDopo(testoValido, DOPPIE_GRAFFE_INI);
        testoValido = textService.levaDopo(testoValido, PUNTO_INTERROGATIVO);
        testoValido = textService.setNoQuadre(testoValido);
        testoValido = testoValido.trim();

        if (testoValido.length() > 253) {
            testoValido = testoValido.substring(0, 252);
            //@todo manca warning
        }

        return testoValido;
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
    public String fixGiornoValido(String testoGrezzo) {
        String testoValido = fixGiorno(testoGrezzo);
        Giorno giorno = null;

        try {
            giorno = giornoBackend.findByNome(testoValido);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
        }

        return giorno != null ? giorno.nome : VUOTA;
    }

    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese) <br>
     * Elimina eventuali spazi vuoti DOPPI o TRIPLI (tipico della data tra il giorno ed il mese) <br>
     * Forza a minuscolo il primo carattere del mese <br>
     * Forza a ordinale un eventuale primo giorno del mese scritto come numero o come grado <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixGiorno(String testoGrezzo) {
        //--se contiene un punto interrogativo (in coda) è valido
        String testoValido = wikiBotService.estraeValoreInizialeGrezzoPuntoEscluso(testoGrezzo);
        int pos;
        String primo;
        String mese;

        //--spazio singolo
        testoValido = textService.fixOneSpace(testoValido);

        //--senza spazio
        if (!testoValido.contains(SPAZIO)) {
            testoValido = separaMese(testoValido);
        }

        if (!testoValido.contains(SPAZIO)) {
            return VUOTA;
        }

        //--elimina eventuali quadre (ini o end) rimaste
        testoValido = testoValido.replaceAll(QUADRA_INI_REGEX, VUOTA);
        testoValido = testoValido.replaceAll(QUADRA_END_REGEX, VUOTA);

        //--deve iniziare con un numero
        if (!Character.isDigit(testoValido.charAt(0))) {
            return VUOTA;
        }

        //--deve finire con una lettera
        if (Character.isDigit(testoValido.charAt(testoValido.length() - 1))) {
            return VUOTA;
        }

        //--minuscola
        testoValido = testoValido.toLowerCase();

        //--Forza a ordinale un eventuale primo giorno del mese scritto come numero o come grado
        if (testoValido.contains(SPAZIO)) {
            pos = testoValido.indexOf(SPAZIO);
            primo = testoValido.substring(0, pos);
            mese = testoValido.substring(pos + SPAZIO.length());

            if (primo.equals("1") || primo.equals("1°")) {
                primo = "1º";
                testoValido = primo + SPAZIO + mese;
            }
        }

        return testoValido.trim();
    }

    public int fixGiornoOrd(String testoGrezzo) {
        int giornoOrdine = 0;
        Giorno giorno;
        String testoGiorno = fixGiorno(testoGrezzo);

        if (textService.isEmpty(testoGiorno)) {
            return giornoOrdine;
        }

        giorno = giornoBackend.findByNome(testoGiorno);
        if (giorno != null) {
            giornoOrdine = giorno.ordine;
        }

        return giornoOrdine;
    }

    /**
     * Regola la property <br>
     * <p>
     * Elimina il testo successivo a vari tag (fixPropertyBase) <br>
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese) <br>
     * Elimina eventuali DOPPI spazi vuoto (tipico della data tra il giorno ed il mese) <br>
     * Elimina eventuali spazi vuoti (trim) <br>
     * Controlla che il valore esista nella collezione Giorno <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di giorno valido
     */
    public Giorno fixGiornoLink(String testoGrezzo) throws Exception {
        Giorno giorno = null;
        String testoValido = fixGiorno(testoGrezzo);

        if (textService.isValid(testoValido)) {
            giorno = giornoBackend.findByNome(testoValido);
        }

        return giorno;
    }

    /**
     * Regola questa property <br>
     * <p>
     * Regola il testo con le regolazioni di base (fixValoreGrezzo) <br>
     * Elimina il testo se contiene la dicitura 'circa' (tipico dell'anno)
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testo/parametro regolato in uscita
     */
    public String fixAnno(String testoGrezzo) {
        //--se contiene un punto interrogativo (in coda) è valido
        String testoValido = wikiBotService.estraeValoreInizialeGrezzoPuntoAmmesso(testoGrezzo);

        if (textService.isEmpty(testoValido)) {
            return VUOTA;
        }

        //--deve iniziare con un numero
        if (!Character.isDigit(testoValido.charAt(0))) {
            return VUOTA;
        }

        //--tag non ammesso
        if (testoValido.contains("secolo")) {
            return VUOTA;
        }

        //--non deve contenere caratteri alfabetici
        //--solo (eventualmente): A, a, C, c
        //--per gli anni prima di Cristo
        if (contieneCaratteriAlfabetici(testoValido)) {
            return VUOTA;
        }

        //--non deve contenere caratteri divisivi di due anni
        if (testoValido.contains(SLASH) || testoValido.contains(PIPE) || testoValido.contains(TRATTINO)) {
            return VUOTA;
        }

        return testoValido.trim();
    }


    public int fixAnnoOrd(String testoGrezzo) {
        int annoOrdine = 0;
        Anno anno;
        String testoAnno = fixAnno(testoGrezzo);

        if (textService.isEmpty(testoAnno)) {
            return annoOrdine;
        }

        anno = annoBackend.findByNome(testoAnno);
        if (anno != null) {
            annoOrdine = anno.ordine;
        }

        return annoOrdine;
    }

    /**
     * Regola la property <br>
     * <p>
     * Elimina il testo successivo a vari tag (fixPropertyBase) <br>
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese) <br>
     * Elimina eventuali DOPPI spazi vuoto (tipico della data tra il giorno ed il mese) <br>
     * Elimina eventuali spazi vuoti (trim) <br>
     * Controlla che il valore esista nella collezione Anno <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di anno valido
     */
    public Anno fixAnnoLink(final String testoGrezzo) throws Exception {
        Anno anno = null;
        String titoloAncheAnteCristo = fixAnno(testoGrezzo);

        if (textService.isValid(titoloAncheAnteCristo)) {
            anno = this.findAnnoByKey(titoloAncheAnteCristo);
        }

        return anno;
    }


    /**
     * Retrieves an entity by its keyProperty.
     * Considera anche gli anni Ante Cristo, eventualmente scritti male <br>
     *
     * @param titoloAncheAnteCristo must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     */
    public Anno findAnnoByKey(final String titoloAncheAnteCristo) throws Exception {
        Anno anno = null;
        String titoloEsatto = titoloAncheAnteCristo;
        String tagA = "a";
        String tagC = "C";

        //--a minuscola
        titoloEsatto = titoloEsatto.replaceAll("A", tagA);

        //--c maiuscola
        titoloEsatto = titoloEsatto.replaceAll("c", tagC);

        //--manca spazio
        if (!titoloEsatto.contains(SPAZIO)) {
            titoloEsatto = titoloEsatto.replace(tagA, SPAZIO + tagA);
        }

        //--manca punto dopo 'a'
        if (!titoloEsatto.contains(tagA + PUNTO)) {
            titoloEsatto = titoloEsatto.replace(tagA, tagA + PUNTO);
        }

        //--manca punto dopo 'C'
        if (!titoloEsatto.contains(tagC + PUNTO)) {
            titoloEsatto = titoloEsatto.replace(tagC, tagC + PUNTO);
        }

        anno = annoBackend.findByNome(titoloEsatto);

        return anno;
    }

    public boolean contieneCaratteriAlfabetici(String testoIn) {
        boolean contiene = false;
        // Create a Pattern object
        Pattern pattern = Pattern.compile("[bd-zBD-Z]");

        // Now create matcher object.
        Matcher matcher = pattern.matcher(testoIn);

        if (matcher != null && matcher.find()) {
            return true;
        }

        return contiene;
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
            attivita = attivitaBackend.findFirstBySingolare(testoValido);
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
        testoValido = textService.primaMinuscola(testoValido);

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
            nazionalita = nazionalitaBackend.findFirstBySingolare(testoValido);
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
        testoValido = textService.primaMinuscola(testoValido);

        //        //--eventuali quadre rimaste (può succedere per le attività ex-)
        //        testoValido = testoValido.replaceAll(QUADRA_INI_REGEX,VUOTA);
        //        testoValido = testoValido.replaceAll(QUADRA_END_REGEX,VUOTA);

        return testoValido.trim();
    }

    /**
     * Regola la property <br>
     * <p>
     * Elimina il testo successivo a vari tag (fixPropertyBase) <br>
     * Controlla che il valore esista nella collezione Nazionalità <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return istanza di nazionalità valida
     */
    public Nazionalita fixNazionalitaLink(String testoGrezzo) {
        Nazionalita nazionalita = null;
        String testoValido = VUOTA;

        if (textService.isValid(testoGrezzo)) {
            testoValido = fixNazionalitaValida(testoGrezzo);
        }

        if (textService.isValid(testoValido)) {
            try {
                nazionalita = nazionalitaBackend.findFirstBySingolare(testoValido);
            } catch (Exception unErrore) {
                logger.error(new WrapLog().exception(unErrore).usaDb());
            }
        }

        return nazionalita;
    }

    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixValoreGrezzo(String valoreGrezzo) {
        String valoreValido = valoreGrezzo.trim();

        if (textService.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }

        valoreValido = textService.setNoQuadre(valoreValido);

        //--elimina ref in coda
        valoreValido = textService.levaDopo(valoreValido, REF_OPEN);

        //--elimina quadra in coda
        valoreValido = textService.levaDopo(valoreValido, QUADRA_INI);

        return valoreValido.trim();
    }


    public String separaMese(String testoTuttoAttaccato) {
        String testoSeparato = testoTuttoAttaccato.trim();
        String giorno;
        String mese;
        String inizio;

        if (textService.isEmpty(testoTuttoAttaccato)) {
            return VUOTA;
        }

        if (testoSeparato.contains(SPAZIO)) {
            return testoSeparato;
        }

        if (Character.isDigit(testoSeparato.charAt(0))) {
            if (Character.isDigit(testoSeparato.charAt(1))) {
                giorno = testoSeparato.substring(0, 2);
                mese = testoSeparato.substring(2);
            }
            else {
                giorno = testoSeparato.substring(0, 1);
                mese = testoSeparato.substring(1);
            }
        }
        else {
            return testoSeparato;
        }

        inizio = mese.substring(0, 1);
        if (inizio.equals(TRATTINO) || inizio.equals(SLASH)) {
            mese = mese.substring(1);
        }

        if (textService.isValid(giorno) && textService.isValid(mese)) {
            testoSeparato = giorno + SPAZIO + mese;
        }

        return testoSeparato;
    }

}