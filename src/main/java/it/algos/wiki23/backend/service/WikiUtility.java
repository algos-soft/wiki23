package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

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

        paragrafo += PARAGRAFO;
        paragrafo += SPAZIO;
        paragrafo += titolo;
        if (numero > 0) {
            paragrafo += SPAZIO;
            paragrafo += smallNumero(numero);
        }
        paragrafo += SPAZIO;
        paragrafo += PARAGRAFO;

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

}
