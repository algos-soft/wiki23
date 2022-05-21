package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.json.simple.*;
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
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryWrapBio extends AQuery {


    /**
     * Wraps di pagine biografiche <br>
     *
     * @param listaPageids lista dei pageIds delle pagine wiki da controllare
     *
     * @return lista di wraps delle pagine
     */
    public List<WrapBio> getWrap(final List<Long> listaPageids) {
        return urlRequest(listaPageids).getLista();
    }


    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param listaPageids lista dei pageIds delle pagine wiki da controllare
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final List<Long> listaPageids) {
        queryType = AETypeQuery.getLoggatoConCookies;
        return requestGetTitle(WIKI_QUERY_BASE_PAGE, arrayService.toStringaPipe(listaPageids));
    }


    protected WResult checkInizialePipe(WResult result, final String wikiTitleGrezzo) {
        return result;
    }

    @Override
    protected WResult elaboraResponse(WResult result, String rispostaDellaQuery) {
        result = super.elaboraResponse(result, rispostaDellaQuery);
        String tmplBio;
        String wikiTitle = result.getWikiTitle();
        long pageId = result.getPageid();
        List<WrapBio> listaWrap;
        WrapBio wrapBio;

        //--controllo del missing e leggera modifica delle informazioni di errore
        if (result.getErrorCode().equals(KEY_JSON_MISSING_TRUE)) {
            result.setErrorMessage(String.format("La pagina bio '%s' non esiste", result.getWikiTitle()));
            result.typePage(AETypePage.nonEsiste);
            result.setWrap(new WrapBio().valida(false).title(wikiTitle).pageid(pageId).type(AETypePage.nonEsiste));
            return result;
        }

        if (mappaUrlResponse.get(KEY_JSON_NUM_PAGES) instanceof Integer numPages) {
            if (numPages == 0) {
                result.setErrorMessage("Qualcosa non ha funzionato");
                result.setWrap(new WrapBio().valida(false).title(wikiTitle).pageid(pageId).type(AETypePage.indeterminata));
                return result;
            }

            if (numPages == 1) {
                listaWrap = new ArrayList<>();
                listaWrap.add(result.getWrap());
                result.setLista(listaWrap);
            }

            if (numPages > 1) {
                if (mappaUrlResponse.get(KEY_JSON_PAGES) instanceof JSONArray pages) {
                    listaWrap = new ArrayList<>();
                    for (Object obj : pages) {
                        JSONObject pagina = (JSONObject) obj;
                        wrapBio = getWrap(pagina);
                        listaWrap.add(wrapBio);
                    }
                    result.setLista(listaWrap);
                }
            }
        }

        return result;
    }

}
