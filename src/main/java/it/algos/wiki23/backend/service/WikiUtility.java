package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.regex.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 20-feb-2022
 * Time: 08:56
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WikiUtility extends WAbstractService {

    public String fixTitolo(String titoloGrezzo) {
        return fixTitolo(VUOTA, titoloGrezzo);
    }

    public String fixTitolo(String wikiTitleBase, String titoloGrezzo) {
        String paragrafoVisibile = VUOTA;

        if (!wikiTitleBase.endsWith(SLASH)) {
            wikiTitleBase += SLASH;
        }

        if (textService.isValid(titoloGrezzo)) {
            paragrafoVisibile = wikiTitleBase;
            paragrafoVisibile += textService.primaMaiuscola(titoloGrezzo);
            paragrafoVisibile += PIPE;
            paragrafoVisibile += textService.primaMaiuscola(titoloGrezzo);
            paragrafoVisibile = textService.setDoppieQuadre(paragrafoVisibile);
            paragrafoVisibile = PARAGRAFO + paragrafoVisibile + PARAGRAFO;
        }
        else {
            paragrafoVisibile = "Altre...";
            paragrafoVisibile = setParagrafo(paragrafoVisibile);
        }

        return paragrafoVisibile;
    }

    public String fixTitolo(String titoloGrezzo, int numVoci) {
        return fixTitolo(VUOTA, titoloGrezzo, numVoci);
    }

    public String fixTitolo(String wikiTitleBase, String titoloGrezzo, int numVoci) {
        String paragrafoVisibile = VUOTA;

        if (textService.isValid(wikiTitleBase)) {
            if (!wikiTitleBase.endsWith(SLASH)) {
                wikiTitleBase += SLASH;
            }
            paragrafoVisibile = wikiTitleBase;
        }

        if (textService.isValid(titoloGrezzo)) {
            paragrafoVisibile += textService.primaMaiuscola(titoloGrezzo);
            if (textService.isValid(wikiTitleBase)) {
                paragrafoVisibile += PIPE;
                paragrafoVisibile += textService.primaMaiuscola(titoloGrezzo);
                paragrafoVisibile = textService.setDoppieQuadre(paragrafoVisibile);
            }
            if (numVoci > 0) {
                paragrafoVisibile = setParagrafo(paragrafoVisibile, numVoci);
            }
        }
        else {
            paragrafoVisibile = "Altre...";
            paragrafoVisibile = setParagrafo(paragrafoVisibile, numVoci);
        }

        return paragrafoVisibile;
    }

    /**
     * Inserisce un numero in caratteri ridotti <br>
     *
     * @param numero da visualizzare
     *
     * @return testo coi tag html
     */
    public String smallNumero(final int numero) {
        String testo = VUOTA;

        testo += "<span style=\"font-size:70%\">(";
        testo += numero;
        testo += ")</span>";

        return testo;
    }

    public String setParagrafoSub(final String titolo) {
        String paragrafo = VUOTA;

        paragrafo += PARAGRAFO_SUB;
        paragrafo += SPAZIO;
        paragrafo += titolo;
        paragrafo += SPAZIO;
        paragrafo += PARAGRAFO_SUB;
        paragrafo += CAPO;

        return paragrafo;
    }

    public String setParagrafo(final String titolo) {
        return setParagrafo(titolo, 0);
    }

    /**
     * Inserisce un numero in caratteri ridotti <br>
     *
     * @param titolo da inglobare nei tag wiki (paragrafo)
     * @param numero da visualizzare (maggiore di zero)
     *
     * @return testo coi tag html
     */
    public String setParagrafo(final String titolo, final int numero) {
        String paragrafo = VUOTA;

        paragrafo += CAPO;
        paragrafo += PARAGRAFO;
        paragrafo += SPAZIO;
        paragrafo += titolo;
        if (numero > 0) {
            paragrafo += SPAZIO;
            paragrafo += smallNumero(numero);
        }
        paragrafo += SPAZIO;
        paragrafo += PARAGRAFO;
        paragrafo += CAPO;

        return paragrafo;
    }

    /**
     * Contorna il testo con un wiki bold. <br>
     *
     * @param stringaIn in ingresso
     *
     * @return stringa regolata secondo la property mediawiki
     */
    public String bold(String stringaIn) {
        String stringaOut = VUOTA;

        if (textService.isValid(stringaIn)) {
            stringaOut = TAG_BOLD;
            stringaOut += stringaIn;
            stringaOut += TAG_BOLD;
        }

        return stringaOut.trim();
    }


    public int getSizeAll(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa) {
        int size = 0;

        if (mappa != null) {
            for (String key : mappa.keySet()) {
                size += getSize(mappa.get(key));
            }
        }

        return size;
    }

    public int getSize(LinkedHashMap<String, List<String>> mappa) {
        int size = 0;

        if (mappa != null) {
            for (String key : mappa.keySet()) {
                size += mappa.get(key).size();
            }
        }

        return size;
    }

    public String nati(String anno) {
        return natiMorti("Nati", anno);
    }

    public String morti(String anno) {
        return natiMorti("Morti", anno);
    }

    /**
     * I numeri che iniziano (parlato) con vocale richiedono l'apostrofo  <br>
     * Sono:
     * 1
     * 11
     * tutti quelli che iniziano con 8
     *
     * @param tag  nati/morti
     * @param anno di riferimento
     *
     * @return titolo della pagina wiki
     */
    private String natiMorti(String tag, String anno) {
        String testo = VUOTA;
        String tagSpecifico = "l'";
        tag += " nel";
        List<Integer> lista = Arrays.asList(1, 8, 11);
        String regex = "(1|11|8.*)";

        if (textService.isEmpty(anno)) {
            return VUOTA;
        }

        if (Pattern.matches(regex, anno)) {
            testo = tag + tagSpecifico + anno;
        }
        else {
            testo = tag + SPAZIO + anno;
        }

        return testo;
    }

}
