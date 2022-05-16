package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.SpringComponent;
import static it.algos.vaad23.backend.boot.VaadCost.*;
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
 * Date: ven, 29-apr-2022
 * Time: 19:42
 * UrlRequest:
 * urlDomain = "&rvslots=main&prop=revisions&rvprop=content|ids|timestamp"
 * GET request
 * No POST text
 * No upload cookies
 * No bot needed
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryBio extends AQuery {


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
     * ....batchcomplete <br>
     * ....query <br>
     * ........normalized <br>
     * ........pages <br>
     * ............[0] (sempre solo uno se non si usa il PIPE) <br>
     * ................ns <br>
     * ................missing=true <br>
     * ................pageid <br>
     * ................title <br>
     * <p>
     * Nella risposta positiva la gerarchia è: <br>
     * ....batchcomplete <br>
     * ....query <br>
     * ........normalized <br>
     * ........pages <br>
     * ............[0] (sempre solo uno se non si usa il PIPE) <br>
     * ................pageid <br>
     * ................title <br>
     * ................revisions <br>
     * ....................[0] (sempre solo uno con la query utilizzata) <br>
     * ........................revid <br>
     * ........................parentid <br>
     * ........................timestamp <br>
     * ........................slots <br>
     * ............................main <br>
     * ................................contentformat <br>
     * ................................contentmodel <br>
     * ................................content (da cui estrarre il tmpl bio) <br>
     *
     * @param wikiTitleGrezzoBio della pagina biografica wiki (necessita di codifica) usato nella urlRequest. Non accetta il separatore PIPE
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final String wikiTitleGrezzoBio) {
        queryType = AETypeQuery.get;
        return requestGetTitle(WIKI_QUERY_BASE_TITLE, wikiTitleGrezzoBio);
    }

    /**
     * Wrap della pagina biografica <br>
     *
     * @param wikiTitleGrezzoBio della pagina biografica wiki (necessita di codifica) usato nella urlRequest. Non accetta il separatore PIPE
     *
     * @return wrap della pagina
     */
    public WrapBio getWrap(final String wikiTitleGrezzoBio) {
        return urlRequest(wikiTitleGrezzoBio).getWrap();
    }

    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene elaborata <br>
     * Si crea una connessione di tipo GET <br>
     * Si invia la request <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param pageid della pagina wiki usato nella urlRequest
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest(final long pageid) {
        queryType = AETypeQuery.get;
        return requestGetPage(WIKI_QUERY_BASE_PAGE, pageid);
    }

    /**
     * Wrap della pagina biografica <br>
     *
     * @param pageid della pagina wiki usato nella urlRequest
     *
     * @return wrap della pagina
     */
    public WrapBio getWrap(final long pageid) {
        return urlRequest(pageid).getWrap();
    }


    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     */
    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        result = super.elaboraResponse(result, rispostaDellaQuery);
        String tmplBio;
        //        String content = VUOTA;
        String wikiTitle = result.getWikiTitle();
        long pageId = result.getPageid();

        //--controllo del missing e leggera modifica delle informazioni di errore
        if (result.getErrorCode().equals(KEY_JSON_MISSING_TRUE)) {
            result.setErrorMessage(String.format("La pagina bio '%s' non esiste", result.getWikiTitle()));
            result.typePage(AETypePage.nonEsiste);
            result.setWrap(new WrapBio().type(AETypePage.nonEsiste).valida(false).title(wikiTitle));
            return result;
        }

        if (result.getTypePage() == AETypePage.disambigua) {
            result.setWrap(new WrapBio().valida(false).title(wikiTitle).pageid(pageId).type(AETypePage.disambigua));
            return result;
        }

        if (result.getTypePage() == AETypePage.redirect) {
            result.setWrap(new WrapBio().valida(false).title(wikiTitle).pageid(pageId).type(AETypePage.redirect));
            return result;
        }

        //        //--estrae informazioni dalla pagina 'zero'
        //        //--estrae il 'content' dalla pagina 'zero'
        //        if (mappaUrlResponse.get(KEY_JSON_ZERO) instanceof JSONObject jsonPageZero) {
        //            if (jsonPageZero.get(KEY_JSON_PAGE_ID) instanceof Long pageid) {
        //                pageId = pageid;
        //                result.pageid(pageId);
        //            }
        //            if (jsonPageZero.get(KEY_JSON_TITLE) instanceof String title) {
        //                wikiTitle = title;
        //                result.wikiTitle(wikiTitle);
        //            }
        //
        //            if (jsonPageZero.get(KEY_JSON_REVISIONS) instanceof JSONArray jsonRevisions) {
        //                if (jsonRevisions.size() > 0) {
        //                    JSONObject jsonRevZero = (JSONObject) jsonRevisions.get(0);
        //                    if (jsonRevZero.get(KEY_JSON_SLOTS) instanceof JSONObject jsonSlots) {
        //                        if (jsonSlots.get(KEY_JSON_MAIN) instanceof JSONObject jsonMain) {
        //                            content = (String) jsonMain.get(KEY_JSON_CONTENT);
        //                        }
        //                    }
        //                }
        //            }
        //        }

        //--controllo del 'content'
        //--controllo l'esistenza del template bio
        //--estrazione del template
        if (mappaUrlResponse.get(KEY_JSON_CONTENT) instanceof String content) {
            tmplBio = wikiBot.estraeTmpl(content);
            if (textService.isValid(tmplBio)) {
                result.setErrorCode(VUOTA);
                result.setErrorMessage(VUOTA);
                result.setCodeMessage("valida");
                result.setValidMessage(String.format("La pagina wiki '%s' è una biografia", wikiTitle));
                result.setWrap(new WrapBio().valida(true).title(wikiTitle).pageid(pageId).type(AETypePage.testoConTmpl).templBio(tmplBio));
            }
            else {
                result.setErrorCode("manca tmpl Bio");
                result.setErrorMessage(String.format("La pagina wiki '%s' esiste ma non è una biografia", wikiTitle));
                result.setWrap(new WrapBio().valida(false).title(wikiTitle).pageid(pageId).type(AETypePage.testoSenzaTmpl));

            }
            return result;
        }

        result.setErrorCode("pippoz");
        result.setErrorMessage(String.format("La pagina wiki '%s' esiste ? forse ? ", wikiTitle));
        result.setWrap(new WrapBio().type(AETypePage.indeterminata).valida(false));
        return result;
    }

}
