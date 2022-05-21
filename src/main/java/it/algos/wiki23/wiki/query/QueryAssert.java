package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.net.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 24-lug-2021
 * Time: 18:52
 * <p>
 * Query di controllo per 'testare' il collegamento come bot <br>
 * È di tipo GET <br>
 * Necessita dei cookies, recuperati da BotLogin (singleton) <br>
 * Restituisce true or false <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryAssert extends AQuery {


    public QueryAssert() {
        super();
    }


    /**
     * La request unica <br>
     * <p>
     * Nella risposta negativa la gerarchia è: <br>
     * ....error <br>
     * ........code <br>
     * ........docref <br>
     * ........info <br>
     * <p>
     * Nella risposta positiva la gerarchia è: <br>
     * ....batchcomplete=true <br>
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequest() {
        WResult result = WResult.valido();
        Map<String, String> cookies;
        String urlDomain = TAG_REQUEST_ASSERT;
        String urlResponse = VUOTA;
        URLConnection urlConn;

        result.setUrlRequest(TAG_REQUEST_ASSERT);
        result.setQueryType(AETypeQuery.getLoggatoConCookies);

        //--se manca il botLogin
        if (botLogin == null) {
            result.setValido(false);
            result.setErrorCode(JSON_BOT_LOGIN);
            result.setMessage("Manca il botLogin");
            return result;
        }

        //--se il botLogin non ha registrato nessuna chiamata di QueryLogin
        if (botLogin.getResult() == null) {
            result.setValido(false);
            result.setErrorCode(JSON_NOT_QUERY_LOGIN);
            result.setMessage("Il botLogin non ha registrato nessuna chiamata di QueryLogin");
            return result;
        }

        //--se mancano i cookies
        cookies = botLogin.getCookies();
        if (cookies == null || cookies.size() < 1) {
            result.setValido(false);
            result.setErrorCode(JSON_COOKIES);
            result.setMessage("Mancano i cookies nel result di botLogin");
            return result;
        }

        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, cookies);
            urlResponse = sendRequest(urlConn);
        } catch (Exception unErrore) {
        }

        return elaboraResponse(result, urlResponse);
    }

    /**
     * Esistenza del botLogin valido e del collegamento come bot <br>
     *
     * @return esistenza del botLogin valido
     */
    public boolean isEsiste() {
        return urlRequest().isValido();
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Recupera il token 'logintoken' dalla preliminaryRequestGet <br>
     * Viene convertito in lgtoken necessario per la successiva secondaryRequestPost <br>
     */
    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        result = super.elaboraResponse(result, rispostaDellaQuery);

        if ((boolean) mappaUrlResponse.get(KEY_JSON_MISSING)) {
            result.setValido(false);
        }

        if ((boolean) mappaUrlResponse.get(KEY_JSON_ERROR)) {
            result.setValido(false);
        }

        return result;
    }


}
