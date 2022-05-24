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
 * Query per recuperare i timestamp da una serie di pageIds <br>
 * È di tipo GET <br>
 * Necessita dei cookies, recuperati da BotLogin (singleton) <br>
 * Restituisce una lista di MiniWrap con 'pageid' e 'lastModifica' <br>
 * <p>
 * Riceve una lista di pageIds ed esegue una serie di request ognuna col valore massimo di elementi ammissibile per le API di MediWiki <br>
 * Accumula i risultati <br>
 * La query restituisce SOLO MiniWrap <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryTimestamp extends AQuery {


    /**
     * Miniwrap delle pagine wiki <br>
     *
     * @param listaPageids lista dei pageIds delle pagine wiki da controllare
     *
     * @return lista di wraps delle pagine
     */
    public List<MiniWrap> getWrap(final List<Long> listaPageids) {
        return urlRequest(listaPageids).getLista();
    }


    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     * <p>
     * Nella risposta negativa la gerarchia è: <br>
     * ....batchcomplete <br>
     * <p>
     * Nella risposta positiva la gerarchia è: <br>
     * ....batchcomplete <br>
     * ....query <br>
     * ........pages <br>
     * ............[0] <br>
     * ............[1] <br>
     * ................pageid <br>
     * ................title <br>
     * ................revisions <br>
     * ....................[0] (sempre solo uno con la query utilizzata) <br>
     * ........................revid <br>
     * ........................parentid <br>
     * ........................timestamp <br>
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
            urlDomain = WIKI_QUERY_TIMESTAMP + strisciaIds;
            result = requestGet(result, urlDomain);
        }

        result.setGetRequest(VUOTA);
        return result;
    }


    protected WResult checkInizialePipe(WResult result, final String wikiTitleGrezzo) {
        return result;
    }


    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        List<MiniWrap> listaNew = new ArrayList<>();
        List<MiniWrap> listaOld;
        MiniWrap miniWrap;
        long pageid;
        String wikiTitle;
        JSONObject revisionZero;
        String timeStamp = VUOTA;
        result = super.elaboraResponse(result, rispostaDellaQuery);
        result.typePage(AETypePage.pageIds);
        result.setWikiTitle(VUOTA);
        result.setPageid(0L);

        if (mappaUrlResponse.get(KEY_JSON_PAGES) instanceof JSONArray jsonPages) {
            if (jsonPages.size() > 0) {
                for (Object obj : jsonPages) {
                    pageid = (long) ((JSONObject) obj).get(KEY_JSON_PAGE_ID);
                    wikiTitle = (String) ((JSONObject) obj).get(KEY_JSON_TITLE);
                    if (((JSONObject) obj).get(KEY_JSON_REVISIONS) instanceof JSONArray jsonRevisions) {
                        if (jsonRevisions != null && jsonRevisions.size() == 1) {
                            revisionZero = (JSONObject) jsonRevisions.get(0);
                            timeStamp = (String) revisionZero.get(KEY_JSON_TIMESTAMP);
                        }
                        if (pageid > 0 && textService.isValid(timeStamp)) {
                            miniWrap = new MiniWrap(pageid, wikiTitle, timeStamp);
                            listaNew.add(miniWrap);
                        }
                    }
                }
                result.setCodeMessage(JSON_SUCCESS);
                listaOld = (List<MiniWrap>) result.getLista();
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
            }
        }

        return result;
    }

}
