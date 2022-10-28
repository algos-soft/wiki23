package it.algos.vaad23.backend.boot;

import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.spring.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.time.*;
import java.util.*;

/**
 * Project vaadin23
 * Created by Algos
 * User: gac
 * Date: dom, 06-mar-2022
 * Time: 17:20
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class VaadCost {

    public static final String ENCODE = "UTF-8";

    public static final String PATH_WIKI = "https://it.wikipedia.org/wiki/";

    public static final String TAG_WIKI = "https://it.wikipedia.org/wiki/";

    public static final String ALGOS = "algos";

    public static final LocalDateTime ROOT_DATA_TIME = LocalDateTime.of(2000, 1, 1, 0, 0);

    public static final LocalDate ROOT_DATA = LocalDate.of(2000, 1, 1);

    public static final LocalTime ROOT_TIME = LocalTime.of(0, 0, 0);

    public static final String PATH_RISORSE = "src/main/resources/META-INF/resources/";

    public static final String PATH_PREFIX = "src/main/java/it/algos/";

    public static final String PATH_PREFIX_ALGOS = "it/algos/";

    public static final String PATH_ALGOS = "it.algos";

    public static final String PROJECT_VAADIN23 = "vaadin23";

    public static final String MODULO_VAADIN23 = "vaad23";

    public static final String TAG_WIZ = "wizard";

    public static final String VUOTA = "";

    public static final String CAPO = "\n";

    public static final String TAB = "\t";

    public static final String ASTERISCO = "*";

    public static final String PIENA = "Piena";

    public static final VaadinIcon DEFAULT_ICON = VaadinIcon.ASTERISK;

    public static final String DEFAULT_ICON_NAME = "asterisk";

    public static final String FORWARD = " -> ";

    public static final String SEP = " - ";

    public static final String PUNTO = ".";

    public static final String APICE = "'";

    public static final String ESCLAMATIVO = "!";

    public static final String UGUALE = "=";

    public static final String DOPPIO_UGUALE = UGUALE + UGUALE;

    public static final String DOPPIO_ESCLAMATIVO = ESCLAMATIVO + ESCLAMATIVO;

    public static final String PIPE_REGEX = "\\|";

    public static final String DOPPIO_PIPE_REGEX = PIPE_REGEX + PIPE_REGEX;

    public static final String VIRGOLA = ",";

    public static final String PUNTO_VIRGOLA = ";";

    public static final String PUNTO_INTERROGATIVO = "?";

    public static final String VIRGOLA_CAPO = VIRGOLA + CAPO;

    public static final String DUE_PUNTI = ":";

    public static final String TRE_PUNTI = "...";

    public static final String SPAZIO = " ";

    public static final String SPAZIO_NON_BREAKING = "&nbsp;";

    public static final String UNDERSCORE = "_";

    public static final String DUE_PUNTI_SPAZIO = DUE_PUNTI + SPAZIO;

    public static final String DOPPIO_SPAZIO = SPAZIO + SPAZIO;

    public static final String VIRGOLA_SPAZIO = VIRGOLA + SPAZIO;


    public static final String CONFIG = "config";

    public static final String FRONTEND = "frontend";


    public static final String QUADRA_INI = "[";

    public static final String QUADRA_INI_REGEX = "\\[";

    public static final String DOPPIE_QUADRE_INI = QUADRA_INI + QUADRA_INI;

    public static final String QUADRA_END = "]";

    public static final String QUADRA_END_REGEX = "\\]";

    public static final String DOPPIE_QUADRE_END = QUADRA_END + QUADRA_END;

    public static final String PARENTESI_TONDA_INI = "(";

    public static final String PARENTESI_TONDA_END = ")";

    public static final String PARENTESI_TONDA_INI_REGEX = "\\(";

    public static final String PARENTESI_TONDA_END_REGEX = "\\)";

    public static final String SLASH = "/";

    public static final String SLASH_SPACE = SPAZIO + SLASH + SPAZIO;

    public static final String PIPE = "|";

    public static final String REGEX_PIPE = "\\|";

    public static final String RETURN = "\n";

    public static final String REF = "<ref>";

    public static final String HTML = "[html";

    public static final String REF_OPEN = "<ref";

    public static final String REF_END = "</ref>";

    public static final String CIRCA = "circa";

    public static final String TAG_REF = SPAZIO + "{{#tag:ref";

    //--codifica dei caratteri
    public static final String INPUT = "UTF8";

    public static final String SORT_SPRING_ASC = "ASC";

    public static final String SORT_SPRING_DESC = "DESC";

    public static final String TXT_SUFFIX = ".txt";

    public static final String XML_SUFFIX = ".xml";

    public static final String JAVA_SUFFIX = ".java";

    public static final String KEY_NULL = "null";

    public static final String NULL = "(null)";

    public static final String TAG_ALTRE = "...";

    public static final String TAG_ALTRE_BY = TAG_ALTRE + SPAZIO + "by" + SPAZIO;

    public static final String KEY_ROUTE_TYPE = "type";

    public static final String KEY_BEAN_CLASS = "beanClass";

    public static final String KEY_FORM_TYPE = "formType";

    public static final String KEY_BEAN_ENTITY = "beanID";

    public static final String KEY_BEAN_PREV_ID = "beanPrevID";

    public static final String KEY_BEAN_NEXT_ID = "beanNextID";

    public static final String KEY_SERVICE_CLASS = "serviceClass";

    public static final String KEY_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public static final String KEY_SESSION_LOGIN = "SESSION_LOGIN";

    public static final String KEY_SESSION_MOBILE = "sessionMobile";

    public static final String KEY_MAPPA_PARSE = "parse";

    public static final String KEY_MAPPA_PAGEID = "pageid";

    public static final String KEY_MAPPA_TITLE = "title";

    public static final String KEY_MAPPA_TEXT = "wikitext";

    public static final String KEY_MAPPA_DOMAIN = "domain";

    public static final String KEY_MAPPA_LAST_MODIFICA = "lastModifica";

    /**
     * The constant ROUTE_NAME.
     */
    public static final String ROUTE_NAME_LOGIN = "Login";

    public static final String ROUTE_NAME_LOGIN_ERROR = ROUTE_NAME_LOGIN + "?error";

    public static final String ROUTE_NAME_ABOUT = "About";

    public static final String ROUTE_NAME_HOME = "Home";

    public static final String ROUTE_NAME_GENERIC_VIEW = "vista";

    public static final String ROUTE_NAME_GENERIC_LIST = "list";

    public static final String ROUTE_NAME_GENERIC_FORM = "form";

    public static final String SUFFIX_LIST = "List";

    public static final String SUFFIX_FORM = "Form";

    public static final String SUFFIX_ENTITY = VUOTA;

    public static final String SUFFIX_BACKEND = "Backend";

    public static final String SUFFIX_REPOSITORY = "Repository";

    public static final String SUFFIX_VIEW = "View";

    public static final String SUFFIX_DIALOG = "Dialog";

    public static final String SUFFIX_SERVICE = "Service";

    public static final String SWITCH = "Switch - caso non previsto";

    public static final String SWITCH_FUTURO = "Switch - caso ancora da implementare";

    /**
     * tag per la singola graffa di apertura
     */
    public static final String GRAFFA_INI = "{";

    /**
     * tag per le doppie graffe di apertura
     */
    public static final String DOPPIE_GRAFFE_INI = GRAFFA_INI + GRAFFA_INI;

    /**
     * tag per la singola graffa di apertura
     */
    public static final String GRAFFA_END = "}";

    /**
     * tag per le doppie graffe di chiusura
     */
    public static final String DOPPIE_GRAFFE_END = GRAFFA_END + GRAFFA_END;

    //--chiavi mappa eventi
    public static final String JSON_SUCCESS = "Success";

    public static final String JSON_ERROR = "error";

    public static final String JSON_REASON = "reason";

    public static final String JSON_FAILED = "Failed";

    public static final String JSON_COOKIES = "cookies";

    public static final String JSON_BOT_LOGIN = "botLogin";

    public static final String JSON_NOT_QUERY_LOGIN = "notQueryLogin";

    public static final String JSON_NO_BOT = "assertbotfailed";

    public static final String JSON_CODE = "code";

    public static final String JSON_INFO = "info";

    public static final String NULL_WIKI_TITLE = "Il titolo wiki è nullo";

    public static final String ERROR_WIKI_TITLE = "Manca il titolo wiki";

    public static final String ERROR_WIKI_PAGINA = "Manca la pagina wiki";

    public static final String ERROR_WIKI_CATEGORIA = "Manca la categoria wiki";

    public static final String ERROR_FILE_WIKI = "java.io.FileNotFoundException: https://it.wikipedia.org/wiki/";

    public static final String KEY_MAPPA_EVENTO_AZIONE = "eventoAzione";

    public static final String KEY_MAPPA_SEARCH = "search";

    public static final String ERRORE = "Errore";

    public static final String UGUALE_SEMPLICE = "=";

    public static final String UGUALE_SPAZIATO = SPAZIO + UGUALE_SEMPLICE + SPAZIO;

    public static final String PARAGRAFO = UGUALE_SEMPLICE + UGUALE_SEMPLICE;

    public static final String PARAGRAFO_SUB = UGUALE_SEMPLICE + UGUALE_SEMPLICE + UGUALE_SEMPLICE;

    /**
     * tag '@Qualifier'
     */
    public static final String TAG_PRE = "preferenza";

    public static final String TAG_REGIONE = "regione";

    public static final String TAG_PROVINCIA = "provincia";

    public static final String TAG_COMUNE = "comune";


    public static final String TAG_GIORNO = "giorno";

    public static final String TAG_MESE = "mese";

    public static final String TAG_ANNO = "anno";

    public static final String TAG_SECOLO = "secolo";

    public static final String TAG_VERSIONE = "versione";

    public static final String TAG_CONTINENTE = "continente";

    public static final String TAG_VIA = "via";

    public static final String TAG_NOTA = "nota";

    public static final String TAG_LOGGER = "logger";


    public static final String TAG_FLOW_DATA = "flowdata";

    public static final String TAG_FLOW_VERSION = "flowversion";

    public static final String QUALIFIER_VERSION_VAAD = "versionVaad";

    public static final String QUALIFIER_DATA_VAAD = "dataVaad";

    public static final String QUALIFIER_PREFERENCES_VAAD = "preferencesVaad";

    public static final String TAG_FLOW_PREFERENCES = "23preferences";

    public static final int PAD_TYPE = 10;

    public static final int PAD_CLASS = 20;

    public static final int PAD_SYSTEM = 10;

    public static final int PAD_METHOD = 20;

    public static final int PAD_LINE = 4;

    public static final int PAD_COMPANY = 5;

    public static final int PAD_USER = 18;

    public static final int PAD_IP = 37;

    public static final String VERO = "true";

    public static final String FALSO = "false";

    /**
     * Nomi delle properties.
     */
    public static final String FIELD_NAME_ID_SENZA = "id";

    public static final String FIELD_NAME_ID_CON = "_id";

    public static final String FIELD_NAME_ID_LINK = ".$id";

    public static final String FIELD_NAME_ORDINE = "ordine";

    public static final String FIELD_NAME_CODE = "code";

    public static final String FIELD_NAME_DESCRIZIONE = "descrizione";

    public static final String FIELD_NAME_RESET = "reset";

    public static final String FIELD_NAME_COMPANY = "company";

    public static final String FIELD_KEY_ORDER = "#";

    public static final String TRATTINO = "-";

    public static final String COLOR_VERO = "#9FE2BF";

    public static final String COLOR_FALSO = "#FF7F50";

    public static final String COLOR_BLUE = "blue";

    public static final String TAG_INIZIALE = "http://";

    public static final String TAG_INIZIALE_SECURE = "https://";

    public static final String PROPERTY_SERIAL = "serialVersionUID";

    public static final String PROPERTY_CREAZIONE = "creazione";

    public static final String PROPERTY_MODIFICA = "modifica";

    public static final String PROPERTY_NOTE = "note";

    public static final String ECC = "ecc.";

    public static final String NOTE = "<!--";

    public static final String NO_WIKI = "<nowiki>";

    public static final List<String> ESCLUSI_SEMPRE = Arrays.asList(PROPERTY_SERIAL);

    public static final List<String> ESCLUSI_ALL = Arrays.asList(PROPERTY_SERIAL, PROPERTY_NOTE, PROPERTY_CREAZIONE, PROPERTY_MODIFICA);


    public static final String TAG_EM = "em";

    public static final String PRIMO_GIORNO_MESE = "1º";

    public static final String KEY_MAPPA_GIORNI_MESE_NUMERO = "meseNumero";

    public static final String KEY_MAPPA_GIORNI_MESE_TESTO = "meseTesto";

    public static final String KEY_MAPPA_GIORNI_NORMALE = "normale";

    public static final String KEY_MAPPA_GIORNI_BISESTILE = "bisestile";

    public static final String WIDTH_EM = "18ex";

    public static final String WIDTH_EM_LARGE = "24ex";

    public static final String SUMMARY = "[[Utente:Biobot|Biobot]]";

    public static final String KEY_MAPPA_GIORNI_NOME = "nome";

    public static final String KEY_MAPPA_GIORNI_TITOLO = "titolo";

    public static final String KEY_MAPPA_GIORNI_MESE_MESE = "meseMese";

    public static final int ANNO_INIZIALE = 2000;

    public static final int ANTE_CRISTO_MAX = 1000;

    public static final int DOPO_CRISTO_MAX = 2030;


    public static final List<String> MASCHI = Arrays.asList("m", "uomo", "maschio", "maschile", "M", "Uomo", "Maschio", "Maschile");

    public static final List<String> FEMMINE = Arrays.asList("f", "donna", "femmina", "femminile", "F", "Donna", "Femmina", "Femminile");

    public static final List<String> TRANS = Arrays.asList("", "trans", "incerto", "non si sa", "dubbio", "?", "*", "ǝ");

    /**
     * UNICODEU+000BA
     * HEX CODE&#xba;
     * HTML CODE&#186;
     * HTML ENTITY&ordm;
     * CSS CODE\00BA
     */
    public static final String MASCULINE_ORDINAL_INDICATOR = "º"; //-70

    public static final char CHAR_MASCULINE_ORDINAL_INDICATOR = (char) 186;

    /**
     * UNICODEU+000AA
     * HEX CODE&#xaa;
     * HTML CODE&#170;
     * HTML ENTITY&ordf;
     * CSS CODE\00AA
     */
    public static final String FEMININE_ORDINAL_INDICATOR = "º"; //-70


    /**
     * UNICODEU+000B0
     * HEX CODE&#xb0;
     * HTML CODE&#176;
     * HTML ENTITY&deg;
     * CSS CODE\00B0
     */
    public static final String DEGREE_SIGN = "°"; //-80
    public static final char CHAR_DEGREE_SIGN = (char) 176;

}
