package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
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
        String strisciaIds;
        queryType = AETypeQuery.getLoggatoConCookies;

        strisciaIds = array.toStringaPipe(listaPageids);
        return requestGetTitle(WIKI_QUERY_TIMESTAMP, strisciaIds);
    }


    protected WResult checkInizialePipe(WResult result, final String wikiTitleGrezzo) {
        return result;
    }


    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        List<MiniWrap> listaWraps = null;
        MiniWrap miniWrap;
        long pageid;
        String wikiTitle;
        JSONObject revisionZero = null;
        String timeStamp = VUOTA;
        result = super.elaboraResponse(result, rispostaDellaQuery);
        result.setWikiTitle(VUOTA);

        if (mappaUrlResponse.get(KEY_JSON_PAGES) instanceof JSONArray jsonPages) {
            result.setIntValue(jsonPages.size());
            if (jsonPages.size() > 0) {
                listaWraps = new ArrayList<>();
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
                            listaWraps.add(miniWrap);
                        }
                    }
                }
                result.setLista(listaWraps);
            }
            else {
                //                result.setErrorMessage("Non ci sono pagine nella categoria");
            }
        }

        return result;
    }

}
