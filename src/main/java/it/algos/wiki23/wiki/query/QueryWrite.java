package it.algos.wiki23.wiki.query;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import static it.algos.wiki23.backend.service.WikiApiService.*;
import it.algos.wiki23.backend.wrapper.*;
import org.json.simple.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 12-feb-2022
 * Time: 20:35
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QueryWrite extends AQuery {

    public static final AETypeQuery QUERY_TYPE = AETypeQuery.postPiuCookies;

    // token di controllo recuperato dalla urlResponse della primaryRequestGet
    private String csrftoken;

    /**
     * Request al software mediawiki composta di tre passaggi:
     * 1. Log in, via one of the methods described in API:Login. Note that while this is required to correctly attribute the edit to its author, many wikis do allow users to edit without registering or logging into an account.
     * 2. GET a CSRF token.
     * 3. Send a POST request, with the CSRF token, to take action on a page.
     * <p>
     * Controllo del login già effettuato ad inizio programma <br>
     * I dati del login di collegamento (userid, username e cookies) sono nel singleton botLogin <br>
     * <p>
     * La prima request preliminare è di tipo GET, per recuperare token e session <br>
     * urlDomain = "&meta=tokens&type=csrf" <br>
     * Invia la request con i cookies di login e senza testo POST <br>
     * Recupera i cookies della connessione (in particolare 'itwikisession') <br>
     * Recupera il logintoken dalla urlResponse <br>
     * <p>
     * La seconda request è di tipo POST <br>
     * urlDomain = "&action=xxxx" <br>
     * Invia la request con i cookies ricevuti (solo 'session') <br>
     * Scrive il testo post con i valori di lgname, lgpassword e lgtoken <br>
     * <p>
     * La response viene elaborata per conferma <br>
     *
     * @param wikiTitleGrezzo della pagina wiki (necessita di codifica) usato nella urlRequest
     * @param newText         da inserire
     * @param summary         oggetto della modifica (facoltativo)
     *
     * @return wrapper di informazioni
     *
     * @see https://www.mediawiki.org/wiki/API:Edit#Example
     */
    private WResult checkWrite(final String wikiTitleGrezzo, final String newText, final String summary) {
        queryType = AETypeQuery.getLoggatoConCookies;

        WResult result = checkIniziale(QUERY_CAT_REQUEST, wikiTitleGrezzo);
        result.setLimit(0);
        if (result.isErrato()) {
            return result.queryType(AETypeQuery.getLoggatoConCookies);
        }

        if (textService.isEmpty(newText)) {
            String message = "Manca il newText da inserire";
            logger.warn(new WrapLog().exception(new AlgosException(message)));
            result.errorMessage(message);
            result.setFine();
            return result;
        }

        //--Controllo del Login
        if (!botLogin.isBot()) {
            result.setWikiTitle(wikiTitleGrezzo);
            result.setErrorMessage("Login non valido come bot");
            return result;
        }

        result.setQueryType(QUERY_TYPE);
        result.setWikiTitle(wikiTitleGrezzo);
        result.setSummary(summary);
        result.setNewtext(newText);

        if (textService.isEmpty(wikiTitleGrezzo)) {
            result.setErrorMessage("Manca il titolo della pagina wiki");
            return result;
        }

        if (textService.isEmpty(newText)) {
            result.setErrorMessage("Manca il nuovo testo da inserire");
            return result;
        }

        //--codifica del titolo (spazi vuoti e simili)
        return result;
    }

    /**
     * Request condizionata al contenuto pre-esistente <br>
     * Se cambia SOLO la parte 'modificabile' iniziale e non quella 'significativa', la query NON deve scrivere <br>
     * Se cambia ANCHE la parte 'significativa' finale, la query DEVE scrivere <br>
     *
     * @param wikiTitleGrezzo      della pagina wiki (necessita di codifica) usato nella urlRequest
     * @param newText              complessivo da inserire
     * @param newTextSignificativo successivo alla parte iniziale che può variare senza che la pagina venga riscritta
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequestCheck(final String wikiTitleGrezzo, final String newText, final String newTextSignificativo) {
        return urlRequestCheck(wikiTitleGrezzo, newText, newTextSignificativo, VUOTA);
    }

    /**
     * Request condizionata al contenuto pre-esistente <br>
     * Se cambia SOLO la parte 'modificabile' iniziale e non quella 'significativa', la query NON deve scrivere <br>
     * Se cambia ANCHE la parte 'significativa' finale, la query DEVE scrivere <br>
     *
     * @param wikiTitleGrezzo      della pagina wiki (necessita di codifica) usato nella urlRequest
     * @param newTextAll           complessivo da inserire
     * @param newTextSignificativo successivo alla parte iniziale che può variare senza che la pagina venga riscritta
     * @param summary              oggetto della modifica (facoltativo)
     *
     * @return wrapper di informazioni
     */
    public WResult urlRequestCheck(final String wikiTitleGrezzo, final String newTextAll, final String newTextSignificativo, final String summary) {
        WResult result = checkWrite(wikiTitleGrezzo, newTextAll, summary);
        if (result.isErrato()) {
            return result;
        }

        if (textService.isEmpty(newTextSignificativo)) {
            return (WResult) result.errorMessage("Manca il newTextSignificativo");
        }

        if (!newTextAll.endsWith(newTextSignificativo)) {
            return (WResult) result.errorMessage("Il newTextAll NON finisce col newTextSignificativo");
        }

        //--confronto della parte 'significativa' per decidere se registrare
        if (isModificataSignificativamente(wikiTitleGrezzo, newTextAll, newTextSignificativo)) {
            //--La prima request è di tipo GET
            //--Indispensabile aggiungere i cookies del botLogin
            result = this.primaryRequestGet(result);

            //--La seconda request è di tipo POST
            //--Indispensabile aggiungere i cookies
            //--Indispensabile aggiungere il testo POST
            return this.secondaryRequestPost(result);
        }
        else {
            result.setModificata(false);
            result.setValidMessage("Nessuna modifica sostanziale ai contenuti");
            return result;
        }
    }

    /**
     * confronto della parte 'significativa' per decidere se scriverla <br>
     * <p>
     * recupera la precedente esistente revisione della pagina <br>
     * confronto le due parti 'significative' del testo <br>
     *
     * @param wikiTitleGrezzo      della pagina wiki (necessita di codifica) usato nella urlRequest
     * @param newTextAll           complessivo da inserire
     * @param newTextSignificativo successivo alla parte iniziale che può variare senza che la pagina venga riscritta
     *
     * @return true se significativamente cambiata e deve essere scritta
     */
    public boolean isModificataSignificativamente(final String wikiTitleGrezzo, final String newTextAll, final String newTextSignificativo) {
        boolean modificataSignificativamente = true;
        String oldTextAll;

        oldTextAll = appContext.getBean(QueryRead.class).getText(wikiTitleGrezzo);
        if (textService.isValid(oldTextAll)) {
            if (oldTextAll.endsWith(newTextSignificativo)) {
                modificataSignificativamente = false;
            }
        }

        return modificataSignificativamente;
    }

    public WResult urlRequest(final String wikiTitleGrezzo, final String newText) {
        return urlRequest(wikiTitleGrezzo, newText, VUOTA);
    }


    /**
     * Request al software mediawiki composta di tre passaggi:
     * 1. Log in, via one of the methods described in API:Login. Note that while this is required to correctly attribute the edit to its author, many wikis do allow users to edit without registering or logging into an account.
     * 2. GET a CSRF token.
     * 3. Send a POST request, with the CSRF token, to take action on a page.
     * <p>
     * Controllo del login già effettuato ad inizio programma <br>
     * I dati del login di collegamento (userid, username e cookies) sono nel singleton botLogin <br>
     * <p>
     * La prima request preliminare è di tipo GET, per recuperare token e session <br>
     * urlDomain = "&meta=tokens&type=csrf" <br>
     * Invia la request con i cookies di login e senza testo POST <br>
     * Recupera i cookies della connessione (in particolare 'itwikisession') <br>
     * Recupera il logintoken dalla urlResponse <br>
     * <p>
     * La seconda request è di tipo POST <br>
     * urlDomain = "&action=xxxx" <br>
     * Invia la request con i cookies ricevuti (solo 'session') <br>
     * Scrive il testo post con i valori di lgname, lgpassword e lgtoken <br>
     * <p>
     * La response viene elaborata per conferma <br>
     *
     * @param wikiTitleGrezzo della pagina wiki (necessita di codifica) usato nella urlRequest
     * @param newText         da inserire
     * @param summary         oggetto della modifica (facoltativo)
     *
     * @return wrapper di informazioni
     *
     * @see https://www.mediawiki.org/wiki/API:Edit#Example
     */
    public WResult urlRequest(final String wikiTitleGrezzo, final String newText, final String summary) {
        WResult result = checkWrite(wikiTitleGrezzo, newText, summary);
        if (result.isErrato()) {
            return result;
        }

        //--La prima request è di tipo GET
        //--Indispensabile aggiungere i cookies del botLogin
        result = this.primaryRequestGet(result);

        //--La seconda request è di tipo POST
        //--Indispensabile aggiungere i cookies
        //--Indispensabile aggiungere il testo POST
        return this.secondaryRequestPost(result);
    }

    /**
     * <br>
     * Request preliminare. Crea la connessione base di tipo GET <br>
     * La request preliminare è di tipo GET, per recuperare token e session <br>
     * <p>
     * Request 1
     * URL: https://it.wikipedia.org/w/api.php?action=query&format=json&meta=tokens
     * This will return a "token" parameter in JSON form, as documented at
     * Other output formats are available. It will also return HTTP cookies as described below.
     * <p>
     * urlDomain += "&meta=tokens" <br>
     * Invia la request con i cookies di login e senza testo POST <br>
     * Recupera i cookies della connessione (in particolare 'itwikisession') <br>
     * Recupera il token dalla urlResponse <br>
     *
     * @return wrapper di informazioni
     */
    public WResult primaryRequestGet(WResult result) {
        String urlDomain = TAG_PRELIMINARY_REQUEST_GET;
        String urlResponse = VUOTA;
        URLConnection urlConn;

        result.setUrlPreliminary(urlDomain);
        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, botLogin.getCookies());
            urlResponse = sendRequest(urlConn);
            cookies = downlodCookies(urlConn);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
        }

        return elaboraPreliminaryResponse(result, urlResponse);
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     */
    protected WResult elaboraPreliminaryResponse(WResult result, final String rispostaDellaQuery) {
        result.setPreliminaryResponse(rispostaDellaQuery);
        csrftoken = getCsrfToken(rispostaDellaQuery);
        result.setToken(csrftoken);

        if (textService.isValid(csrftoken)) {
            try {
                csrftoken = URLEncoder.encode(csrftoken, ENCODE);
            } catch (Exception unErrore) {
                logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
            }
            result.setValido(true);
        }
        else {
            result.setValido(true);
        }

        return result;
    }


    /**
     * Request principale. Crea la connessione base di tipo POST <br>
     * <p>
     * Request 2
     * URL: https://en.wikipedia.org/w/api.php?action=login&format=json
     * COOKIES parameters:
     * itwikiSession
     * POST parameters:
     * lgname=BOTUSERNAME
     * lgpassword=BOTPASSWORD
     * lgtoken=TOKEN
     * <p>
     * where TOKEN is the token from the previous result. The HTTP cookies from the previous request must also be passed with the second request.
     * <p>
     * A successful login attempt will result in the Wikimedia server setting several HTTP cookies. The bot must save these cookies and send them back every time it makes a request (this is particularly crucial for editing). On the English Wikipedia, the following cookies should be used: enwikiUserID, enwikiToken, and enwikiUserName. The enwikisession cookie is required to actually send an edit or commit some change, otherwise the MediaWiki:Session fail preview error message will be returned.
     * <p>
     * Crea la connessione base di tipo POST <br>
     * Invia la request con i cookies ricevuti (solo 'session') <br>
     * Scrive il testo post con i valori di lgname, lgpassword e lgtoken <br>
     * <p>
     * Risposta in formato testo JSON <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     * Recupera i cookies allegati alla risposta e li memorizza in WikiLogin per poterli usare in query successive <br>
     *
     * @return true se il collegamento come bot è confermato
     */
    public WResult secondaryRequestPost(WResult result) {
        String wikiTitleEncoded = fixWikiTitle(result.getWikiTitle());
        String urlDomain = TAG_SECONDARY_REQUEST_POST + wikiTitleEncoded;
        String urlResponse = VUOTA;
        URLConnection urlConn;
        Map<String, String> cookies = botLogin.getCookies();

        result.setCookies(cookies);
        result.setUrlRequest(urlDomain);
        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, cookies);
            result = addPostConnection(urlConn, result);
            urlResponse = sendRequest(urlConn);
            result.setMappa(downlodCookies(urlConn));
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(new AlgosException(unErrore)).usaDb());
        }

        return elaboraSecondaryResponse(result, urlResponse);
    }

    /**
     * Aggiunge il POST della request <br>
     *
     * @param urlConn connessione con la request
     */
    protected WResult addPostConnection(URLConnection urlConn, final WResult result) throws Exception {
        if (urlConn != null) {
            PrintWriter out = new PrintWriter(urlConn.getOutputStream());
            out.print(elaboraPost(result).getPost());
            out.close();
        }

        return result;
    }

    /**
     * Crea il testo del POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected WResult elaboraPost(final WResult result) {
        String testoPost = "";
        String summary = result.getSummary();
        String testoCodificato = "";
        String newText = result.getNewtext();

        if (textService.isValid(csrftoken)) {
            testoPost += "token" + "=";
            testoPost += csrftoken;
        }

        testoPost += "&bot=true";
        testoPost += "&minor=true";

        // summary
        if (botLogin != null) {
            summary = textService.isValid(summary) ? summary : textService.setDoppieQuadre("Utente:" + botLogin.getUsername() + "|" + botLogin.getUsername());
        }

        if (textService.isValid(summary)) {
            result.setSummary(summary);
            testoPost += "&summary=" + summary;
        }

        try { // prova a eseguire il codice
            testoCodificato = URLEncoder.encode(newText, ENCODE);
        } catch (Exception unErrore) { // intercetta l'errore
            System.out.println(unErrore.toString());
        }

        if (textService.isValid(testoCodificato)) {
            testoPost += "&text" + "=";
            testoPost += testoCodificato;
        }

        return result.post(testoPost);
    }

    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     *
     * @return true se il collegamento come bot è confermato
     */
    protected WResult elaboraSecondaryResponse(WResult result, final String rispostaDellaQuery) {
        result.setResponse(rispostaDellaQuery);
        long pageid = getPageid(rispostaDellaQuery);
        long revid = getNewRevid(rispostaDellaQuery);
        String newtimestamp = getNewTimestamp(rispostaDellaQuery);
        boolean modificata = getModificata(rispostaDellaQuery);

        result.setModificata(modificata);
        if (modificata) {
            result.setValidMessage("Pagina modificata");
        }
        else {
            result.setErrorMessage("Pagina con lo stesso testo");
            result.setValido(true);
        }

        result.setTypePage(AETypePage.pagina);
        result.setPageid(pageid);
        result.setNewrevid(revid);
        result.setNewtimestamp(newtimestamp);
        result.setFine();

        return result;
    }

    /**
     * Restituisce il token dal testo JSON di una pagina di GET preliminary
     *
     * @param textJSON in ingresso
     *
     * @return logintoken
     */
    public String getCsrfToken(String contenutoCompletoPaginaWebInFormatoJSON) {
        String csrfToken = VUOTA;
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject objectQuery = null;
        JSONObject objectToken = null;

        if (objectAll != null && objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
        }

        if (objectQuery.get(AQuery.TOKENS) != null && objectQuery.get(AQuery.TOKENS) instanceof JSONObject) {
            objectToken = (JSONObject) objectQuery.get(TOKENS);
        }

        if (objectToken != null && objectToken.get(CSRF_TOKEN) != null && objectToken.get(CSRF_TOKEN) instanceof String tokenString) {
            csrfToken = tokenString;
        }

        return csrfToken;
    }

    /**
     * Restituisce il token dal testo JSON di una pagina di GET preliminary
     *
     * @param textJSON in ingresso
     *
     * @return logintoken
     */
    public long getPageid(String contenutoCompletoPaginaWebInFormatoJSON) {
        long pageid = 0;
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject objectEdit = null;

        if (objectAll != null && objectAll.get(EDIT) != null && objectAll.get(EDIT) instanceof JSONObject) {
            objectEdit = (JSONObject) objectAll.get(EDIT);
        }
        if (objectEdit != null && objectEdit.get(PAGE_ID) != null && objectEdit.get(PAGE_ID) instanceof Long) {
            pageid = (Long) objectEdit.get(PAGE_ID);
        }

        return pageid;
    }

    /**
     * Restituisce il token dal testo JSON di una pagina di GET preliminary
     *
     * @param textJSON in ingresso
     *
     * @return logintoken
     */
    public long getNewRevid(String contenutoCompletoPaginaWebInFormatoJSON) {
        long newrevid = 0;
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject objectEdit = null;

        if (objectAll != null && objectAll.get(EDIT) != null && objectAll.get(EDIT) instanceof JSONObject) {
            objectEdit = (JSONObject) objectAll.get(EDIT);
        }
        if (objectEdit.get(NEW_REV_ID) != null && objectEdit.get(NEW_REV_ID) instanceof Long) {
            newrevid = (Long) objectEdit.get(NEW_REV_ID);
        }

        return newrevid;
    }

    /**
     * Restituisce il timestamp della modifica <br>
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return timestamp della modifica
     */
    public String getNewTimestamp(String contenutoCompletoPaginaWebInFormatoJSON) {
        String newtimestamp = VUOTA;
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject objectEdit = null;

        if (objectAll != null && objectAll.get(EDIT) != null && objectAll.get(EDIT) instanceof JSONObject) {
            objectEdit = (JSONObject) objectAll.get(EDIT);
        }

        if (objectEdit.get(NEW_TIME_STAMP) != null && objectEdit.get(NEW_TIME_STAMP) instanceof String) {
            newtimestamp = (String) objectEdit.get(NEW_TIME_STAMP);
        }

        return newtimestamp;
    }

    /**
     * Restituisce lo stato del flag 'nochange' <br>
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return flag 'nochange'
     */
    public boolean getModificata(String contenutoCompletoPaginaWebInFormatoJSON) {
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);
        JSONObject objectEdit = null;

        if (objectAll != null && objectAll.get(EDIT) != null && objectAll.get(EDIT) instanceof JSONObject) {
            objectEdit = (JSONObject) objectAll.get(EDIT);
        }

        return objectEdit.get(NO_CHANGE) == null;
    }

}
