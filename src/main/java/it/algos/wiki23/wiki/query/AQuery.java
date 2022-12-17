package it.algos.wiki23.wiki.query;

import static it.algos.vaad24.backend.boot.VaadCost.*;
import it.algos.vaad24.backend.enumeration.*;
import it.algos.vaad24.backend.exception.*;
import it.algos.vaad24.backend.service.*;
import it.algos.vaad24.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.enumeration.*;
import it.algos.wiki23.backend.login.*;
import it.algos.wiki23.backend.packages.bio.*;
import it.algos.wiki23.backend.service.*;
import static it.algos.wiki23.backend.service.WikiApiService.*;
import it.algos.wiki23.backend.wrapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.json.simple.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 08-mag-2021
 * Time: 15:53
 * <p>
 * Legge (scrive) una pagina internet di tipo HTTP oppure di tipo HTTPS. <br>
 */
public abstract class AQuery {

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Tag base delle API per costruire l' 'urlDomain' completo <br>
     */
    protected static final String TAG_API = "https://it.wikipedia.org/w/api.php?";

    /**
     * Tag per la costruzione di un 'urlDomain' completo <br>
     * Viene usato in tutte le urlRequest delle sottoclassi di QueryWiki <br>
     * La urlRequest funzionerebbe anche senza questo tag, ma la urlResponse sarebbe meno 'leggibile' <br>
     */
    protected static final String TAG_FORMAT = TAG_API + "&format=json&formatversion=2";

    /**
     * Tag per la costruzione di un 'urlDomain' completo per una query <br>
     * Viene usato in molte (non tutte) urlRequest delle sottoclassi di QueryWiki <br>
     */
    protected static final String TAG_QUERY = TAG_FORMAT + "&action=query";

    /**
     * Tag per la costruzione del primo 'urlDomain' completo per una queryWrite <br>
     */
    protected static final String TAG_PRELIMINARY_REQUEST_GET = TAG_QUERY + "&meta=tokens&type=csrf";

    /**
     * Tag per la costruzione del primo 'urlDomain' completo per la preliminaryRequestGet di login <br>
     */
    protected static final String TAG_LOGIN_PRELIMINARY_REQUEST_GET = TAG_PRELIMINARY_REQUEST_GET + "&type=login";

    /**
     * Tag per la costruzione del secondo 'urlDomain' completo per una queryWrite <br>
     */
    protected static final String TAG_SECONDARY_REQUEST_POST = TAG_QUERY + "&action=edit&title=";

    /**
     * Tag per la costruzione del 'urlDomain' completo per la request di login <br>
     */
    protected static final String TAG_REQUEST_ASSERT = TAG_QUERY + "&assert=bot";

    /**
     * Tag per la costruzione del 'urlDomain' completo per la ricerca dei pageIds di una categoria <br>
     */
    protected static final String TAG_REQUEST_CAT = TAG_QUERY + "&list=categorymembers&cmtitle=Categoria:";

    /**
     * Tag per la costruzione del secondo 'urlDomain' completo per la secondaryRequestPost di login <br>
     */
    protected static final String TAG_LOGIN_SECONDARY_REQUEST_POST = TAG_FORMAT + "&action=login";


    protected static final String CSRF_TOKEN = "csrftoken";

    protected static final String TOKENS = "tokens";

    protected AETypeQuery queryType;

    protected String wikiCategory;

    //    /**
    //     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
    //     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
    //     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
    //     */
    //    @Autowired
    //    public AWikiApiService wikiApi;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiBotService wikiBot;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService textService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ArrayService arrayService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DateService dateService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public LogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BotLogin botLogin;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ArrayService array;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DateService date;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WebService webService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public JSonService jSonService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioBackend bioBackend;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ElaboraService elaboraService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public MathService mathService;

    //        public QueryAssert queryAssert;

    // ci metto tutti i cookies restituiti da URLConnection.responses
    protected Map<String, Object> cookies;

    protected LinkedHashMap<String, Object> mappaUrlResponse;

    Map<String, String> mappaStringhe = new HashMap<>();

    /**
     * Controlla l'esistenza e la validità del collegamento come bot <br>
     *
     * @param result di informazioni eventualmente da modificare
     *
     * @return true se la connessione è valida
     */
    protected WResult checkBot(WResult result) {
        WResult assertResult = appContext.getBean(QueryAssert.class).urlRequest();

        if (!assertResult.isValido()) {
            result.setValido(false);
            result.setErrorCode(assertResult.getErrorCode());
            result.setErrorMessage(assertResult.getErrorMessage());
        }

        return result;
    }


    /**
     * Inserisce eventualmente una affermazione di controllo <br>
     *
     * @param urlDomain della richiesta
     *
     * @return urlDomain integrato
     */
    protected String fixAssert(String urlDomain) {
        AETypeUser type = null;

        if (botLogin != null && botLogin.isValido()) {
            type = botLogin.getUserType();
        }

        if (type != null) {
            if (type == AETypeUser.user || type == AETypeUser.bot) {
                urlDomain += type.affermazione();
            }
        }

        return urlDomain;
    }

    protected WResult checkInizialeBase(final String pathQuery) {
        WResult result = WResult.valido()
                .queryType(queryType)
                .typePage(AETypePage.indeterminata)
                .userType(AETypeUser.anonymous)
                .limit(AETypeUser.anonymous.getLimit());
        String message;

        if (textService.isEmpty(pathQuery)) {
            message = String.format("Manca il pathQuery");
            logger.error(new WrapLog().exception(new AlgosException(message)).usaDb());
            result.errorMessage(message);
            result.setFine();
            return result;
        }

        //--richiama una query specializzata per controllare l'esistenza della pagina/categoria
        //--esclude la query stessa per evitare un loop
        if (this.getClass() != QueryExist.class) {
            //            if (!appContext.getBean(QueryExist.class).isEsiste(wikiTitlePageid)) {
            //                message = String.format("La pagina/categoria '%s' non esiste su wikipedia", wikiTitlePageid);
            //                logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
            //                result.errorMessage(message);
            //                result.setWrap(new WrapBio().valida(false).type(AETypePage.nonEsiste));
            //                result.setFine();
            //                return result;
            //            }
        }

        return result;
    }

    protected WResult checkInizialePipe(WResult result, final String wikiTitleGrezzo) {

        if (wikiTitleGrezzo != null && wikiTitleGrezzo.contains(PIPE)) {
            //            result = WResult.errato();
            String message = "Il wikiTitle contiene un 'pipe' non accettabile";
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
            result.errorMessage(message);
            result.setWikiTitle(wikiTitleGrezzo);
            result.typePage(AETypePage.contienePipe);
            result.setFine();
        }

        return result;
    }

    protected WResult checkIniziale(final String pathQuery, final String wikiTitleGrezzo) {
        WResult result = checkInizialeBase(pathQuery);
        result = checkInizialePipe(result, wikiTitleGrezzo);

        if (textService.isEmpty(wikiTitleGrezzo)) {
            String message = "Manca il wikiTitleGrezzo";
            logger.warn(new WrapLog().exception(new AlgosException(message)));
            result.errorMessage(message);
            result.setFine();
            return result;
        }

        if (result.getWrap() != null) {
            result.getWrap().title(wikiTitleGrezzo);
        }
        return result.wikiTitle(wikiTitleGrezzo);
    }


    protected WResult checkIniziale(final String pathQuery, final long pageid) {
        WResult result = checkInizialeBase(pathQuery);

        result.setPageid(pageid);
        if (pageid < 1) {
            String message = "Manca il pageid";
            logger.warn(new WrapLog().exception(new AlgosException(message)).usaDb());
            result.errorMessage(message);
            result.setPageid(pageid);
            result.setFine();
            return result;
        }

        if (result.getWrap() != null) {
            result.getWrap().pageid(pageid);
        }
        return result.pageid(pageid);
    }

    /**
     * Request semplice. Crea una connessione base di tipo GET <br>
     *
     * @param pathQuery       della richiesta
     * @param wikiTitleGrezzo della pagina wiki (necessita di codifica) usato nella urlRequest
     *
     * @return wrapper di informazioni
     */
    protected WResult requestGetTitle(final String pathQuery, final String wikiTitleGrezzo) {
        WResult result = checkIniziale(pathQuery, wikiTitleGrezzo);
        return requestGet(result, pathQuery + fixWikiTitle(wikiTitleGrezzo));
    }


    /**
     * Request semplice. Crea una connessione base di tipo GET <br>
     *
     * @param pathQuery della richiesta
     * @param pageid    della pagina wiki  usato nella urlRequest
     *
     * @return wrapper di informazioni
     */
    protected WResult requestGetPage(final String pathQuery, final long pageid) {
        WResult result = checkIniziale(pathQuery, pageid);
        return requestGet(result, pathQuery + pageid);
    }


    /**
     * Request semplice. Crea una connessione base di tipo GET <br>
     *
     * @param urlDomain della richiesta
     *
     * @return wrapper di informazioni
     */
    protected WResult requestGet(WResult result, String urlDomain) {
        URLConnection urlConn;
        String urlResponse;

        switch (queryType) {
            case getSenzaLoginSenzaCookies -> {
                urlDomain = fixAssert(urlDomain);
                result.setCookies(botLogin != null ? botLogin.getCookies() : null);
            }
            case getLoggatoConCookies -> {
                urlDomain = fixAssert(urlDomain);
                result.setCookies(botLogin != null ? botLogin.getCookies() : null);
            }
            default -> {}
        }
        result.setGetRequest(urlDomain);

        if (result.isErrato()) {
            return result;
        }

        try {
            urlConn = this.creaGetConnection(urlDomain);
            uploadCookies(urlConn, result.getCookies());
            urlResponse = sendRequest(urlConn);
            result = elaboraResponse(result, urlResponse);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
        }
        return result;
    }


    public WResult urlRequestCiclica(final List<Long> listaPageids, final String wikiQuery) {
        WResult result = WResult.valido()
                .queryType(AETypeQuery.getLoggatoConCookies)
                .typePage(AETypePage.indeterminata);
        queryType = AETypeQuery.getLoggatoConCookies;
        long inizio = System.currentTimeMillis();
        String strisciaIds;
        String message;
        AETypeUser type;
        int num = 0;
        String urlDomain;
        int size = listaPageids != null ? listaPageids.size() : 0;
        int max = 500; //--come da API mediaWiki
        int cicli;

        if (listaPageids == null) {
            message = "Nessun valore per la lista di pageIds";
            return WResult.errato(message).queryType(AETypeQuery.getLoggatoConCookies).fine();
        }

        type = botLogin != null ? botLogin.getUserType() : null;
        result.setCookies(botLogin != null ? botLogin.getCookies() : null);
        result.limit(max);
        result.userType(type);
        switch (type) {
            case anonymous, user, admin -> {
                if (size > type.getLimit()) {
                    message = String.format("Sei collegato come %s e nella request ci sono %s pageIds", type, textService.format(size));
                    logger.info(new WrapLog().exception(new AlgosException(message)).usaDb());
                    return WResult.errato(message).queryType(AETypeQuery.getLoggatoConCookies).fine();
                }
                else {
                    strisciaIds = array.toStringaPipe(listaPageids);
                    urlDomain = wikiQuery + strisciaIds;
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
            urlDomain = wikiQuery + strisciaIds;
            result = requestGet(result, urlDomain);

            if (Pref.debug.is()) {
                num += max;
                if (mathService.multiploEsatto(50000, num)) {
                    String time = dateService.deltaText(inizio);
                    message = String.format("Finora creati %s wrapTimes in %s (la categoria ha %s voci)", textService.format(num), time,
                            textService.format(size)
                    );
                    logger.info(new WrapLog().message(message).type(AETypeLog.bio));
                }
            }
        }

        result.setGetRequest(VUOTA);
        return result;
    }

    /**
     * Crea la connessione base (GET) <br>
     * Regola i parametri della connessione <br>
     *
     * @param urlDomain stringa della request
     *
     * @return connessione con la request
     */
    protected URLConnection creaGetConnection(String urlDomain) throws Exception {
        URLConnection urlConn;

        urlConn = new URL(urlDomain).openConnection();
        urlConn.setDoOutput(true);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");

        return urlConn;
    }

    /**
     * Allega i cookies alla request (upload) <br>
     * Serve solo la sessione <br>
     *
     * @param urlConn connessione
     */
    protected void uploadCookies(final URLConnection urlConn, final Map<String, String> cookies) {
        String cookiesText;

        if (cookies != null) {
            cookiesText = this.creaCookiesText(cookies);
            urlConn.setRequestProperty("Cookie", cookiesText);
        }
    }

    /**
     * Costruisce la stringa dei cookies da allegare alla request POST <br>
     *
     * @param cookies mappa dei cookies
     */
    protected String creaCookiesText(Map<String, String> cookies) {
        String cookiesTxt = VUOTA;
        String sep = UGUALE_SEMPLICE;
        Object valObj;
        String key;

        if (cookies != null && cookies.size() > 0) {
            for (Object obj : cookies.keySet()) {
                if (obj instanceof String) {
                    key = (String) obj;
                    valObj = cookies.get(key);
                    cookiesTxt += key;
                    cookiesTxt += sep;
                    cookiesTxt += valObj;
                    cookiesTxt += PUNTO_VIRGOLA;
                }
            }
        }

        return cookiesTxt;
    }


    /**
     * Aggiunge il POST della request <br>
     *
     * @param urlConn connessione con la request
     */
    protected void addPostConnection(URLConnection urlConn) throws Exception {
        if (urlConn != null) {
            PrintWriter out = new PrintWriter(urlConn.getOutputStream());
            out.print(elaboraPost());
            out.close();
        }
    }

    /**
     * Crea il testo del POST della request <br>
     * DEVE essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    protected String elaboraPost() {
        return VUOTA;
    }


    /**
     * Invia la request (GET oppure POST) <br>
     *
     * @param urlConn connessione con la request
     *
     * @return valore di ritorno della request
     */
    protected String sendRequest(URLConnection urlConn) throws Exception {
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;

        if (urlConn == null) {
            return VUOTA;
        }

        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, "UTF8");

        // read the response
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();

        return textBuffer.toString();
    }


    /**
     * Grabs cookies from the URL connection provided <br>
     * Cattura i cookies di ritorno e li memorizza nei parametri <br>
     *
     * @param urlConn connessione
     */
    protected Map downlodCookies(URLConnection urlConn) {
        Map cookiesMap = new HashMap();
        String headerName;
        String cookie;
        String name;
        String value;

        if (urlConn != null) {
            for (int i = 1; (headerName = urlConn.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals("set-cookie") || headerName.equals("Set-Cookie")) {
                    cookie = urlConn.getHeaderField(i);
                    cookie = cookie.substring(0, cookie.indexOf(";"));
                    name = cookie.substring(0, cookie.indexOf("="));
                    value = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
                    cookiesMap.put(name, value);
                }
            }
        }

        return cookiesMap;
    }


    /**
     * Elabora la risposta <br>
     * <p>
     * Informazioni, contenuto e validità della risposta <br>
     * Controllo del contenuto (testo) ricevuto <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param rispostaDellaQuery in formato JSON da elaborare
     *
     * @return wrapper di informazioni
     */
    protected WResult elaboraResponse(WResult result, final String rispostaDellaQuery) {
        //--fissa durata
        result.setFine();
        result.setTypePage(AETypePage.indeterminata);

        //--mappa utilizzata (eventualmente) nelle sottoclassi
        this.fixMappaUrlResponse();

        JSONObject jsonContinue = null;
        String message;
        JSONObject jsonAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);
        mappaUrlResponse.put(KEY_JSON_ALL, jsonAll);

        //--controllo del batchcomplete
        if (jsonAll != null && jsonAll.get(KEY_JSON_BATCH) != null) {
            if (!(boolean) jsonAll.get(KEY_JSON_BATCH)) {
                mappaUrlResponse.put(KEY_JSON_BATCH, false);
                result.setErrorCode("batchcomplete=false");
                message = String.format("Qualcosa non ha funzionato nella lettura della pagina wiki '%s'", result.getWikiTitle());
                result.setErrorMessage(message);
                return result;
            }
        }

        //--controllo dell'errore
        if (jsonAll != null && jsonAll.get(KEY_JSON_ERROR) != null && jsonAll.get(KEY_JSON_ERROR) instanceof JSONObject jsonError) {
            if (jsonError != null && jsonError.get(KEY_JSON_CODE) != null && jsonError.get(KEY_JSON_CODE) instanceof String errorMessage) {
                result.setErrorCode(errorMessage);
            }
            if (jsonError != null && jsonError.get(KEY_JSON_INFO) != null && jsonError.get(KEY_JSON_INFO) instanceof String infoMessage) {
                result.setErrorMessage(infoMessage);
            }
            mappaUrlResponse.put(KEY_JSON_ERROR, true);
            return result.typePage(AETypePage.nonEsiste);
        }
        else {
            mappaUrlResponse.put(KEY_JSON_ERROR, false);
        }

        //--regola il token per le categorie
        if (jsonAll != null && jsonAll.get(KEY_JSON_CONTINUE) != null) {
            jsonContinue = (JSONObject) jsonAll.get(KEY_JSON_CONTINUE);
            mappaUrlResponse.put(KEY_JSON_CONTINUE, jsonContinue);
        }

        result = fixQueryPagesZero(result, jsonAll);

        result = fixQueryCategoryMembers(result, jsonAll);

        //--controllo del missing
        if (mappaUrlResponse.get(KEY_JSON_ZERO) != null && ((JSONObject) mappaUrlResponse.get(KEY_JSON_ZERO)).get(KEY_JSON_MISSING) != null) {
            if ((boolean) ((JSONObject) mappaUrlResponse.get(KEY_JSON_ZERO)).get(KEY_JSON_MISSING)) {
                result.setValido(false);
                result.setErrorCode(KEY_JSON_MISSING_TRUE);
                result.setErrorMessage(String.format("La pagina wiki '%s' non esiste", result.getWikiTitle()));
                result.setTypePage(AETypePage.nonEsiste);
                mappaUrlResponse.put(KEY_JSON_MISSING, true);
                return result;
            }
            else {
                mappaUrlResponse.put(KEY_JSON_MISSING, false);
            }
        }

        //--regola il login
        if (jsonAll != null && jsonAll.get(LOGIN) != null) {
            JSONObject jsonLogin = (JSONObject) jsonAll.get(LOGIN);
            mappaUrlResponse.put(KEY_JSON_LOGIN, jsonLogin);
            return result;
        }
        //--estrae il 'content' dalla pagina 'zero'
        result = fixQueryContent(result);

        //--regola le pagine di disambigua e redirect
        result = fixQueryDisambiguaRedirect(result);

        return result;
    }

    protected WResult fixQueryPagesZero(WResult result, final JSONObject jsonAll) {
        JSONObject queryPageZero = null;
        JSONObject query = null;
        JSONArray pages = null;

        if (jsonAll != null && jsonAll.get(KEY_JSON_QUERY) != null) {
            query = (JSONObject) jsonAll.get(KEY_JSON_QUERY);
            mappaUrlResponse.put(KEY_JSON_QUERY, query);
        }

        if (query != null && query.get(KEY_JSON_PAGES) != null) {
            pages = (JSONArray) query.get(KEY_JSON_PAGES);
            mappaUrlResponse.put(KEY_JSON_PAGES, pages);
            mappaUrlResponse.put(KEY_JSON_NUM_PAGES, pages.size());
        }

        if (pages != null && pages.size() > 0) {
            queryPageZero = (JSONObject) pages.get(0);
            mappaUrlResponse.put(KEY_JSON_ZERO, queryPageZero);
            result.setValido().typePage(AETypePage.indeterminata);

        }

        return result;
    }

    protected WResult fixQueryCategoryMembers(WResult result, final JSONObject jsonAll) {
        JSONArray queryCategory = null;
        JSONObject query = null;

        if (jsonAll != null && jsonAll.get(KEY_JSON_QUERY) != null) {
            query = (JSONObject) jsonAll.get(KEY_JSON_QUERY);
            mappaUrlResponse.put(KEY_JSON_QUERY, query);
        }

        if (query != null && query.get(KEY_JSON_CATEGORY_MEMBERS) != null) {
            queryCategory = (JSONArray) query.get(KEY_JSON_CATEGORY_MEMBERS);
            mappaUrlResponse.put(KEY_JSON_CATEGORY_MEMBERS, queryCategory);
            result.typePage(AETypePage.categoria).setValido();
        }

        return result;
    }

    //--estrae informazioni dalla pagina 'zero'
    //--estrae il 'content' dalla pagina 'zero'
    protected WResult fixQueryContent(WResult result) {
        String content;
        String wikiTitle;
        long pageId;

        if (mappaUrlResponse.get(KEY_JSON_ZERO) instanceof JSONObject jsonPageZero) {
            if (jsonPageZero.get(KEY_JSON_NS) instanceof Long nameSpace) {
                result.setNameSpace(nameSpace);
                mappaUrlResponse.put(KEY_JSON_NS, nameSpace);
            }

            if (jsonPageZero.get(KEY_JSON_PAGE_ID) instanceof Long pageid) {
                pageId = pageid;
                result.pageid(pageId);
            }
            if (jsonPageZero.get(KEY_JSON_TITLE) instanceof String title) {
                wikiTitle = title;
                result.wikiTitle(wikiTitle);
            }

            if (jsonPageZero.get(KEY_JSON_REVISIONS) instanceof JSONArray jsonRevisions) {
                if (jsonRevisions.size() > 0) {
                    JSONObject jsonRevZero = (JSONObject) jsonRevisions.get(0);
                    if (jsonRevZero.get(KEY_JSON_SLOTS) instanceof JSONObject jsonSlots) {
                        if (jsonSlots.get(KEY_JSON_MAIN) instanceof JSONObject jsonMain) {
                            content = (String) jsonMain.get(KEY_JSON_CONTENT);
                            mappaUrlResponse.put(KEY_JSON_CONTENT, content);
                        }
                    }
                    if (jsonRevZero.get(KEY_JSON_CONTENT) instanceof String contentTxt) {
                        mappaUrlResponse.put(KEY_JSON_CONTENT, contentTxt);
                        result.setContent(contentTxt);
                        result.setTypePage(AETypePage.pagina);
                    }
                }
            }
        }

        return result;
    }


    protected WResult fixQueryDisambiguaRedirect(WResult result) {
        String wikiLink = VUOTA;

        if (mappaUrlResponse.get(KEY_JSON_CONTENT) instanceof String content) {
            //--contenuto inizia col tag della disambigua
            if (content.startsWith(TAG_DISAMBIGUA_UNO) || content.startsWith(TAG_DISAMBIGUA_DUE)) {
                result.setValido(false);
                result.setErrorCode("disambigua");
                result.setErrorMessage(String.format("La pagina wiki '%s' è una disambigua", result.getWikiTitle()));
                //                wrap = result.getWrap().valida(false).type(AETypePage.disambigua);
                //                result.setWrap(wrap);
                mappaUrlResponse.put(KEY_JSON_DISAMBIGUA, true);
                return result.typePage(AETypePage.disambigua);
            }

            //--contenuto inizia col tag del redirect
            if (content.startsWith(TAG_REDIRECT_UNO) || content.startsWith(TAG_REDIRECT_DUE) || content.startsWith(TAG_REDIRECT_TRE) || content.startsWith(TAG_REDIRECT_QUATTRO)) {
                result.setValido(false);
                result.setErrorCode("redirect");
                result.setErrorMessage(String.format("La pagina wiki '%s' è un redirect", result.getWikiTitle()));
                //                wrap = result.getWrap().valida(false).type(AETypePage.redirect);
                //                result.setWrap(wrap);
                mappaUrlResponse.put(KEY_JSON_REDIRECT, true);
                wikiLink = content.substring(content.indexOf(SPAZIO)).trim();
                wikiLink = textService.setNoQuadre(wikiLink);
                result.setTxtValue(wikiLink);
                return result.typePage(AETypePage.redirect);
            }
        }

        return result;
    }


    protected WrapBio getWrap(JSONObject jsonPageZero) {
        WrapBio wrapBio;
        String content = VUOTA;
        String wikiTitle = VUOTA;
        long pageId = 0L;
        String tmplBio;

        if (jsonPageZero.get(KEY_JSON_PAGE_ID) instanceof Long pageid) {
            pageId = pageid;
        }
        if (jsonPageZero.get(KEY_JSON_TITLE) instanceof String title) {
            wikiTitle = title;
        }

        if (jsonPageZero.get(KEY_JSON_REVISIONS) instanceof JSONArray jsonRevisions) {
            if (jsonRevisions.size() > 0) {
                JSONObject jsonRevZero = (JSONObject) jsonRevisions.get(0);
                if (jsonRevZero.get(KEY_JSON_SLOTS) instanceof JSONObject jsonSlots) {
                    if (jsonSlots.get(KEY_JSON_MAIN) instanceof JSONObject jsonMain) {
                        content = (String) jsonMain.get(KEY_JSON_CONTENT);
                        mappaUrlResponse.put(KEY_JSON_CONTENT, content);
                    }
                }
                if (jsonRevZero.get(KEY_JSON_CONTENT) instanceof String contenuto) {
                    content = contenuto;
                    mappaUrlResponse.put(KEY_JSON_CONTENT, content);
                }
            }
        }

        tmplBio = wikiBot.estraeTmpl(content);
        if (textService.isValid(tmplBio)) {
            wrapBio = new WrapBio().valida(true).title(wikiTitle).pageid(pageId).type(AETypePage.testoConTmpl).templBio(tmplBio);
        }
        else {
            wrapBio = new WrapBio().valida(false).title(wikiTitle).pageid(pageId).type(AETypePage.testoSenzaTmpl).templBio(tmplBio);
        }

        return wrapBio;
    }

    //--controllo del 'content'
    //--controllo l'esistenza del template bio
    //--estrazione del template
    protected WResult fixQueryTmplBio(WResult result) {
        String tmplBio;
        String wikiTitle = result.getWikiTitle();
        long pageId = result.getPageid();

        if ((boolean) mappaUrlResponse.get(KEY_JSON_MISSING)) {
            return result;
        }

        if (mappaUrlResponse.get(KEY_JSON_CONTENT) instanceof String content) {
            if (textService.isEmpty(content)) {
                return result;
            }

            tmplBio = wikiBot.estraeTmpl(content);
            if (textService.isValid(tmplBio)) {
                result.setErrorCode(VUOTA);
                result.setErrorMessage(VUOTA);
                result.setCodeMessage("valida");
                result.typePage(AETypePage.pagina);
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

        return result;
    }


    protected void fixMappaUrlResponse() {
        mappaUrlResponse = new LinkedHashMap<>();
        mappaUrlResponse.put(KEY_JSON_BATCH, true);
        mappaUrlResponse.put(KEY_JSON_MISSING, false);
        mappaUrlResponse.put(KEY_JSON_CONTENT, VUOTA);
    }

    /**
     * Recupera spazio e caratteri strani nel titolo <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return titolo 'spedibile' al server
     */
    public String fixWikiTitle(final String wikiTitleGrezzo) {
        String wikiTitle;

        if (wikiTitleGrezzo == null) {
            logger.info(new WrapLog().exception(new AlgosException("Manca il wikiTitle della pagina")).usaDb());
            return VUOTA;
        }
        if (wikiTitleGrezzo.length() < 1) {
            logger.info(new WrapLog().exception(new AlgosException("Il wikiTitle della pagina è vuoto")).usaDb());
            return VUOTA;
        }

        wikiTitle = wikiTitleGrezzo.replaceAll(SPAZIO, UNDERSCORE);
        try {
            wikiTitle = URLEncoder.encode(wikiTitle, ENCODE);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
        }

        return wikiTitle;
    }

}
