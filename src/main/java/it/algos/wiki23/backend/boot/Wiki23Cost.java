package it.algos.wiki23.backend.boot;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaad23.backend.boot.VaadCost.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project Wiki23
 * Created by Algos
 * User: gac
 * Date: ven, 29 apr 22
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Wiki23Cost {

    public static final String WIKI_TITLE_DEBUG = "Utente:Biobot/2";

    public static final String TAG_BOLD = "'''";

    public static final String TAG_ITALIC = "''";

    public static final String TAG_WIKI23_VERSION = "wikiversion";

    public static final String TAG_WIKI23_PREFERENCES = "wikipreferences";

    public static final String PATH_MODULO = "Modulo:Bio/";

    public static final String PATH_PROGETTO = "Progetto:Biografie/";

    public static final String PATH_MODULO_PLURALE = PATH_MODULO + "Plurale ";

    public static final String PATH_MODULO_LINK = PATH_MODULO + "Link ";

    public static final String GENERE = "genere";

    public static final String GIORNI = "Giorni";

    public static final String ANNI = "ANNI";

    public static final String ATT = "Attività";

    public static final String NAZ = "Nazionalità";


    public static final String PATH_WIKI = "https://it.wikipedia.org/wiki/";

    public static final String API_BASE = "https://it.wikipedia.org/w/api.php?&format=json&formatversion=2";

    public static final String ACTION_PARSE = API_BASE + "&action=parse";

    public static final String WIKI_PARSE = ACTION_PARSE + "&prop=wikitext&page=";

    public static final String ACTION_QUERY = API_BASE + "&action=query";

    public static final String API_TITLES = "&titles=";

    public static final String API_PAGEIDS = "&pageids=";

    public static final String CAT = "Category:";

    public static final String API_CAT = "&cmtitle=";

    public static final String API_CAT_INFO = API_TITLES;

    public static final String WIKI_QUERY = ACTION_QUERY + API_TITLES;

    public static final String WIKI_QUERY_PAGEIDS = ACTION_QUERY + API_PAGEIDS;

    public static final String QUERY_TIMESTAMP = "&prop=revisions&rvprop=timestamp";

    public static final String QUERY_INFO = QUERY_TIMESTAMP + "|content";

    public static final String QUERY_INFO_ALL = "&rvslots=main&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size";

    public static final String QUERY_CAT_INFO = "&prop=categoryinfo";

    public static final String QUERY_CAT_REQUEST = ACTION_QUERY + "&list=categorymembers" + API_CAT;

    public static final String WIKI_QUERY_BASE_TITLE = ACTION_QUERY + QUERY_INFO + API_TITLES;

    public static final String WIKI_QUERY_BASE_PAGE = ACTION_QUERY + QUERY_INFO + API_PAGEIDS;

    public static final String WIKI_QUERY_TIMESTAMP = ACTION_QUERY + QUERY_TIMESTAMP + API_PAGEIDS;

    public static final String WIKI_QUERY_ALL = ACTION_QUERY + QUERY_INFO_ALL + API_TITLES;

    public static final String WIKI_QUERY_CAT_INFO = ACTION_QUERY + QUERY_CAT_INFO + API_CAT_INFO;

    //
    //    public static final String API = "https://it.wikipedia.org/w/api.php?&format=json&formatversion=2";
    //
    //    public static final String API_QUERY = API + "&action=query&titles=";
    //
    //    public static final String API_PARSE = "https://it.wikipedia.org/w/api.php?action=parse&prop=wikitext&formatversion=2&format=json&page=";

    //    public static final String API_EDIT = "https://it.wikipedia.org/w/index.php?action=edit&section=0&title=";

    //    public static final String API_HISTORY = "https://it.wikipedia.org/w/index.php?action=history&title=";


    public static final String PATH_ATTIVITA = PATH_PROGETTO + ATT;

    public static final String PATH_NAZIONALITA = PATH_PROGETTO + NAZ;


    public static final String ATT_LOWER = ATT.toLowerCase();

    public static final String PATH_MODULO_GENERE = PATH_MODULO_PLURALE + ATT_LOWER + SPAZIO + GENERE;

    public static final String PATH_MODULO_ATTIVITA = PATH_MODULO_PLURALE + ATT_LOWER;

    public static final String PATH_TABELLA_NOMI_DOPPI = "Progetto:Antroponimi/Nomi_doppi";

    public static final String PATH_MODULO_PROFESSIONE = PATH_MODULO_LINK + ATT_LOWER;


    public static final String NAZ_LOWER = NAZ.toLowerCase();

    public static final String PATH_MODULO_NAZIONALITA = PATH_MODULO_PLURALE + NAZ_LOWER;

    public static final String PATH_STATISTICHE_ATTIVITA = PATH_PROGETTO + ATT;

    public static final String PATH_STATISTICHE_NAZIONALITA = PATH_PROGETTO + NAZ;

    public static final String PATH_MODULO_PRENOME = "Progetto:Antroponimi/Nomi doppi";

    public static final String PATH_STATISTICHE_GIORNI = PATH_PROGETTO + GIORNI;

    public static final String PATH_CATEGORIA = PATH_WIKI + "Categoria:";

    public static final String TAG_EX = "ex ";

    public static final String TAG_EX2 = "ex-";

    public static final String TAG_GENERE = "genere";

    public static final String TAG_ATTIVITA = "attivita";

    public static final String TAG_NAZIONALITA = "nazionalita";

    public static final String TAG_PROFESSIONE = "professione";

    public static final String TAG_DOPPIO_NOME = "doppionome";

    public static final String TAG_BIO = "bio";

    public static final String TAG_ALTRE = "...";


    public static final String KEY_JSON_QUERY = "query";

    public static final String KEY_JSON_ERROR = "error";

    public static final String KEY_JSON_NORMALIZED = "normalized";

    public static final String KEY_JSON_ENCODED = "fromencoded";

    public static final String KEY_JSON_CONTINUE = "continue";

    public static final String KEY_JSON_CONTINUE_CM = "cmcontinue";

    public static final String KEY_JSON_ALL = "all";

    public static final String KEY_JSON_PAGES = "pages";

    public static final String KEY_JSON_NUM_PAGES = "numpages";

    public static final String KEY_JSON_PAGE_ID = "pageid";

    public static final String KEY_JSON_NS = "ns";

    public static final String KEY_JSON_TITLE = "title";

    public static final String KEY_JSON_MISSING = "missing";

    public static final String KEY_JSON_MISSING_TRUE = "missing=true";

    public static final String KEY_JSON_REVISIONS = "revisions";

    public static final String KEY_JSON_TIMESTAMP = "timestamp";

    public static final String KEY_JSON_LOGIN = "login";

    public static final String KEY_JSON_SLOTS = "slots";

    public static final String KEY_JSON_MAIN = "main";

    public static final String KEY_JSON_CONTENT = "content";


    public static final String KEY_JSON_CATEGORY_MEMBERS = "categorymembers";

    public static final String KEY_JSON_CATEGORY_INFO = "categoryinfo";

    public static final String KEY_JSON_CATEGORY_PAGES = "categorypages";

    public static final String KEY_JSON_CATEGORY_SIZE = "size";

    public static final String KEY_JSON_CODE = "code";

    public static final String KEY_JSON_INFO = "info";

    public static final String KEY_JSON_ZERO = "zero";

    public static final String KEY_JSON_BATCH = "batchcomplete";

    public static final String KEY_JSON_DISAMBIGUA = "disambigua";

    public static final String KEY_JSON_REDIRECT = "redirect";


    public static final String KEY_MAP_GRAFFE_ESISTONO = "keyMapGraffeEsistono";

    public static final String KEY_MAP_GRAFFE_TYPE = "keyMapGraffeType";

    public static final String KEY_MAP_GRAFFE_NUMERO = "keyMapGraffeNumero";

    public static final String KEY_MAP_GRAFFE_VALORE_CONTENUTO = "keyMapGraffeValoreContenuto";

    public static final String KEY_MAP_GRAFFE_TESTO_PRECEDENTE = "keyMapGraffeTestoPrecedente";

    public static final String KEY_MAP_GRAFFE_NOME_PARAMETRO = "keyMapGraffeNomeParametro";

    public static final String KEY_MAP_GRAFFE_VALORE_PARAMETRO = "keyMapGraffeValoreParametro";

    public static final String KEY_MAP_GRAFFE_LISTA_WRAPPER = "keyMapGraffeListaWrapper";

    private static final String PROPERTY_ERROR = "In application.properties non esiste il valore di ";

    public static final String PROPERTY_LOGIN_NAME = PROPERTY_ERROR + "loginName";

    public static final String PROPERTY_LOGIN_PASSWORD = PROPERTY_ERROR + "loginPassword";

    public static final String SUCCESS_LOGIN_RESPONSE = "lguserid: 124123, lgusername: Biobot";


    public static final String ATTIVITA_PROPERTY = "attivita";

    public static final String ATTIVITA_PROPERTY_2 = "attivita2";

    public static final String ATTIVITA_PROPERTY_3 = "attivita3";

    public static final String DEFAULT_SORT = "{{DEFAULTSORT:";

    public static final String LISTA_ATTIVITA_TRE = "Ogni persona è presente in '''diverse [[Progetto:Biografie/Attività|liste]]''', in " +
            "base a quanto riportato in uno " +
            "dei '''3''' parametri '''''attività, attività2 e attività3''''' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona";

    public static final String LISTA_ATTIVITA_UNICA = String.format("Ogni persona è presente in '''una sola [[Progetto:Biografie/Attività|lista]]''', in base a quanto " +
            "riportato nel '''primo''' parametro '''''attività''''' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona");

    public static final String KEY_ERROR_CANCELLANDA = "esistenteDaCancellare";

    public static final String LISTA_NAZIONALITA = String.format("Le nazionalità sono quelle [[Discussioni progetto:Biografie/Nazionalità|'''convenzionalmente''' previste]] dalla " +
            "comunità ed [[Modulo:Bio/Plurale nazionalità|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]");

}
