package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.json.simple.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: mar, 10-mag-2022
 * Time: 15:09
 * UrlRequest:
 * urlDomain = "&prop=categoryinfo"
 * GET request
 * No POST text
 * No upload cookies
 * No bot needed
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryInfoCat extends AQuery {


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
        queryType = AETypeQuery.get;
        return requestGetTitle(WIKI_QUERY_CAT_INFO, CAT + wikiTitleGrezzo);
    }


    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     */
    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        result = super.elaboraResponse(result, rispostaDellaQuery);
        Long pagine = 0L;

        //--controllo del missing e leggera modifica delle informazioni di errore
        if (result.getErrorCode().equals(KEY_JSON_MISSING_TRUE)) {
            result.setErrorMessage(String.format("La categoria wiki '%s' non esiste", result.getWikiTitle()));
        }

        if (result.getWrap() != null && result.getWrap().isValida()) {
            result.getWrap().type(AETypePage.categoria);
            result.getWrap().valida(false);
        }

        if (mappaUrlResponse.get(KEY_JSON_CATEGORY_INFO) instanceof JSONObject jsonCategoryInfo) {
            Object alfa = jsonCategoryInfo;
            if (jsonCategoryInfo.get(KEY_JSON_PAGES) != null) {
                pagine = (Long) jsonCategoryInfo.get(KEY_JSON_PAGES);
                result.setIntValue(pagine.intValue());
            }
        }

        return result;
    }

}
