package it.algos.wiki23.backend.service;

import static it.algos.vaad23.backend.boot.VaadCost.*;
import it.algos.vaad23.backend.enumeration.*;
import it.algos.vaad23.backend.exception.*;
import it.algos.vaad23.backend.wrapper.*;
import static it.algos.wiki23.backend.boot.Wiki23Cost.*;
import it.algos.wiki23.backend.packages.bio.*;
import static it.algos.wiki23.backend.service.WikiApiService.*;
import it.algos.wiki23.backend.wrapper.*;
import org.json.simple.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 10-mag-2021
 * Time: 14:06
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AWikiBotService.class); <br>
 * 3) @Autowired public AWikiBotService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WikiBotService extends WAbstractService {

    public static final String TAG_BIO = "Bio";

    public static final String CATEGORIA = "Categoria:";

    public static final String WIKI = "https://it.wikipedia.org/w/api.php?&format=json&formatversion=2&action=query";

    public static final String WIKI_QUERY = WIKI + "&rvslots=main&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles=";

    public static final String WIKI_PARSE = "https://it.wikipedia.org/w/api.php?action=parse&prop=wikitext&formatversion=2&format=json&page=";

    public static final String WIKI_QUERY_TITLES = WIKI + "&rvslots=main&prop=revisions&rvprop=content|ids|timestamp&titles=";

    public static final String WIKI_QUERY_PAGEIDS = WIKI + "&rvslots=main&prop=revisions&rvprop=content|ids|timestamp&pageids=";

    public static final String WIKI_QUERY_TIMESTAMP = WIKI + "&prop=revisions&rvprop=timestamp&pageids=";

    public static final String WIKI_QUERY_CATEGORY = WIKI + "&list=categorymembers&cmtitle=" + CATEGORIA;

    public static final String WIKI_QUERY_CAT_LIMIT_USER = "&cmlimit=500";

    public static final String WIKI_QUERY_CAT_LIMIT_BOT = "&cmlimit=5000";

    public static final String WIKI_QUERY_CAT_CONTINUE = "&cmcontinue=";

    public static final String WIKI_QUERY_CAT_TYPE = "&cmtype=";

    public static final String WIKI_QUERY_CAT_PROP = "&cmprop=";

    public static final String WIKI_QUERY_CAT_TOTALE = WIKI + "&prop=categoryinfo&titles=" + CATEGORIA;

    public static final String WIKI_QUERY_USER = "&assert=user";

    public static final String WIKI_QUERY_BOT = "&assert=bot";

    private static final LocalDateTime MONGO_TIME_ORIGIN = LocalDateTime.of(2000, 1, 1, 0, 0);

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiApiService wikiApi;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public JSonService jSonService;

    //    /**
    //     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
    //     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
    //     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
    //     */
    //    @Autowired
    //    public BioUtility utility;
    //
    //    /**
    //     * Istanza di una interfaccia <br>
    //     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
    //     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
    //     */
    //    @Autowired
    //    public BotLogin login;

    /**
     * Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb <br>
     */
    protected Predicate<WrapTime> checkNuovi = wrap -> {
        try {
            return !bioBackend.isExist(wrap.getPageid());
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
            return false;
        }
    };

    /**
     * Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb <br>
     */
    protected Predicate<WrapTime> checkEsistenti = wrap -> {
        try {
            return bioBackend.isExist(wrap.getPageid());
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
        }
        return false;
    };

    /**
     * Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica <br>
     */
    protected Predicate<WrapTime> checkModificati = wrap -> {
        LocalDateTime wrapTime = wrap.getLastModifica();
        long key = wrap.getPageid();
        Bio bio = null;
        try {
            bio = bioBackend.findByKey(key);
        } catch (Exception unErrore) {
            logger.error(new WrapLog().exception(unErrore).usaDb());
        }
        LocalDateTime mongoTime = bio != null ? bio.getLastMongo() : MONGO_TIME_ORIGIN;

        boolean modificato = wrapTime.isAfter(mongoTime);
        return wrapTime.isAfter(mongoTime);
    };

    //    /**
    //     * Legge (come user) una pagina dal server wiki <br>
    //     * Usa una API con action=query SENZA bisogno di loggarsi <br>
    //     * Recupera dalla urlRequest title, pageid, timestamp e tmpl <br>
    //     * Estrae il wikitext in linguaggio wiki visibile <br>
    //     *
    //     * @param wikiTitleGrezzo della pagina wiki
    //     *
    //     * @return wrapper con template (visibile) della pagina wiki
    //     */
    //    public WrapPage leggePage(final String wikiTitleGrezzo) {
    //        return this.leggePage(wikiTitleGrezzo, TAG_BIO);
    //    }

    //    /**
    //     * Legge (come user) una serie pagina dal server wiki <br>
    //     * Usa una API con action=query SENZA bisogno di loggarsi <br>
    //     * Recupera dalla urlRequest title, pageid, timestamp e wikitext <br>
    //     * Estrae il wikitext in linguaggio wiki visibile <br>
    //     *
    //     * @param listaPageIdsDaLeggere sul server
    //     *
    //     * @return lista di wrapper con testo completo (visibile) della pagina wiki
    //     */
    //    public List<WrapPage> leggePages(List<Long> listaPageIdsDaLeggere) {
    //        return leggePages(array.toStringaPipe(listaPageIdsDaLeggere));
    //    }

    //    /**
    //     * Legge (come user) una serie pagina dal server wiki <br>
    //     * Usa una API con action=query SENZA bisogno di loggarsi <br>
    //     * Recupera dalla urlRequest title, pageid, timestamp e wikitext <br>
    //     * Estrae il wikitext in linguaggio wiki visibile <br>
    //     *
    //     * @param pageIds stringa dei pageIds delle pagine wiki da leggere
    //     *
    //     * @return lista di wrapper con testo completo (visibile) della pagina wiki
    //     */
    //    public List<WrapPage> leggePages(String pageIds) {
    //        long inizio = System.currentTimeMillis();
    //
    //        List<WrapPage> listaWrapPage = this.leggePages(pageIds, TAG_BIO);
    //        if (listaWrapPage != null) {
    //            if (listaWrapPage.size() > 0) {
    //                logger.info(AETypeLog.bio, String.format("Recupera una lista di %d wrapPage valide (con tmplBio) in %s", listaWrapPage.size(), date.deltaTextEsatto(inizio)));
    //            }
    //            else {
    //                logger.info(AETypeLog.bio, "Non ci sono wrapPages valide (con tmplBio) da leggere");
    //            }
    //        }
    //        return listaWrapPage;
    //    }

    //    /**
    //     * Recupera (come user) 'lastModifica' di una serie di pageid <br>
    //     * Usa una API con action=query SENZA bisogno di loggarsi <br>
    //     * Recupera dalla urlRequest  pageid e timestamp <br>
    //     *
    //     * @param categoryTitle da recuperare
    //     *
    //     * @return lista di MiniWrap con 'pageid' e 'lastModifica'
    //     */
    //    public List<MiniWrap> getMiniWrap(final String categoryTitle) {
    //        List<Long> listaPageids = this.getLongCat(categoryTitle, AETypeUser.anonymous);
    //        return getMiniWrap(categoryTitle, listaPageids);
    //    }

    //    /**
    //     * Recupera (come user) 'lastModifica' di una serie di pageid <br>
    //     * Usa una API con action=query SENZA bisogno di loggarsi <br>
    //     * Recupera dalla urlRequest  pageid e timestamp <br>
    //     *
    //     * @param catTitle da recuperare
    //     *
    //     * @return lista di MiniWrap con 'pageid' e 'lastModifica'
    //     */
    //    public List<MiniWrap> getMiniWrap(final String catTitle, final List<Long> listaPageids) {
    //        List<MiniWrap> wraps = new ArrayList<>();
    //        long inizio = System.currentTimeMillis();
    //        int limit = LIMIT_USER;
    //        limit = 500;
    //        int dimLista = listaPageids.size();
    //        int cicli = (dimLista / limit) + 1;
    //        String strisciaIds = VUOTA;
    //        int ini = 0;
    //        int end = 0;
    //
    //        for (int k = 0; k < cicli; k++) {
    //            ini = k * limit;
    //            end = ((k + 1) * limit);
    //            end = Math.min(end, dimLista);
    //            strisciaIds = array.toStringaPipe(listaPageids.subList(ini, end));
    //            wraps.addAll(fixPages(strisciaIds));
    //        }
    //
    //        logger.info(AETypeLog.bio, String.format("La categoria [%s] ha %s miniWrap letti con %s cicli di %d in %s", catTitle, text.format(wraps.size()), cicli, limit, date.deltaTextEsatto(inizio)));
    //
    //        return wraps;
    //    }

    //    /**
    //     * Recupera (come user) 'lastModifica' di una serie di pageid <br>
    //     * Usa una API con action=query SENZA bisogno di loggarsi <br>
    //     * Recupera dalla urlRequest  pageid e timestamp <br>
    //     * Non devono arrivare più di 50 pageid <br>
    //     *
    //     * @param pageIds stringa dei pageIds delle pagine wiki da controllare
    //     *
    //     * @return lista di MiniWrap con 'pageid' e 'lastModifica'
    //     */
    //    public List<MiniWrap> fixPages(final String pageIds) {
    //        List<MiniWrap> wraps = new ArrayList<>();
    //        String webUrl;
    //        AIResult result;
    //        String rispostaAPI;
    //
    //        if (text.isEmpty(pageIds)) {
    //            return wraps;
    //        }
    //
    //        //        if (pageIds.split(PIPE_REGEX).length > LIMIT_USER) {
    //        //            return null;
    //        //        }
    //
    //        webUrl = WIKI_QUERY_TIMESTAMP + pageIds;
    //        result = web.legge(webUrl);
    //        rispostaAPI = result.getResponse();
    //
    //        JSONArray jsonPages = jSonService.getArrayPagine(rispostaAPI);
    //        if (jsonPages != null) {
    //            for (Object obj : jsonPages) {
    //                wraps.add(creaPage((JSONObject) obj));
    //            }
    //        }
    //        return wraps;
    //    }

    //    public MiniWrap creaPage(final JSONObject jsonPage) {
    //        long pageid;
    //        String stringTimestamp;
    //
    //        if (jsonPage.get(KEY_JSON_MISSING) != null && (boolean) jsonPage.get(KEY_JSON_MISSING)) {
    //            return null;
    //        }
    //
    //        pageid = (long) jsonPage.get(KEY_JSON_PAGE_ID);
    //        JSONArray jsonRevisions = (JSONArray) jsonPage.get(KEY_JSON_REVISIONS);
    //        JSONObject jsonRevZero = (JSONObject) jsonRevisions.get(0);
    //        stringTimestamp = (String) jsonRevZero.get(KEY_JSON_TIMESTAMP);
    //
    //        return new MiniWrap(pageid, stringTimestamp);
    //    }

    /**
     * Elabora la lista di MiniWrap e costruisce una lista di pageIds da leggere <br>
     * Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb <br>
     * Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica <br>
     *
     * @param listaMiniWrap da elaborare
     *
     * @return lista di pageId da leggere dal server
     */
    public List<Long> elaboraMiniWrap(final List<WrapTime> listaMiniWrap) {
        List<Long> listaPageIdsDaLeggere = new ArrayList<>();
        List<WrapTime> listaMiniWrapTotali = new ArrayList<>();
        long inizio = System.currentTimeMillis();
        int nuove;
        int modificate;
        int totali;
        String message;

        //--Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb
        List<WrapTime> listaMiniWrapNuovi = listaMiniWrap
                .stream()
                .filter(checkNuovi)
                .sorted()
                .collect(Collectors.toList());
        nuove = listaMiniWrapNuovi.size();
        listaMiniWrapTotali.addAll(listaMiniWrapNuovi);

        //--Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica
        List<WrapTime> listaMiniWrapModificati = listaMiniWrap
                .stream()
                .filter(checkEsistenti)
                .filter(checkModificati)
                .sorted()
                .collect(Collectors.toList());
        modificate = listaMiniWrapModificati.size();
        listaMiniWrapTotali.addAll(listaMiniWrapModificati);
        totali = nuove + modificate;

        for (WrapTime wrap : listaMiniWrapTotali) {
            listaPageIdsDaLeggere.add(wrap.getPageid());
        }

        message = String.format("Elaborata una lista di miniWrap da leggere: %s nuove e %s modificate (%s totali) in %s",
                textService.format(nuove), textService.format(modificate), textService.format(totali), dateService.deltaText(inizio)
        );
        logger.info(new WrapLog().type(AETypeLog.bio).exception(new AlgosException(message)).usaDb());

        return listaPageIdsDaLeggere;
    }

    /**
     * Legge il testo del template Bio da una voce <br>
     * Esamina solo il PRIMO template BIO che trova <br>
     * Gli estremi sono COMPRESI <br>
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag iniziale con o senza primo carattere maiuscolo
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @param testoCompletoPagina in ingresso
     *
     * @return template completo di doppie graffe iniziali e finali
     */
    public String estraeTmpl(final String testoCompletoPagina) {
        return wikiApi.estraeTmpl(testoCompletoPagina, "Bio");
    }

    //    /**
    //     * Legge il testo del template Bio da una voce <br>
    //     * Esamina solo il PRIMO template BIO che trova <br>
    //     * Gli estremi sono COMPRESI <br>
    //     * <p>
    //     * Recupera il tag iniziale con o senza ''Template''
    //     * Recupera il tag iniziale con o senza primo carattere maiuscolo
    //     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
    //     * Controlla che non esistano doppie graffe dispari all'interno del template
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return template completo di doppie graffe iniziali e finali
    //     */
    //    public String leggeTmpl(final String wikiTitle) {
    //        return wikiApi.leggeTmpl(wikiTitle, "Bio");
    //    }

    //    /**
    //     * Legge una singola pagina da wiki <br>
    //     * Non serve essere loggato come Bot <br>
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return pagina wiki
    //     */
    //    public Pagina leggePagina(final String wikiTitle) {
    //        Pagina pagina = null;
    //
    //        return pagina;
    //    }

    //    /**
    //     * Legge (come user) una pagina dal server wiki <br>
    //     * Usa una API con action=parse SENZA bisogno di loggarsi <br>
    //     * Recupera dalla urlRequest title, pageid e wikitext <br>
    //     * Estrae il wikitext in linguaggio wiki visibile <br>
    //     * Elaborazione della urlRequest leggermente meno complessa di leggeQuery <br>
    //     * Tempo di download leggermente più lungo di leggeQuery <br>
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return testo completo (visibile) della pagina wiki
    //     */
    //    public String leggeParse(final String wikiTitle) {
    //        Map mappa = getMappaParse(wikiTitle);
    //        return (String) mappa.get(KEY_MAPPA_TEXT);
    //    }

    //    /**
    //     * Recupera i parametri fondamentali di una singola pagina con action=parse <br>
    //     * 3 parametri:
    //     * title
    //     * pageid
    //     * wikitext
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return mappa dei parametri
    //     */
    //    public Map<String, Object> getMappaParse(final String wikiTitle) {
    //        AIResult result;
    //        Map<String, Object> mappa = new HashMap<>();
    //        String webUrl = WIKI_PARSE + wikiTitle;
    //        String rispostaDellaParse = web.leggeWikiTxt(webUrl);
    //
    //        if (false) {
    //            result=null;
    //        }
    //
    //        JSONObject jsonRisposta = (JSONObject) JSONValue.parse(rispostaDellaParse);
    //        JSONObject jsonParse = (JSONObject) jsonRisposta.get(KEY_MAPPA_PARSE);
    //
    //        mappa.put(KEY_MAPPA_TITLE, jsonParse.get(KEY_MAPPA_TITLE));
    //        mappa.put(KEY_MAPPA_PAGEID, jsonParse.get(KEY_MAPPA_PAGEID));
    //        mappa.put(KEY_MAPPA_TEXT, jsonParse.get(KEY_MAPPA_TEXT));
    //
    //        return mappa;
    //    }

    //    /**
    //     * Costruisce un wrapper di dati essenziali per una Biografia <br>
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return pagina wiki
    //     */
    //    public BioWrap getBioWrap(final String wikiTitle) {
    //        BioWrap wrap = null;
    //        Map mappa = getMappaParse(wikiTitle);
    //
    //        if (array.isAllValid(mappa)) {
    //            wrap = appContext.getBean(BioWrap.class, mappa);
    //        }
    //
    //        return wrap;
    //    }

    /**
     * Estrae una mappa chiave-valore per un fix di parametri, dal tmplBioServer di una biografia <br>
     * <p>
     * E impossibile sperare in uno schema fisso <br>
     * I parametri sono spesso scritti in ordine diverso da quello previsto <br>
     * Occorre considerare le {{ graffe annidate, i | (pipe) annidati ed i mancati ritorni a capo, ecc., ecc. <br>
     * <p>
     * Uso la lista dei parametri che può riconoscere <br>
     * (è meno flessibile, ma più sicuro) <br>
     * Cerco il primo parametro nel testo e poi spazzolo il testo per cercare <br>
     * il primo parametro noto e così via <br>
     *
     * @param tmplBioServer del template Bio
     *
     * @return mappa dei parametri esistenti nella enumeration e presenti nel testo
     */
    public LinkedHashMap<String, String> getMappaDownload(String tmplBioServer) {
        LinkedHashMap<String, String> mappa = null;
        LinkedHashMap<Integer, String> mappaTmp = new LinkedHashMap<Integer, String>();
        String chiave;
        String uguale = UGUALE_SEMPLICE;
        String valore = VUOTA;
        int pos = 0;
        int posUgu;
        int posEnd;

        //        if (tmplBioServer != null && !tmplBioServer.equals("")) {
        //            mappa = new LinkedHashMap();
        //            for (ParBio par : ParBio.values()) {
        //                if (par == ParBio.titolo) {
        //                    continue;
        //                }
        //                valore = par.getTag();
        //
        //                try { // prova ad eseguire il codice
        //                    pos = text.getPosFirstTag(tmplBioServer, valore);
        //                } catch (Exception unErrore) { // intercetta l'errore
        //                    int a = 87;
        //                }
        //                if (pos > 0) {
        //                    mappaTmp.put(pos, valore);
        //                }
        //            }
        //
        //            Object[] matrice = mappaTmp.keySet().toArray();
        //            Arrays.sort(matrice);
        //            ArrayList<Object> lista = new ArrayList<Object>();
        //            for (Object lungo : matrice) {
        //                lista.add(lungo);
        //            }
        //
        //            for (int k = 1; k <= lista.size(); k++) {
        //                chiave = mappaTmp.get(lista.get(k - 1));
        //
        //                try { // prova ad eseguire il codice
        //                    if (k < lista.size()) {
        //                        posEnd = (Integer) lista.get(k);
        //                    }
        //                    else {
        //                        posEnd = tmplBioServer.length();
        //                    }
        //                    valore = tmplBioServer.substring((Integer) lista.get(k - 1), posEnd);
        //                } catch (Exception unErrore) { // intercetta l'errore
        //                    int c = 76;
        //                }
        //                if (!valore.equals(VUOTA)) {
        //                    valore = valore.trim();
        //                    posUgu = valore.indexOf(uguale);
        //                    if (posUgu != -1) {
        //                        posUgu += uguale.length();
        //                        valore = valore.substring(posUgu).trim();
        //                    }
        //                    valore = regValore(valore);
        //                    if (!text.isPariTag(valore, DOPPIE_GRAFFE_INI, DOPPIE_GRAFFE_END)) {
        //                        valore = regGraffe(valore);
        //                    }
        //                    valore = regACapo(valore);
        //                    valore = regBreakSpace(valore);
        //                    valore = valore.trim();
        //                    mappa.put(chiave, valore);
        //                }
        //            }
        //        }

        return mappa;
    }

    //    /**
    //     * Mappa chiave-valore con i valori 'troncati' <br>
    //     * Valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
    //     *
    //     * @param mappaDownload coi valori originali provenienti dalla property tmplBioServer della entity Bio
    //     *
    //     * @return mappa con i valori 'troncati'
    //     */
    //    public LinkedHashMap<String, String> getMappaTroncata(LinkedHashMap<String, String> mappaDownload) {
    //        LinkedHashMap<String, String> mappa = null;
    //        ParBio par;
    //        String key;
    //        String value;
    //
    //        if (mappaDownload != null) {
    //            mappa = new LinkedHashMap<>();
    //            for (Map.Entry<String, String> entry : mappaDownload.entrySet()) {
    //                key = entry.getKey();
    //                value = entry.getValue();
    //                par = ParBio.getType(key);
    //                value = par.estraeValoreInizialeGrezzo(value);
    //                mappa.put(entry.getKey(), value);
    //            }
    //        }
    //
    //        return mappa;
    //    }

    //    /**
    //     * Mappa chiave-valore con i valori 'elaborati' <br>
    //     * Valore elaborato valido (minuscole, quadre, ecc.) <br>
    //     *
    //     * @param mappaTroncata dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante
    //     *
    //     * @return mappa con i valori 'elaborati'
    //     */
    //    public LinkedHashMap<String, String> getMappaElaborata(LinkedHashMap<String, String> mappaTroncata) throws AlgosException {
    //        LinkedHashMap<String, String> mappa = null;
    //        ParBio par;
    //        String key;
    //        String value;
    //
    //        if (mappaTroncata != null) {
    //            mappa = new LinkedHashMap<>();
    //            for (Map.Entry<String, String> entry : mappaTroncata.entrySet()) {
    //                key = entry.getKey();
    //                value = entry.getValue();
    //                par = ParBio.getType(key);
    //                value = par.regolaValoreInizialeValido(value);
    //                mappa.put(entry.getKey(), value);
    //            }
    //        }
    //
    //        return mappa;
    //    }


    /**
     * Elimina il testo se inizia con un pipe <br>
     */
    public String regValore(String valoreIn) {
        String valoreOut = valoreIn;

        if (valoreIn.startsWith(PIPE)) {
            valoreOut = VUOTA;
        }

        return valoreOut.trim();
    }


    /**
     * Elimina le graffe finali
     */
    public String regGraffe(String valoreIn) {
        String valoreOut = valoreIn;

        if (valoreIn.endsWith(DOPPIE_GRAFFE_END)) {
            valoreOut = valoreOut.substring(0, valoreOut.length() - DOPPIE_GRAFFE_END.length());
        }

        return valoreOut.trim();
    }


    /**
     * Controlla il primo aCapo che trova:
     * - se è all'interno di doppie graffe, non leva niente
     * - se non ci sono dopppie graffe, leva dopo l' aCapo
     */
    public String regACapo(String valoreIn) {
        String valoreOut = valoreIn;
        String doppioACapo = CAPO + CAPO;
        String pipeACapo = CAPO + PIPE;
        int pos;
        HashMap mappaGraffe;

        if (!valoreIn.equals(VUOTA) && valoreIn.contains(doppioACapo)) {
            valoreOut = valoreOut.replace(doppioACapo, CAPO);
        }

        //        if (!valoreIn.equals(VUOTA) && valoreIn.contains(pipeACapo)) {
        //            mappaGraffe = utility.checkGraffe(valoreIn);
        //
        //            if (mappaGraffe.containsKey(KEY_MAP_GRAFFE_ESISTONO)) {
        //            }
        //            else {
        //                pos = valoreIn.indexOf(pipeACapo);
        //                valoreOut = valoreIn.substring(0, pos);
        //            }
        //        }

        return valoreOut.trim();
    }


    /**
     * Elimina un valore strano trovato (ed invisibile)
     * ATTENZIONE: non è uno spazio vuoto !
     * Trattasi del carattere: C2 A0 ovvero U+00A0 ovvero NO-BREAK SPACE
     * Non viene intercettato dal comando Java TRIM()
     */
    public String regBreakSpace(String valoreIn) {
        String valoreOut = valoreIn;
        String strano = " ";   //NON cancellare: sembra uno spazio, ma è un carattere invisibile

        if (valoreIn.startsWith(strano)) {
            valoreOut = valoreIn.substring(1);
        }

        if (valoreIn.endsWith(strano)) {
            valoreOut = valoreIn.substring(0, valoreIn.length() - 1);
        }

        return valoreOut.trim();
    }

    /**
     * Regola questo campo
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixNomeValido(String testoGrezzo) {
        return fixValoreGrezzo(testoGrezzo);
    }

    /**
     * Regola questo campo
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    public String fixCognomeValido(String testoGrezzo) {
        return fixValoreGrezzo(testoGrezzo);
    }

    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixValoreGrezzo(String valoreGrezzo) {
        String valoreValido = valoreGrezzo.trim();

        if (textService.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }

        valoreValido = textService.setNoQuadre(valoreValido);

        return valoreValido.trim();
    }

    //    /**
    //     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
    //     *
    //     * @param valoreGrezzo in entrata da elaborare
    //     *
    //     * @return valore finale valido del parametro
    //     */
    //    public String fixMaiuscola(String valoreGrezzo) {
    //        String valoreValido = fixValoreGrezzo(valoreGrezzo);
    //
    //        if (text.isEmpty(valoreGrezzo)) {
    //            return VUOTA;
    //        }
    //
    //        valoreValido = text.primaMaiuscola(valoreValido);
    //
    //        return valoreValido.trim();
    //    }


    /**
     * Elabora un valore GREZZO e restituisce un valore VALIDO <br>
     *
     * @param valoreGrezzo in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    public String fixMinuscola(String valoreGrezzo) {
        String valoreValido = fixValoreGrezzo(valoreGrezzo);

        if (textService.isEmpty(valoreGrezzo)) {
            return VUOTA;
        }

        valoreValido = textService.primaMinuscola(valoreValido);

        return valoreValido.trim();
    }


    /**
     * Restituisce un valore grezzo troncato dopo alcuni tag chiave <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    public String estraeValoreInizialeGrezzoPuntoAmmesso(String valorePropertyTmplBioServer) {
        return troncaDopoTag(valorePropertyTmplBioServer, true);
    }


    /**
     * Restituisce un valore grezzo troncato dopo alcuni tag chiave <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    public String estraeValoreInizialeGrezzoPuntoEscluso(String valorePropertyTmplBioServer) {
        return troncaDopoTag(valorePropertyTmplBioServer, false);
    }


    /**
     * Elimina gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * <p>
     * Tag chiave di troncatura sempre validi:
     * REF = "<ref"
     * NOTE = "<!--"
     * GRAFFE = "{{"
     * UGUALE = "="
     * CIRCA = "circa";
     * ECC = "ecc."
     * INTERROGATIVO = "?"
     * <p>
     * Tag chiave di troncatura opzionali a seconda del parametro:
     * PARENTESI = "("
     * VIRGOLA = ","
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    public String troncaDopoTag(String valorePropertyTmplBioServer, boolean puntoAmmesso) {
        String valoreGrezzo = valorePropertyTmplBioServer.trim();

        if (textService.isEmpty(valorePropertyTmplBioServer)) {
            return VUOTA;
        }

        //--solo date certe ed esatte
        //        if (valoreGrezzo.contains(CIRCA)) {
        //            return VUOTA;
        //        }

        if (valorePropertyTmplBioServer.equals(PUNTO_INTERROGATIVO) && puntoAmmesso) {
            return PUNTO_INTERROGATIVO;
        }

        valoreGrezzo = textService.levaDopoRef(valoreGrezzo);
        valoreGrezzo = textService.levaDopoTagRef(valoreGrezzo);
        valoreGrezzo = textService.levaDopoNote(valoreGrezzo);
        valoreGrezzo = textService.levaDopoGraffe(valoreGrezzo);
        valoreGrezzo = textService.levaDopoWiki(valoreGrezzo);
        valoreGrezzo = textService.levaDopoUguale(valoreGrezzo);
        valoreGrezzo = textService.levaDopoCirca(valoreGrezzo);
        valoreGrezzo = textService.levaCoda(valoreGrezzo, CIRCA);
        valoreGrezzo = textService.levaDopoEccetera(valoreGrezzo);
        valoreGrezzo = textService.levaDopoInterrogativo(valoreGrezzo);
        valoreGrezzo = textService.setNoQuadre(valoreGrezzo);

        return valoreGrezzo.trim();
    }
    //
    //    /**
    //     * Legge (come user) una serie pagina dal server wiki <br>
    //     * Usa una API con action=query SENZA bisogno di loggarsi <br>
    //     * Recupera dalla urlRequest title, pageid, timestamp e wikitext <br>
    //     * Estrae il wikitext in linguaggio wiki visibile <br>
    //     *
    //     * @param pageIds della pagina wiki
    //     *
    //     * @return wrapper con testo completo (visibile) della pagina wiki
    //     */
    //    public List<WrapPage> leggePages(String pageIds, String tagTemplate) {
    //        List<WrapPage> wraps = null;
    //        pageIds = wikiApi.fixWikiTitle(pageIds);
    //        String webUrl = WIKI_QUERY_PAGEIDS + pageIds;
    //        String rispostaAPI = web.legge(webUrl).getResponse();
    //        WrapPage wrap = null;
    //
    //        JSONArray jsonPages = jSonService.getArrayPagine(rispostaAPI);
    //        if (jsonPages != null) {
    //            wraps = new ArrayList<>();
    //            for (Object obj : jsonPages) {
    //                wrap = creaPage(webUrl, (JSONObject) obj, tagTemplate);
    //                if (wrap.isValida()) {
    //                    wraps.add(wrap);
    //                }
    //            }
    //        }
    //
    //        return wraps;
    //    }
    //
    //    /**
    //     * Legge (come user) una pagina dal server wiki <br>
    //     * Usa una API con action=query SENZA bisogno di loggarsi <br>
    //     * Recupera dalla urlRequest title, pageid, timestamp e tmpl <br>
    //     * Estrae il wikitext in linguaggio wiki visibile <br>
    //     *
    //     * @param wikiTitleGrezzo della pagina wiki
    //     *
    //     * @return wrapper con template (visibile) della pagina wiki
    //     */
    //    public WrapPage leggePage(final String wikiTitleGrezzo, String tagTemplate) {
    //        String webUrl = webUrlQueryTitles(wikiTitleGrezzo);
    //        return creaPage(webUrl, tagTemplate);
    //    }

    //    /**
    //     * Legge (come user) una pagina dal server wiki <br>
    //     * Usa una API con action=query SENZA bisogno di loggarsi <br>
    //     * Recupera dalla urlRequest title, pageid, timestamp e wikitext <br>
    //     * Estrae il wikitext in linguaggio wiki visibile <br>
    //     *
    //     * @param webUrl completo
    //     *
    //     * @return wrapper con testo completo (visibile) della pagina wiki
    //     */
    //    private WrapPage creaPage(final String webUrl, String tagTemplate) {
    //        String rispostaAPI = web.legge(webUrl).getResponse();
    //        JSONObject jsonPageZero = jSonService.getObjectPage(rispostaAPI);
    //        return creaPage(webUrl, jsonPageZero, tagTemplate);
    //    }

    //    /**
    //     * Recupera una singola page dal testo JSON di risposta ad una query <br>
    //     *
    //     * @param rispostaDellaQuery in ingresso
    //     *
    //     * @return singola page (la prima)
    //     */
    //    public JSONObject getObjectPage(String rispostaDellaQuery) {
    //        JSONObject objectPage = null;
    //        JSONArray arrayPagine = jSonService.getArrayPagine(rispostaDellaQuery);
    //
    //        if (arrayPagine != null) {
    //            objectPage = (JSONObject) arrayPagine.get(0);
    //        }
    //
    //        return objectPage;
    //    }

    //    /**
    //     * Recupera un array di pages dal testo JSON di risposta ad una query <br>
    //     *
    //     * @param rispostaDellaQuery in ingresso
    //     *
    //     * @return array di pages
    //     */
    //    public JSONArray getArrayPagine(String rispostaDellaQuery) {
    //        JSONArray arrayQuery = null;
    //        JSONObject objectQuery = getPages(rispostaDellaQuery);
    //
    //        //--recupera i valori dei parametri pages
    //        if (objectQuery != null && objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
    //            arrayQuery = (JSONArray) objectQuery.get(PAGES);
    //        }
    //
    //        return arrayQuery;
    //    }

    //    /**
    //     * Recupera l'oggetto 'pages'' dal testo JSON di una pagina action=query
    //     *
    //     * @param rispostaDellaQuery in ingresso
    //     *
    //     * @return oggetto 'pages'
    //     */
    //    public JSONObject getPages(String rispostaDellaQuery) {
    //        JSONObject objectQuery = null;
    //        JSONObject objectAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);
    //
    //        //--recupera i valori dei parametri pages
    //        if (objectAll != null && objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
    //            objectQuery = (JSONObject) objectAll.get(QUERY);
    //        }
    //
    //        return objectQuery;
    //    }

    //    private WrapPage creaPage(final String webUrl, final JSONObject jsonPage, String tagTemplate) {
    //        long pageid;
    //        String title;
    //        String stringTimestamp;
    //        String content;
    //        String tmpl;
    //
    //        title = (String) jsonPage.get(KEY_JSON_TITLE);
    //
    //        if (isMissing(jsonPage)) {
    //            return new WrapPage(webUrl, title, AETypePage.nonEsiste);
    //        }
    //
    //        pageid = (long) jsonPage.get(KEY_JSON_PAGE_ID);
    //        JSONArray jsonRevisions = (JSONArray) jsonPage.get(KEY_JSON_REVISIONS);
    //        JSONObject jsonRevZero = (JSONObject) jsonRevisions.get(0);
    //        stringTimestamp = (String) jsonRevZero.get(KEY_JSON_TIMESTAMP);
    //        JSONObject jsonSlots = (JSONObject) jsonRevZero.get(KEY_JSON_SLOTS);
    //        JSONObject jsonMain = (JSONObject) jsonSlots.get(KEY_JSON_MAIN);
    //        content = (String) jsonMain.get(KEY_JSON_CONTENT);
    //
    //        //--la pagina esiste ma il content no
    //        if (text.isEmpty(content)) {
    //            return new WrapPage(webUrl, pageid, title, VUOTA, stringTimestamp, AETypePage.testoVuoto);
    //        }
    //
    //        //--contenuto inizia col tag della disambigua
    //        if (content.startsWith(TAG_DISAMBIGUA_UNO) || content.startsWith(TAG_DISAMBIGUA_DUE)) {
    //            return new WrapPage(webUrl, title, AETypePage.disambigua);
    //        }
    //
    //        //--contenuto inizia col tag del redirect
    //        if (content.startsWith(TAG_REDIRECT_UNO) || content.startsWith(TAG_REDIRECT_DUE) || content.startsWith(TAG_REDIRECT_TRE) || content.startsWith(TAG_REDIRECT_QUATTRO)) {
    //            return new WrapPage(webUrl, title, AETypePage.redirect);
    //        }
    //
    //        //--flag per la ricerca o meno del template
    //        if (text.isValid(tagTemplate)) {
    //            //--prova ad estrarre il template
    //            //            tmpl = wikiApi.estraeTmpl(content, tagTemplate);//@todo RIMETTERE
    //            tmpl = "pippoz";
    //            if (text.isValid(tmpl)) {
    //                return new WrapPage(webUrl, pageid, title, tmpl, stringTimestamp, AETypePage.testoConTmpl);
    //            }
    //            else {
    //                return new WrapPage(webUrl, pageid, title, content, stringTimestamp, AETypePage.mancaTmpl);
    //            }
    //        }
    //        else {
    //            return new WrapPage(webUrl, pageid, title, content, stringTimestamp, AETypePage.testoSenzaTmpl);
    //        }
    //    }

    public boolean isMissing(final JSONObject jsonPage) {
        return jsonPage.get(KEY_JSON_MISSING) != null && (boolean) jsonPage.get(KEY_JSON_MISSING);
    }

    /**
     * Recupera il testo di una singola pagina dalla risposta alla query <br>
     * La query è la richiesta di una sola singola pagina <br>
     * <p>
     * 21 parametri
     * 10 generali
     * 8 revisions
     * 3 slots/main
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return testo della prima pagina
     */
    public String estraeTestoPaginaWiki(final String rispostaDellaQuery) {
        String testoPagina = VUOTA;
        JSONObject jsonPageZero = jSonService.getObjectPage(rispostaDellaQuery);

        if (jsonPageZero != null) {
            testoPagina = this.getContent(jsonPageZero);
        }

        return testoPagina;
    }

    //    public String getContent(String wikiTitle) {
    //        String textContent = VUOTA;
    //        WikiPage wikiPage = getWikiPageFromTitle(wikiTitle);
    //
    //        if (wikiPage != null) {
    //            textContent = wikiPage.getContent();
    //        }
    //
    //        return textContent;
    //    }

    /**
     * Recupera il contenuto testuale dal testo JSON di una singola pagina <br>
     * 21 parametri
     * 10 generali
     * 8 revisions
     * 3 slots/main
     *
     * @param paginaTextJSON in ingresso
     *
     * @return testo della pagina wiki
     */
    public String getContent(JSONObject paginaTextJSON) {
        String textContent = VUOTA;
        JSONArray arrayRevisions;
        JSONObject objectRevisions = null;
        JSONObject objectSlots;
        JSONObject objectMain = null;

        if (paginaTextJSON == null) {
            return null;
        }

        //--parametri revisions
        if (paginaTextJSON.get(REVISIONS) != null && paginaTextJSON.get(REVISIONS) instanceof JSONArray) {
            arrayRevisions = (JSONArray) paginaTextJSON.get(REVISIONS);
            if (arrayRevisions != null && arrayRevisions.size() > 0 && arrayRevisions.get(0) instanceof JSONObject) {
                objectRevisions = (JSONObject) arrayRevisions.get(0);
            }
        }

        //--parametri slots/main -> content
        if (objectRevisions != null && objectRevisions.get(SLOTS) != null && objectRevisions.get(SLOTS) instanceof JSONObject) {
            objectSlots = (JSONObject) objectRevisions.get(SLOTS);
            if (objectSlots.get(MAIN) != null && objectSlots.get(MAIN) instanceof JSONObject) {
                objectMain = (JSONObject) objectSlots.get(MAIN);
            }
        }

        if (objectMain != null && objectMain.get(CONTENT) != null) {
            textContent = (String) objectMain.get(CONTENT);
        }

        return textContent;
    }

    //    public WikiPage getWikiPageFromTitle(String wikiTitle) {
    //        String rispostaDellaQuery = leggeJsonTxt(wikiTitle);
    //        JSONObject jsonPageZero = jSonService.getObjectPage(rispostaDellaQuery);
    //        Map<String, Object> mappa = jSonService.getMappaJSON(jsonPageZero);
    //
    //        return getWikiPageFromMappa(mappa);
    //    }

    //    /**
    //     * Legge la risposta in formato JSON ad una query su API Mediawiki <br>
    //     * Usa le API base SENZA loggarsi <br>
    //     * Testo in linguaggio JSON non leggibile <br>
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return risultato col testo completo in formato JSON della pagina wiki, che può contenere più 'pages'
    //     */
    //    public String leggeJsonTxt(final String wikiTitle) {
    //        return leggeJson(wikiTitle).getResponse();
    //    }

    //    /**
    //     * Legge la risposta in formato JSON ad una query su API Mediawiki <br>
    //     * Usa le API base SENZA loggarsi <br>
    //     * Testo in linguaggio JSON non leggibile <br>
    //     *
    //     * @param wikiTitle della pagina wiki
    //     *
    //     * @return risultato col testo completo in formato JSON della pagina wiki, che può contenere più 'pages'
    //     */
    //    public WResult leggeJson(final String wikiTitle) {
    //        if (textService.isEmpty(wikiTitle)) {
    //            return WResult.errato("Manca il wikiTitle");
    //        }
    //
    //        return web.legge(WIKI_QUERY_TITLES + wikiTitle);
    //    }

    //    /**
    //     * Crea una mappa standard (valori reali) da una singola page JSON di una multi-pagina action=query <br>
    //     *
    //     * @param paginaJSON in ingresso
    //     *
    //     * @return mappa query (valori reali)
    //     */
    //    public HashMap<String, Object> getMappaJSON(JSONObject paginaJSON) {
    //        HashMap<String, Object> mappa = new HashMap<String, Object>();
    //        HashMap<String, Object> mappaRev;
    //        JSONArray arrayRev;
    //        String keyPage;
    //        Object value;
    //
    //        if (paginaJSON == null) {
    //            return null;
    //        }
    //
    //        //--recupera i valori dei parametri info
    //        for (PagePar par : PagePar.getRead()) {
    //            keyPage = par.toString();
    //            if (paginaJSON.get(keyPage) != null) {
    //                value = paginaJSON.get(keyPage);
    //                mappa.put(keyPage, value);
    //            }
    //        }
    //
    //        //--recupera i valori dei parametri revisions
    //        arrayRev = (JSONArray) paginaJSON.get(REVISIONS);
    //        if (arrayRev != null) {
    //            mappaRev = estraeMappaJsonPar(arrayRev);
    //            for (String key : mappaRev.keySet()) {
    //                value = mappaRev.get(key);
    //                mappa.put(key, value);
    //            }
    //        }
    //
    //        return mappa;
    //    }

    //    /**
    //     * Estrae una mappa standard da un JSONArray
    //     * Considera SOLO i valori della Enumeration PagePar
    //     *
    //     * @param arrayJson JSONArray in ingresso
    //     *
    //     * @return mappa standard (valori String)
    //     */
    //    private HashMap<String, Object> estraeMappaJsonPar(JSONArray arrayJson) {
    //        return estraeMappaJsonPar(arrayJson, 0);
    //    }

    //    /**
    //     * Estrae una mappa standard da un JSONArray
    //     * Considera SOLO i valori della Enumeration PagePar
    //     *
    //     * @param arrayJson JSONArray in ingresso
    //     * @param pos       elemento da estrarre
    //     *
    //     * @return mappa standard (valori String)
    //     */
    //    public HashMap<String, Object> estraeMappaJsonPar(JSONArray arrayJson, int pos) {
    //        HashMap<String, Object> mappaOut = new HashMap<String, Object>();
    //        JSONObject mappaJSON = null;
    //        String key;
    //        Object value;
    //        String slots = "slots";
    //        String main = "main";
    //        String contentName = "content";
    //        String content = VUOTA;
    //        JSONObject slotsObject = null;
    //        JSONObject mainObject = null;
    //        JSONObject contentObject = null;
    //
    //        if (arrayJson != null && arrayJson.size() > pos) {
    //            if (arrayJson.get(pos) != null && arrayJson.get(pos) instanceof JSONObject) {
    //                mappaJSON = (JSONObject) arrayJson.get(pos);
    //            }
    //        }
    //
    //        if (mappaJSON != null) {
    //            for (PagePar par : PagePar.getRead()) {
    //                key = par.toString();
    //                if (mappaJSON.get(key) != null) {
    //                    value = mappaJSON.get(key);
    //                    mappaOut.put(key, value);
    //                }
    //            }
    //        }
    //
    //        //--content
    //        if (mappaJSON.get(slots) != null) {
    //            slotsObject = (JSONObject) mappaJSON.get(slots);
    //            mainObject = (JSONObject) slotsObject.get(main);
    //            mappaOut.put(contentName, mainObject.get(contentName));
    //        }
    //
    //        return mappaOut;
    //    }

    //    public WikiPage getWikiPageFromMappa(Map<String, Object> mappa) {
    //        WikiPage wiki = new WikiPage();
    //        fixMappaWiki(wiki, mappa);
    //
    //        return wiki;
    //    }
    //
    //    /**
    //     * Regola i parametri della tavola in base alla mappa letta dal server
    //     * Aggiunge le date di riferimento lettura/scrittura
    //     */
    //    public WikiPage fixMappaWiki(WikiPage wiki, Map mappa) {
    //        List<PagePar> lista = PagePar.getDB();
    //        String key;
    //        Object value;
    //
    //        for (PagePar par : lista) {
    //            key = par.toString();
    //            value = null;
    //
    //            if (mappa.get(key) != null) {
    //                value = mappa.get(key);
    //            }
    //
    //            //--controllo dei LONG che POSSONO essere anche zero
    //            if (par.getType() == PagePar.TypeField.longzero) {
    //                if (value == null) {
    //                    value = 0;
    //                }
    //            }
    //
    //            //--patch
    //            if (par == PagePar.comment) {
    //                if (value instanceof String) {
    //                    if (((String) value).startsWith("[[WP:OA|←]]")) {
    //                        value = "Nuova pagina";
    //                    }
    //                }
    //            }
    //
    //            par.setWiki(wiki, value);
    //        }
    //
    //        return wiki;
    //    }

    /**
     * Legge (come user) una pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest tutti i dati della pagina <br>
     * Estrae il testo in linguaggio wiki visibile <br>
     * Elaborazione della urlRequest leggermente più complessa di leggeParse <br>
     * Tempo di download leggermente più corto di leggeParse <br>
     * Metodo base per tutte le API in semplice lettura <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return risultato col testo completo (visibile) della pagina wiki
     */
    public WResult leggeQuery(final String wikiTitleGrezzo) {
        WResult result = null;
        String webUrl;
        String rispostaDellaQuery;
        String testoValido;

        if (textService.isEmpty(wikiTitleGrezzo)) {
            return WResult.errato("Manca il wikiTitle");
        }

        webUrl = webUrlQuery(wikiTitleGrezzo);
        if (textService.isValid(webUrl)) {
            //            result = webService.legge(webUrl);
            //            rispostaDellaQuery = result.getResponse();
            //            testoValido = estraeTestoPaginaWiki(rispostaDellaQuery);
            //            result.setResponse(testoValido);
            return result;
        }
        else {
            return WResult.errato("Manca il domain");
        }
    }


    /**
     * Regola l'url per interrogare una pagina wiki <br>
     * Recupera spazio e caratteri strani nel titolo <br>
     * Aggiunge in testa il prefisso della API Mediawiki <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return webUrl completo
     */
    public String webUrlQuery(final String wikiTitleGrezzo) {
        return WIKI_QUERY + wikiApi.fixWikiTitle(wikiTitleGrezzo);
    }

    /**
     * Regola l'url per interrogare una pagina wiki <br>
     * Recupera spazio e caratteri strani nel titolo <br>
     * Aggiunge in testa il prefisso della API Mediawiki <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return webUrl completo
     */
    public String webUrlQueryTitles(final String wikiTitleGrezzo) {
        return WIKI_QUERY_TITLES + wikiApi.fixWikiTitle(wikiTitleGrezzo);
    }

    /**
     * Regola l'url per interrogare una pagina wiki <br>
     * Recupera spazio e caratteri strani nel titolo <br>
     * Aggiunge in testa il prefisso della API Mediawiki <br>
     *
     * @param wikiTitleGrezzo della pagina wiki
     *
     * @return webUrl completo
     */
    public String webUrlParse(final String wikiTitleGrezzo) {
        return WIKI_PARSE + wikiApi.fixWikiTitle(wikiTitleGrezzo);
    }

    /**
     * Controlla (come anonymous) l'esistenza di una pagina/categoria wiki <br>
     *
     * @param wikiSimplePageCategoryTitle della pagina/categoria wiki
     *
     * @return true se esiste, false se non esiste
     */
    public WResult isEsisteResult(final String wikiSimplePageCategoryTitle) {
        WResult resultWiki;
        WResult resultWeb = null;
        String wikiTitleElaborato;
        String rispostaDellaQuery;
        JSONObject objectJson = null;
        String wikiText;
        String wikiBio;
        long pageId = 0;

        if (wikiSimplePageCategoryTitle == null) {
            resultWiki = WResult.errato(NULL_WIKI_TITLE);
            resultWiki.setWebTitle(null);
            return resultWiki;
        }
        if (textService.isEmpty(wikiSimplePageCategoryTitle)) {
            resultWiki = WResult.errato(ERROR_WIKI_TITLE);
            resultWiki.setWebTitle(VUOTA);
            return resultWiki;
        }
        wikiTitleElaborato = wikiSimplePageCategoryTitle.replaceAll(SPAZIO, UNDERSCORE);

        rispostaDellaQuery = webService.legge(WIKI_PARSE + wikiTitleElaborato);
        //        resultWeb = (WResult)webService.legge(WIKI_PARSE + wikiTitleElaborato);
        resultWeb = WResult.crea().webTitle(wikiSimplePageCategoryTitle).wikiTitle(wikiTitleElaborato);

        //        rispostaDellaQuery = resultWeb.isValido() ? resultWeb.getResponse() : VUOTA;
        //        if (textService.isValid(rispostaDellaQuery)) {
        objectJson = jSonService.getObjectJSON(rispostaDellaQuery);
        if (objectJson == null) {
            return resultWeb;
        }
        else {
            if (objectJson.get(JSON_ERROR) != null) {
                resultWiki = WResult.errato(ERROR_WIKI_PAGINA);
                resultWiki.setUrlRequest(resultWeb.getUrlRequest());
                resultWiki.setResponse(rispostaDellaQuery);
                return resultWiki;
            }
            else {
                JSONObject parse = (JSONObject) objectJson.get("parse");
                pageId = (Long) parse.get("pageid");
                resultWeb.setLongValue(pageId);
                wikiText = (String) parse.get("wikitext");
                resultWeb.setWikiText(wikiText);
                wikiBio = wikiApi.estraeTmpl(wikiText, "Bio");
                resultWeb.setWikiBio(wikiBio);
                return resultWeb;
            }
        }
        //        }
        //        else {
        //            return resultWeb;
        //        }
    }

    /**
     * Controlla (come anonymous) l'esistenza di una pagina/categoria wiki <br>
     *
     * @param wikiSimplePageCategoryTitle della pagina/categoria wiki
     *
     * @return true se esiste, false se non esiste
     */
    public boolean isEsiste(final String wikiSimplePageCategoryTitle) {
        WResult result = isEsisteResult(wikiSimplePageCategoryTitle);
        return result.isValido();
    }


    /**
     * Controlla (come anonymous) l'esistenza di una categoria wiki <br>
     *
     * @param wikiSimpleCategoryTitle della categoria wiki
     *
     * @return true se esiste, false se non esiste
     */
    public boolean isEsisteCat(final String wikiSimpleCategoryTitle) {
        String catTitle = wikiSimpleCategoryTitle.startsWith(CATEGORIA) ? wikiSimpleCategoryTitle : CATEGORIA + wikiSimpleCategoryTitle;
        return isEsiste(catTitle);
    }

    /**
     * Legge una lista di pageid di una categoria wiki <br>
     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
     * La query restituisce SOLO pageid <br>
     *
     * @param catTitle da cui estrarre le pagine
     *
     * @return lista di pageid
     */
    public List<Long> getLongCat(final String catTitle) {
        //        return getLongCat(catTitle, null);
        return null;
    }

    //    /**
    //     * Legge una categoria wiki <br>
    //     * Senza cicli. Legge una volta sola. Restituisce il risultato <br>
    //     *
    //     * @param catTitle da cui estrarre le pagine
    //     * @param userType selezione tra (anonymous, user, bot)
    //     *
    //     * @return lista di pageid
    //     */
    //    public AIResult getResultCat(final String catTitle, final AETypeUser userType) {
    //        AIResult result;
    //        String catType = AECatType.page.getTag();
    //        String propType = AECatProp.pageid.getTag();
    //        String urlDomain = fixUrlCat(catTitle, catType, propType, userType, VUOTA);
    //
    //        result = readCategory(catTitle, urlDomain);
    //
    //        return result;
    //    }

    //    /**
    //     * Legge una lista di pageid di una categoria wiki <br>
    //     * Se arriva userType=null oppure userType=anonymous:
    //     * mette &cmlimit=500
    //     * non mette &assert=
    //     * ed esegue regolarmente tutti i cicli necessari (ci mette un po) <br>
    //     * //     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
    //     * //     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
    //     * //     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
    //     * Esegue dei cicli di dimensioni uguali a 'cmlimit' ammesso <br>
    //     * La query restituisce SOLO pageid <br>
    //     *
    //     * @param catTitle da cui estrarre le pagine
    //     * @param userType selezione tra (anonymous, user, bot)
    //     *
    //     * @return lista di pageid
    //     */
    //    public List<Long> getLongCat(final String catTitle, final AETypeUser userType) {
    //        List<Long> lista = new ArrayList<>();
    //        String catType = AECatType.page.getTag();
    //        String urlDomain;
    //        String propType = AECatProp.pageid.getTag();
    //        AIResult result;
    //        String continueParam = VUOTA;
    //        AETypeUser loggedUserType = userType != null ? userType : login.getUserType();
    //        long inizio = System.currentTimeMillis();
    //        int cicli = 0;
    //
    //        if (catTitle == null) {
    //            return null;
    //        }
    //        if (text.isEmpty(catTitle)) {
    //            return null;
    //        }
    //
    //        do {
    //            urlDomain = fixUrlCat(catTitle, catType, propType, loggedUserType, continueParam);
    //            //devo fare una POST e non una GET
    //            result = readCategory(catTitle, urlDomain);
    //            lista.addAll(getListaLongCategoria(result.getResponse()));
    //            continueParam = getContinuaCategoria(result.getResponse());
    //            cicli++;
    //        }
    //        while (text.isValid(continueParam));
    //
    //        logger.info(AETypeLog.bio, String.format("La categoria [%s] ha %s pageIds letti con %s cicli di %s in %s", catTitle, text.format(lista.size()), cicli, loggedUserType.limit(), date.deltaTextEsatto(inizio)));
    //
    //        return lista;
    //    }


    /**
     * Legge la pagina web <br>
     * Interpreta la response ricevuta <br>
     * Se è falsa, non fa nulla e rinvia la response falsa <br>
     * Se è valida, conta le pagine e regola il valore numerico <br>
     * Se le pagine sono 0, mette a false la result <br>
     *
     * @param catTitle  da cui estrarre le pagine
     * @param urlDomain da cui estrarre le pagine
     *
     * @return result della query
     */
    public WResult readCategory(final String catTitle, final String urlDomain) {
        //        WResult result = webService.legge(urlDomain);
        WResult result = null;
        JSONArray jsonPagine = null;
        boolean valido = result != null && result.isValido();
        int totalePagine;

        if (valido) {
            //            jsonPagine = jSonService.getJsonPagine(result.getResponse());
            totalePagine = jsonPagine != null ? jsonPagine.size() : 0;
            valido = totalePagine > 0;
            result.setIntValue(totalePagine);
            result.setValido(valido);
            //            result.setMessage(valido ? VUOTA : String.format("%s", NO_PAGES_CAT, catTitle));
        }
        else {
            //            result.setMessage(String.format("%s", NO_CAT, urlDomain));
        }

        return result;
    }

    /**
     * Legge una lista di titles di una categoria wiki <br>
     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
     * La query restituisce SOLO pageid <br>
     *
     * @param catTitle da cui estrarre le pagine
     *
     * @return lista di titles
     */
    public List<String> getTitleCat(final String catTitle) {
        //        return getTitleCat(catTitle, null);
        return null;
    }

    //    /**
    //     * Legge una lista di titles di una categoria wiki <br>
    //     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
    //     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
    //     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
    //     * La query restituisce SOLO pageid <br>
    //     *
    //     * @param catTitle da cui estrarre le pagine
    //     * @param userType selezione tra (anonymous, user, bot)
    //     *
    //     * @return lista di titles
    //     */
    //    public List<String> getTitleCat(final String catTitle, final AETypeUser userType) {
    //        List<String> lista = new ArrayList<>();
    //        String catType = AECatType.page.getTag();
    //        String urlDomain;
    //        String propType = AECatProp.title.getTag();
    //        AIResult result;
    //        String continueParam = VUOTA;
    //
    //        if (catTitle == null) {
    //            return null;
    //        }
    //        if (text.isEmpty(catTitle)) {
    //            return null;
    //        }
    //
    //        do {
    //            urlDomain = fixUrlCat(catTitle, catType, propType, userType, continueParam);
    //            result = web.legge(urlDomain);
    //            lista.addAll(getListaTitleCategoria(result.getResponse()));
    //            continueParam = getContinuaCategoria(result.getResponse());
    //        }
    //        while (text.isValid(continueParam));
    //
    //        return lista;
    //    }

    //    /**
    //     * Costruisce l'url <br>
    //     * Se entriamo come (anonymous) restituisce, senza errore, le prime 500 pagine <br>
    //     * Se entriamo come (user) e NON lo siamo, non restituisce nulla e da errore <br>
    //     * Se entriamo come (bot) e NON lo siamo, non restituisce nulla e da errore <br>
    //     *
    //     * @param catTitle      da cui estrarre le pagine
    //     * @param catType       selezione tra (page, subcat or file)
    //     * @param propType      selezione tra (ids, sortkey, sortkeyprefix, timestamp, title, type)
    //     * @param userType      selezione tra (anonymous, user, bot)
    //     * @param continueParam per la successiva query
    //     *
    //     * @return testo dell'url
    //     */
    //    private String fixUrlCat(final String catTitle, final String catType, final String propType, final AETypeUser userType, final String continueParam) {
    //        String query = WIKI_QUERY_CATEGORY + wikiApi.fixWikiTitle(catTitle);
    //        String type = WIKI_QUERY_CAT_TYPE + catType;
    //        String prop = WIKI_QUERY_CAT_PROP + propType;
    //        String limit = userType != null ? userType.limit() : AETypeUser.anonymous.limit();
    //        String user = userType != null ? userType.affermazione() : AETypeUser.anonymous.affermazione();
    //        String continua = WIKI_QUERY_CAT_CONTINUE + continueParam;
    //
    //        return String.format("%s%s%s%s%s%s", query, type, prop, limit, user, continua);
    //    }

    //    /**
    //     * Recupera un lista di 'pageid'' dal testo JSON di risposta ad una query <br>
    //     *
    //     * @param rispostaDellaQuery in ingresso
    //     *
    //     * @return array di pageid
    //     */
    //    private List<Long> getListaLongCategoria(String rispostaDellaQuery) {
    //        JSONArray jsonPagine = jSonService.getJsonPagine(rispostaDellaQuery);
    //        return getListaLongCategoria(jsonPagine);
    //    }

    /**
     * Recupera un lista di 'pageid'' dal testo JSON di risposta ad una query <br>
     *
     * @param jsonPagine in ingresso
     *
     * @return array di 'pageid'
     */
    private List<Long> getListaLongCategoria(JSONArray jsonPagine) {
        List<Long> lista = new ArrayList<>();
        long pageid;

        if (jsonPagine != null && jsonPagine.size() > 0) {
            for (Object obj : jsonPagine) {
                pageid = (long) ((JSONObject) obj).get(PAGE_ID);
                lista.add(pageid);
            }
        }

        return lista;
    }

    //    /**
    //     * Recupera un lista di 'pageid'' dal testo JSON di risposta ad una query <br>
    //     *
    //     * @param rispostaDellaQuery in ingresso
    //     *
    //     * @return array di pageid
    //     */
    //    private List<String> getListaTitleCategoria(String rispostaDellaQuery) {
    //        JSONArray jsonPagine = jSonService.getJsonPagine(rispostaDellaQuery);
    //        return getListaTitleCategoria(jsonPagine);
    //    }

    /**
     * Recupera un lista di 'title'' dal testo JSON di risposta ad una query <br>
     *
     * @param jsonPagine in ingresso
     *
     * @return array di 'title'
     */
    private List<String> getListaTitleCategoria(JSONArray jsonPagine) {
        List<String> lista = new ArrayList<>();
        String title;

        if (jsonPagine != null && jsonPagine.size() > 0) {
            for (Object obj : jsonPagine) {
                title = (String) ((JSONObject) obj).get(TITLE);
                lista.add(title);
            }
        }

        return lista;
    }


    /**
     * Legge (come anonymous) le info di una categoria wiki <br>
     * Controlla la validità della response <br>
     *
     * @param categoryTitle da controllare
     *
     * @return numero di pagine (subcategorie escluse)
     */
    public WResult getInfoCategoria(final String categoryTitle) {
        WResult result = null;
        String catTitleUnderscored;
        String catTitle;
        int totale;
        String rispostaDellaQuery;
        String webUrl;

        if (categoryTitle == null) {
            result = WResult.errato(NULL_WIKI_TITLE);
            result.setWebTitle(null);
            return result;
        }
        if (textService.isEmpty(categoryTitle)) {
            result = WResult.errato(ERROR_WIKI_TITLE);
            result.setWebTitle(VUOTA);
            return result;
        }

        catTitleUnderscored = categoryTitle.replaceAll(SPAZIO, UNDERSCORE);
        catTitle = catTitleUnderscored.startsWith(CATEGORIA) ? catTitleUnderscored : CATEGORIA + catTitleUnderscored;
        result = isEsisteResult(catTitle);

        if (result.isValido()) {
            webUrl = WIKI_QUERY_CAT_TOTALE + catTitleUnderscored;
            //            result = webService.legge(webUrl);
            result.setWebTitle(categoryTitle);
            result.setWikiTitle(catTitleUnderscored);
        }
        else {
            result.setWebTitle(categoryTitle);
            result.setErrorCode(ERROR_WIKI_CATEGORIA);
            result.setErrorMessage(ERROR_WIKI_CATEGORIA);
            return result;
        }

        rispostaDellaQuery = result.getResponse();
        //        JSONObject jsonPageZero = jSonService.getObjectPage(rispostaDellaQuery);
        //        if (isMissing(jsonPageZero)) {
        //            return AResult.errato();
        //        }

        //        JSONObject categoryInfo = (JSONObject) jsonPageZero.get(CATEGORY_INFO);
        //        if (categoryInfo != null && categoryInfo.get(CATEGORY_PAGES) != null) {
        //            totale = ((Long) categoryInfo.get(CATEGORY_PAGES)).intValue();
        //            result.setIntValue(totale);
        //        }

        return result;
    }

    //    /**
    //     * Legge (come anonymous) il numero di pagine di una categoria wiki <br>
    //     *
    //     * @param categoryTitle da controllare
    //     *
    //     * @return numero di pagine (subcategorie escluse)
    //     */
    //    public int getTotaleCategoria(final String categoryTitle) {
    //        AIResult result = getInfoCategoria(categoryTitle);
    //
    //        if (result != null && result.isValido()) {
    //            logger.info(AETypeLog.bio, String.format("La categoria [%s] esiste e ci sono %s voci", categoryTitle, text.format(result.getIntValue())));
    //            return result.getIntValue();
    //        }
    //        else {
    //            logger.info(AETypeLog.bio, String.format("La categoria [%s] non esiste oppure è vuota", categoryTitle));
    //            return 0;
    //        }
    //    }

    //    /**
    //     * Legge una lista di pageid di una categoria wiki <br>
    //     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
    //     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
    //     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
    //     * La query restituisce SOLO pageid <br>
    //     *
    //     * @param categoryTitle da recuperare
    //     *
    //     * @return lista di pageid
    //     */
    //    public String getPageidsCat(final String categoryTitle) {
    //        String striscia = VUOTA;
    //        List<Long> lista = getLongCat(categoryTitle, AETypeUser.anonymous);
    //
    //        striscia = array.toStringaPipe(lista);
    //
    //        return striscia;
    //    }

    //    /**
    //     * Legge una lista di WrapCat di una categoria wiki <br>
    //     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
    //     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
    //     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
    //     * La query restituisce sia pageid che title <br>
    //     *
    //     * @param titleWikiCategoria da recuperare
    //     *
    //     * @return lista di WrapCat
    //     */
    //    public List<WrapCat> getWrapCat(final String titleWikiCategoria) {
    //        return getWrapCat(titleWikiCategoria, AECatType.page);
    //    }

    //    /**
    //     * Legge una lista di WrapCat di pagine/files/subcategorie di una categoria wiki <br>
    //     * Se non si mette 'cmlimit' restituisce 10 pagine <br>
    //     * Valore massimo di 'cmlimit' (come user) 500 pagine <br>
    //     * Il valore massimo (come user) di 'cmlimit' è 20 <br>
    //     * La query restituisce sia pageid che title <br>
    //     *
    //     * @param catTitle  da recuperare
    //     * @param aeCatType per la selezione
    //     *
    //     * @return lista di WrapCat
    //     */
    //    public List<WrapCat> getWrapCat(final String catTitle, final AECatType aeCatType) {
    //        List<WrapCat> lista = new ArrayList<>();
    //        String catType = aeCatType.getTag();
    //        String urlDomain;
    //        AIResult result;
    //        String propType = AECatProp.all.getTag();
    //        String continueParam = VUOTA;
    //
    //        do {
    //            urlDomain = fixUrlCat(catTitle, catType, propType, null, continueParam);
    //            result = web.legge(urlDomain);
    //            if (result.isValido()) {
    //                lista.addAll(getListaWrapCategoria(result.getResponse()));
    //                continueParam = getContinuaCategoria(result.getResponse());
    //            }
    //            else {
    //                int a = 87;
    //            }
    //        }
    //        while (text.isValid(continueParam));
    //
    //        return lista;
    //    }

    //    /**
    //     * Recupera un lista di WrapCat dal testo JSON di risposta ad una query <br>
    //     *
    //     * @param rispostaDellaQuery in ingresso
    //     *
    //     * @return array di WrapCat
    //     */
    //    private List<WrapCat> getListaWrapCategoria(String rispostaDellaQuery) {
    //        JSONArray jsonPagine = jSonService.getJsonPagine(rispostaDellaQuery);
    //        return getListaWrapCategoria(jsonPagine);
    //    }

    /**
     * Recupera il tag per le categorie successive <br>
     * Nella forma 'page|token|pageid' <br>
     *
     * @param rispostaDellaQuery in ingresso
     *
     * @return token e prossima pagina
     */
    private String getContinuaCategoria(String rispostaDellaQuery) {
        String continua = VUOTA;
        JSONObject objectAll = (JSONObject) JSONValue.parse(rispostaDellaQuery);
        JSONObject jsonContinue;

        //--recupera il valore del parametro
        if (objectAll != null && objectAll.get(CONTINUE) != null && objectAll.get(CONTINUE) instanceof JSONObject) {
            jsonContinue = (JSONObject) objectAll.get(CONTINUE);
            continua = (String) jsonContinue.get(CONTINUE_CM);
        }

        return continua;
    }

    //    /**
    //     * Recupera un lista di WrapCat dal testo JSON di risposta ad una query <br>
    //     *
    //     * @param jsonPagine in ingresso
    //     *
    //     * @return array di WrapCat
    //     */
    //    private List<WrapCat> getListaWrapCategoria(JSONArray jsonPagine) {
    //        List<WrapCat> lista = new ArrayList<>();
    //        WrapCat wrap;
    //        long pageid;
    //        String title;
    //
    //        if (jsonPagine != null && jsonPagine.size() > 0) {
    //            for (Object obj : jsonPagine) {
    //                pageid = (long) ((JSONObject) obj).get(PAGE_ID);
    //                title = (String) ((JSONObject) obj).get(TITLE);
    //                wrap = new WrapCat(pageid, title);
    //                lista.add(wrap);
    //            }
    //        }
    //
    //        return lista;
    //    }

    /**
     * Legge (come user) una pagina dal server wiki <br>
     * Usa una API con action=query SENZA bisogno di loggarsi <br>
     * Recupera dalla urlRequest tutti i dati della pagina <br>
     * Estrae il testo in linguaggio wiki visibile <br>
     * Elaborazione della urlRequest leggermente più complessa di leggeParse <br>
     * Tempo di download leggermente più corto di leggeParse <br>
     * Metodo base per tutte le API in semplice lettura <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return risultato col testo completo (visibile) della pagina wiki
     */
    public String leggeQueryTxt(final String wikiTitle) {
        return leggeQuery(wikiTitle).getResponse();
    }

    //    /**
    //     * Controlla di essere loggato come bot <br>
    //     */
    //    public boolean checkCollegamentoComeBot() {
    //        return appContext.getBean(QueryAssert.class).isValida();
    //    }

}