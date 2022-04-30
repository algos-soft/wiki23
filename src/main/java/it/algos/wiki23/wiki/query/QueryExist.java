package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.interfaces.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.json.simple.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.net.*;

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
     * @param wikiTitleGrezzo della pagina wiki (necessita di codifica) usato nella urlRequest
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final String wikiTitleGrezzo) {
        return requestGet(WIKI_QUERY, wikiTitleGrezzo);
    }

    /**
     * Esistenza della pagina <br>
     *
     * @param wikiTitleGrezzo della pagina wiki (necessita di codifica) usato nella urlRequest
     *
     * @return esistenza della pagina
     */
    public boolean isEsiste(final String wikiTitleGrezzo) {
        return urlRequest(wikiTitleGrezzo).isValido();
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     */
    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        JSONObject jsonQuery = null;
        JSONArray jsonPages = null;
        JSONObject jsonPageZero = null;
        String message;
        WrapBio wrap;
        JSONObject jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);

        //--fissa durata
        result.setFine();

        //--controllo del batchcomplete
        if (jsonAll != null && jsonAll.get(KEY_JSON_VALID) != null) {
            if (!(boolean) jsonAll.get(KEY_JSON_VALID)) {
                result.setErrorCode("batchcomplete=false");
                result.setErrorMessage(String.format("Qualcosa non ha funzionato nella lettura delle pagina wiki '%s'", result.getWikiTitle()));
                return result;
            }
        }

        //--controllo dell'errore
        if (jsonAll != null && jsonAll.get(KEY_JSON_ERROR) != null && jsonAll.get(KEY_JSON_ERROR) instanceof JSONObject jsonError) {
            if (jsonError != null && jsonError.get(KEY_JSON_CODE) != null && jsonError.get(KEY_JSON_CODE) instanceof String errorMessage) {
                result.setErrorCode(errorMessage);
            }
            if (jsonError != null && jsonError.get(KEY_JSON_INFO) != null && jsonError.get(KEY_JSON_INFO) instanceof String infoMessage) {
                result.setErrorMessage(infoMessage);
            }
            return result;
        }

        if (jsonAll != null && jsonAll.get(KEY_JSON_QUERY) != null) {
            jsonQuery = (JSONObject) jsonAll.get(KEY_JSON_QUERY);
        }

        if (jsonQuery != null && jsonQuery.get(KEY_JSON_PAGES) != null) {
            jsonPages = (JSONArray) jsonQuery.get(KEY_JSON_PAGES);
        }

        if (jsonPages != null && jsonPages.size() > 0) {
            jsonPageZero = (JSONObject) jsonPages.get(0);
        }

        if (jsonPageZero == null) {
            message = "jsonPageZero mancante";
            logger.error(new WrapLog().exception(new AlgosException(message)).usaDb());
            return WResult.errato(message);
        }

        //--controllo del missing
        if (jsonPageZero.get(KEY_JSON_MISSING) != null) {
            result.setValido(false);
            result.setErrorCode("missing=true");
            result.setErrorMessage(String.format("La pagina wiki '%s' non esiste", result.getWikiTitle()));
            wrap = new WrapBio().title(result.getWikiTitle()).type(AETypePage.nonEsiste);
            result.setWrap(wrap);
            return result;
        }
        else {
            if (jsonPageZero.get(KEY_JSON_PAGE_ID) != null && jsonPageZero.get(KEY_JSON_TITLE) != null) {
                wrap = new WrapBio().valida(true).title(result.getWikiTitle()).type(AETypePage.indeterminata);
                if (jsonPageZero.get(KEY_JSON_TITLE) instanceof String wikiTitle) {
                    wrap = wrap.title(wikiTitle);
                }
                if (jsonPageZero.get(KEY_JSON_PAGE_ID) instanceof Long pageId) {
                    wrap = wrap.pageid(pageId);
                }
                message = String.format("La pagina %s esiste ma è di type %s", result.getWikiTitle(), AETypePage.indeterminata);
                result.setValidMessage(message);
                result.setWrap(wrap);
                return result;
            }
        }

        return result;
    }

}
