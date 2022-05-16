package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: dom, 15-mag-2022
 * Time: 07:19
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class QueryWrapBio extends AQuery {

    /**
     * Wraps di pagine biografica <br>
     *
     * @param wikiTitleGrezzo della pagina wiki (necessita di codifica) usato nella urlRequest
     *
     * @return wrap della pagina
     */
    public List<WrapBio> getWrap(final String wikiTitleGrezzo) {
        return urlRequest(wikiTitleGrezzo).getLista();
    }


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
        return requestGetTitle(WIKI_QUERY_BASE_TITLE, wikiTitleGrezzo);
    }

    @Override
    protected WResult elaboraResponse(WResult result, String rispostaDellaQuery) {
        result = super.elaboraResponse(result, rispostaDellaQuery);
        String tmplBio;
        String wikiTitle = result.getWikiTitle();
        long pageId = result.getPageid();

        if (mappaUrlResponse.get(KEY_JSON_NUM_PAGES) instanceof Integer numPages) {
            if (numPages == 0) {
                result.setErrorMessage("Qualcosa non ha funzionato");
                result.setWrap(new WrapBio().valida(false).title(wikiTitle).pageid(pageId).type(AETypePage.indeterminata));
                return result;
            }

            if (numPages == 1) {
            }
            if (numPages > 1) {
            }

        }

        return result;
    }

}
