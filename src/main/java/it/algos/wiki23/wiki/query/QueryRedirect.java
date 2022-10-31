package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Mon, 31-Oct-2022
 * Time: 09:44
 * Controllo semplice per l'esistenza della pagina <br>
 * Una request di tipo GET, senza necessità di collegamento come Bot <br>
 * UrlRequest:
 * urlDomain = "&action=query&titles=="
 * GET request
 * No POST text
 * No upload cookies
 * No bot needed
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryRedirect extends AQuery {


    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param wikiTitoloGrezzoPagina usato nella urlRequest.
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final String wikiTitoloGrezzoPagina) {
        queryType = AETypeQuery.getSenzaLoginSenzaCookies;
        return requestGetTitle(WIKI_QUERY_BASE_TITLE, wikiTitoloGrezzoPagina);
    }


    /**
     * Esistenza del redirect <br>
     *
     * @param wikiTitoloGrezzoPagina usato nella urlRequest.
     *
     * @return true se il redirect esiste
     */
    public boolean isRedirect(final String wikiTitoloGrezzoPagina) {
        WResult result = urlRequest(wikiTitoloGrezzoPagina);
        return result.getTypePage() == AETypePage.redirect;
    }


    /**
     * Valore del wikiLink del redirect <br>
     *
     * @param wikiTitoloGrezzoPagina usato nella urlRequest.
     *
     * @return Valore del wikiLink
     */
    public String getWikiLink(final String wikiTitoloGrezzoPagina) {
        WResult result = urlRequest(wikiTitoloGrezzoPagina);
        return result.getTxtValue();
    }


    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta <br>
     * Controllo del contenuto (testo) ricevuto <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param rispostaDellaQuery in formato JSON da elaborare
     *
     * @return wrapper di informazioni
     */
    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        result = super.elaboraResponse(result, rispostaDellaQuery);
        result.setResponse(rispostaDellaQuery);

        if (result.getTypePage() == AETypePage.redirect) {
            result.setValido();
        }
        else {
            result.setValido(false);
        }

        return result;
    }

}
