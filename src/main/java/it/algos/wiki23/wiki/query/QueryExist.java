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
 * Date: ven, 29-apr-2022
 * Time: 11:13
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
public class QueryExist extends AQuery {

    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param wikiTitlePageid della pagina wiki (necessita di codifica) usato nella urlRequest
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final Object wikiTitlePageid) {
        queryType = AETypeQuery.get;
        if (wikiTitlePageid instanceof String wikiTitleGrezzo) {
            return requestGetTitle(WIKI_QUERY, wikiTitleGrezzo);
        }
        if (wikiTitlePageid instanceof Integer pageid) {
            return requestGetPage(WIKI_QUERY_PAGEIDS, pageid);
        }
        if (wikiTitlePageid instanceof Long pageid) {
            return requestGetPage(WIKI_QUERY_PAGEIDS, pageid);
        }

        return WResult.errato("I parametri della urlRequest sono errati ");
    }


    /**
     * Esistenza della pagina <br>
     *
     * @param wikiTitlePageid della pagina wiki (necessita di codifica) usato nella urlRequest
     *
     * @return esistenza della pagina
     */
    public boolean isEsiste(final Object wikiTitlePageid) {
        return urlRequest(wikiTitlePageid).isValido();
    }


    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta <br>
     * Controllo del contenuto (testo) ricevuto <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * Nella risposta positiva la gerarchia è:
     * batchcomplete
     * query
     *      normalized
     *      pages
     *          [0]
     *             missing
     *             title
     * @param rispostaDellaQuery in formato JSON da elaborare
     *
     * @return wrapper di informazioni
     */
    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        result = super.elaboraResponse(result, rispostaDellaQuery);
        String message;

        if ((boolean) mappaUrlResponse.get(KEY_JSON_BATCH)) {
            return result;
        }
        else {
            result.setErrorCode("batchcomplete=false");
            message = String.format("Qualcosa non ha funzionato nella lettura della pagina wiki '%s'", result.getWikiTitle());
            result.setErrorMessage(message);
            return result;
        }

    }

}
