package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import static it.algos.wiki23.backend.service.WikiApiService.*;
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
 * Date: mer, 11-mag-2022
 * Time: 06:48
 * Query per recuperare una lista di pageIds di una categoria wiki <br>
 * È di tipo GET <br>
 * Necessita dei cookies, recuperati da BotLogin (singleton) <br>
 * Restituisce una lista di pageIds <br>
 * <p>
 * Ripete la request finché riceve un valore valido di cmcontinue <br>
 * La query restituisce SOLO pageIds <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryCat extends AQuery {

    /**
     * Lista dei pageids di una categoria <br>
     *
     * @param catTitleGrezzo della categoria wiki (necessita di codifica) usato nella urlRequest
     *
     * @return lista (pageids) degli elementi contenuti nella categoria
     */
    public List<Long> getListaPageIds(final String catTitleGrezzo) {
        return (List<Long>) urlRequest(catTitleGrezzo).getLista();
    }

    /**
     * Request principale <br>
     * <p>
     * Non accetta il separatore PIPE nel wikiTitoloGrezzoPaginaCategoria <br>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     * <p>
     * Nella risposta negativa la gerarchia è: <br>
     * ....error <br>
     * ........code (forse asserbotfailed) <br>
     * ........docref (info -> You do not have the "bot" right, so the action could not be completed) <br>
     * ........info <br>
     * <p>
     * Nella risposta positiva la gerarchia è: <br>
     * ....batchcomplete <br>
     * ....continue <br>
     * ........continue <br>
     * ........cmcontinue <br>
     * ....query <br>
     * ........normalized <br>
     * ........categorymembers (potrebbe essere vuoto ma tipicamente più di uno) <br>
     * ............[0] <br>
     * ............[1] <br>
     * ................pageid <br>
     *
     * @param wikiTitoloGrezzoPaginaCategoria della pagina/categoria wiki (necessita di codifica) usato nella urlRequest. Non accetta il separatore PIPE
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final String wikiTitoloGrezzoPaginaCategoria) {
        queryType = AETypeQuery.getCookies;
        WResult result = checkIniziale(QUERY_CAT_REQUEST, CAT + wikiTitoloGrezzoPaginaCategoria);
        if (result.isErrato()) {
            return result;
        }
        if (wikiTitoloGrezzoPaginaCategoria == null) {
            return result.errato("Il wikiTitle è nullo");
        }

        String urlDomain = VUOTA;
        String tokenContinue = VUOTA;
        String message = VUOTA;
        URLConnection urlConn;
        String urlResponse = VUOTA;
        int pageIdsRecuperati = 0;
        int cicli = 0;

        if (botLogin == null) {
            result.errorMessage("Manca il botLogin");
            return result;
        }
        else {
            if (botLogin != null) {
                result.setUserType(botLogin.getUserType());
                result.setLimit(botLogin.getUserType().getLimit());
            }
        }

        urlDomain = fixUrlCat(wikiTitoloGrezzoPaginaCategoria, VUOTA);
        result.setUrlPreliminary(urlDomain);
        try {
            do {
                urlDomain = fixUrlCat(wikiTitoloGrezzoPaginaCategoria, tokenContinue);
                urlConn = this.creaGetConnection(urlDomain);
                uploadCookies(urlConn, botLogin != null ? botLogin.getCookies() : null);
                urlResponse = sendRequest(urlConn);
                result = elaboraResponse(result, urlResponse);
                result.setCicli(++cicli);
                tokenContinue = result.getToken();
            }
            while (textService.isValid(tokenContinue));
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
        }

        pageIdsRecuperati = result.getIntValue();
        result.setUrlRequest(urlDomain);
        message = String.format("Recuperati %s pageIds dalla categoria '%s' in %d cicli", textService.format(pageIdsRecuperati), wikiTitoloGrezzoPaginaCategoria, cicli);
        result.setMessage(message);

        return result;
    }


    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        List<Long> listaNew = new ArrayList<>();
        List<Long> listaOld;
        long pageid;
        result = super.elaboraResponse(result, rispostaDellaQuery);
        String tokenContinue = VUOTA;

        if (mappaUrlResponse.get(KEY_JSON_CONTINUE) instanceof JSONObject jsonContinue) {
            tokenContinue = (String) jsonContinue.get(KEY_JSON_CONTINUE_CM);
        }
        result.setToken(tokenContinue);

        if (mappaUrlResponse.get(KEY_JSON_CATEGORY_MEMBERS) instanceof JSONArray jsonCategory) {
            if (jsonCategory.size() > 0) {
                for (Object obj : jsonCategory) {
                    pageid = (long) ((JSONObject) obj).get(PAGE_ID);
                    listaNew.add(pageid);
                }
                result.setCodeMessage(JSON_SUCCESS);
                listaOld = (List<Long>) result.getLista();
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
                result.setErrorMessage("Non ci sono pagine nella categoria");
            }
        }

        return result;
    }

    //    private String fixUrlCat(final String catTitle) {
    //        return fixUrlCat(catTitle, VUOTA);
    //    }

    /**
     * Costruisce un url come user/admin/bot <br>
     * Come 'anonymous' tira 500 pagine
     * Come 'bot' tira 5.000 pagine
     *
     * @param catTitle      da cui estrarre le pagine
     * @param continueParam per la successiva query
     *
     * @return testo dell'url
     */
    private String fixUrlCat(final String catTitle, final String continueParam) {
        String query = QUERY_CAT_REQUEST + CAT + wikiBot.wikiApiService.fixWikiTitle(catTitle);
        String type = WIKI_QUERY_CAT_TYPE + "page";
        String prop = WIKI_QUERY_CAT_PROP + "ids";//--potrebbe essere anche "ids|title"
        String limit = botLogin.getUserType().limit();
        String user = botLogin.getUserType().affermazione();

        if (textService.isValid(continueParam)) {
            String continua = WIKI_QUERY_CAT_CONTINUE + continueParam;
            return String.format("%s%s%s%s%s%s", query, type, prop, limit, user, continua);
        }
        else {
            return String.format("%s%s%s%s%s", query, type, prop, limit, user);
        }

    }

}
