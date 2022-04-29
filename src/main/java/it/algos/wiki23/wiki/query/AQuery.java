package it.algos.wiki23.wiki.query;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.interfaces.*;
import it.algos.vaad23.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

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


//    /**
//     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
//     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
//     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
//     */
//    @Autowired
//    public AWikiApiService wikiApi;

//    /**
//     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
//     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
//     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
//     */
//    @Autowired
//    public WikiBotService wikiBot;
//
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
    public LogService logger;

//    /**
//     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
//     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
//     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
//     */
//    @Autowired
//    public BotLogin botLogin;

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

//    public QueryAssert queryAssert;

    // ci metto tutti i cookies restituiti da URLConnection.responses
    protected Map<String, Object> cookies;

    /**
     * Controlla l'esistenza e la validità del collegamento come bot <br>
     *
     * @param result di informazioni eventualmente da modificare
     *
     * @return true se la connessione è valida
     */
    protected AIResult checkBot(AIResult result) {
        AIResult assertResult;

        //        queryAssert = queryAssert != null ? queryAssert : appContext.getBean(QueryAssert.class);
        //        assertResult = queryAssert.urlRequest();
        //        if (assertResult.isErrato()) {
        //            result.setValido(false);
        //            result.setErrorCode(assertResult.getErrorCode());
        //            result.setErrorMessage(assertResult.getErrorMessage());
        //        }

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
    protected void uploadCookies(final URLConnection urlConn, final Map<String, Object> cookies) {
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
    protected String creaCookiesText(Map<String, Object> cookies) {
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


}
