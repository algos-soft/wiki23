package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
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
        WResult result = WResult.valido()
                .queryType(AETypeQuery.getLoggatoConCookies)
                .typePage(AETypePage.indeterminata);
        queryType = AETypeQuery.getLoggatoConCookies;
        String strisciaIds;
        String message;
        AETypeUser type;
        int num;
        String urlDomain;
        int size = listaPageids != null ? listaPageids.size() : 0;
        int max = 500; //--come da API mediaWiki
        int cicli;

        if (listaPageids == null) {
            message = "Nessun valore per la lista di pageIds";
            return WResult.errato(message).queryType(AETypeQuery.getLoggatoConCookies).fine();
        }
        num = listaPageids != null ? listaPageids.size() : 0;

        type = botLogin != null ? botLogin.getUserType() : null;
        result.setCookies(botLogin != null ? botLogin.getCookies() : null);
        result.limit(max);
        result.userType(type);
        switch (type) {
            case anonymous, user, admin -> {
                if (num > type.getLimit()) {
                    message = String.format("Sei collegato come %s e nella request ci sono %s pageIds", type, textService.format(num));
                    logger.info(new WrapLog().exception(new AlgosException(message)).usaDb());
                    return WResult.errato(message).queryType(AETypeQuery.getLoggatoConCookies).fine();
                }
                else {
                    strisciaIds = array.toStringaPipe(listaPageids);
                    urlDomain = WIKI_QUERY_TIMESTAMP + strisciaIds;
                    return requestGet(result, urlDomain);
                }
            }
            case bot -> {}
            default -> {}
        }

        //--type=bot cicli di request
        cicli = size > max ? listaPageids.size() / max : 1;
        cicli = size > max ? cicli + 1 : cicli;
        result.setCicli(cicli);
        for (int k = 0; k < cicli; k++) {
            strisciaIds = array.toStringaPipe(listaPageids.subList(k * max, Math.min((k * max) + max, size)));
            urlDomain = WIKI_QUERY_BASE_PAGE + strisciaIds;
            result = requestGet(result, urlDomain);
        }

        result.setGetRequest(VUOTA);
        return result;
        //        return requestGetTitle(WIKI_QUERY_BASE_PAGE, arrayService.toStringaPipe(listaPageids));
    }


    protected WResult checkInizialePipe(WResult result, final String wikiTitleGrezzo) {
        return result;
    }

    @Override
    public String fixWikiTitle(String wikiTitleGrezzo) {
        return wikiTitleGrezzo;
    }

    @Override
    protected WResult elaboraResponse(WResult result, String rispostaDellaQuery) {
        result = super.elaboraResponse(result, rispostaDellaQuery);
        String tmplBio;
        long pageId = result.getPageid();
        List<WrapBio> listaWrap;
        WrapBio wrapBio;

        //--controllo del missing e leggera modifica delle informazioni di errore
        if (result.getErrorCode().equals(KEY_JSON_MISSING_TRUE)) {
            result.setErrorMessage(String.format("La pagina bio '%s' non esiste", result.getWikiTitle()));
            result.typePage(AETypePage.nonEsiste);
            result.setWrap(new WrapBio().valida(false).pageid(pageId).type(AETypePage.nonEsiste));
            return result;
        }

        if (mappaUrlResponse.get(KEY_JSON_NUM_PAGES) instanceof Integer numPages) {
            if (numPages == 0) {
                result.setErrorMessage("Qualcosa non ha funzionato");
                result.setWrap(new WrapBio().valida(false).pageid(pageId).type(AETypePage.indeterminata));
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
