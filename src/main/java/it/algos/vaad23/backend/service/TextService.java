package it.algos.vaad23.backend.service;

import com.google.common.base.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;


/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 06-mar-2022
 * Time: 18:34
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ATextService.class); <br>
 * 3) @Autowired public TextService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TextService extends AbstractService {


    /**
     * Null-safe, short-circuit evaluation. <br>
     *
     * @param testoIn in ingresso da controllare
     *
     * @return vero se la stringa è vuota o nulla
     */
    public boolean isEmpty(final String testoIn) {
        return Strings.isNullOrEmpty(testoIn);
    }


    /**
     * Null-safe, short-circuit evaluation. <br>
     * Controlla che sia una stringa e che sia valida.
     *
     * @param obj in ingresso da controllare che non sia nullo, che sia una stringa e che non sia vuota
     *
     * @return vero se la stringa esiste e non è vuota
     */
    public boolean isValid(final Object obj) {
        if (obj == null) {
            return false;
        }

        return (obj instanceof String stringa) && !isEmpty(stringa);
    }

    /**
     * Null-safe, short-circuit evaluation. <br>
     * Controlla che sia una stringa e che sia valida e che non abbia spazi vuoti.
     *
     * @param obj in ingresso da controllare che non sia nullo, che sia una stringa e che non sia vuota
     *
     * @return vero se la stringa esiste e non è vuota
     */
    public boolean isValidNoSpace(final Object obj) {
        if (obj == null) {
            return false;
        }

        return (obj instanceof String stringa) && !isEmpty(stringa) && !stringa.equals(SPAZIO);
    }


    /**
     * Forza il primo carattere della stringa (e solo il primo) al carattere maiuscolo <br>
     * <p>
     * Se la stringa è nulla, ritorna un nullo <br>
     * Se la stringa è vuota, ritorna una stringa vuota <br>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param testoIn stringa in ingresso
     *
     * @return testo formattato in uscita
     */
    public String primaMaiuscola(final String testoIn) {
        String testoOut = isValid(testoIn) ? testoIn.trim() : VUOTA;
        String primoCarattere;

        if (isValid(testoOut)) {
            primoCarattere = testoOut.substring(0, 1).toUpperCase();
            testoOut = primoCarattere + testoOut.substring(1);
        }

        return testoOut.trim();
    }


    /**
     * Forza il primo carattere della stringa (e solo il primo) al carattere minuscolo <br>
     * <p>
     * Se la stringa è nulla, ritorna un nullo
     * Se la stringa è vuota, ritorna una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn stringa in ingresso
     *
     * @return testo formattato in uscita
     */
    public String primaMinuscola(final String testoIn) {
        String testoOut = isValid(testoIn) ? testoIn.trim() : VUOTA;
        String primoCarattere;

        if (isValid(testoOut)) {
            primoCarattere = testoOut.substring(0, 1).toLowerCase();
            testoOut = primoCarattere + testoOut.substring(1);
        }

        return testoOut.trim();
    }


    /**
     * Costruisce un array da una stringa di valori multipli separati da virgole. <br>
     * Se la stringa è nulla, ritorna un nullo <br>
     * Se la stringa è vuota, ritorna un nullo <br>
     * Se manca la virgola, ritorna un array di un solo valore col testo completo <br>
     * Elimina spazi vuoti iniziali e finali di ogni valore <br>
     *
     * @param stringaMultipla in ingresso
     *
     * @return lista di singole stringhe
     */
    public List<String> getArray(final String stringaMultipla) {
        List<String> lista = new ArrayList<>();
        String tag = VIRGOLA;
        String[] parti;

        if (isEmpty(stringaMultipla)) {
            return null;
        }

        if (stringaMultipla.contains(tag)) {
            parti = stringaMultipla.split(tag);
            for (String value : parti) {
                lista.add(value.trim());
            }
        }
        else {
            lista.add(stringaMultipla);
        }

        return lista;
    }

    /**
     * Sostituisce nel testo tutte le occorrenze di oldTag con newTag.
     * Esegue solo se il testo è valido
     * Esegue solo se il oldTag è valido
     * newTag può essere vuoto (per cancellare le occorrenze di oldTag)
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn ingresso da elaborare
     * @param oldTag  da sostituire
     * @param newTag  da inserire
     *
     * @return testo convertito
     */
    public String sostituisce(final String testoIn, final String oldTag, final String newTag) {
        String testoOut = testoIn;
        String prima = VUOTA;
        String rimane = testoIn;
        int pos = 0;
        String charVuoto = SPAZIO;

        if (this.isValid(testoIn) && this.isValid(oldTag)) {
            if (rimane.contains(oldTag)) {
                pos = rimane.indexOf(oldTag);

                while (pos != -1) {
                    pos = rimane.indexOf(oldTag);
                    if (pos != -1) {
                        prima += rimane.substring(0, pos);
                        prima += newTag;
                        pos += oldTag.length();
                        rimane = rimane.substring(pos);
                        if (prima.endsWith(charVuoto) && rimane.startsWith(charVuoto)) {
                            rimane = rimane.substring(1);
                        }
                    }
                }

                testoOut = prima + rimane;
            }
        }

        return testoOut.trim();
    }


    /**
     * Elimina tutti i caratteri contenuti nella stringa. <br>
     * Esegue solo se il testo è valido <br>
     *
     * @param testoIn    stringa in ingresso
     * @param subStringa da eliminare
     *
     * @return testoOut stringa convertita
     */
    public String levaTesto(final String testoIn, final String subStringa) {
        String testoOut = testoIn;

        if (testoIn != null && subStringa != null) {
            testoOut = testoIn.trim();
            if (testoOut.contains(subStringa)) {
                testoOut = sostituisce(testoOut, subStringa, VUOTA);
            }
        }

        return testoOut;
    }

    /**
     * Elimina tutte le virgole contenute nella stringa. <br>
     * Esegue solo se la stringa è valida <br>
     * Se arriva un oggetto non stringa, restituisce l'oggetto <br>
     *
     * @param testoIn stringa in ingresso
     *
     * @return testo convertito
     */
    public String levaVirgole(final String testoIn) {
        return levaTesto(testoIn, VIRGOLA);
    }


    /**
     * Elimina tutti i punti contenuti nella stringa. <br>
     * Esegue solo se la stringa è valida <br>
     * Se arriva un oggetto non stringa, restituisce l'oggetto <br>
     *
     * @param testoIn stringa in ingresso
     *
     * @return testo convertito
     */
    public String levaPunti(final String testoIn) {
        return levaTesto(testoIn, PUNTO);
    }

    /**
     * Sostituisce gli slash con punti. <br>
     * NON sostituisce lo slash iniziale (se esiste) <br>
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn stringa in ingresso
     *
     * @return testo convertito
     */
    public String slashToPoint(final String testoIn) {
        String testoOut = testoIn.trim();

        if (testoOut.startsWith(SLASH)) {
            testoOut = testoOut.substring(SLASH.length());
            return SLASH + sostituisce(testoOut, SLASH, PUNTO);
        }
        else {
            return sostituisce(testoOut, SLASH, PUNTO);
        }
    }


    /**
     * Sostituisce i punti con slash. <br>
     * Elimina spazi vuoti iniziali e finali
     *
     * @param testoIn stringa in ingresso
     *
     * @return testo convertito
     */
    public String pointToSlash(final String testoIn) {
        return sostituisce(testoIn, PUNTO, SLASH);
    }

    /**
     * Formattazione di un numero. <br>
     * <p>
     * Il numero può arrivare come stringa, intero o double <br>
     * Se la stringa contiene punti e virgole, viene pulita <br>
     * Se la stringa non è convertibile in numero, viene restituita uguale <br>
     * Inserisce il punto separatore ogni 3 cifre <br>
     * Se arriva un oggetto non previsto, restituisce null <br>
     *
     * @param numObj da formattare (stringa, intero, long o double)
     *
     * @return testo formattato
     */
    public String format(final Object numObj) {
        String formattato;
        String numText = VUOTA;
        String sep = PUNTO;
        int len;
        String num3;
        String num6;
        String num9;
        String num12;

        if (numObj instanceof String || numObj instanceof Integer || numObj instanceof Long || numObj instanceof Double || numObj instanceof List || numObj instanceof Object[]) {
            if (numObj instanceof String) {
                numText = (String) numObj;
                numText = levaVirgole(numText);
                numText = levaPunti(numText);
                try {
                    Integer.decode(numText);
                } catch (Exception unErrore) {
                    return (String) numObj;
                }
            }
            else {
                if (numObj instanceof Integer) {
                    numText = Integer.toString((int) numObj);
                }
                if (numObj instanceof Long) {
                    numText = Long.toString((long) numObj);
                }
                if (numObj instanceof Double) {
                    numText = Double.toString((double) numObj);
                }
                if (numObj instanceof List) {
                    numText = Integer.toString((int) ((List) numObj).size());
                }
                if (numObj instanceof Object[]) {
                    numText = Integer.toString(((Object[]) numObj).length);
                }
            }
        }
        else {
            return null;
        }

        formattato = numText;
        len = numText.length();
        if (len > 3) {
            num3 = numText.substring(0, len - 3);
            num3 += sep;
            num3 += numText.substring(len - 3);
            formattato = num3;
            if (len > 6) {
                num6 = num3.substring(0, len - 6);
                num6 += sep;
                num6 += num3.substring(len - 6);
                formattato = num6;
                if (len > 9) {
                    num9 = num6.substring(0, len - 9);
                    num9 += sep;
                    num9 += num6.substring(len - 9);
                    formattato = num9;
                    if (len > 12) {
                        num12 = num9.substring(0, len - 12);
                        num12 += sep;
                        num12 += num9.substring(len - 12);
                        formattato = num12;
                    }
                }
            }
        }

        //--valore di ritorno
        return formattato;
    }

    /**
     * Formattazione di un numero giustificato a due cifre. <br>
     * <p>
     * Il numero può arrivare come stringa, intero o double <br>
     * Se la stringa contiene punti e virgole, viene pulita <br>
     * Se la stringa non è convertibile in numero, viene restituita uguale <br>
     * Se arriva un oggetto non previsto, restituisce null <br>
     *
     * @param numObj da formattare (stringa, intero, long o double)
     *
     * @return testo formattato
     */
    public String format2(Object numObj) {
        String numText = VUOTA;
        String sep = PUNTO;
        int num = 0;
        int len;
        String num3;
        String num6;
        String num9;
        String num12;

        if (numObj instanceof String || numObj instanceof Integer || numObj instanceof Long || numObj instanceof Double) {
            if (numObj instanceof String) {
                numText = (String) numObj;
                numText = levaVirgole(numText);
                numText = levaPunti(numText);
                try {
                    num = Integer.decode(numText);
                } catch (Exception unErrore) {
                    return (String) numObj;
                }
            }
            else {
                if (numObj instanceof Integer) {
                    num = (int) numObj;
                }
                if (numObj instanceof Long) {
                    num = ((Long) numObj).intValue();
                }
                if (numObj instanceof Double) {
                    num = ((Double) numObj).intValue();
                }
            }
        }
        else {
            return null;
        }

        numText = "" + num;
        if (num < 10) {
            return numText = "0" + numText;
        }

        return numText;
    }


    /**
     * Formattazione di un numero giustificato a tre cifre.
     * <p>
     * Il numero può arrivare come stringa, intero o double
     * Se la stringa contiene punti e virgole, viene pulita
     * Se la stringa non è convertibile in numero, viene restituita uguale
     * Se arriva un oggetto non previsto, restituisce null
     *
     * @param numObj da formattare (stringa, intero, long o double)
     *
     * @return testo formattato
     */
    public String format3(Object numObj) {
        String numText = VUOTA;
        String sep = PUNTO;
        int num = 0;
        int len;
        String num3;
        String num6;
        String num9;
        String num12;

        if (numObj instanceof String || numObj instanceof Integer || numObj instanceof Long || numObj instanceof Double) {
            if (numObj instanceof String) {
                numText = (String) numObj;
                numText = levaVirgole(numText);
                numText = levaPunti(numText);
                try { // prova ad eseguire il codice
                    num = Integer.decode(numText);
                } catch (Exception unErrore) { // intercetta l'errore
                    return (String) numObj;
                }
            }
            else {
                if (numObj instanceof Integer) {
                    num = (int) numObj;
                }
                if (numObj instanceof Long) {
                    num = ((Long) numObj).intValue();
                }
                if (numObj instanceof Double) {
                    num = ((Double) numObj).intValue();
                }
            }
        }
        else {
            return null;
        }

        numText = "" + num;
        if (num < 100) {
            if (num < 10) {
                return numText = "00" + numText;
            }
            else {
                return numText = "0" + numText;
            }
        }

        return numText;
    }


    /**
     * Elimina la parte di stringa successiva al tag, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata   stringa in ingresso
     * @param tagFinale dopo il quale eliminare
     *
     * @return uscita stringa ridotta
     */
    public String levaDopo(String entrata, String tagFinale) {
        String uscita = entrata;
        int pos;

        if (uscita != null && tagFinale != null) {
            uscita = entrata.trim();
            if (uscita.contains(tagFinale)) {
                pos = uscita.indexOf(tagFinale);
                uscita = uscita.substring(0, pos);
                uscita = uscita.trim();
            }
        }

        return uscita;
    }


    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoRef(String entrata) {
        return levaCodaDa(entrata, REF_OPEN);
    }


    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoNote(String entrata) {
        return levaCodaDa(entrata, NOTE);
    }

    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoWiki(String entrata) {
        return levaCodaDa(entrata, NO_WIKI);
    }


    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoUguale(String entrata) {
        return levaCodaDa(entrata, UGUALE_SEMPLICE);
    }


    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoEccetera(String entrata) {
        return levaCodaDa(entrata, ECC);
    }


    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoGraffe(String entrata) {
        return levaCodaDa(entrata, DOPPIE_GRAFFE_INI);
    }


    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoVirgola(String entrata) {
        return levaCodaDa(entrata, VIRGOLA);
    }


    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoParentesi(String entrata) {
        return levaCodaDa(entrata, PARENTESI_TONDA_END);
    }


    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoInterrogativo(String entrata) {
        return levaCodaDa(entrata, PUNTO_INTERROGATIVO);
    }


    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoCirca(String entrata) {
        return levaCodaDa(entrata, CIRCA);
    }

    /**
     * Elimina la parte di stringa successiva al tag indicato, se esiste.
     * <p>
     * Esegue solo se la stringa è valida
     * Se manca il tag, restituisce la stringa
     * Elimina spazi vuoti iniziali e finali
     *
     * @param entrata stringa in ingresso
     *
     * @return uscita stringa ridotta
     */
    public String levaDopoTagRef(String entrata) {
        return levaCodaDa(entrata, TAG_REF);
    }

    /**
     * Elimina dal testo il tagFinale, se esiste. <br>
     * <p>
     * Esegue solo se il testo è valido <br>
     * Se tagFinale è vuoto o non contenuto nella stringa, restituisce il testo originale intatto <br>
     * Elimina solo spazi vuoti finali e NON eventuali spazi vuoti iniziali <br>
     *
     * @param testoIn   stringa in ingresso
     * @param tagFinale da eliminare
     *
     * @return testo convertito
     */
    public String levaCoda(final String testoIn, final String tagFinale) {
        String testoOut = testoIn;
        String tag;

        if (this.isValid(testoOut) && this.isValid(tagFinale)) {
            testoOut = StringUtils.stripEnd(testoIn, SPAZIO);
            tag = tagFinale.trim();
            if (testoOut.endsWith(tag)) {
                testoOut = testoOut.substring(0, testoOut.length() - tag.length());
                testoOut = StringUtils.stripEnd(testoOut, SPAZIO);
            }
            else {
                testoOut = testoIn;
            }
        }

        return testoOut;
    }


    /**
     * Elimina il testo da tagInterrompi in poi <br>
     * <p>
     * Esegue solo se il testo è valido <br>
     * Se tagInterrompi è vuoto o non contenuto nella stringa, restituisce il testo originale intatto <br>
     * Elimina solo spazi vuoti finali e NON eventuali spazi vuoti iniziali <br>
     *
     * @param testoIn       stringa in ingresso
     * @param tagInterrompi da dove inizia il testo da eliminare
     *
     * @return test ridotto in uscita
     */
    public String levaCodaDa(final String testoIn, final String tagInterrompi) {
        String testoOut = testoIn;
        String tag;

        if (this.isValid(testoOut) && this.isValid(tagInterrompi)) {
            testoOut = StringUtils.stripEnd(testoIn, SPAZIO);
            tag = tagInterrompi.trim();
            if (testoOut.contains(tag)) {
                testoOut = testoOut.substring(0, testoOut.lastIndexOf(tag));
                testoOut = StringUtils.stripEnd(testoOut, SPAZIO);
            }
            else {
                testoOut = testoIn;
            }
        }

        return testoOut;
    }

    /**
     * Elimina (eventuali) parentesi quadre in testa e coda della stringa. <br>
     * Funziona solo se le quadre sono esattamente in TESTA e in CODA alla stringa <br>
     * Se arriva una stringa vuota, restituisce una stringa vuota <br>
     * Elimina spazi vuoti iniziali e finali <br>
     * Esegue anche se le quadre sono presenti in numero diverso tra la testa e la coda della stringa <br>
     * Esegue anche se le quadre in testa o in coda alla stringa sono singole o doppie o triple o quadruple <br>
     *
     * @param testoIn stringa in ingresso
     *
     * @return stringa con quadre iniziali e finali eliminate
     */
    public String setNoQuadre(final String testoIn) {
        String testoOut = VUOTA;

        if (isValid(testoIn)) {
            testoOut = testoIn.trim();

            while (testoOut.startsWith(QUADRA_INI)) {
                testoOut = testoOut.substring(1);
            }

            while (testoOut.endsWith(QUADRA_END)) {
                testoOut = testoOut.substring(0, testoOut.length() - 1);
            }
            return testoOut;
        }
        else {
            return testoOut;
        }
    }


    /**
     * Allunga un testo alla lunghezza desiderata. <br>
     * Se è più corta, aggiunge spazi vuoti <br>
     * Se è più lungo, rimane inalterato <br>
     * La stringa in ingresso viene 'giustificata' a sinistra <br>
     * Vengono eliminati gli spazi vuoti che precedono la stringa <br>
     *
     * @param testoIn stringa in ingresso
     *
     * @return testo della 'lunghezza' richiesta
     */

    public String rightPad(final String testoIn, int size) {
        String testoOut = VUOTA;

        if (isValid(testoIn)) {
            testoOut = testoIn.trim();
            testoOut = StringUtils.rightPad(testoOut, size);
            return testoOut;
        }
        else {
            return testoOut;
        }
    }

    /**
     * Forza un testo alla lunghezza desiderata. <br>
     * Se è più corta, aggiunge spazi vuoti <br>
     * Se è più lungo, lo tronca <br>
     * La stringa in ingresso viene 'giustificata' a sinistra <br>
     * Vengono eliminati gli spazi vuoti che precedono la stringa <br>
     *
     * @param testoIn stringa in ingresso
     *
     * @return testo della 'lunghezza' richiesta
     */

    public String fixSize(final String testoIn, int size) {
        String testoOut = rightPad(testoIn, size);

        if (testoOut.length() > size) {
            testoOut = testoOut.substring(0, size);
        }

        return testoOut;
    }

    /**
     * Forza un testo alla lunghezza desiderata e aggiunge singole parentesi quadre in testa e coda. <br>
     * Se arriva una stringa vuota, restituisce una stringa vuota con singole parentesi quadre aggiunte <br>
     *
     * @param testoIn stringa in ingresso
     *
     * @return stringa con lunghezza prefissata e singole parentesi quadre aggiunte
     */
    public String fixSizeQuadre(final String testoIn, final int size) {
        String stringaOut = setNoQuadre(testoIn);
        stringaOut = rightPad(stringaOut, size);
        stringaOut = fixSize(stringaOut, size);

        if (this.isValid(stringaOut)) {
            if (!stringaOut.startsWith(QUADRA_INI)) {
                stringaOut = QUADRA_INI + stringaOut;
            }
            if (!stringaOut.endsWith(QUADRA_END)) {
                stringaOut = stringaOut + QUADRA_END;
            }
        }

        if (this.isEmpty(stringaOut) && size > 0) {
            stringaOut = QUADRA_INI + StringUtils.rightPad(VUOTA, size) + QUADRA_END;
        }

        return stringaOut.trim();
    }


    /**
     * Elimina dal testo il tagIniziale, se esiste <br>
     * <p>
     * Esegue solo se il testo è valido <br>
     * Se tagIniziale è vuoto, restituisce il testo <br>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param testoIn     ingresso
     * @param tagIniziale da eliminare
     *
     * @return testo ridotto in uscita
     */
    public String levaTesta(final String testoIn, final String tagIniziale) {
        String testoOut = testoIn.trim();
        String tag;

        if (this.isValid(testoOut) && this.isValid(tagIniziale)) {
            tag = tagIniziale.trim();
            if (testoOut.startsWith(tag)) {
                testoOut = testoOut.substring(tag.length());
            }
        }

        return testoOut.trim();
    }

    /**
     * Elimina il testo prima di tagIniziale. <br>
     * <p>
     * Esegue solo se il testo è valido <br>
     * Se tagIniziale è vuoto, restituisce il testo <br>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param testoIn     ingresso
     * @param tagIniziale da dove inizia il testo da tenere
     *
     * @return testo ridotto in uscita
     */
    public String levaTestoPrimaDi(final String testoIn, final String tagIniziale) {
        String testoOut = testoIn.trim();
        String tag;

        if (this.isValid(testoOut) && this.isValid(tagIniziale)) {
            tag = tagIniziale.equals(CAPO) ? tagIniziale : tagIniziale.trim();
            if (testoOut.contains(tag)) {
                testoOut = testoOut.substring(testoOut.indexOf(tag) + tag.length());
            }
        }

        return testoOut.trim();
    }


    public String estrae(String valueIn, String tagIni, String tagEnd) {
        String valueOut = valueIn;
        int length = 0;
        int posIni = 0;
        int posEnd = 0;

        if (isValid(valueIn) && valueIn.contains(tagIni) && valueIn.contains(tagEnd)) {
            length = tagIni.length();
            posIni = valueIn.indexOf(tagIni);
            posEnd = valueIn.indexOf(tagEnd, posIni + length);
            valueOut = valueIn.substring(posIni + length, posEnd);
        }

        return valueOut.trim();
    }


    /**
     * Elimina (eventuali) graffe singole in testa e coda della stringa.
     * Funziona solo se le graffe sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con graffe eliminate
     */
    public String setNoGraffe(String stringaIn) {
        String stringaOut = stringaIn;

        if (isValid(stringaIn)) {
            stringaIn = stringaIn.trim();

            if (stringaIn.startsWith(GRAFFA_INI) && stringaIn.endsWith(GRAFFA_END)) {
                stringaOut = stringaIn;
                stringaOut = levaCoda(stringaOut, GRAFFA_END);
                stringaOut = levaTesta(stringaOut, GRAFFA_INI);
            }
        }

        return stringaOut.trim();
    }


    /**
     * Elimina (eventuali) graffe doppie in testa e coda della stringa.
     * Funziona solo se le graffe sono esattamente in TESTA ed in CODA alla stringa
     * Se arriva una stringa vuota, restituisce una stringa vuota
     * Elimina spazi vuoti iniziali e finali
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con doppie graffe eliminate
     */
    public String setNoDoppieGraffe(String stringaIn) {
        String stringaOut = stringaIn;

        if (isValid(stringaIn)) {
            stringaIn = stringaIn.trim();

            if (stringaIn.startsWith(DOPPIE_GRAFFE_INI) && stringaIn.endsWith(DOPPIE_GRAFFE_END)) {
                stringaOut = stringaIn;
                stringaOut = levaCoda(stringaOut, DOPPIE_GRAFFE_END);
                stringaOut = levaTesta(stringaOut, DOPPIE_GRAFFE_INI);
            }
        }

        return stringaOut.trim();
    }


    /**
     * Elimina (eventuali) 'doppi apici' " in testa ed in coda alla stringa. <br>
     * Se arriva una stringa vuota, restituisce una stringa vuota <br>
     *
     * @param stringaIn in ingresso
     *
     * @return stringa senza doppi apici iniziali e finali
     */
    public String setNoDoppiApici(String stringaIn) {
        String stringaOut = stringaIn.trim();
        String doppioApice = "\"";
        int cicli = 4;

        if (this.isValid(stringaOut)) {
            for (int k = 0; k < cicli; k++) {
                stringaOut = this.levaTesta(stringaOut, doppioApice);
                stringaOut = this.levaCoda(stringaOut, doppioApice);
            }
        }

        return stringaOut.trim();
    }

    /**
     * Controlla se nel testo ci sono occorrenze pari delle graffe.
     * Le graffe devono anche essere nel giusto ordine
     *
     * @param testo in ingresso
     *
     * @return vero se le occorrenze di apertura e chiusura sono uguali
     */
    public boolean isPariGraffe(String testo) {
        return isPariTag(testo, DOPPIE_GRAFFE_INI, DOPPIE_GRAFFE_END);
    }

    /**
     * Controlla che le occorrenze dei due tag si pareggino all'interno del testo.
     *
     * @param testo  di riferimento
     * @param tagIni di apertura
     * @param tagEnd di chiusura
     *
     * @return vero se il numero di tagIni è uguale al numero di tagEnd
     */
    public boolean isPariTag(String testo, String tagIni, String tagEnd) {
        boolean pari = false;
        int numIni;
        int numEnd;

        if (!testo.equals(VUOTA) && !tagIni.equals(VUOTA) && !tagEnd.equals(VUOTA)) {
            numIni = getNumTag(testo, tagIni);
            numEnd = getNumTag(testo, tagEnd);
            pari = (numIni == numEnd);
        }

        return pari;
    }

    /**
     * Numero di occorrenze di un tag in un testo. <br>
     * Il tag non viene trimmato ed è sensibile agli spazi prima e dopo <br>
     * NON si usano le regex <br>
     *
     * @param testoDaSpazzolare di riferimento
     * @param tag               da cercare
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    public int getNumTag(String testoDaSpazzolare, String tag) {
        int numTag = 0;
        int pos;

        //--controllo di congruità
        if (this.isValid(testoDaSpazzolare) && this.isValid(tag)) {
            if (testoDaSpazzolare.contains(tag)) {
                pos = testoDaSpazzolare.indexOf(tag);
                while (pos != -1) {
                    pos = testoDaSpazzolare.indexOf(tag, pos + tag.length());
                    numTag++;
                }
            }
        }

        return numTag;
    }

    /**
     * Elimina gli spazi multipli eventualmente presenti <br>
     * Tutti gli spazi multipli vengono ridotti ad uno spazio singolo <br>
     *
     * @param line in ingresso
     *
     * @return stringa elaborata con tutti gli spazi ridotti ad 1 ed eliminati spazi in testa ed in coda
     */
    public String fixOneSpace(String line) {
        String regexSpazioVariabile = "\\s+"; // una o più occorrenze

        return line.replaceAll(regexSpazioVariabile, SPAZIO).trim();
    }


    /**
     * Aggiunge parentesi quadre in testa e coda alla stringa. <br>
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole) <br>
     * Se arriva una stringa vuota, restituisce una stringa vuota <br>
     * Elimina spazi vuoti iniziali e finali <br>
     * Elimina eventuali quadre già presenti, per evitare di metterle doppie <br>
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con parentesi quadre aggiunte
     */
    public String setQuadre(final String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = this.setNoQuadre(stringaOut);
            if (this.isValid(stringaOut)) {
                if (!stringaOut.startsWith(QUADRA_INI)) {
                    stringaOut = QUADRA_INI + stringaOut;
                }
                if (!stringaOut.endsWith(QUADRA_END)) {
                    stringaOut = stringaOut + QUADRA_END;
                }
            }
        }

        return isValid(stringaOut) ? stringaOut.trim() : VUOTA;
    }


    /**
     * Aggiunge parentesi quadre doppie in testa e coda alla stringa. <br>
     * Aggiunge SOLO se gia non esistono (ne doppie, ne singole) <br>
     * Se arriva una stringa vuota, restituisce una stringa vuota <br>
     * Elimina spazi vuoti iniziali e finali <br>
     * Elimina eventuali quadre già presenti, per evitare di metterle doppie <br>
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con doppie parentesi quadre aggiunte
     */
    public String setDoppieQuadre(final String stringaIn) {
        String stringaOut = stringaIn;

        if (stringaIn != null && stringaIn.length() > 0) {
            stringaOut = this.setNoQuadre(stringaOut);
            stringaOut = this.setNoQuadre(stringaOut);
            if (this.isValid(stringaOut)) {
                if (!stringaOut.startsWith(DOPPIE_QUADRE_INI)) {
                    stringaOut = DOPPIE_QUADRE_INI + stringaOut;
                }
                if (!stringaOut.endsWith(DOPPIE_QUADRE_END)) {
                    stringaOut = stringaOut + DOPPIE_QUADRE_END;
                }
                if (stringaOut.startsWith(QUADRA_INI + DOPPIE_QUADRE_INI)) {
                    stringaOut = stringaOut.substring(1);
                }
                if (stringaOut.endsWith(QUADRA_END + DOPPIE_QUADRE_END)) {
                    stringaOut = stringaOut.substring(0, stringaOut.length() - 1);
                }
            }
        }

        return isValid(stringaOut) ? stringaOut.trim() : VUOTA;
    }


    /**
     * Aggiunge parentesi tonde singole in testa e coda alla stringa. <br>
     * Aggiunge SOLO se gia non esistono <br>
     * Se arriva una stringa vuota, restituisce una stringa vuota <br>
     * Elimina spazi vuoti iniziali e finali <br>
     * Elimina eventuali quadre già presenti, per evitare di metterle doppie <br>
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con parentesi tonde aggiunte
     */
    public String setTonde(String stringaIn) {
        String stringaOut = stringaIn;

        if (!stringaOut.startsWith(PARENTESI_TONDA_INI)) {
            stringaOut = PARENTESI_TONDA_INI + stringaOut;
        }
        if (!stringaOut.endsWith(PARENTESI_TONDA_END)) {
            stringaOut = stringaOut + PARENTESI_TONDA_END;
        }

        return isValid(stringaOut) ? stringaOut.trim() : VUOTA;
    }

    /**
     * Aggiunge i tag '<ref></ref>' in testa e coda alla stringa. <br>
     * Elimina spazi vuoti iniziali e finali <br>
     *
     * @param stringaIn in ingresso
     *
     * @return stringa con <ref></ref> aggiunti
     */
    public String setRef(final String stringaIn) {
        String stringaOut = REF + stringaIn.trim() + REF_END;
        return stringaOut.trim();
    }

}