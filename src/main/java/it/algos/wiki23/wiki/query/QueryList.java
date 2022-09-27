package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import static it.algos.wiki23.backend.service.WikiBotService.*;
import it.algos.wiki23.backend.wrapper.*;
import org.json.simple.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.net.*;
import java.util.*;

/**
 * Project wiki23
 * Created by Algos
 * User: gac
 * Date: Wed, 21-Sep-2022
 * Time: 19:17
 * Query per recuperare una lista di wikiTitle che iniziano con ... <br>
 * È di tipo GET <br>
 * NON necessita dei cookies <br>
 * Restituisce una lista di wikiTitle <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryList extends AQuery {

    private static final int ZERO_NAME_SPACE = 0;

    private int nameSpace = ZERO_NAME_SPACE;

    public QueryList nameSpace(int nameSpace) {
        this.nameSpace = nameSpace;
        return this;
    }

    /**
     * Lista di pagine <br>
     *
     * @param tagIniziale delle pagine wiki da trovare.
     *
     * @return wrap della pagina
     */
    public List<String> getLista(final String tagIniziale) {
        return urlRequest(tagIniziale).getLista();
    }

    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param tagIniziale delle pagine wiki da trovare.
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest( String tagIniziale) {
        queryType = AETypeQuery.post;
        WResult result = checkInizialeBase(tagIniziale).limit(500);

        try {
            tagIniziale = URLEncoder.encode(tagIniziale, ENCODE);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
        }

        String urlDomain = WIKI_QUERY_LIST + tagIniziale + LIST_NAME_SPACE + nameSpace;
//        result = requestGet(result, urlDomain);

//        urlDomain = fixUrlCat(wikiTitoloGrezzoPaginaCategoria, VUOTA);
//        result.setCookies(botLogin != null ? botLogin.getCookies() : null);
        result.setGetRequest(urlDomain);

        return urlRequestContinue(result, urlDomain);
    }


    public WResult urlRequestContinue(WResult result, final String urlDomainGrezzo) {
        String message;
        String urlDomain;
        String tokenContinue = VUOTA;
        URLConnection urlConn;
        String urlResponse;
        int cicli = 0;

        try {
            do {
                urlDomain = urlDomainGrezzo + tokenContinue;
                urlConn = this.creaGetConnection(urlDomain);
                urlResponse = sendRequest(urlConn);
                result = elaboraResponse(result, urlResponse);
                if (result.isValido()) {
                    result.setCicli(++cicli);
                }
                tokenContinue = WIKI_QUERY_LIST_CONTINUE + result.getToken();
            }
            while (textService.isValid(result.getToken()));
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
        }

        if (result.isValido()) {
//            pageIdsRecuperati = result.getIntValue();
//            message = String.format("Recuperati %s pageIds dalla categoria '%s' in %d cicli", textService.format(pageIdsRecuperati), info, cicli);
//            result.setMessage(message);
        }
        else {
//            message = String.format("Nessun pageIds dalla categoria '%s'");
//            result.setMessage(message);
        }

        return result;
    }

    @Override
    protected WResult elaboraResponse(WResult result, String rispostaDellaQuery) {
        List<String> listaNew = new ArrayList<>();
        List<String> listaOld;
        String title;
        result = super.elaboraResponse(result, rispostaDellaQuery);
        result.typePage(AETypePage.listWikiTitles);
        result.setWikiTitle(VUOTA);
        result.setPageid(0L);
        JSONArray jsonPages;
        String tokenContinue = VUOTA;

        if (result.isErrato()) {
            return result;
        }

        if (mappaUrlResponse.get(KEY_JSON_CONTINUE) instanceof JSONObject jsonContinue) {
            tokenContinue = (String) jsonContinue.get(KEY_JSON_CONTINUE_LIST);
            try {
                tokenContinue = URLEncoder.encode(tokenContinue, ENCODE);
            } catch (Exception unErrore) {
                logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
            }
        }
        result.setToken(tokenContinue);

        if (mappaUrlResponse.get(KEY_JSON_QUERY) != null && mappaUrlResponse.get(KEY_JSON_QUERY) instanceof JSONObject jsonQuery) {
            jsonPages = (JSONArray) jsonQuery.get(KEY_JSON_ALL_PAGES);

            if (jsonPages != null && jsonPages.size() > 0) {
                for (Object obj : jsonPages) {
                    title = (String) ((JSONObject) obj).get(KEY_JSON_TITLE);
                    listaNew.add(title);
                }

                result.setCodeMessage(JSON_SUCCESS);
                listaOld = (List<String>) result.getLista();
                if (listaOld != null) {
                    listaOld.addAll(listaNew);
                }
                else {
                    listaOld = listaNew;
                }
                result.setLista(listaOld);
                result.setIntValue(listaOld.size());
                return result;
            }
            else {
                result.setErrorMessage("Qualcosa non ha funzionato");
                result.setWrap(new WrapBio().valida(false).type(AETypePage.indeterminata));
            }
        }

        return result;
    }

}
